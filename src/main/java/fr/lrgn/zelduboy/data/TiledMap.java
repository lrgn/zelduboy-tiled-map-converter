package fr.lrgn.zelduboy.data;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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
    private Map<String, String> properties;
    private int nextobjectid;

    public void check()
    {
        assert tilewidth % 8 == 0 : "tilewidth must be a multiple of 8";
        assert tileheight % 8 == 0 : "tileheight must be a multiple of 8";
        assert tilewidth == tileheight : "a tile has to be square";
        assert "orthogonal".equals(orientation) : "orientation must be orthogonal";
    }

    public TiledLayer getLayer(String name)
    {
        for (final TiledLayer layer : layers)
            if (name.equals(layer.getName()))
                return layer;

        throw new RuntimeException(String.format(LAYER_NOT_FOUND_EXCEPTION, name));
    }

    public Optional<String> getProperty(String name)
    {
        for (final Entry<String, String> property : properties.entrySet())
            if (name.equals(property.getKey()))
                return Optional.of(property.getValue());

        return Optional.empty();
    }
}
