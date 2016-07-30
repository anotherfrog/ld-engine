package uk.co.nobber.engine.gfx;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Window {

	private int width;
	private int height;
	private int scale;
	private String title;

	private double widthToHeight;

	private JFrame frame;
	private Canvas canvas;
	
	private BufferStrategy bufferStrat;

	public Window(int width, int height, int scale, String title) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.title = title;

		this.widthToHeight = (double) width / (double) height;

		this.frame = new JFrame();
		this.frame.setResizable(false);
		this.frame.setTitle(title);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setVisible(true);

		this.canvas = new Canvas();
		this.canvas.setSize(width * scale, height * scale);

		this.frame.add(this.canvas);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		
		this.canvas.createBufferStrategy(3);
	}

	//TODO: Fix maintaining aspect ratio
	public void update(Bitmap bitmap) {
		bufferStrat = this.canvas.getBufferStrategy();

		Graphics graphics = bufferStrat.getDrawGraphics();
		graphics.drawImage(bitmap.getImage(), 0, 0, width * scale, height * scale, null);
		graphics.dispose();

		bufferStrat.show();
	}

	public void setTitle(String title) {
		this.frame.setTitle(title);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getScale() {
		return this.scale;
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	public String getTitle() {
		return this.title;
	}
}
