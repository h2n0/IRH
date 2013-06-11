package com.fls.irh.main.entity.particle;

import com.fls.irh.main.entity.Entity;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Screen;

public class FishingBob extends Entity {

    public int x, y;
    public double xx, yy, zz, xa, ya, za;

    public FishingBob(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {
        xx += xa;
        yy += ya;
        zz += za;
        if (zz < 0) {
            zz = 0;
            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }
        za -= 0.15;
        x = (int) xx;
        y = (int) yy;
    }

    public void render(Screen screen) {
        screen.render(x - 8, y - 8, 29 + 0 * 32, Colour.get(005, 4, 11, 555), 0);
    }
}
