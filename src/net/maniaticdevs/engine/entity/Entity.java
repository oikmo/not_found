package net.maniaticdevs.engine.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.util.math.Vector2;

/**
 * Abstract class for Entities of any type.
 * @author Oikmo
 */
public abstract class Entity {
	
	public BufferedImage image;
	public Vector2 position = new Vector2();
	public int speed = 5;
	
	public Entity() {
		setDefaultValues();
	}
	
	protected abstract void setDefaultValues();
	public void move(int x, int y) {
		position.change(x, y);
	}
	public abstract void tick();
	public abstract void draw(Graphics2D g2);
}
