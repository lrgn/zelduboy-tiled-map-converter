package fr.lrgn.zelduboy.data;

import java.util.List;
import java.util.Map;

public class TiledLayer
{
	private int width;
	private int height;
	private String name;
	private String type;
	private boolean visible;
	private int x;
	private int y;
	private int[] data;
	private List<TiledObject> objects;
	private Map<String, String> properties;
	private float opacity;
	private String draworder;

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public String getName()
	{
		return name;
	}

	public int[] getData()
	{
		return data;
	}

	public List<TiledObject> getObjects()
	{
		return objects;
	}
}
