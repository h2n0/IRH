package com.fls.irh.main.entity;

import com.fls.irh.main.crafting.Crafting;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.screen.CraftingMenu;

public class Furnace extends Furniture {
	public Furnace() {
		super("Furnace");
		col = Colour.get(-1, 000, 222, 333);
		sprite = 3;
		xr = 3;
		yr = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.furnaceRecipes, player));
		return true;
	}
}