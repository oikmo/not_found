package net.maniaticdevs.engine.objects;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiComputer;

/**
 * Piece of technology? Weird.
 * @author Oikmo
 */
public class Computer extends OBJ {
	
	/**
	 * Computer constructor
	 * @param x X coordinate
	 * @param y X coordinate
	 */
	public Computer(int x, int y) {
		name = "Computer";
		
		image = ImageUtils.scaleImage(ResourceLoader.loadImage("/textures/object/computer16"), Settings.worldTileSize, Settings.worldTileSize);
		collision = true;
		position.set(x,y);
	}
	
	public void interact(Entity entity) {
		Main.currentScreen = new GuiComputer();
	}
}
