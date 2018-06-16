package fr.lrgn.zelduboy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import fr.lrgn.zelduboy.data.TiledMap;

public class TiledMapConverter
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        if (args.length == 0)
        {
            printZelduboyUsage();
            printRX31Usage();
            return;
        }

        switch (args[0].toLowerCase())
        {
            case "zelduboy":
            {
                if (args.length != 3)
                {
                    printZelduboyUsage();
                    return;
                }

                final File mapFile = new File(args[1]);
                assert mapFile.exists() && mapFile.isFile() : "Argument 2 should be an existing file";

                final File outputFolder = new File(args[2]);
                assert outputFolder.exists() && outputFolder.isDirectory() : "Argument 3 should be an existing folder";

                generateZelduboyCode(mapFile, outputFolder);
                break;
            }

            case "rx-31":
            {
                if (args.length < 3)
                {
                    printRX31Usage();
                    return;
                }

                final File[] levelFiles = new File[args.length - 2];

                for (int i = 0; i != levelFiles.length; i++)
                {
                    levelFiles[i] = new File(args[1 + i]);
                    assert levelFiles[i].exists() && levelFiles[i].isFile() : "Argument " + (1 + i)
                            + " should be an existing file";
                }

                final File outputFolder = new File(args[2]);
                assert outputFolder.exists()
                        && outputFolder.isDirectory() : "Last argument should be an existing folder";

                generateRX31Code(levelFiles, outputFolder);
                break;
            }

            default:
                printZelduboyUsage();
                printRX31Usage();
                return;
        }
    }

    private static void generateZelduboyCode(File mapFile, File outputFolder) throws FileNotFoundException, IOException
    {
        try (FileReader reader = new FileReader(mapFile))
        {
            final Gson gson = new Gson();

            final TiledMap map = gson.fromJson(reader, TiledMap.class);
            map.check();

            final ZelduboyCodeGenerator gen = new ZelduboyCodeGenerator(map, outputFolder);
            gen.generate();
        }
    }

    private static void generateRX31Code(File[] levelFiles, File outputFolder) throws FileNotFoundException, IOException
    {
        final TiledMap[] levels = new TiledMap[levelFiles.length];

        for (int i = 0; i != levels.length; i++)
        {
            try (FileReader reader = new FileReader(levelFiles[i]))
            {
                final Gson gson = new Gson();

                levels[i] = gson.fromJson(reader, TiledMap.class);
                levels[i].check();
            }
        }

        final RX31CodeGenerator gen = new RX31CodeGenerator(levels, outputFolder);
        gen.generate();
    }

    private static void printZelduboyUsage()
    {
        System.out.println(
                "Usage: java -jar zelduboy-tiled-map-converter.jar zelduboy <path to Tiled JSON map> <path to generated folder>");
    }

    private static void printRX31Usage()
    {
        System.out.println(
                "Usage: java -jar zelduboy-tiled-map-converter.jar RX-31 <path to Tiled JSON levels> <path to generated folder>");
    }
}