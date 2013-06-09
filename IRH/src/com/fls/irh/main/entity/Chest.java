package com.fls.irh.main.entity;

import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.screen.ContainerMenu;

public class Chest extends Furniture {
	public Inventory inventory = new Inventory();

	public Chest() {
		super("Chest");
		col = Colour.get(-1, 110, 331, 552);
		sprite = 0;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new ContainerMenu(player, "Chest", inventory));
		return true;
	}
}