package com.fls.irh.main.entity;

import com.fls.irh.main.crafting.Crafting;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.screen.CraftingMenu;

public class Oven extends Furniture {
	public Oven() {
		super("Oven");
		col = Colour.get(-1, 000, 332, 442);
		sprite = 1;
		xr = 3;
		yr = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.ovenRecipes, player));
		return true;
	}
}