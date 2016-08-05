package uk.co.nobber.engine.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.nobber.engine.entities.Entity;
import uk.co.nobber.engine.entities.EntityManager;
import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.gfx.BlendMode;
import uk.co.nobber.engine.gfx.Spritesheet;
import uk.co.nobber.engine.maths.Vector2f;
import uk.co.nobber.engine.util.Rectangle;
import uk.co.nobber.engine.util.Utils;

public class Level {

	private int width;
	private int height;
	private int xOffset;
	private int yOffset;
	private int shadowMapWidth;
	private int shadowMapHeight;

	private String jsonText;
	private JSONObject map;
	
	private ArrayList<Rectangle> collision;
	private ArrayList<Rectangle> walls;
	private ArrayList<int[]> layers;
	private Spritesheet spritesheet;
	private ArrayList<Light> lights;

	private EntityManager entityManager;

	private Bitmap shadowMap;

	public Level(String path, Spritesheet spritesheet, int shadowMapWidth, int shadowMapHeight) {
		this.spritesheet = spritesheet;

		this.collision = new ArrayList<Rectangle>();
		this.walls = new ArrayList<Rectangle>();
		this.layers = new ArrayList<int[]>();
		this.lights = new ArrayList<Light>();

		this.entityManager = new EntityManager();

		this.jsonText = this.read(path);
		this.map = new JSONObject(jsonText);

		this.width = map.getInt("width");
		this.height = map.getInt("height");
		
		this.shadowMapWidth = shadowMapWidth;
		this.shadowMapHeight = shadowMapHeight;
		
		this.shadowMap = new Bitmap(shadowMapWidth, shadowMapHeight);
		
		this.genMap();
	}
	
	public void generateShadowMap(Bitmap screen) {
		int clearColour = 0xAAAAAA;
		shadowMap.fill(clearColour);
		
		for (Light light : lights) {
			light.trace(shadowMap, walls, entityManager.getEntities(), width * spritesheet.getSpriteWidth(), height * spritesheet.getSpriteHeight());
		}

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

	}
	
	public void renderEntities(Bitmap bitmap) {
		this.entityManager.render(bitmap);
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
	
	private void genMap() {
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

	public Bitmap getShadowMap() {
		return this.shadowMap;
	}

	public ArrayList<Light> getLights() {
		return this.lights;
	}
}
