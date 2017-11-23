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
		// if (args.length != 2)
		// {
		// System.out.println("Usage: java -jar zelduboy-tiled-map-converter.jar <path to Tiled JSON map> <path to generated folder>");
		// System.exit(0);
		// }

		try (FileReader reader = new FileReader("//home//lrgn//git//repositories//zelduboy//tiled//map.json"))
		// try (FileReader reader = new FileReader(args[0]))
		{
			final Gson gson = new Gson();

			final TiledMap map = gson.fromJson(reader, TiledMap.class);

			map.check();
			CodeGenerator.generate(map, new File("//home//lrgn//git//repositories//zelduboy//tiled//test.cpp"));
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