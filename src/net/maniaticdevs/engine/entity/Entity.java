package net.maniaticdevs.engine.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.util.math.Vector2;

/**
 * Abstract class for Entities of any type.
 * @author Oikmo
 */
public abstract class Entity {
	
	/** Visual representation of Entity to be displayed on screen. */
	public BufferedImage[] sprites = null;
	/** World position of entity */
	public Vector2 position = new Vector2();
	/** Speed, measured in px/s */
	public int speed = 5;
	
	/** Index for sprite sheets, 5 frames expected. */
	public int spriteNum = 0;
	/** Keeps track of ticks, used in {@link #animate()} */
	public int spriteCounter = 0;
	
	/** Calls {@link #setDefaultValues()} */
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
	 * @param x - amount of X to move
	 * @param y - amount of Y to move
	 */
	public void move(int x, int y) {
		position.change(x, y);
	}
	/**
	 * Logic function that syncs with the 60 tick interval as to maintain a constant speed
	 */
	public abstract void tick();
	
	/**
	 * Called on every tick, increments {@link #spriteNum} every 8 ticks
	 */
	protected void animate() {
		spriteCounter++;
		if (spriteCounter > 8) { 
			if (spriteNum >= 0) { 
				spriteNum++; 
			} 
			if (spriteNum > 5) { 
				spriteNum = 0; 
			} 
			spriteCounter = 0; 
		}
	}
	
	/**
	 * Defined by abstracted entity, this draws whatever is defined on screen.
	 * @param g2 - Graphics2D class used to draw shapes and images on screen.
	 */
	public abstract void draw(Graphics2D g2);
}
