package com.fls.irh.main.level.tile;

import com.fls.irh.main.entity.Entity;
import com.fls.irh.main.entity.ItemEntity;
import com.fls.irh.main.entity.Mob;
import com.fls.irh.main.entity.Player;
import com.fls.irh.main.entity.particle.SmashParticle;
import com.fls.irh.main.entity.particle.TextParticle;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Screen;
import com.fls.irh.main.item.Item;
import com.fls.irh.main.item.ResourceItem;
import com.fls.irh.main.item.ToolItem;
import com.fls.irh.main.item.ToolType;
import com.fls.irh.main.item.resource.Resource;
import com.fls.irh.main.level.Level;

public class OreTile extends Tile {
	private Resource toDrop;
	private int color;

	public OreTile(int id, Resource toDrop) {
		super(id);
		this.toDrop = toDrop;
		this.color = toDrop.color & 0xffff00;
	}

	public void render(Screen screen, Level level, int x, int y) {
		color = (toDrop.color & 0xffffff00) + Colour.get(level.dirtColor);
		screen.render(x * 16 + 0, y * 16 + 0, 17 + 1 * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 18 + 1 * 32, color, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 17 + 2 * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 18 + 2 * 32, color, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
		hurt(level, x, y, 0);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.pickaxe) {
				if (player.payStamina(6 - tool.level)) {
					hurt(level, xt, yt, 1);
					return true;
				}
			}
		}
		return false;
	}

	public void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + 1;
		level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Colour.get(-1, 500, 500, 500)));
		if (dmg > 0) {
			int count = random.nextInt(2);
			if (damage >= random.nextInt(10) + 3) {
				level.setTile(x, y, Tile.dirt, 0);
				count += 2;
			} else {
				level.setData(x, y, damage);
			}
			for (int i = 0; i < count; i++) {
				level.add(new ItemEntity(new ResourceItem(toDrop), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
			}
		}
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		entity.hurt(this, x, y, 3);
	}
}