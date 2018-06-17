package fr.lrgn.zelduboy.data;

import java.util.Map;

public class TiledObject
{
    // From JSON
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
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
