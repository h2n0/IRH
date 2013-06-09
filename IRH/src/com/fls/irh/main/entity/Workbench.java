package com.fls.irh.main.entity;

import com.fls.irh.main.crafting.Crafting;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.screen.CraftingMenu;

public class Workbench extends Furniture {
	public Workbench() {
		super("Workbench");
		col = Colour.get(-1, 100, 321, 431);
		sprite = 2;
		xr = 3;
		yr = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.workbenchRecipes, player));
		return true;
	}
}