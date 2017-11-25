package fr.lrgn.zelduboy.data;

import java.util.List;
import java.util.Map;

public class TiledLayer
{
	private static final String OBJECT_NOT_FOUND_EXCEPTION = "Object %1$s not found on layer %2$s";

	// From JSON
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

	public TiledObject getObject(String name)
	{
		for (final TiledObject object : objects)
			if (name.equals(object.getName()))
				return object;

		throw new RuntimeException(String.format(OBJECT_NOT_FOUND_EXCEPTION, name, this.name));
	}
}
