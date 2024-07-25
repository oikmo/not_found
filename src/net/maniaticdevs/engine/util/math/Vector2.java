package net.maniaticdevs.engine.util.math;

/**
 * Vector maths soon enough but a basic container for storing x and y.
 * 
 * @author Oikmo
 */
public class Vector2 {
	
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
	 * @param x
	 * @param y
	 */
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Offsets from currently set values
	 * @param x
	 * @param y
	 */
	public void change(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Sets values
	 * 
	 * @param x
	 * @param y
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
	
}
