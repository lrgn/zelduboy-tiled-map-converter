package fr.lrgn.zelduboy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

import fr.lrgn.zelduboy.data.TiledLayer;
import fr.lrgn.zelduboy.data.TiledMap;

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
        generateHeader();
    }

    private void generateHeader() throws IOException
    {
        final File hFile = new File(outputFolder, LEVELS_HEADER_FILE);

        try (final FileWriter hWriter = new FileWriter(hFile))
        {
            write(hWriter, IFNDEF);

            for (final TiledMap level : levels)
            {
                generateLevel(hWriter, level);
            }

            write(hWriter, ENDIF);
        }
    }

    private void generateLevel(Writer hWriter, TiledMap level) throws IOException
    {
        final TiledLayer tiles = level.getLayer("tiles");
        final Optional<String> levelName = level.getProperty("name");

        assert levelName.isPresent() : "no property 'name' found on level";
        assert tiles.getHeight() == 8 : "tiles layer should be 8 tiles tall";
        assert tiles.getWidth() >= 16 : "tiles layer should be at least 16 tiles wide";

        write(hWriter, LEVEL_DATA_DECLARATION, levelName.get());
        write(hWriter, "{\n");
        write(hWriter, LEVEL_DATA_WIDTH_HEIGHT, tiles.getWidth(), tiles.getHeight());

        final int[] data = tiles.getData();

        for (int i = 0; i != data.length; i++)
        {
            if (i % tiles.getWidth() == 0)
                write(hWriter, "\n");
            // Tiled count tiles in tileset from 1, where on Arduboy we count them from 0
            write(hWriter, Integer.toString(data[i] - 1));
            if (i != data.length - 1)
                write(hWriter, ",");
        }

        write(hWriter, "}\n");

    }
}
