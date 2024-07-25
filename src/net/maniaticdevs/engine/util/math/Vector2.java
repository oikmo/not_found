package net.maniaticdevs.engine.util.math;

public class Vector2 {
	
	public int x, y;
	
	public Vector2() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void change(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
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
