package fr.lrgn.zelduboy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

import fr.lrgn.zelduboy.data.TiledLayer;
import fr.lrgn.zelduboy.data.TiledMap;
import fr.lrgn.zelduboy.data.TiledObject;

public class RX31CodeGenerator extends AbstractCodeGenerator
{
    private static final String LEVELS_SOURCE_FILE = "levels.cpp";
    private static final String LEVELS_HEADER_FILE = "levels.h";

    private static final String IFNDEF = "#ifndef LEVELS_H\n#define LEVELS_H\n\n";
    private static final String ENDIF = "#endif\n";

    private final TiledMap[] levels;
    private final File outputFolder;

    private static final String LEVEL_DATA_DECLARATION = "const unsigned char PROGMEM %1$sData[] =\n";
    private static final String LEVEL_DATA_WIDTH_HEIGHT = "// width, height,\n%1$s, %2$s,\n";

    public RX31CodeGenerator(TiledMap[] levels, File outputFolder)
    {
        this.levels = levels;
        this.outputFolder = outputFolder;
    }

    public void generate() throws IOException
    {
        generateSource();
        generateHeader();
    }

    private void generateHeader() throws IOException
    {
        final File hFile = new File(outputFolder, LEVELS_HEADER_FILE);

        try (final FileWriter writer = new FileWriter(hFile))
        {
            write(writer, IFNDEF);
            write(writer, INCLUDE, "Arduboy2.h");
            write(writer, INCLUDE, "Level.hpp");
            write(writer, DEFINE, "ENT_BOOSTER", "1");
            write(writer, DEFINE, "ENT_BURGERSHIP", "2");

            write(writer, "Level* createLevel(uint8_t levelId);\n");

            for (final TiledMap level : levels)
            {
                generateLevel(writer, level);
            }

            write(writer, ENDIF);
        }
    }

    private void generateLevel(Writer hWriter, TiledMap level) throws IOException
    {
        final TiledLayer tiles = level.getLayer("tiles");
        final Optional<String> levelName = level.getProperty("name");

        assert levelName.isPresent() : "no property 'name' found on level";
        assert tiles.getHeight() == 8 : "tiles layer should be 8 tiles tall";
        assert tiles.getWidth() >= 16 : "tiles layer should be at least 16 tiles wide";

        write(hWriter, DEFINE, "LVL_" + levelName.get().toUpperCase(), 1);

        write(hWriter, LEVEL_DATA_DECLARATION, levelName.get());
        write(hWriter, "{\n");
        write(hWriter, LEVEL_DATA_WIDTH_HEIGHT, tiles.getWidth(), tiles.getHeight());

        final int[] data = tiles.getData();

        for (int x = 0; x != tiles.getWidth(); x++)
        {
            for (int y = 0; y != tiles.getHeight(); y++)
            {
                final int i = x + y * tiles.getWidth();

                // Tiled count tiles in tileset from 1, where on Arduboy we count them from 0
                write(hWriter, Integer.toString(data[i] - 1));

                if (i != data.length - 1)
                    write(hWriter, ",");
            }
            write(hWriter, "\n");
        }

        write(hWriter, "};\n");

        final TiledLayer entities = level.getLayer("entities");

        String entitiesData = "";
        int entitiesDataLength = 0;

        for (final TiledObject entity : entities.getObjects())
        {
            entitiesData += entitiesData.isEmpty() ? "" : ",\n";
            switch (entity.getName())
            {
                case "Booster":
                    entitiesData += "ENT_BOOSTER," + Integer.toString(entity.getX()) + ',' + entity.getY();
                    entitiesDataLength += 3;
                    break;
                default:
                    System.err.println("Unknown entity type " + entity.getName());
                    break;
            }
        }

        write(hWriter, LEVEL_DATA_DECLARATION, levelName.get() + "entities");
        write(hWriter, "{\n");
        write(hWriter, entitiesDataLength + ",\n");
        write(hWriter, entitiesData);

        write(hWriter, "};\n");
    }

    private void generateSource() throws IOException
    {
        try (final FileWriter writer = new FileWriter(new File(outputFolder, LEVELS_SOURCE_FILE)))
        {
            write(writer, INCLUDE, LEVELS_HEADER_FILE);
            write(writer, EMPTY_LINE);
            write(writer, INCLUDE, "Level.hpp");

            write(writer, "Level* createLevel(uint8_t levelId) {");
            write(writer, "switch (levelId) {");

            for (final TiledMap level : levels)
            {
                final String levelName = level.getProperty("name").get();

                write(writer, "case LVL_" + levelName.toUpperCase() + ":");
                write(writer, "return new Level(%1$sData, %1$sentitiesData);", levelName);
            }
            write(writer, "default: return NULL;");

            write(writer, "}}");
        }
    }

}
