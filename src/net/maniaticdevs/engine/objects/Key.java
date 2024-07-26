package net.maniaticdevs.engine.objects;

import java.io.IOException;

import javax.imageio.ImageIO;

import net.maniaticdevs.engine.util.ImageUtils;

/**
 * Key class, used for opening things (in progress)
 * @author Oikmo
 */
public class Key extends OBJ {
	
	/**
	 * Key Constructor
	 */
	public Key() {
		name = "Key";
		try {
			image = ImageUtils.fromSheet2D(ImageIO.read(Key.class.getResourceAsStream("/textures/object/items.png")), 8, 8)[0][3];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
