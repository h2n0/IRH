package com.fls.irh.main.level.levelgen;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.fls.irh.main.level.tile.Tile;

public class LevelGen
{
    private static final Random random = new Random();
    public double[] values;
    private int w;
    private int h;

    public LevelGen(int w, int h, int featureSize)
    {
        this.w = w;
        this.h = h;

        this.values = new double[w * h];

        for (int y = 0; y < w; y += featureSize) {
            for (int x = 0; x < w; x += featureSize) {
                setSample(x, y, random.nextFloat() * 2.0F - 1.0F);
            }
        }

        int stepSize = featureSize;
        double scale = 1.0D / w;
        double scaleMod = 1.0D;
        do {
            int halfStep = stepSize / 2;
            for (int y = 0; y < w; y += stepSize) {
                for (int x = 0; x < w; x += stepSize) {
                    double a = sample(x, y);
                    double b = sample(x + stepSize, y);
                    double c = sample(x, y + stepSize);
                    double d = sample(x + stepSize, y + stepSize);

                    double e = (a + b + c + d) / 4.0D + (random.nextFloat() * 2.0F - 1.0F) * stepSize * scale;
                    setSample(x + halfStep, y + halfStep, e);
                }
            }
            for (int y = 0; y < w; y += stepSize) {
                for (int x = 0; x < w; x += stepSize) {
                    double a = sample(x, y);
                    double b = sample(x + stepSize, y);
                    double c = sample(x, y + stepSize);
                    double d = sample(x + halfStep, y + halfStep);
                    double e = sample(x + halfStep, y - halfStep);
                    double f = sample(x - halfStep, y + halfStep);

                    double H = (a + b + d + e) / 4.0D + (random.nextFloat() * 2.0F - 1.0F) * stepSize * scale * 0.5D;
                    double g = (a + c + d + f) / 4.0D + (random.nextFloat() * 2.0F - 1.0F) * stepSize * scale * 0.5D;
                    setSample(x + halfStep, y, H);
                    setSample(x, y + halfStep, g);
                }
            }
            stepSize /= 2;
            scale *= (scaleMod + 0.8D);
            scaleMod *= 0.3D;
        } while (stepSize > 1);
    }

    private double sample(int x, int y) {
        return this.values[((x & this.w - 1) + (y & this.h - 1) * this.w)];
    }

    private void setSample(int x, int y, double value) {
        this.values[((x & this.w - 1) + (y & this.h - 1) * this.w)] = value;
    }

    public static byte[][] createAndValidateTopMap(int w, int h) {
        byte[][] result;
        int[] count;
        do {
            result = createTopMap(w, h);

            count = new int[256];

            for (int i = 0; i < w * h; i++)
                count[(result[0][i] & 0xFF)] += 1;
        } while ((count[(Tile.rock.id & 0xFF)] < 100) ||
                (count[(Tile.sand.id & 0xFF)] < 100) ||
                (count[(Tile.grass.id & 0xFF)] < 100) ||
                (count[(Tile.tree.id & 0xFF)] < 100) ||
                (count[(Tile.stairsDown.id & 0xFF)] == 0));

        return result;
    }

    public static byte[][] createAndValidateUndergroundMap(int w, int h, int depth) {
        byte[][] result;
        int[] count;
        do {
            result = createUndergroundMap(w, h, depth);

            count = new int[256];

            for (int i = 0; i < w * h; i++)
                count[(result[0][i] & 0xFF)] += 1;
        } while ((count[(Tile.rock.id & 0xFF)] < 100) ||
                (count[(Tile.dirt.id & 0xFF)] < 100) ||
                (count[((Tile.ironOre.id & 0xFF) + depth - 1)] < 20) || (
                (depth < 3) && (count[(Tile.stairsDown.id & 0xFF)] < 2)));

        return result;
    }

    private static byte[][] createTopMap(int w, int h) {
        LevelGen mnoise1 = new LevelGen(w, h, 16);
        LevelGen mnoise2 = new LevelGen(w, h, 16);
        LevelGen mnoise3 = new LevelGen(w, h, 16);

        LevelGen noise1 = new LevelGen(w, h, 32);
        LevelGen noise2 = new LevelGen(w, h, 32);

        byte[] map = new byte[w * h];
        byte[] data = new byte[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = x + y * w;

                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3.0D - 2.0D;
                double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
                mval = Math.abs(mval - mnoise3.values[i]) * 3.0D - 2.0D;

                double xd = x / (w - 1.0D) * 2.0D - 1.0D;
                double yd = y / (h - 1.0D) * 2.0D - 1.0D;
                if (xd < 0.0D)
                    xd = -xd;
                if (yd < 0.0D)
                    yd = -yd;
                double dist = xd >= yd ? xd : yd;
                dist = dist * dist * dist * dist;
                dist = dist * dist * dist * dist;
                val = val + 1.0D - dist * 20.0D;

                if (val < -0.5D)
                    map[i] = Tile.water.id;
                else if ((val > 0.5D) && (mval < -1.5D))
                    map[i] = Tile.rock.id;
                else {
                    map[i] = Tile.grass.id;
                }
            }
        }

        for (int i = 0; i < w * h / 2800; i++) {
            int xs = random.nextInt(w);
            int ys = random.nextInt(h);
            for (int k = 0; k < 10; k++) {
                int x = xs + random.nextInt(21) - 10;
                int y = ys + random.nextInt(21) - 10;
                for (int j = 0; j < 100; j++) {
                    int xo = x + random.nextInt(5) - random.nextInt(5);
                    int yo = y + random.nextInt(5) - random.nextInt(5);
                    for (int yy = yo - 1; yy <= yo + 1; yy++) {
                        for (int xx = xo - 1; xx <= xo + 1; xx++) {
                            if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h) &&
                                    (map[(xx + yy * w)] == Tile.grass.id)) {
                                map[(xx + yy * w)] = Tile.sand.id;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            for (int j = 0; j < 200; j++) {
                int xx = x + random.nextInt(15) - random.nextInt(15);
                int yy = y + random.nextInt(15) - random.nextInt(15);
                if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h) &&
                        (map[(xx + yy * w)] == Tile.grass.id)) {
                    map[(xx + yy * w)] = Tile.tree.id;
                }
            }

        }

        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int col = random.nextInt(4);
            for (int j = 0; j < 30; j++) {
                int xx = x + random.nextInt(5) - random.nextInt(5);
                int yy = y + random.nextInt(5) - random.nextInt(5);
                if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h) &&
                        (map[(xx + yy * w)] == Tile.grass.id)) {
                    map[(xx + yy * w)] = Tile.flower.id;
                    data[(xx + yy * w)] = ((byte) (col + random.nextInt(4) * 16));
                }
            }

        }

        for (int i = 0; i < w * h / 100; i++) {
            int xx = random.nextInt(w);
            int yy = random.nextInt(h);
            if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h) &&
                    (map[(xx + yy * w)] == Tile.sand.id)) {
                map[(xx + yy * w)] = Tile.cactus.id;
            }

        }

        int count = 0;
        for (int i = 0; i < w * h / 100; i++) {
            int x = random.nextInt(w - 2) + 1;
            int y = random.nextInt(h - 2) + 1;

            for (int yy = y - 1; yy <= y + 1; yy++) {
                for (int xx = x - 1; xx <= x + 1; xx++)
                    if (map[(xx + yy * w)] != Tile.rock.id)
                        break;
            }
            map[(x + y * w)] = Tile.stairsDown.id;
            count++;
            if (count == 4)
                break;
        }
        return new byte[][] { map, data };
    }

    private static byte[][] createUndergroundMap(int w, int h, int depth) {
        LevelGen mnoise1 = new LevelGen(w, h, 16);
        LevelGen mnoise2 = new LevelGen(w, h, 16);
        LevelGen mnoise3 = new LevelGen(w, h, 16);

        LevelGen nnoise1 = new LevelGen(w, h, 16);
        LevelGen nnoise2 = new LevelGen(w, h, 16);
        LevelGen nnoise3 = new LevelGen(w, h, 16);

        LevelGen wnoise1 = new LevelGen(w, h, 16);
        LevelGen wnoise2 = new LevelGen(w, h, 16);
        LevelGen wnoise3 = new LevelGen(w, h, 16);

        LevelGen noise1 = new LevelGen(w, h, 32);
        LevelGen noise2 = new LevelGen(w, h, 32);

        byte[] map = new byte[w * h];
        byte[] data = new byte[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = x + y * w;

                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3.0D - 2.0D;

                double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
                mval = Math.abs(mval - mnoise3.values[i]) * 3.0D - 2.0D;

                double nval = Math.abs(nnoise1.values[i] - nnoise2.values[i]);
                nval = Math.abs(nval - nnoise3.values[i]) * 3.0D - 2.0D;

                double wval = Math.abs(wnoise1.values[i] - wnoise2.values[i]);
                wval = Math.abs(nval - wnoise3.values[i]) * 3.0D - 2.0D;

                double xd = x / (w - 1.0D) * 2.0D - 1.0D;
                double yd = y / (h - 1.0D) * 2.0D - 1.0D;
                if (xd < 0.0D)
                    xd = -xd;
                if (yd < 0.0D)
                    yd = -yd;
                double dist = xd >= yd ? xd : yd;
                dist = dist * dist * dist * dist;
                dist = dist * dist * dist * dist;
                val = val + 1.0D - dist * 20.0D;

                if ((val > -2.0D) && (wval < -2.0D + depth / 2 * 3)) {
                    if (depth > 2)
                        map[i] = Tile.lava.id;
                    else
                        map[i] = Tile.water.id;
                } else if ((val > -2.0D) && ((mval < -1.7D) || (nval < -1.4D)))
                    map[i] = Tile.dirt.id;
                else {
                    map[i] = Tile.rock.id;
                }
            }

        }

        int r = 2;
        for (int i = 0; i < w * h / 400; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            for (int j = 0; j < 30; j++) {
                int xx = x + random.nextInt(5) - random.nextInt(5);
                int yy = y + random.nextInt(5) - random.nextInt(5);
                if ((xx >= r) && (yy >= r) && (xx < w - r) && (yy < h - r) &&
                        (map[(xx + yy * w)] == Tile.rock.id)) {
                    map[(xx + yy * w)] = ((byte) ((Tile.ironOre.id & 0xFF) + depth - 1));
                }

            }

        }

        if (depth < 3) {
            int count = 0;
            for (int i = 0; i < w * h / 100; i++) {
                int x = random.nextInt(w - 20) + 10;
                int y = random.nextInt(h - 20) + 10;

                for (int yy = y - 1; yy <= y + 1; yy++) {
                    for (int xx = x - 1; xx <= x + 1; xx++)
                        if (map[(xx + yy * w)] != Tile.rock.id)
                            break;
                }
                map[(x + y * w)] = Tile.stairsDown.id;
                count++;
                if (count == 4)
                    break;
            }
        }
        return new byte[][] { map, data };
    }

    public static void main(String[] args) {
        int d = 0;
        System.out.println("LevelGen Start!");
        while (true) {
            int w = 128;
            int h = 128;

            byte[] map = createAndValidateTopMap(w, h)[0];

            BufferedImage img = new BufferedImage(w, h, 1);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = x + y * w;

                    if (map[i] == Tile.water.id)
                        pixels[i] = 128;
                    if (map[i] == Tile.grass.id)
                        pixels[i] = 2129952;
                    if (map[i] == Tile.rock.id)
                        pixels[i] = 10526880;
                    if (map[i] == Tile.dirt.id)
                        pixels[i] = 6307904;
                    if (map[i] == Tile.sand.id)
                        pixels[i] = 10526784;
                    if (map[i] == Tile.tree.id)
                        pixels[i] = 12288;
                    if (map[i] == Tile.lava.id)
                        pixels[i] = 16719904;
                    if (map[i] == Tile.stairsDown.id)
                        pixels[i] = 16777215;
                    if (map[i] == Tile.stairsUp.id)
                        pixels[i] = 16777215;
                }
            }
            img.setRGB(0, 0, w, h, pixels, 0, w);
            JOptionPane.showMessageDialog(null, null, "Another", 0, new ImageIcon(img.getScaledInstance(w * 4, h * 4, 16)));
        }
    }
}