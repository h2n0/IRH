package com.fls.irh.main.entity;

import com.fls.irh.main.crafting.Crafting;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.screen.CraftingMenu;

public class Anvil extends Furniture {
	public Anvil() {
		super("Anvil");
		col = Colour.get(-1, 000, 111, 222);
		sprite = 0;
		xr = 3;
		yr = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.anvilRecipes, player));
		return true;
	}
}