package uk.co.nobber.minild;

import java.awt.event.KeyEvent;

import uk.co.nobber.engine.Game;
import uk.co.nobber.engine.entities.Entity;
import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.gfx.BlendMode;
import uk.co.nobber.engine.gfx.Spritesheet;
import uk.co.nobber.engine.level.Level;
import uk.co.nobber.engine.level.Light;
import uk.co.nobber.engine.maths.Vector2f;

public class MiniLD extends Game {

	private Spritesheet spritesheet;
	
	private Level level;
	private Entity player;
	
	public MiniLD(int width, int height, int scale, String title) {
		super(width, height, scale, title);
		
		this.spritesheet = new Spritesheet(new Bitmap("/test.png"), 16, 16);
		
		this.level = new Level("/levels/test.json", new Spritesheet(new Bitmap("/sprites.png"), 16, 16));
		
		Bitmap temp = new Bitmap(16, 16, 0xFF4242);
		this.player = new Entity(32, 32, 16, 16, temp);
		this.level.addEntity(this.player);
		
		this.level.addLight(new Light(new Vector2f(28, 28)));
		this.level.generateShadowMap();
	}

	@Override
	public void update() {
		level.update();
		
		int speed = 2;
		if (KEYBOARD.isPressed(KeyEvent.VK_W)) player.move(0, -speed, level);
		if (KEYBOARD.isPressed(KeyEvent.VK_S)) player.move(0, speed, level);
		if (KEYBOARD.isPressed(KeyEvent.VK_A)) player.move(-speed, 0, level);
		if (KEYBOARD.isPressed(KeyEvent.VK_D)) player.move(speed, 0, level);
		
		this.level.getLights().get(0).setPosition(MOUSE.getX() / window.getScale(), MOUSE.getY() / window.getScale());
		this.level.generateShadowMap();
	}

	@Override
	public void render() {
		screen.clear();

		level.render(screen);
//		level.renderColl(screen, 0, 0);
		screen.blendBlit(level.getShadowMap(), level.getXOffset(), level.getYOffset(), BlendMode.ADD);
//		screen.blit(level.getShadowMap(), level.getXOffset(), level.getYOffset());
	}
	
	
}
