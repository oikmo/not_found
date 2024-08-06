package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.main.Main;

/**
 * HUD for the player
 * @author Oikmo
 */
public class GuiInGame extends GuiScreen {
	
	/**
	 * Heart sprites. Loaded statically as to not reload the same image over and over again.
	 */
	private static BufferedImage[] sprites = ImageUtils.setupSheet("player/heartSheet", 3, 1);
	
	public static String message = null;
	
	public void draw(Graphics2D g2) {
		//this.drawImage(g2, image, Main.getInstance().getWidth()/2, Main.getInstance().getHeight()/2, 128, 128);
		
		//this.drawImage(g2, sprites[0], 10, 10, 20, 20);
		
		int x = Settings.tileSize/2;
		int y = Settings.tileSize/2;
		int i = 0;
		//draw max life
		if(Main.thePlayer == null) { return; }
		while(i < Main.thePlayer.maxHealth/2) {
			g2.drawImage(sprites[2], x, y, null);
			i++;
			x += Settings.tileSize;
		}
		//reset
		x = Settings.tileSize/2;
		y = Settings.tileSize/2;
		i = 0;
		//draw current life
		while(i<Main.thePlayer.health) {
			g2.drawImage(sprites[1], x, y, null);
			i++;
			if(i<Main.thePlayer.health) { g2.drawImage(sprites[0], x, y, null); }
			i++;
			x += Settings.tileSize;
		}
		
		if(message != null) {
			int frameHeight = Settings.tileSize;
			int frameY = 10;
			int textWidth = (int) g2.getFontMetrics(font).getStringBounds(message, g2).getWidth();
			int frameX = (Main.getInstance().getWidth()/2)-(textWidth+Settings.tileSize)/2;
			int frameWidth = textWidth+Settings.tileSize;
			
			drawSubWindow(g2, frameX,frameY, frameWidth,frameHeight, 2);
			
			this.drawStringCentered(g2, font, Color.WHITE, message, this.getXforCenteredText(g2, message, frameX), frameY+(int)(Settings.tileSize/1.25));
		}
	}

}
