package com.fls.irh.main.screen;

import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;
import com.fls.irh.main.sound.Sound;

public class TitleMenu extends Menu
{
    private int selected = 0;

    private static final String[] options = { "Start game", "How to play", "About", "Exit" };

    public void tick()
    {
        if (this.input.up.clicked)
            this.selected -= 1;
        if (this.input.down.clicked) {
            this.selected += 1;
        }
        int len = options.length;
        if (this.selected < 0)
            this.selected += len;
        if (this.selected >= len) {
            this.selected -= len;
        }
        if ((this.input.attack.clicked) || (this.input.menu.clicked)) {
            if (this.selected == 0) {
                Sound.test.play();
                this.game.resetGame();
                this.game.setMenu(null);
            }
            if (this.selected == 1)
                this.game.setMenu(new InstructionsMenu(this));
            if (this.selected == 2)
                this.game.setMenu(new AboutMenu(this));
            if (this.selected == 3)
                System.exit(3);
        }
    }

    public void render(Screen screen){
        screen.clear(0);

        int h = 2;
        int w = 13;
        int titleColor = Colour.get(0, 8, 131, 551);
        int xo = (screen.w - w * 8) / 2;
        int yo = 25;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                screen.render(xo + x * 8, yo + y * 8, x + (y + 6) * 32, titleColor, 0);
            }
        }

        for (int i = 0; i < options.length; i++) {
            String msg = options[i];
            int col = Colour.get(0, 222, 222, 222);
            if (i == this.selected) {
                msg = "> " + msg + " <";
                col = Colour.get(0, 555, 555, 555);
            }
            Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (8 + i) * 8, col);
        }

        Font.draw("(Arrow Keys,X and C)", screen, 0, screen.h - 8, Colour.get(0, 111, 111, 111));
    }
}