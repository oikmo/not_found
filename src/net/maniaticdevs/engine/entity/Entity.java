package net.maniaticdevs.engine.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.util.math.Vector2;

/**
 * Abstract class for Entities of any type.
 * @author Oikmo
 */
public abstract class Entity {
	
	/**
	 * Visual representation of Entity to be displayed on screen.
	 */
	public BufferedImage image;
	/**
	 * World position of entity
	 */
	public Vector2 position = new Vector2();
	/**
	 * Speed, measured in px/s
	 */
	public int speed = 5;
	
	/**
	 * Calls {@link #setDefaultValues()}
	 */
	public Entity() {
		setDefaultValues();
	}
	
	/**
	 * Entity's default values such as position and image are defined in the abstracted class.
	 */
	protected abstract void setDefaultValues();
	/**
	 * Position manipulation.
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		position.change(x, y);
	}
	/**
	 * Logic function that syncs with the 60 tick interval as to maintain a constant speed
	 */
	public abstract void tick();
	/**
	 * Defined by abstracted entity, this draws whatever is defined on screen.
	 * @param g2
	 */
	public abstract void draw(Graphics2D g2);
}
