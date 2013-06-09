package com.fls.irh.main.item.resource;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.level.Level;
import com.fls.irh.main.level.tile.Tile;

public class Resource
{
    public static Resource wood = new Resource("Wood", 133, Colour.get(-1, 200, 531, 430));
    public static Resource stone = new Resource("Stone", 130, Colour.get(-1, 111, 333, 555));
    public static Resource flower = new PlantableResource("Flower", 128, Colour.get(-1, 10, 555, 330), Tile.flower, new Tile[] { Tile.grass });
    public static Resource apple = new FoodResource("Apple", 132, Colour.get(-1, 210, 400, 500), 1, 1);
    public static Resource acorn = new PlantableResource("acorn", 129, Colour.get(-1, 210, 420, 430), Tile.treeSapling, new Tile[] { Tile.grass });
    public final String name;
    public final int sprite;
    public final int color;

    public Resource(String name, int sprite, int color)
    {
        if (name.length() > 6)
            throw new RuntimeException("Name cannot be longer than six characters!");
        this.name = name;
        this.sprite = sprite;
        this.color = color;
    }

    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        return false;
    }
}