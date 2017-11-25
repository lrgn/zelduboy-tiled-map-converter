package fr.lrgn.zelduboy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import fr.lrgn.zelduboy.data.TiledMap;

public class TiledMapConverter
{
	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage: java -jar zelduboy-tiled-map-converter.jar <path to Tiled JSON map> <path to generated folder>");
			System.exit(0);
		}

		final File mapFile = new File(args[0]);
		assert mapFile.exists() && mapFile.isFile() : "Argument 1 should be an existing file";

		final File outputFolder = new File(args[1]);
		assert outputFolder.exists() && outputFolder.isDirectory() : "Argument 2 should be an existing folder";

		try (FileReader reader = new FileReader(mapFile))
		{
			final Gson gson = new Gson();

			final TiledMap map = gson.fromJson(reader, TiledMap.class);
			map.check();

			final CodeGenerator gen = new CodeGenerator(map, outputFolder);
			gen.generate();
		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}