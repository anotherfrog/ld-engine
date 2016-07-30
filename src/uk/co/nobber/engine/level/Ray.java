package uk.co.nobber.engine.level;

import uk.co.nobber.engine.maths.Vector2f;

public class Ray {

	private Vector2f startPosition;
	private Vector2f endPosition;
	
	public Ray(Vector2f startPosition, Vector2f endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	public Vector2f getStartPos() {
		return this.startPosition;
	}
	
	public Vector2f getEndPos() {
		return this.endPosition;
	}
}
