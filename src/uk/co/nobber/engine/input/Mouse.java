package uk.co.nobber.engine.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener, MouseInputListener {

	private int button = -1;
	private boolean pressed;
	private int posX;
	private int posY;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		button = e.getButton();
		pressed = true;
		posX = e.getX();
		posY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		posX = e.getX();
		posY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		button = e.getButton();
		pressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		button = -1;
		pressed = false;
	}
	
	public int getButton() {
		return this.button;
	}
	public boolean isPressed() {
		return this.pressed;
	}
	public boolean isClicked() {
		if (this.pressed) {
			this.pressed = false;
			return true;
		}
		return false;
	}
	public int getX() {
		return this.posX;
	}
	public int getY() {
		return this.posY;
	}

}
