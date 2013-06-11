package com.fls.irh.main.item.useitem;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.entity.particle.FishingBob;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;
import com.fls.irh.main.item.Item;
import com.fls.irh.main.level.Level;
import com.fls.irh.main.level.tile.Tile;

public class FishingRod extends Item {

    public boolean interactOn(Tile tile, Level level, int x, int y, Player player, int attackDir) {
        level.add(new FishingBob(player.x, player.y));
      //  level.add(new TextParticle("HU",player.x,player.y,Colour.get(-1, -1, -1, 555)));
        return true;
    }

    public int getColor() {
        return Colour.get(-1, 531, 444, 555);
    }

    public String getName() {
        return "Fshg rod";
    }

    public int getSprite() {
        return 0 + 5 * 32;
    }
    
    public boolean canAttack(){
        return true;
    }

    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColor(), 0);
    }

    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColor(), 0);
        Font.draw(getName(), screen, x + 8, y, Colour.get(-1, 555, 555, 555));
    }

}
