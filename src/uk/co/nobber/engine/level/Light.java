package uk.co.nobber.engine.level;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import uk.co.nobber.engine.entities.Entity;
import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.maths.Vector2f;
import uk.co.nobber.engine.util.Rectangle;
import uk.co.nobber.engine.util.Utils;

public class Light {

	private Vector2f position;
	private float radius;
	
	private int colour;
	
	private ArrayList<Vector2f> intersections;

	public Light(Vector2f position, float radius, int colour) {
		this.position = position;
		this.radius = radius;
		
		this.colour = colour;
		
		this.intersections = new ArrayList<Vector2f>();
	}
	
	public Light(Vector2f position) {
		this(position, Integer.MAX_VALUE, 0xFFFFFF);
	}
	
	public void trace(Bitmap shadowMap, ArrayList<Rectangle> walls, ArrayList<Entity> entities, int levelWidth, int levelHeight) {
		this.intersections.clear();
		
		int step = 5;
		for (int i = 0; i < 360; i += step) {
			float x = position.getX() + levelWidth *  (float) Math.cos(i * (Math.PI / 180F));
			float y = position.getY() + levelHeight *  (float) Math.sin(i * (Math.PI / 180F));
			
			intersections.add(getIntersection(shadowMap, x, y, walls, entities));
		}
		
		for (Entity entity : entities) {
			Rectangle rect = entity.getRect();
			
			intersections.add(getIntersection(shadowMap, rect.getX(), rect.getY(),
					walls, entities));
			intersections.add(getIntersection(shadowMap, rect.getX() + rect.getWidth(),
					rect.getY(), walls, entities));
			
			intersections.add(getIntersection(shadowMap, rect.getX(), 
					rect.getY() + rect.getHeight(), walls, entities));
			intersections.add(getIntersection(shadowMap, rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight(), walls, entities));
		}
		
		for (Rectangle rect : walls) {
			intersections.add(getIntersection(shadowMap, rect.getX(), (float) rect.getY(),
					walls, entities));
			intersections.add(getIntersection(shadowMap, rect.getX() + rect.getWidth(),
					rect.getY(), walls, entities));

			intersections.add(getIntersection(shadowMap, rect.getX(),
					rect.getY() + rect.getHeight(), walls, entities));
			intersections.add(getIntersection(shadowMap, rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight(), walls, entities));
		}
		
		ListIterator<Vector2f> iter = intersections.listIterator();
		while (iter.hasNext()) {
			Vector2f intersection = iter.next();
			if (intersection != null) {
				float theta = Utils.getAngle(position, intersection);
				
				for (float offs = 0.01f; offs < 0.03f; offs += 0.01f) {
					iter.add(getIntersection(shadowMap, position.getX() + levelWidth * 2 * (float) Math.cos(theta + offs), 
							position.getY() + levelHeight * 2 * (float) Math.sin(theta + offs), walls, entities));
					
					iter.add(getIntersection(shadowMap, position.getX() + levelWidth * 2 * (float) Math.cos(theta - offs), 
							position.getY() + levelHeight * 2 * (float) Math.sin(theta - offs), walls, entities));
				}
				
			}
		}
		
		NavigableMap<Float, Vector2f> sorted = this.sortIntersections(position, intersections);

		for (Entry<Float, Vector2f> entry : sorted.entrySet()) {
			Vector2f value = entry.getValue();

			Entry<Float, Vector2f> next = sorted.higherEntry(entry.getKey());

			try {
				shadowMap.fillTriangle(position, value, sorted.get(next.getKey()), colour);
//				shadowMap.drawLineF(value.getX(), value.getY(), light.getPosition().getX(), light.getPosition().getY(), 0xFF4242);
			} catch (NullPointerException e) {
				shadowMap.fillTriangle(position, value, sorted.get(sorted.firstKey()), colour);
			}
		}
	}
		
	public Vector2f getIntersection(Bitmap bitmap, float x2, float y2, ArrayList<Rectangle> walls, ArrayList<Entity> entities) {
		float x = position.getX();
		float y = position.getY();
		
		Rectangle lightRect = new Rectangle(x, y, 1, 1);
		
		float w = (x2 - x);
		float h = (y2 - y);
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
			for (Rectangle rect : walls) {
				if (Utils.collision(lightRect, rect)) {
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
			
			if (x < -10 || x >= bitmap.getWidth() + 10 || y < -10 || y >= bitmap.getHeight() + 10) {
				return new Vector2f(x, y);
			} else if (Utils.distance(position.getX(), position.getY(), x, y) >= radius) {
				return new Vector2f(x, y);
			}
		}
		
		return new Vector2f(x2, y2);
	}
	
	private NavigableMap<Float, Vector2f> sortIntersections(Vector2f lightPosition, ArrayList<Vector2f> intersections) {
		NavigableMap<Float, Vector2f> angleToIntersection = new TreeMap<Float, Vector2f>();

		for (int i = 0; i < intersections.size(); i++) {
			Vector2f intersection = intersections.get(i);

			angleToIntersection.put(Utils.getAngle(lightPosition, intersection), intersection);
		}

		return angleToIntersection;
	}
	
	public Vector2f getPosition() {
		return this.position;
	}
	
	public void setPosition(float x, float y) {
		this.position.setX(x);
		this.position.setY(y);
	}

}
