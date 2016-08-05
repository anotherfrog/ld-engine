package uk.co.nobber.engine.entities;

import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.level.Level;
import uk.co.nobber.engine.util.Rectangle;

public class Entity {

	private int x;
	private int y;
	private int width;
	private int height;
	
	private Rectangle collisionMask;
	
	private Bitmap bitmap;
	
	public Entity(int x, int y, int width, int height, Bitmap bitmap) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.collisionMask = new Rectangle(x, y, width, height);
		
		this.bitmap = bitmap;
	}
	
	public void update() {
		
	}
	
	public void move(int xa, int ya, Level level) {
		Rectangle mask = new Rectangle(collisionMask.getX() + xa, collisionMask.getY() + ya, width, height);
		if (level.collide(mask)) return;
		this.x += xa;
		this.y += ya;
		this.collisionMask.add(xa, ya);
	}
	
	public void render(Bitmap screen) {
		screen.blit(bitmap, x, y);
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
}
