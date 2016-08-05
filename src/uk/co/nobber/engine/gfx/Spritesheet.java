package uk.co.nobber.engine.gfx;

public class Spritesheet {

	private Bitmap sheet;
	private int spriteWidth;
	private int spriteHeight;
	
	private int sheetWidthInSprites;
	private int sheetHeightInSprites;
	
	private Bitmap[] cached;
	
	public Spritesheet(Bitmap sheet, int spriteWidth, int spriteHeight) {
		this.sheet = sheet;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		
		this.sheetWidthInSprites = this.sheet.getWidth() / this.spriteWidth;
		this.sheetHeightInSprites = this.sheet.getHeight() / this.spriteHeight;
		
		this.cached = new Bitmap[sheetWidthInSprites * sheetHeightInSprites];
	}
	
	public Bitmap getSprite(int xSprite, int ySprite) {
		if (xSprite >= sheetWidthInSprites || ySprite >= sheetHeightInSprites) return Bitmap.PLACEHOLDER;
		return getSprite(xSprite, ySprite, this.spriteWidth, this.spriteHeight);
	}
	public Bitmap getSprite(int xSprite, int ySprite, int spriteWidth, int spriteHeight) {
		Bitmap bitmap = new Bitmap(spriteWidth, spriteHeight);
		for (int x = 0; x < spriteWidth; x++) {
			for (int y = 0; y < spriteHeight; y++) {
				bitmap.setPixel(x, y, this.sheet.getPixel(x + (xSprite * spriteWidth), y + (ySprite * spriteHeight)));
			}
		}
		
		return bitmap;
	}
	
	public int getSheetWidthInSprites() {
		return this.sheetWidthInSprites;
	}
	public int getSheetHeightInSprites() {
		return this.sheetHeightInSprites;
	}
	public int getSpriteWidth() {
		return this.spriteWidth;
	}
	public int getSpriteHeight() {
		return this.spriteHeight;
	}
}
