package com.fls.irh.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.gfx.Colour;
import com.fls.irh.main.gfx.Font;
import com.fls.irh.main.gfx.Screen;
import com.fls.irh.main.gfx.SpriteSheet;
import com.fls.irh.main.level.Level;
import com.fls.irh.main.level.tile.Tile;
import com.fls.irh.main.screen.DeadMenu;
import com.fls.irh.main.screen.LevelTransitionMenu;
import com.fls.irh.main.screen.Menu;
import com.fls.irh.main.screen.TitleMenu;
import com.fls.irh.main.screen.WonMenu;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable{
    public static final String NAME = "IRH";
    public static final int HEIGHT = 120;
    public static final int WIDTH = 160;
    public static final String version = "V0.7 A";
    private BufferedImage image = new BufferedImage(160, 120, 1);
    private int[] pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
    private boolean running = false;
    private Screen screen;
    private Screen lightScreen;
    private InputHandler input = new InputHandler(this);

    private int[] colors = new int[256];
    private int tickCount = 0;
    public int gameTime = 0;
    private Level level;
    private Level[] levels = new Level[5];
    private int currentLevel = 3;
    public Player player;
    public Menu menu;
    private int playerDeadTime;
    private int pendingLevelChange;
    private int wonTimer = 0;
    public boolean hasWon = false;

    public void setMenu(Menu menu) {
        this.menu = menu;
        if (menu != null)
            menu.init(this, this.input);
    }

    public void start()
    {
        this.running = true;
        new Thread(this).start();
    }

    public void stop() {
        this.running = false;
    }

    public void resetGame() {
        this.playerDeadTime = 0;
        this.wonTimer = 0;
        this.gameTime = 0;
        this.hasWon = false;

        this.levels = new Level[4];
        this.currentLevel = 3;

        this.levels[3] = new Level(128, 128, 0, null);
        this.levels[2] = new Level(128, 128, -1, this.levels[3]);
        this.levels[1] = new Level(128, 128, -2, this.levels[2]);
        this.levels[0] = new Level(128, 128, -3, this.levels[1]);

        this.level = this.levels[this.currentLevel];
        this.player = new Player(this, this.input);
        this.player.findStartPos(this.level);

        this.level.add(this.player);

    }

    private void init()
    {
        int pp = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = r * 255 / 5;
                    int gg = g * 255 / 5;
                    int bb = b * 255 / 5;
                    int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

                    int r1 = (rr + mid * 1) / 2 * 230 / 255 + 10;
                    int g1 = (gg + mid * 1) / 2 * 230 / 255 + 10;
                    int b1 = (bb + mid * 1) / 2 * 230 / 255 + 10;
                    this.colors[(pp++)] = (r1 << 16 | g1 << 8 | b1);
                }
            }
        }
        try
        {
            this.screen = new Screen(160, 120, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
            this.lightScreen = new Screen(160, 120, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        resetGame();
        setMenu(new TitleMenu());
    }

    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0.0D;
        double nsPerTick = 16666666.666666666D;
        int frames = 0;
        int ticks = 0;
        long lastTimer1 = System.currentTimeMillis();

        init();

        while (this.running) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (unprocessed >= 1.0D) {
                ticks++;
                tick();
                unprocessed -= 1.0D;
                shouldRender = true;
            }
            try
            {
                Thread.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer1 > 1000L) {
                lastTimer1 += 1000L;
                System.out.println(ticks + " ticks, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        this.tickCount += 1;
        if (!hasFocus()) {
            this.input.releaseAll();
        } else {
            if ((!this.player.removed) && (!this.hasWon))
                this.gameTime += 1;

            this.input.tick();
            if (this.menu != null) {
                this.menu.tick();
            } else {
                if (this.player.removed) {
                    this.playerDeadTime += 1;
                    if (this.playerDeadTime > 60) {
                        setMenu(new DeadMenu());
                    }
                }
                else if (this.pendingLevelChange != 0) {
                    setMenu(new LevelTransitionMenu(this.pendingLevelChange));
                    this.pendingLevelChange = 0;
                }

                if ((this.wonTimer > 0) &&
                        (--this.wonTimer == 0)) {
                    setMenu(new WonMenu());
                }

                this.level.tick();
                Tile.tickCount += 1;
            }
        }
    }

    public void changeLevel(int dir) {
        this.level.remove(this.player);
        this.currentLevel += dir;
        this.level = this.levels[this.currentLevel];
        this.player.x = ((this.player.x >> 4) * 16 + 8);
        this.player.y = ((this.player.y >> 4) * 16 + 8);
        this.level.add(this.player);
    }

    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        int xScroll = this.player.x - this.screen.w / 2;
        int yScroll = this.player.y - (this.screen.h - 8) / 2;
        if (xScroll < 16)
            xScroll = 16;
        if (yScroll < 16)
            yScroll = 16;
        if (xScroll > this.level.w * 16 - this.screen.w - 16)
            xScroll = this.level.w * 16 - this.screen.w - 16;
        if (yScroll > this.level.h * 16 - this.screen.h - 16)
            yScroll = this.level.h * 16 - this.screen.h - 16;
        if (this.currentLevel > 3) {
            int col = Colour.get(20, 20, 121, 121);
            for (int y = 0; y < 14; y++) {
                for (int x = 0; x < 24; x++) {
                    this.screen.render(x * 8 - (xScroll / 4 & 0x7), y * 8 - (yScroll / 4 & 0x7), 0, col, 0);
                }
            }
        }
        this.level.renderBackground(this.screen, xScroll, yScroll);
        this.level.renderSprites(this.screen, xScroll, yScroll);

        if (this.currentLevel < 3) {
            this.lightScreen.clear(0);
            this.level.renderLight(this.lightScreen, xScroll, yScroll);
            this.screen.overlay(this.lightScreen, xScroll, yScroll);
        }

        renderGui();

        if (!hasFocus())
            renderFocusNagger();

        for (int y = 0; y < this.screen.h; y++) {
            for (int x = 0; x < this.screen.w; x++) {
                int cc = this.screen.pixels[(x + y * this.screen.w)];
                if (cc < 255)
                    this.pixels[(x + y * 160)] = this.colors[cc];
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());

        int ww = 480;
        int hh = 360;
        int xo = (getWidth() - ww) / 2;
        int yo = (getHeight() - hh) / 2;
        g.drawImage(this.image, xo, yo, ww, hh, null);
        g.dispose();
        bs.show();
    }

    private void renderGui() {
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 20; x++) {
                this.screen.render(x * 8, this.screen.h - 16 + y * 8, 384, Colour.get(0, 0, 0, 0), 0);
            }
        }

        for (int i = 0; i < 10; i++) {
            if (i < this.player.health)
                this.screen.render(i * 8, this.screen.h - 16, 384, Colour.get(0, 200, 500, 533), 0);
            else {
                this.screen.render(i * 8, this.screen.h - 16, 384, Colour.get(0, 100, 0, 0), 0);
            }
            if (this.player.staminaRechargeDelay > 0) {
                if (this.player.staminaRechargeDelay / 4 % 2 == 0)
                    this.screen.render(i * 8, this.screen.h - 8, 385, Colour.get(0, 555, 0, 0), 0);
                else
                    this.screen.render(i * 8, this.screen.h - 8, 385, Colour.get(0, 110, 0, 0), 0);
            }
            else if (i < this.player.stamina)
                this.screen.render(i * 8, this.screen.h - 8, 385, Colour.get(0, 220, 550, 553), 0);
            else {
                this.screen.render(i * 8, this.screen.h - 8, 385, Colour.get(0, 110, 0, 0), 0);
            }
        }
        if (this.player.activeItem != null) {
            this.player.activeItem.renderInventory(this.screen, 80, this.screen.h - 16);
        }

        if (this.menu != null)
            this.menu.render(this.screen);
    }

    private void renderFocusNagger()
    {
        String msg = "Click to focus!";
        int xx = (160 - msg.length() * 8) / 2;
        int yy = 56;
        int w = msg.length();
        int h = 1;

        this.screen.render(xx - 8, yy - 8, 416, Colour.get(-1, 1, 5, 445), 0);
        this.screen.render(xx + w * 8, yy - 8, 416, Colour.get(-1, 1, 5, 445), 1);
        this.screen.render(xx - 8, yy + 8, 416, Colour.get(-1, 1, 5, 445), 2);
        this.screen.render(xx + w * 8, yy + 8, 416, Colour.get(-1, 1, 5, 445), 3);
        for (int x = 0; x < w; x++) {
            this.screen.render(xx + x * 8, yy - 8, 417, Colour.get(-1, 1, 5, 445), 0);
            this.screen.render(xx + x * 8, yy + 8, 417, Colour.get(-1, 1, 5, 445), 2);
        }
        for (int y = 0; y < h; y++) {
            this.screen.render(xx - 8, yy + y * 8, 418, Colour.get(-1, 1, 5, 445), 0);
            this.screen.render(xx + w * 8, yy + y * 8, 418, Colour.get(-1, 1, 5, 445), 1);
        }

        if (this.tickCount / 20 % 2 == 0)
            Font.draw(msg, this.screen, xx, yy, Colour.get(5, 333, 333, 333));
        else
            Font.draw(msg, this.screen, xx, yy, Colour.get(5, 555, 555, 555));
    }

    public void scheduleLevelChange(int dir)
    {
        this.pendingLevelChange = dir;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setMinimumSize(new Dimension(480, 360));
        game.setMaximumSize(new Dimension(480, 360));
        game.setPreferredSize(new Dimension(480, 360));

        JFrame frame = new JFrame("IRH");
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());
        frame.add(game, "Center");
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    public void won() {
        this.wonTimer = 180;
        this.hasWon = true;
    }
}