package net.maniaticdevs.engine.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

/**
 * Abstract class for interactable projects
 * @author Oikmo
 */
public abstract class OBJ {
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
	protected Rectangle hitBox = new Rectangle(0, 0, Settings.tileSize, Settings.tileSize);

	/**
	 * Draws object, if is not seen by player then it is not drawn
	 * @param g2 Graphics
	 * @param playerPos Player world position
	 * @param playerScreenPos Player screen positino
	 */
	public void draw(Graphics2D g2, Vector2 playerPos, Vector2 playerScreenPos) {
		int screenX = position.x - playerPos.x + playerScreenPos.x;
		int screenY = position.y - playerPos.y + playerScreenPos.y;

		if(position.x + Settings.tileSize > playerPos.x - playerScreenPos.x &&
				position.x - Settings.tileSize < playerPos.x + playerScreenPos.x &&
				position.y +Settings.tileSize > playerPos.y - playerScreenPos.y &&
				position.y - Settings.tileSize < playerPos.y + playerScreenPos.y) {
			if(this instanceof PickableObject) {
				g2.drawImage(image, screenX, screenY, Settings.tileSize/2, Settings.tileSize/2, null);
			} else {
				g2.drawImage(image, screenX, screenY, Settings.tileSize, Settings.tileSize, null);
			}
		}
	}

	/** Called by {@link Main} */
	public void tick() {}

	/**
	 * Returns hit box of object
	 * @return {@link Rectangle}
	 */
	public Rectangle getHitBox() {
		return hitBox;
	}
}
