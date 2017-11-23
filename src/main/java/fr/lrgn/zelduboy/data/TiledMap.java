package fr.lrgn.zelduboy.data;

import java.util.HashMap;
import java.util.Map;

public class TiledMap
{
	private int version;
	private String tiledversion;
	private int width;
	private int height;
	private int tilewidth;
	private int tileheight;
	private String orientation;
	private TiledLayer[] layers;
	private TiledTileset[] tilesets;
	private String backgroundcolor;
	private String renderorder;
	private String properties;
	private int nextobjectid;

	private Map<String, TiledLayer> layersByName;

	public void check()
	{
		assert tilewidth == 16 : "tilewidth must be 16";
		assert tileheight == 16 : "tileheight must be 16";
		assert "orthogonal".equals(orientation) : "orientation must be orthogonal";
	}

	public TiledLayer getLayerByName(String name)
	{
		if (layersByName == null)
		{
			layersByName = new HashMap<String, TiledLayer>();
			for (final TiledLayer layer : layers)
				layersByName.put(layer.getName(), layer);
		}

		return layersByName.get(name);
	}
}
