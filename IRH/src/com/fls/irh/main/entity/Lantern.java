package com.fls.irh.main.entity;

import com.fls.irh.main.gfx.Colour;

public class Lantern extends Furniture {
	public Lantern() {
		super("Lantern");
		col = Colour.get(-1, 000, 111, 555);
		sprite = 5;
		xr = 3;
		yr = 2;
	}

	public int getLightRadius() {
		return 8;
	}
}