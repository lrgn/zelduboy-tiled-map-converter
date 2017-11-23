package fr.lrgn.zelduboy.data;

import java.util.Map;

public class TiledObject
{
	private int id;
	private int width;
	private int height;
	private String name;
	private String type;
	private Map<String, String> properties;
	private boolean visible;
	private int x;
	private int y;
	private float rotation;
	private int gid;
	private boolean point;
	private boolean ellipse;
	// polygon
	// polyline
	private Map<String, String> text;

	public String getName()
	{
		return name;
	}

	public int getWidth()
	{
		return width >> 4;
	}

	public int getHeight()
	{
		return height >> 4;
	}

	public int getX()
	{
		return x >> 4;
	}

	public int getY()
	{
		return y >> 4;
	}
}
