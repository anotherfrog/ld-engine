package uk.co.nobber.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[65535];
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	public boolean isPressed(int keyCode) {
		return keys[keyCode];
	}
	public boolean isTyped(int keyCode) {
		if (keys[keyCode]) {
			keys[keyCode] = false;
			return true;
		}
		return false;
	}
}
