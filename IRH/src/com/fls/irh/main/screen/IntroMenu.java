package com.fls.irh.main.screen;

import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;

public class IntroMenu extends Menu {

    public void tick() {
        if (this.input.attack.clicked)
            this.game.setMenu(new TitleMenu());
    }

    public void render(Screen screen) {
        screen.clear(0);
        Font.draw("This is only", screen, 32, 8, Colour.get(-1, -1, -1, 555));
        Font.draw("a demo", screen, 56, 16, Colour.get(-1, -1, -1, 555));
        Font.draw("and is in anyway", screen, 8, 32, Colour.get(-1, -1, -1, 555));
        Font.draw("finished", screen, 8, 40, Colour.get(-1, -1, -1, 555));
        Font.draw("please do not post", screen, 8, 48, Colour.get(-1, -1, -1, 555));
        Font.draw("footage / gameplay", screen, 8, 56, Colour.get(-1, -1, -1, 555));
        Font.draw("of this demo with", screen, 8, 64, Colour.get(-1, -1, -1, 555));
        Font.draw("Consent from", screen, 8, 72, Colour.get(-1, -1, -1, 555));
        Font.draw("Fire Leaf Studios", screen, 8, 80, Colour.get(-1, -1, -1, 555));
        Font.draw("Thank you", screen, 8, 88, Colour.get(-1, -1, -1, 555));

        Font.draw("Press SPACE to start ", screen, 2, 104, Colour.get(-1, -1, -1, 555));
    }
}