package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.Main;

/**
 * HUD for the player
 * @author Oikmo
 */
public class GuiInGame extends GuiScreen {
	
	/** Chat screen to show during game */
	public GuiChat chatScreen;
	
	/**
	 * Heart sprites. Loaded statically as to not reload the same image over and over again.
	 */
	private static BufferedImage[] sprites = ImageUtils.setupSheet("player/heartSheet", 3, 1);
	/** Pick up alerts */
	public static String message = null;
	
	public void draw(Graphics2D g2) {
		
		if(Input.isKeyDownExplicit(Input.KEY_T) && chatScreen == null) {
			chatScreen = new GuiChat();
		}
		
		if(chatScreen != null) {
			chatScreen.draw(g2);
		}
		
		int x = Settings.tileSize/2;
		int y = Settings.tileSize/2;
		
		if(Main.thePlayer == null) {
			return;
		}
		
		for(int i = 0; i < Main.thePlayer.maxHealth/2; i++) {
			g2.drawImage(sprites[2], x, y, null);
			x += Settings.tileSize;
		}
		x = Settings.tileSize/2;
		
		int i = 0;
		while(i < Main.thePlayer.health) {
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
