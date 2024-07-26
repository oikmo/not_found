package net.maniaticdevs.engine.objects;

import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.util.math.Vector2;

/**
 * Abstract class for interactable projects (in progress)
 * @author Oikmo
 */
public abstract class OBJ {
	/** Sprite */
	public BufferedImage image;
	/** Keys, Doors, etc... */
	public String baseName;
	/** ID or something why did I make this */
	public String name;
	/** Collidable or nah */
	public boolean collision = false;
	/** World position */
	public Vector2 position;
	
}
