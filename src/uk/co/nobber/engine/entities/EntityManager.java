package uk.co.nobber.engine.entities;

import java.util.ArrayList;

import uk.co.nobber.engine.gfx.Bitmap;

public class EntityManager {

	private ArrayList<Entity> entities;
	
	public EntityManager() {
		this.entities = new ArrayList<Entity>();
	}
	
	public void update() {
		for (Entity e : this.entities) {
			e.update();
		}
	}
	
	public void render(Bitmap bitmap) {
		for (Entity e : this.entities) {
			e.render(bitmap);
		}
	}
	
	public void add(Entity entity) {
		this.entities.add(entity);
	}
	
	public ArrayList<Entity> getEntities() {
		return this.entities;
	}
}
