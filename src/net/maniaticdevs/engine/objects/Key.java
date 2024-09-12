package net.maniaticdevs.engine.objects;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.util.ImageUtils;

/**
 * Key class, used for opening things
 * @author Oikmo
 */
public class Key extends OBJ {
	
	/**
	 * Key Constructor
	 * @param name Name of key
	 * @param x X position
	 * @param y Y position
	 */
	public Key(String name, int x, int y) {
		this.name = name;
		this.description = "It looks like this goes\nsomewhere...\n\nBut where?";
		this.collision = false;
		this.image = ImageUtils.fromSheet2D(ResourceLoader.loadImage("/textures/object/items"), 8, 8)[3][0];
		this.position.set(x,y);
	}

}
