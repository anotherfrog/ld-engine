package uk.co.nobber.engine.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.css.Rect;

import uk.co.nobber.engine.entities.Entity;
import uk.co.nobber.engine.entities.EntityManager;
import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.gfx.BlendMode;
import uk.co.nobber.engine.gfx.Font;
import uk.co.nobber.engine.gfx.Spritesheet;
import uk.co.nobber.engine.maths.Vector2f;
import uk.co.nobber.engine.util.Rectangle;
import uk.co.nobber.engine.util.Utils;

public class Level {

	private int width;
	private int height;
	private int xOffset;
	private int yOffset;

	private ArrayList<Rectangle> collision;
	private ArrayList<Rectangle> walls;
	private ArrayList<int[]> layers;
	private Spritesheet spritesheet;
	private ArrayList<Light> lights;

	private Random random = new Random();

	private EntityManager entityManager;

	private Bitmap shadowMap;
	
	private Font font;

	public Level(String path, Spritesheet spritesheet) {
		this.spritesheet = spritesheet;

		this.collision = new ArrayList<Rectangle>();
		this.walls = new ArrayList<Rectangle>();
		this.layers = new ArrayList<int[]>();
		this.lights = new ArrayList<Light>();

		this.entityManager = new EntityManager();

		String jsonText = this.read(path);
		JSONObject map = new JSONObject(jsonText);

		this.width = map.getInt("width");
		this.height = map.getInt("height");
		
		this.font = new Font(new Spritesheet(new Bitmap("/font.png"), 6, 11 ));

		this.shadowMap = new Bitmap(width * spritesheet.getSpriteWidth(), height * spritesheet.getSpriteHeight());

		JSONArray layers = map.getJSONArray("layers");
		for (int i = 0; i < layers.length(); i++) {
			JSONObject layer = layers.getJSONObject(i);
			if (layer.getString("name").startsWith("layer_")) {
				this.layers.add(new int[0]);
				int layerNum = Integer.parseInt(layer.getString("name").split("_")[1]);
				JSONArray data = layer.getJSONArray("data");
				int[] currentLayer = new int[data.length()];
				for (int j = 0; j < data.length(); j++) {
					currentLayer[j] = (data.getInt(j) - 1);
				}
				this.layers.set(layerNum, currentLayer);
			} else if (layer.getString("name").equals("collision")) {
				JSONArray objects = layer.getJSONArray("objects");
				for (int j = 0; j < objects.length(); j++) {
					JSONObject collide = objects.getJSONObject(j);

					this.collision.add(new Rectangle(collide.getInt("x"), collide.getInt("y"), collide.getInt("width"),
							collide.getInt("height")));
				}

			} else if (layer.getString("name").equals("lights")) {
				JSONArray objects = layer.getJSONArray("objects");
				for (int j = 0; j < objects.length(); j++) {
					JSONObject opaque = objects.getJSONObject(j);

					this.walls.add(new Rectangle(opaque.getInt("x"), opaque.getInt("y"), opaque.getInt("width"),
							opaque.getInt("height")));
				}
			}
		}
	}
	
	public void generateShadowMap() {
		
		shadowMap.fill(0xFF000000);
		for (Light light : lights) {

			Vector2f position = light.getPosition();
			ArrayList<Vector2f> intersections = new ArrayList<Vector2f>();

			float step = 5f;
			for (float i = 0f; i <= 360; i += step) {
				int x = (int) (light.getPosition().getX()
						+ width * spritesheet.getSpriteWidth() * Math.cos(i * (Math.PI / 180F)));
				int y = (int) (light.getPosition().getY()
						+ height * spritesheet.getSpriteHeight() * Math.sin(i * (Math.PI / 180F)));

				intersections.add(light.getIntersection(shadowMap, x, y, 0xFFFFFF, walls, entityManager.getEntities()));
//
//				int x1 = (int) (light.getPosition().getX()
//						+ width * spritesheet.getSpriteWidth() * Math.cos((i + step) * (Math.PI / 180F)));
//				int y1 = (int) (light.getPosition().getY()
//						+ height * spritesheet.getSpriteHeight() * Math.sin((i + step) * (Math.PI / 180F)));

//				shadowMap.drawLine((int) light.getPosition().getX(), (int) light.getPosition().getY(), x, y, 0xFFFFFF);
//				shadowMap.fillTriangle(position, light.getIntersection(shadowMap, x, y, 0xFFFFFF, walls),
//						light.getIntersection(shadowMap, x1, y1, 0xFFFFFF, walls), 0xFFFFFF);
			}

			for (Entity e : entityManager.getEntities()) {
				Rectangle rect = e.getRect();
				
				Vector2f topLeft = light.getIntersection(shadowMap, (int) rect.getX(), (int) rect.getY(), 0xFFFFFF,
						walls, entityManager.getEntities());
				Vector2f topRight = light.getIntersection(shadowMap, (int) rect.getX() + rect.getWidth(),
						(int) rect.getY(), 0xFFFFFF, walls, entityManager.getEntities());
				
				Vector2f bottomLeft = light.getIntersection(shadowMap, (int) rect.getX(),
						(int) rect.getY() + rect.getHeight(), 0xFFFFFF, walls, entityManager.getEntities());
				Vector2f bottomRight = light.getIntersection(shadowMap, (int) rect.getX() + rect.getWidth(),
						(int) rect.getY() + rect.getHeight(), 0xFFFFFF, walls, entityManager.getEntities());
				
				intersections.add(topLeft);
				intersections.add(topRight);
				intersections.add(bottomLeft);
				intersections.add(bottomRight);
				
//				System.out.println(Math.toDegrees(getAngle(light.getPosition(), new Vector2f(e.getX(), e.getY()))));
			}
			
			Iterator<Rectangle> rectInter = walls.iterator();
			while(rectInter.hasNext()) {
				Rectangle rect = rectInter.next();
			}
			
			for (Rectangle rect : walls) {
				Vector2f topLeft = light.getIntersection(shadowMap, (int) rect.getX(), (int) rect.getY(), 0xFFFFFF,
						walls, entityManager.getEntities());
				Vector2f topRight = light.getIntersection(shadowMap, (int) rect.getX() + rect.getWidth(),
						(int) rect.getY(), 0xFFFFFF, walls, entityManager.getEntities());

				Vector2f bottomLeft = light.getIntersection(shadowMap, (int) rect.getX(),
						(int) rect.getY() + rect.getHeight(), 0xFFFFFF, walls, entityManager.getEntities());
				Vector2f bottomRight = light.getIntersection(shadowMap, (int) rect.getX() + rect.getWidth(),
						(int) rect.getY() + rect.getHeight(), 0xFFFFFF, walls, entityManager.getEntities());

				intersections.add(topLeft);
				intersections.add(topRight);
				intersections.add(bottomLeft);
				intersections.add(bottomRight);
			}
			
			
			ListIterator<Vector2f> iter = intersections.listIterator();
			while (iter.hasNext()) {
				Vector2f intersection = iter.next();
				if (intersection != null) {
					float theta = (float) getAngle(light.getPosition(), intersection);
					
					int levelWidth = this.width * spritesheet.getSpriteWidth();
					int levelHeight = this.height * spritesheet.getSpriteHeight();
					
					for (float offs = 0.1f; offs < 0.5f; offs+=0.1f) {
						iter.add(light.getIntersection(shadowMap, (int) (light.getPosition().getX() + levelWidth * 2 * Math.cos(theta + 0.00001)), 
								(int) (light.getPosition().getY() + levelHeight * 2 * Math.sin(theta + offs)), 0x00FF00, walls, entityManager.getEntities()));
						
						iter.add(light.getIntersection(shadowMap, (int) (light.getPosition().getX() + levelWidth * 2 * Math.cos(theta - 0.00001)), 
								(int) (light.getPosition().getY() + levelHeight * 2 * Math.sin(theta - offs)), 0x00FF00, walls, entityManager.getEntities()));
					}
					
				}
				
				
			}

			NavigableMap<Float, Vector2f> sorted = this.sort(light, intersections);

			for (Entry<Float, Vector2f> entry : sorted.entrySet()) {
				Vector2f value = entry.getValue();

//				shadowMap.drawLine((int) position.getX(), (int) position.getY(), (int) value.getX(), (int) value.getY(),
//					 0xFFFFFF);

				Entry<Float, Vector2f> next = sorted.higherEntry(entry.getKey());

				try {
//					shadowMap.drawString("" + entry.getKey().intValue(), font, (int) value.getX(), (int) value.getY());
					shadowMap.fillTriangle(position, value, sorted.get(next.getKey()), 0xFFFFFF);
//					shadowMap.drawLine((int) position.getX(), (int) position.getY(), (int) value.getX(), (int) value.getY(), 0xFFFFFF);
				} catch (NullPointerException e) {
					shadowMap.fillTriangle(position, value, sorted.get(sorted.firstKey()), 0xFFFFFF);
				}
			}
		}

	}
	
	public float getAngle(Vector2f origin, Vector2f position) {
		float a = position.getX() - origin.getX();
		float o = position.getY() - origin.getY();
		float h = (float) Math.sqrt(a * a + o * o);

		float theta = (float) (Math.atan2(o, a));
		
		return theta;
	}

	private NavigableMap<Float, Vector2f> sort(Light light, ArrayList<Vector2f> intersections) {
		Vector2f lightPosition = light.getPosition();
		
		NavigableMap<Float, Vector2f> angleToIntersection = new TreeMap<Float, Vector2f>();

		for (int i = 0; i < intersections.size(); i++) {
			Vector2f intersection = intersections.get(i);

			angleToIntersection.put(getAngle(lightPosition, intersection), intersection);
		}

		return angleToIntersection;
	}

	public void addEntity(Entity entity) {
		this.entityManager.add(entity);
	}

	public void update() {
		this.entityManager.update();
	}

	public void render(Bitmap bitmap) {
		for (int layer = 0; layer < this.layers.size(); layer++) {
			renderLayer(bitmap, layer);
		}

		this.entityManager.render(bitmap);

		bitmap.blendBlit(shadowMap, xOffset, yOffset, BlendMode.ADD);
	}

	private void renderLayer(Bitmap bitmap, int layer) {
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				int tile = ((int[]) this.layers.get(layer))[(x + y * this.width)];
				if (tile > -1) {
					int xTile = tile % spritesheet.getSheetWidthInSprites();
					int yTile = tile / spritesheet.getSheetHeightInSprites();
					bitmap.blit(spritesheet.getSprite(xTile, yTile), x * spritesheet.getSpriteWidth() + xOffset,
							y * spritesheet.getSpriteHeight() + this.yOffset);
				}
			}
		}
	}

	public boolean collide(Rectangle check) {
		for (Rectangle rectangle : this.collision) {
			if (Utils.collision(rectangle, check)) {
				return true;
			}
		}
		return false;
	}

	public void renderColl(Bitmap bitmap, int xOffset, int yOffset) {
		for (Rectangle rectangle : this.collision) {
			bitmap.blendFillRectangle((int) (rectangle.getX() + xOffset), (int) (rectangle.getY() + yOffset), (int) rectangle.getWidth(),
					(int) rectangle.getHeight(), 0xAAFFFFFF, BlendMode.ADD);
		}

		for (Rectangle rectangle : this.walls) {
			bitmap.blendFillRectangle((int) (rectangle.getX() + xOffset), (int) (rectangle.getY() + yOffset), (int) rectangle.getWidth(),
					(int) rectangle.getHeight(), 0xAA424242, BlendMode.ADD);
		}
	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public void offset(int xa, int ya) {
		this.xOffset += xa;
		this.yOffset += ya;
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public int getXOffset() {
		return this.xOffset;
	}

	public int getYOffset() {
		return this.yOffset;
	}

	private String read(String path) {
		StringBuilder result = new StringBuilder("");

		// ClassLoader classLoader = getClass().getClassLoader();
		// URL url = classLoader.getResource(new File(path));
		// File file = new File(url.getFile());

		File file = new File(this.getClass().getResource(path).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	public Bitmap getShadowMap() {
		return this.shadowMap;
	}

	public ArrayList<Light> getLights() {
		return this.lights;
	}
}
