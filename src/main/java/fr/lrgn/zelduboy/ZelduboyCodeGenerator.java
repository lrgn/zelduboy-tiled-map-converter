package fr.lrgn.zelduboy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import fr.lrgn.zelduboy.data.TiledLayer;
import fr.lrgn.zelduboy.data.TiledMap;
import fr.lrgn.zelduboy.data.TiledObject;

public class ZelduboyCodeGenerator extends AbstractCodeGenerator
{
    private static final String MAP_DATA_SOURCE_FILE = "mapData.cpp";
    private static final String MAP_DATA_HEADER_FILE = "mapData.h";
    private static final String MAP_HEADER_FILE = "Map.hpp";
    private static final String ROOM_HEADER_FILE = "Room.hpp";

    private static final String IFNDEF = "#ifndef MAP_DATA_H\n#define MAP_DATA_H\n\n";
    private static final String ENDIF = "#endif\n";
    private static final String DEFINE = "#define %1$s %2$s\n";
    private static final String INCLUDE = "#include \"%1$s\"\n";

    private static final String ROOM_EXTERN = "extern Room * %1$s;\n";
    private static final String ROOM_INSTANCE = "Room * %1$s = new Room(%2$s, %3$s, %1$sData);\n";

    private static final String ROOMS_ARRAY = "Room* %1$sRooms[] = {%2$s};\n";
    private static final String MAP_EXTERN = "extern Map * %1$s;\n\n";
    private static final String MAP_INSTANCE = "Map * %1$s = new Map(%1$sRooms, %2$s);\n";

    // TODO: remove
    private static final String MAP_NAME = "dungeon";

    private final TiledMap map;
    private final File outputFolder;

    private final TiledLayer tiles;
    private final TiledLayer rooms;
    private final TiledLayer locations;
    private final TiledLayer entities;

    public ZelduboyCodeGenerator(TiledMap map, File outputFolder)
    {
        this.map = map;
        this.outputFolder = outputFolder;

        tiles = map.getLayer("tiles");
        rooms = map.getLayer("rooms");
        locations = map.getLayer("locations");
        entities = map.getLayer("entities");
    }

    public void generate() throws IOException
    {
        generateMapDataSource();
        generateMapDataHeader();
    }

    private void generateMapDataSource() throws IOException
    {
        final File cFile = new File(outputFolder, MAP_DATA_SOURCE_FILE);

        try (FileWriter cWriter = new FileWriter(cFile))
        {
            write(cWriter, INCLUDE, MAP_DATA_HEADER_FILE);

            String roomsArray = "";
            for (final TiledObject room : rooms.getObjects())
            {
                write(cWriter, ROOM_INSTANCE, room.getName(), room.getX(), room.getY());

                roomsArray += (roomsArray.isEmpty() ? "" : ",") + room.getName();
            }

            write(cWriter, ROOMS_ARRAY, MAP_NAME, roomsArray);
            write(cWriter, MAP_INSTANCE, MAP_NAME, rooms.getObjects().size());
        }
    }

    private void generateMapDataHeader() throws IOException
    {
        final File hFile = new File(outputFolder, MAP_DATA_HEADER_FILE);

        try (final FileWriter hWriter = new FileWriter(hFile))
        {
            write(hWriter, IFNDEF);

            write(hWriter, INCLUDE, MAP_HEADER_FILE);
            write(hWriter, INCLUDE, ROOM_HEADER_FILE);

            write(hWriter, DEFINE, "SPAWN_MAP", MAP_NAME);
            write(hWriter, DEFINE, "SPAWN_X", locations.getObject("spawnLocation").getX());
            write(hWriter, DEFINE, "SPAWN_Y", locations.getObject("spawnLocation").getY());

            write(hWriter, MAP_EXTERN, MAP_NAME);

            for (final TiledObject room : rooms.getObjects())
            {
                write(hWriter, ROOM_EXTERN, room.getName());

                generateRoomData(hWriter, room);
            }
            write(hWriter, ENDIF);
        }
    }

    private static final String ROOM_DATA_DECLARATION = "const unsigned char PROGMEM %1$sData[] =\n";
    private static final String ROOM_DATA_WIDTH_HEIGHT = "// width, height,\n%1$s, %2$s,\n";

    private void generateRoomData(final FileWriter writer, final TiledObject room) throws IOException
    {
        write(writer, ROOM_DATA_DECLARATION, room.getName());
        write(writer, "{\n");
        write(writer, ROOM_DATA_WIDTH_HEIGHT, room.getWidth(), room.getHeight());

        String passability = "";

        write(writer, "// tiles data\n");

        for (int y = 0; y != room.getHeight(); y++)
        {
            for (int x = 0; x != room.getWidth(); x++)
            {
                final int mapX = room.getX() + x;
                final int mapY = room.getY() + y;

                assert mapX < tiles.getWidth() : mapX + " < " + tiles.getWidth() + " failed";
                assert mapY < tiles.getHeight() : mapY + " < " + tiles.getHeight() + " failed";

                int tile = tiles.getData()[mapY * tiles.getWidth() + mapX];
                tile -= 1; // Tiled count tiles in tileset from 1, where on Zelduboy we count them from 0

                write(writer, Integer.toString(tile));
                write(writer, ",");

                passability += tile == 0 ? "0" : 1;
            }
            write(writer, "\n");
        }

        final int size = passability.length() % 8 == 0 ? passability.length() : passability.length() / 8 * 8 + 8;
        StringUtils.rightPad(passability, size, "0");

        write(writer, "// passability data\n");

        for (int i = 0; i != size; i += 8)
        {
            write(writer, "0b");
            write(writer, passability.substring(i, i + 8));
            if (i + 8 != size)
                write(writer, ",");
            write(writer, "\n");
        }

        write(writer, "};\n\n");
    }
}
