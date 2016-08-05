package uk.co.nobber.engine.level;

import uk.co.nobber.engine.gfx.Bitmap;

public abstract class Tile {

	private int x;
	private int y;
	
	private Bitmap bitmap;

	public Tile() {
		this.x = -1;
		this.y = -1;
		
		this.bitmap = new Bitmap(16, 16);
	}
	
	public Tile(int x, int y, Bitmap bitmap) {
		this.x = x;
		this.y = y;
		
		this.bitmap = bitmap;
	}

	public void update() { }
	public void render(Bitmap screen, int tileSize) {
		screen.blit(bitmap, this.x * tileSize, this.y * tileSize);
	}
	
	public abstract boolean isSolid();
}
