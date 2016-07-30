package uk.co.nobber.engine.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import uk.co.nobber.engine.maths.Vector2f;
import uk.co.nobber.engine.util.Utils;

public class Bitmap {

	private int width;
	private int height;

	private BufferedImage image;
	private int[] pixels;

	private Random random;

	public static final Bitmap PLACEHOLDER = new Bitmap(16, 16, 0xFF00FF);

	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;

		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();

		this.random = new Random();
	}

	public Bitmap(int width, int height, int colour) {
		this(width, height);

		this.fill(colour);
	}

	public Bitmap(String path) {
		try {
			this.image = ImageIO.read(this.getClass().getResourceAsStream(path));
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			this.pixels = new int[width * height];

			this.image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load: " + path);
		}

		this.random = new Random();
	}

	public void noise() {
		for (int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] = random.nextInt();
		}
	}

	public void fill(int colour) {
		Arrays.fill(this.pixels, colour);
	}

	public void clear() {
		this.fill(0);
	}

	public int getPixel(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0;
		return this.pixels[x + y * width];
	}

	public int getPixel(int i) {
		if (i < 0 || i >= this.pixels.length)
			return 0;
		return this.pixels[i];
	}

	public void setPixel(int x, int y, int colour) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		
		this.pixels[x + y * width] = colour;
	}

	public void setPixel(int i, int colour) {
		if (i < 0 || i >= this.pixels.length)
			return;
		this.pixels[i] = colour;
	}

	public void drawRectangle(int xp, int yp, int wa, int ha, int colour) {
		for (int x = xp; x <= wa; x++) {
			setPixel(x, yp, colour);
			setPixel(x, ha, colour);
		}
		for (int y = yp; y <= ha; y++) {
			setPixel(xp, y, colour);
			setPixel(wa, y, colour);
		}
	}

	public void fillRectangle(int xp, int yp, int wa, int ha, int colour) {
		for (int x = xp; x < xp + wa; x++) {
			for (int y = yp; y < yp + ha; y++) {
				setPixel(x, y, colour);
			}
		}
	}

	public void blendFillRectangle(int xp, int yp, int wa, int ha, int colour, BlendMode blendMode) {
		for (int x = xp; x < xp + wa; x++) {
			for (int y = yp; y < yp + ha; y++) {
				setPixel(x, y, blend(colour, this.getPixel(x, y), 50, blendMode));
			}
		}
	}

	public void fillCircle(int xPos, int yPos, int radius, int colour) {
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				if (x * x + y * y < radius * radius) {
					setPixel(xPos + x, yPos + y, colour);
				}
			}
		}
	}

	public void drawLine(int x, int y, int x2, int y2, int colour) {
		int w = x2 - x;
		int h = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
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
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			setPixel(x, y, colour);
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}
	
	public void drawLineF(float x, float y, float x2, float y2, int colour) {
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
		float numerator = longest / 2;
		for (float i = 0; i <= longest; i++) {
			setPixel((int) x, (int) y, colour);
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	public void fillBottomFlatTriangle(Vector2f v1, Vector2f v2, Vector2f v3, int colour) {
		float invslope1 = (v2.getX() - v1.getX()) / (v2.getY() - v1.getY());
		float invslope2 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());

		float currentX = v1.getX();
		float currentX2 = v1.getX();

		for (int sLineY = (int) v1.getY(); sLineY <= v2.getY(); sLineY++) {
			drawLineF(currentX, sLineY, currentX2, sLineY, colour);
			currentX += invslope1;
			currentX2 += invslope2;
		}
	}

	public void fillTopFlatTriangle(Vector2f v1, Vector2f v2, Vector2f v3, int colour) {
		float invslope1 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
		float invslope2 = (v3.getX() - v2.getX()) / (v3.getY() - v2.getY());

		float currentX = v3.getX();
		float currentX2 = v3.getX();

		for (int sLineY = (int) v3.getY(); sLineY > v1.getY(); sLineY--) {
			drawLineF(currentX, sLineY, currentX2, sLineY, colour);
			currentX -= invslope1;
			currentX2 -= invslope2;
		}
	}

	public void fillTriangle(Vector2f v1, Vector2f v2, Vector2f v3, int colour) {
		Vector2f[] vertices = sortVerticesAscendingByY(v1, v2, v3);

		v1 = vertices[0];
		v2 = vertices[1];
		v3 = vertices[2];

		if (v2.getY() == v3.getY()) {
			fillBottomFlatTriangle(v1, v2, v3, colour);
		} else if (v1.getY() == v2.getY()) {
			fillTopFlatTriangle(v1, v2, v3, colour);
		} else {
			Vector2f v4 = new Vector2f(v1.getX()
					+ ((float) (v2.getY() - v1.getY()) / (float) (v3.getY() - v1.getY()) * (v3.getX() - v1.getX())),
					v2.getY());
			fillBottomFlatTriangle(v1, v2, v4, colour);
			fillTopFlatTriangle(v2, v4, v3, colour);
		}
	}

	private Vector2f[] sortVerticesAscendingByY(Vector2f v1, Vector2f v2, Vector2f v3) {
		Vector2f[] result = { v1, v2, v3 };

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				if (result[j].getY() > result[j + 1].getY()) {
					Vector2f temp;
					temp = result[j + 1];
					result[j + 1] = result[j];
					result[j] = temp;
				}
			}
		}
		return result;
	}

	public void drawString(String string, Font font, int x, int y) {
		Spritesheet sheet = font.getSheet();
		for (int i = 0; i < string.length(); i++) {
			String currentChar = string.split("")[i];
			int index = font.getFontMap().indexOf(currentChar);

			Bitmap character = sheet.getSprite(index % sheet.getSheetWidthInSprites(),
					(int) Math.floor(index / sheet.getSheetWidthInSprites()));
			blit(character, x + (i * sheet.getSpriteWidth()), y);
		}
	}
	
	public void blit(Bitmap bitmap, int xp, int yp) {
		blit(bitmap, xp, yp, bitmap.getWidth(), bitmap.getHeight());
	}

	public void blit(Bitmap bitmap, int xp, int yp, int cw, int ch) {
		cw = (cw > bitmap.getWidth()) ? bitmap.getWidth() : cw;
		ch = (ch > bitmap.getHeight()) ? bitmap.getHeight() : ch;

		for (int x = 0; x < cw; x++) {
			int xa = xp + x;
			for (int y = 0; y < ch; y++) {
				int ya = yp + y;
				setPixel(xa, ya, bitmap.getPixel(x, y));
			}
		}
	}

	public void blendBlit(Bitmap bitmap, int xp, int yp, BlendMode blendMode) {
		for (int x = 0; x < bitmap.getWidth(); x++) {
			int xa = xp + x;
			for (int y = 0; y < bitmap.getHeight(); y++) {
				int ya = yp + y;
				setPixel(xa, ya, blend(getPixel(xa, ya), bitmap.getPixel(x, y), 50, blendMode));
			}
		}
	}

	public int blend(int colourOne, int colourTwo, int factor, BlendMode blendMode) {
		if (factor < 0)
			factor = 0;

		int r1 = (colourOne >> 16) & 0xFF;
		int g1 = (colourOne >> 8) & 0xFF;
		int b1 = (colourOne) & 0xFF;

		int r2 = (colourTwo >> 16) & 0xFF;
		int g2 = (colourTwo >> 8) & 0xFF;
		int b2 = (colourTwo) & 0xFF;

		if (blendMode == BlendMode.ADD) {
			int r = r1 + ((r2 / 100) * factor);
			int g = g1 + ((g2 / 100) * factor);
			int b = b1 + ((b2 / 100) * factor);

			return (0xFF << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
		} else if (blendMode == BlendMode.SUB) {
			int r = r1 - ((r2 / 100) * factor);
			int g = g1 - ((g2 / 100) * factor);
			int b = b1 - ((b2 / 100) * factor);

			return (0xFF << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
		} else if (blendMode == BlendMode.MUL) {
//			float r = (r1 / 255f) * (r2 / 255f);
//			float g = (g1 / 255f) * (g2 / 255f);
//			float b = (b1 / 255f) * (b2 / 255f);
			
			int r = (r1) * (r2);
			int g = (g1) * (g2);
			int b = (b1) * (b2);
			
//			if (r != 0f && g != 0f && b != 0f) {
//				System.out.println("hh");
//			}
			
			return Utils.getHex(r, g, b);
		}

		return (0xFF << 24) | ((r1 & 0xFF) << 16) | ((g1 & 0xFF) << 8) | (b1 & 0xFF);
	}
	
	public void invert() {
		for (int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] *= -1;
		}
	}

	public void save(String path) {
		try {
			ImageIO.write(image, "PNG", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public int[] getPixels() {
		return this.pixels;
	}
}
