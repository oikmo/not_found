/**
 * 
 */
package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.main.Main;

/**
 * To interface with <b>it.</b>
 * @author Oikmo
 */
public class GuiComputer extends GuiScreen {
	
	private static BufferedImage computerImage;
	/** Background */
	private static Color color = new Color(0,0,0,180);
	
	
	/**
	 * Computer GUI constructor
	 */
	public GuiComputer() {
		if(computerImage == null) {
			computerImage = ResourceLoader.loadImage("textures/computer");
		}
	}
	
	public void tick() {
		
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());
		int x = (Main.getInstance().getWidth()/2)-Main.getInstance().getHeight()/2;
		int y = 0;
		g2.drawImage(computerImage, x, y, Main.getInstance().getHeight(), Main.getInstance().getHeight(), null);
		this.drawString(g2, font, Color.green, "> Test", x, y+Settings.tileSize);
	}

}
