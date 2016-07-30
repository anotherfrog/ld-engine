package uk.co.nobber.engine.gfx;

public class Font {

	private Spritesheet sheet;
	private String fontmap;

	public Font(Spritesheet sheet) {
		this.sheet = sheet;
		this.fontmap = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]/\\{}¬~<>=_*,.!?1234567890+-\"':;#@ ";
	}

	// --- getters and setters
	public Spritesheet getSheet() {
		return this.sheet;
	}

	public String getFontMap() {
		return this.fontmap;
	}
}
