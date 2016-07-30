package uk.co.nobber.engine.level;

import uk.co.nobber.engine.gfx.Bitmap;

public class TileVoid extends Tile {

	public TileVoid(int x, int y, Bitmap bitmap) {
		super(x, y, bitmap);
	}

	@Override
	public boolean isSolid() {
		return true;
	}

}
