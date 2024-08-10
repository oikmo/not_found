package net.maniaticdevs.engine.util.math;

/**
 * Vector maths soon enough but a basic container for storing x and y.
 * 
 * @author Oikmo
 */
public class Vector2 {
	
	/**
	 * Coordinates
	 */
	public int x, y;
	
	/**
	 * Too lazy to set values? No problem!
	 */
	public Vector2() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Basic contructor
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 position) {
		this.x = position.x;
		this.y = position.y;
	}

	/**
	 * Offsets from currently set values
	 * @param x amount of X to change
	 * @param y amount of Y to change
	 */
	public void change(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	
	/**
	 * Sets values
	 * 
	 * @param vec Vector coordinates to be set to
	 */
	public void set(Vector2 vec) {
		set(vec.x, vec.y);
	}
	
	/**
	 * Sets values
	 * 
	 * @param x X coordinate to be set to
	 * @param y Y coordinate to be set to
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Vector2) {
			Vector2 vec = (Vector2)obj;
			return x == vec.x && y == vec.y;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return x << 16 ^ y;
	}
	
	public String toString() {
		return "Vector2[x="+x+",y="+y+"]";
	}
}
