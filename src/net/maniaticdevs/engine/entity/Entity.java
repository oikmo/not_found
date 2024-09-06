package net.maniaticdevs.engine.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.UUID;

import net.maniaticdevs.engine.util.math.Vector2;

/**
 * Abstract class for Entities of any type.
 * @author Oikmo
 */
public abstract class Entity {
	
	/** To keep track of entities for network synchronisation */
	public String networkID = UUID.randomUUID().toString();
	
	/** Entity name for like easier tracking and nameplates */
	public String name;
	
	/** Visual representation of Entity to be displayed on screen. */
	public BufferedImage[] sprites = null;
	/** Direction of entity */
	protected EntityDirection direction = EntityDirection.IDLE;
	/** World position of entity */
	protected Vector2 position = new Vector2();
	/** Speed, measured in px/s */
	protected int speed = 5;
	
	/** Used for collision checking */
	protected Rectangle hitBox = new Rectangle(8, 1, 32, 46);
	
	/** Index for sprite sheets, 5 frames expected. */
	public int spriteNum = 0;
	/** Keeps track of ticks, used in {@link #animate()} */
	protected int spriteCounter = 0;
	
	/** If this is true then no matter what direction the entity is, it will add speed to it's opposite direction */
	public boolean colliding = false;
	
	/* STATS */
	/** maximum amount of health */
	public int maxHealth;
	/** current health */
	public int health;
	/** maximum amount of mana */
	public int maxMana;
	/** current mana */
	public int mana;
	/** Leveling system like cmon */
	public int level;
	/** Modifier for how much {@link #attack} the can entity do */
	public int strength;
	/** Stealth or something */
	public int dexterity;
	/** The amount of damage that can be done by entity */
	public int attack;
	/** The amount of damage that can be resisted */
	public int defense;
	/** Leveling system */
	public int exp;
	/** Leveling system */
	public int nextLevelExp;
	/** Moolas... */
	public int coin;
	
	/** Should entity be allowed to attack? */
	public boolean isInvince;
	/** How long the invincibility lasts for */
	public int invinceCounter;
	
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
	
	/** 
	 * Returns entity world position  
	 *  @return {@link Vector2}
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * Returns the direction of Entity from the {@link EntityDirection}
	 * @return {@link EntityDirection}
	 */
	public EntityDirection getDirection() {
		return direction;
	}
	
	/**
	 * Returns hit box of entity
	 * @return {@link Rectangle}
	 */
	public Rectangle getHitBox() {
		return hitBox;
	}
	
	/**
	 * Returns the speed of entity (default 5)
	 * @return {@link Integer}
	 */
	public int getSpeed() {
		return speed;
	}
	
	/** 
	 * Sets the {@link #networkID}
	 * @param ID ID to be set to
	 */
	public void setNetworkID(String ID) {
		this.networkID = ID;
	}
	
	/**
	 * Sets the {@link EntityDirection} of Entity (networking reasons)
	 * @param entityDirection direction to be set
	 */
	public void setDirection(EntityDirection entityDirection) {
		this.direction = entityDirection;
	}
}
