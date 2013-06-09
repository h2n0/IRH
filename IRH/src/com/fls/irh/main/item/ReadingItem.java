package com.fls.irh.main.item;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;
import com.fls.irh.main.item.book.Book;
import com.fls.irh.main.level.Level;
import com.fls.irh.main.level.tile.Tile;
import com.fls.irh.main.screen.ReadingMenu;

public class ReadingItem extends Item {
    private Book book;
    private boolean read = false;

    public ReadingItem(Book book) {
        this.book = book;
    }

    public int getSprite() {
        return 136;
    }

    public int getColour() {
        return Colour.get(-1, 110, this.book.getCoverColour(), 552);
    }

    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColour(), 0);
    }

    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColour(), 0);
        Font.draw(getName(), screen, x + 8, y, !this.read ? Colour.get(-1, -1, -1, 550) : Colour.get(-1, 555, 555, 555));
    }

    public String getName() {
        return this.book.getTitle();
    }

    public boolean interactOn(Tile t, Level l, int x, int y, Player p, int d) {
        p.game.setMenu(new ReadingMenu(this.book.getTitle(), this.book.getContent()));
        this.read = true;
        return true;
    }
}