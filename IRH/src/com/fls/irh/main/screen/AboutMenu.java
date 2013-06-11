package com.fls.irh.main.screen;

import com.fls.irh.main.Game;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;

public class AboutMenu extends Menu {
	private Menu parent;

	public AboutMenu(Menu parent) {
		this.parent = parent;
	}

	public void tick() {
		if (input.attack.clicked || input.menu.clicked) {
			game.setMenu(parent);
		}
	}

	public void render(Screen screen) {
		screen.clear(0);

		Font.draw("About IRH", screen, 2 * 8 + 4, 1 * 8, Colour.get(0, 555, 555, 555));
		Font.draw("IRH is being", screen, 0 * 8 + 4, 3 * 8, Colour.get(0, 333, 333, 333));
		Font.draw("made by", screen, 0 * 8 + 4, 4 * 8, Colour.get(0, 333, 333, 333));
		Font.draw("Fire leaf studios", screen, 0 * 8 + 4, 5 * 8, Colour.get(0, 333, 333, 333));
		Font.draw("in under a year", screen, 0 * 8 + 4, 6 * 8, Colour.get(0, 333, 333, 333));
		Font.draw("as a test game", screen, 0 * 8 + 4, 7 * 8, Colour.get(0, 333, 333, 333));
		Font.draw("Current version", screen, 0 * 8 + 4, 9 * 8, Colour.get(0, 333, 333, 333));
		Font.draw(""+Game.version, screen, 0 * 8 + 4, 10 * 8, Colour.get(0, 333, 333, 333));
	}
}
