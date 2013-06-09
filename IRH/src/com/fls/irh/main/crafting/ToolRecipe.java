package com.fls.irh.main.crafting;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.item.ToolItem;
import com.fls.irh.main.item.ToolType;

public class ToolRecipe extends Recipe {
	private ToolType type;
	private int level;

	public ToolRecipe(ToolType type, int level) {
		super(new ToolItem(type, level));
		this.type = type;
		this.level = level;
	}

	public void craft(Player player) {
		player.inventory.add(0, new ToolItem(type, level));
	}
}
