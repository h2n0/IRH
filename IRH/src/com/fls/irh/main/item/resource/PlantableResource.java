package com.fls.irh.main.item.resource;

import java.util.Arrays;
import java.util.List;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.level.Level;
import com.fls.irh.main.level.tile.Tile;

public class PlantableResource extends Resource {
	private List<Tile> sourceTiles;
	private Tile targetTile;

	public PlantableResource(String name, int sprite, int color, Tile targetTile, Tile... sourceTiles1) {
		this(name, sprite, color, targetTile, Arrays.asList(sourceTiles1));
	}

	public PlantableResource(String name, int sprite, int color, Tile targetTile, List<Tile> sourceTiles) {
		super(name, sprite, color);
		this.sourceTiles = sourceTiles;
		this.targetTile = targetTile;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		if (sourceTiles.contains(tile)) {
			level.setTile(xt, yt, targetTile, 0);
			return true;
		}
		return false;
	}
}
