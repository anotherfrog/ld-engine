package uk.co.nobber.engine.maths;

public class Vector2f {

	private float x;
	private float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2f add(Vector2f vec) {
		return new Vector2f(vec.getX() + this.x, vec.getY() + this.y);
	}
	public Vector2f add(float xa, float ya) {
		return new Vector2f(this.x + xa, this.y + ya);
	}
	
	public Vector2f sub(Vector2f vec) {
		return new Vector2f(vec.getX() - this.x, vec.getY() - this.y);
	}
	public Vector2f sub(float xa, float ya) {
		return new Vector2f(this.x - xa, this.y - ya);
	}
	
	public Vector2f mul(Vector2f vec) {
		return new Vector2f(vec.getX() * this.x, vec.getY() * this.y);
	}
	public Vector2f mul(float xa, float ya) {
		return new Vector2f(this.x * xa, this.y * ya);
	}
	
	public Vector2f div(Vector2f vec) {
		return new Vector2f(vec.getX() / this.x, vec.getY() / this.y);
	}
	public Vector2f div(float xa, float ya) {
		return new Vector2f(this.x / xa, this.y / ya);
	}
	
	public boolean equals(Vector2f vec) {
		if (this.x == vec.getX() && this.y == vec.getY()) return true;
		return false;
	}
	public boolean equals(float xa, float ya) {
		if (this.x == xa && this.y == ya) return true;
		return false;
	}
	
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
}
