package com.fls.irh.main.screen;

import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;

public class ErrorMenu extends Menu {

    public ErrorMenu(String[] e){
        System.out.println(e.toString());
    }
    
    public void render(Screen screen){
        screen.clear(0);
        Font.draw("Sorry the game", screen, 8, 1, textColour);
        Font.draw("has crashed", screen, 8, 2 * 8, textColour);
    }
}
