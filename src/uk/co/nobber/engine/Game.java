package uk.co.nobber.engine;

import uk.co.nobber.engine.gfx.Bitmap;
import uk.co.nobber.engine.gfx.Window;
import uk.co.nobber.engine.input.Keyboard;
import uk.co.nobber.engine.input.Mouse;

public abstract class Game {

	protected Window window;
	
	protected Bitmap screen;
	
	private long ticks;
	private boolean running;
	
	public static Keyboard KEYBOARD = new Keyboard();
	public static Mouse MOUSE = new Mouse();
	
	public Game(int width, int height, int scale, String title) {
		this.window = new Window(width, height, scale, title);
		
		this.window.getCanvas().addKeyListener(KEYBOARD);
		this.window.getCanvas().addMouseListener(MOUSE);
		this.window.getCanvas().addMouseMotionListener(MOUSE);
		
		this.screen = new Bitmap(width, height);
	}
	
	public void start() {
		start(60, true);
	}
	
	public void start(int updateRate, boolean capFps) {
		int frames = 0;
		
		double unprocessed = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / (double) updateRate;
		
		running = true;
		while(running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			
			if (passedTime < 0) passedTime = 0;
			if (passedTime > 100000000) passedTime = 100000000;
			
			unprocessed += passedTime / 1000000000.0;
			
			boolean ticked = false;
			while (unprocessed > secondsPerTick) {
				update();
				unprocessed -= secondsPerTick;
				ticked = true;
				
				ticks++;
				if (ticks % updateRate == 0) {
					System.out.println("fps: " + frames);
					lastTime += 1000;
					frames = 0;
				}
			}
			
			if (capFps && ticked) {
				render();
				this.window.update(screen);
				frames++;
			} else {
				render();
				this.window.update(screen);
				frames++;
			}
		}
	}
	
	public abstract void update();
	public abstract void render();
}
