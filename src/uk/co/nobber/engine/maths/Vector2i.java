package uk.co.nobber.engine.maths;

public class Vector2i {
	private int x;
	private int y;
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2i add(Vector2i vec) {
		return new Vector2i(vec.getX() + this.x, vec.getY() + this.y);
	}
	public Vector2i add(int xa, int ya) {
		return new Vector2i(this.x + xa, this.y + ya);
	}
	
	public Vector2i sub(Vector2i vec) {
		return new Vector2i(vec.getX() - this.x, vec.getY() - this.y);
	}
	public Vector2i sub(int xa, int ya) {
		return new Vector2i(this.x - xa, this.y - ya);
	}
	
	public Vector2i mul(Vector2i vec) {
		return new Vector2i(vec.getX() * this.x, vec.getY() * this.y);
	}
	public Vector2i mul(int xa, int ya) {
		return new Vector2i(this.x * xa, this.y * ya);
	}
	
	public Vector2i div(Vector2i vec) {
		return new Vector2i(vec.getX() / this.x, vec.getY() / this.y);
	}
	public Vector2i div(int xa, int ya) {
		return new Vector2i(this.x / xa, this.y / ya);
	}
	
	public boolean equals(Vector2i vec) {
		if (this.x == vec.getX() && this.y == vec.getY()) return true;
		return false;
	}
	public boolean equals(int xa, int ya) {
		if (this.x == xa && this.y == ya) return true;
		return false;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
}
