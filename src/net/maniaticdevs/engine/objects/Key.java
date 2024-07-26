package net.maniaticdevs.engine.objects;

import java.io.IOException;

import javax.imageio.ImageIO;

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
		collision = false;
		try {
			image = ImageUtils.fromSheet2D(ImageIO.read(ResourceLoader.class.getResourceAsStream("/textures/object/items.png")), 8, 8)[3][0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.position.set(x,y);
	}

}
