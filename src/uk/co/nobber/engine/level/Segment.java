package uk.co.nobber.engine.level;

import uk.co.nobber.engine.maths.Vector2f;

public class Segment {

	private Vector2f startPoint;
	private Vector2f endPoint;
	
	public Segment(Vector2f startPoint, Vector2f endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public Vector2f getStartPoint() {
		return this.startPoint;
	}
	
	public Vector2f getEndPoint() {
		return this.endPoint;
	}
}
