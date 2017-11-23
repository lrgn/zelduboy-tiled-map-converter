package fr.lrgn.zelduboy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.lrgn.zelduboy.data.TiledLayer;
import fr.lrgn.zelduboy.data.TiledMap;
import fr.lrgn.zelduboy.data.TiledObject;

public class CodeGenerator
{
	public static void generate(TiledMap map, File output) throws IOException
	{
		try (FileWriter writer = new FileWriter(output))
		{
			final TiledLayer tiles = map.getLayerByName("tiles");
			assert tiles != null : "No layer called 'tiles' defined";

			final TiledLayer rooms = map.getLayerByName("rooms");
			assert tiles != null : "No layer called 'rooms' defined";

			for (final TiledObject room : rooms.getObjects())
			{
				writer.write("const unsigned char PROGMEM ");
				writer.write(room.getName());
				writer.write("Data[] =\n");
				writer.write("{\n");
				writer.write(Integer.toString(room.getWidth()));
				writer.write(", ");
				writer.write(Integer.toString(room.getHeight()));
				writer.write(",\n");

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

						writer.write(Integer.toString(tile));
						if (y != room.getHeight() - 1 || x != room.getWidth() - 1)
							writer.write(",");
					}
					writer.write("\n");
				}

				writer.write("};");
			}
		}
	}
}
