package com.fls.irh.main.item;

import java.util.Random;

import com.fls.irh.main.entity.Entity;
import com.fls.irh.main.entity.ItemEntity;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;

public class ToolItem extends Item {
	private Random random = new Random();

	public static final int MAX_LEVEL = 5;
	public static final String[] LEVEL_NAMES = { //
	"Wood", "Rock", "Iron", "Gold", "Gem"//
	};

	public static final int[] LEVEL_COLORS = {//
	Colour.get(-1, 100, 321, 431),//
			Colour.get(-1, 100, 321, 111),//
			Colour.get(-1, 100, 321, 555),//
			Colour.get(-1, 100, 321, 550),//
			Colour.get(-1, 100, 321, 055),//
	};

	public ToolType type;
	public int level = 0;

	public ToolItem(ToolType type, int level) {
		this.type = type;
		this.level = level;
	}

	public int getColor() {
		return LEVEL_COLORS[level];
	}

	public int getSprite() {
		return type.sprite + 5 * 32;
	}

	public void renderIcon(Screen screen, int x, int y) {
		screen.render(x, y, getSprite(), getColor(), 0);
	}

	public void renderInventory(Screen screen, int x, int y) {
		screen.render(x, y, getSprite(), getColor(), 0);
		Font.draw(getName(), screen, x + 8, y, Colour.get(-1, 555, 555, 555));
	}

	public String getName() {
		return LEVEL_NAMES[level] + " " + type.name;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public boolean canAttack() {
		return true;
	}

	public int getAttackDamageBonus(Entity e) {
		if (type == ToolType.axe) {
			return (level + 1) * 2 + random.nextInt(4);
		}
		if (type == ToolType.sword) {
			return (level + 1) * 3 + random.nextInt(2 + level * level * 2);
		}
		return 1;
	}

	public boolean matches(Item item) {
		if (item instanceof ToolItem) {
			ToolItem other = (ToolItem) item;
			if (other.type != type) return false;
			if (other.level != level) return false;
			return true;
		}
		return false;
	}
}