package net.maniaticdevs.engine.tile;

import java.awt.image.BufferedImage;

/**
 * Data to store tile information
 * @author Oikmo
 */
public class Tile {
	/** What order it is in */
	public int index;
	/** Image for tile */
	public BufferedImage image;
	/** Can it be collided with */
	public boolean collision = false;
}
