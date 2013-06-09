package com.fls.irh.main.item;

import com.fls.irh.main.entity.Entity;
import com.fls.irh.main.entity.ItemEntity;
import com.fls.irh.main.entity.Player;
import com.fls.irh.main.gfx.Screen;
import com.fls.irh.main.level.Level;
import com.fls.irh.main.level.tile.Tile;
import com.fls.irh.main.screen.ListItem;

public class Item implements ListItem {
	public int getColor() {
		return 0;
	}

	public int getSprite() {
		return 0;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public void renderInventory(Screen screen, int x, int y) {
	}

	public boolean interact(Player player, Entity entity, int attackDir) {
		return false;
	}

	public void renderIcon(Screen screen, int x, int y) {
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		return false;
	}
	
	public boolean isDepleted() {
		return false;
	}

	public boolean canAttack() {
		return false;
	}

	public int getAttackDamageBonus(Entity e) {
		return 0;
	}

	public String getName() {
		return "";
	}

	public boolean matches(Item item) {
		return item.getClass() == getClass();
	}
}