package net.maniaticdevs.engine.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.UUID;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

/**
 * Abstract class for interactable projects
 * @author Oikmo
 */
public abstract class OBJ {
	/** To keep track of objects for network synchronisation */
	public String networkID = UUID.randomUUID().toString();
	
	/** Sprite */
	public BufferedImage image;
	/** Custom names */
	public String name;
	/** Inventory purposes */
	public String description;
	/** Collidable or nah */
	public boolean collision = false;
	/** World position */
	public Vector2 position = new Vector2();
	/** Used for collision checking */
	protected Rectangle hitBox = new Rectangle(0, 0, Settings.worldTileSize, Settings.worldTileSize);
	
	/**
	 * Draws object, if is not seen by player then it is not drawn
	 * @param g2 Graphics
	 * @param playerPos Player world position
	 * @param playerScreenPos Player screen positino
	 */
	public void draw(Graphics2D g2, Vector2 playerPos, Vector2 playerScreenPos) {
		int screenX = position.x - playerPos.x + playerScreenPos.x;
		int screenY = position.y - playerPos.y + playerScreenPos.y;

		if(position.x + Settings.worldTileSize > playerPos.x - playerScreenPos.x &&
				position.x - Settings.worldTileSize < playerPos.x + playerScreenPos.x &&
				position.y +Settings.worldTileSize > playerPos.y - playerScreenPos.y &&
				position.y - Settings.worldTileSize < playerPos.y + playerScreenPos.y) {
			if(this instanceof PickableObject) {
				g2.drawImage(image, screenX, screenY, Settings.worldTileSize/2, Settings.worldTileSize/2, null);
			} else {
				g2.drawImage(image, screenX, screenY, Settings.worldTileSize, Settings.worldTileSize, null);
			}
			//g2.setColor(Color.white);
			//g2.drawRect(screenX+getHitBox().x, screenY+getHitBox().y, getHitBox().width, getHitBox().height);
		}
	}
	
	/**
	 * 
	 * Method to handle entity interactions with objects
	 * @param entity Entity that interacts with object
	 */
	public void interact(Entity entity) {}

	/** Called by {@link Main} */
	public void tick() {}

	/**
	 * Returns hit box of object
	 * @return {@link Rectangle}
	 */
	public Rectangle getHitBox() {
		return hitBox;
	}
	
	/** 
	 * Sets the {@link #networkID}
	 * @param netID ID to be set to
	 */
	public void setNetworkID(String netID) {
		this.networkID = netID;
	}
}
