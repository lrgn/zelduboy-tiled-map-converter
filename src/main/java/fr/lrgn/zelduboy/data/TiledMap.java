package fr.lrgn.zelduboy.data;

public class TiledMap
{
	private static final String LAYER_NOT_FOUND_EXCEPTION = "Layer %1$s not found";

	// From JSON
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

	public void check()
	{
		assert tilewidth == 16 : "tilewidth must be 16";
		assert tileheight == 16 : "tileheight must be 16";
		assert "orthogonal".equals(orientation) : "orientation must be orthogonal";
	}

	public TiledLayer getLayer(String name)
	{
		for (final TiledLayer layer : layers)
			if (name.equals(layer.getName()))
				return layer;

		throw new RuntimeException(String.format(LAYER_NOT_FOUND_EXCEPTION, name));
	}
}
