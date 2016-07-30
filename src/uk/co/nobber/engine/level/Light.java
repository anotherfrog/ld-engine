package uk.co.nobber.engine.level;

import java.util.ArrayList;

import uk.co.nobber.engine.entities.Entity;
import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.maths.Vector2f;
import uk.co.nobber.engine.util.Rectangle;
import uk.co.nobber.engine.util.Utils;

public class Light {

	private Vector2f position;

	public Light(Vector2f position) {
		this.position = position;
	}
		
	public Vector2f getIntersection(Bitmap bitmap, float x2, float y2, float colour, ArrayList<Rectangle> walls, ArrayList<Entity> entities) {
		float x = (float) position.getX();
		float y = (float) position.getY();
		
		Rectangle lightRect = new Rectangle(x, y, 1, 1);
		
		float w = x2 - x;
		float h = y2 - y;
		float dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		
		float longest = Math.abs(w);
		float shortest = Math.abs(h);
		
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		
		float numerator = longest / 2f;
		for (float i = 0; i <= longest; i++) {
//			bitmap.setPixel(x, y, colour);
			
			for (Rectangle rect : walls) {
				if (Utils.collision(lightRect, rect)) {
//					bitmap.fillRectangle(x, y, 4, 4, (float) Math.floor(Math.random() * 0xFFFFFF));
					return new Vector2f(x, y);
				}
			}
			
			for (Entity e : entities) {
				if (Utils.collision(lightRect, e.getRect())) {
					return new Vector2f(x, y);
				}
			}
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
				
				lightRect.setX(x);
				lightRect.setY(y);
			} else {
				x += dx2;
				y += dy2;
				
				lightRect.setX(x);
				lightRect.setY(y);
			}
		}
		
//		bitmap.drawLine((float) position.getX(), (float) position.getY(), x2,y2,0xFFFFFF);
		
		return new Vector2f(x2, y2);
	}
		
	public Vector2f getPosition() {
		return this.position;
	}
	
	public void setPosition(float x, float y) {
		this.position.setX(x);
		this.position.setY(y);
	}
}
