package com.fls.irh.main.screen;

import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;

public class ReadingMenu extends Menu
{
    private String title = "";
    private String[] content;
    private int maxPages = 0;
    private int currentPage = 1;

    int x = 0, y = 0;

    public ReadingMenu(String title, String[] content) {
        this.title = title;
        if (title != "")
            maxPages++;
        this.content = content;
        if (content != null)
            maxPages++;
        initContentScan(content);
    }

    public void tick() {
        if (this.input.attack.clicked || input.menu.clicked) {
            this.game.setMenu(null);
        }
        if (this.input.left.clicked) {
            if (this.currentPage > 0)
                this.currentPage -= 1;
            if (this.currentPage <= 0)
                this.currentPage = 1;
        }
        if ((this.input.right.clicked) &&
                (this.currentPage < this.maxPages))
            this.currentPage++;
    }

    public void render(Screen screen) {
        Font.renderFrame(screen, "", 4, 0, 15, 14);
        if (this.currentPage == 1)
            Font.draw(this.title, screen, (screen.w - this.title.length() * 7) / 2 + 2, 7 * 8, textColour);
        else {
            for (int i = 0; i < this.content.length; i++) {
                Font.draw(this.content[i], screen, 40 + x, (y * 8) + (i * 8) + 8, textColour);
            }
        }
        Font.draw("<" + this.currentPage + "/" + this.maxPages + ">", screen, 60, 104, textColour);
    }

    private void initContentScan(String[] text) {
        for (int i = 0; i < text.length; i++) {
            if (text[i].contains("/n"))
                this.maxPages += 1;
            if(this.content[i].length() >= 8){
                x = -i;
                y++;
            }
        }
    }

}