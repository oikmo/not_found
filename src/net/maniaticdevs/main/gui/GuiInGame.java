package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	
	/** Heart sprites. Loaded statically as to not reload the same image over and over again. */
	private static BufferedImage[] sprites = ImageUtils.setupSheet("player/heartSheet", 3, 1);
	/** Pick up alerts */
	public static String message = null;
	
	/** For the holding item box in the top right */
	private Color selectedColor = new Color(100,100,100, 200);
	
	/** The glitch box list */
	private List<Rectangle> glitchBoxes = new ArrayList<Rectangle>();
	/** To determine size and position of each glitch box */
	private Random random = new Random();
	
	/** How many glitch boxes at once? (Dynamic) */
	public static int sizeGlitches = 0;
	
	public void tick() {
		if(sizeGlitches != glitchBoxes.size()) {
			if(sizeGlitches == 0) {
				glitchBoxes.clear();
			} else if(sizeGlitches > glitchBoxes.size()) {
				glitchBoxes.add(new Rectangle());
			} else if(sizeGlitches < glitchBoxes.size()) {
				glitchBoxes.remove(0);
			}
		}
		
		for(Rectangle glitchBox : glitchBoxes) {
			glitchBox.x = random.ints(0, Main.getInstance().getWidth()).iterator().nextInt();
			glitchBox.y = random.ints(0, Main.getInstance().getHeight()).iterator().nextInt();
			glitchBox.width = random.ints(0, 150).iterator().nextInt();
			glitchBox.height = random.ints(0, 150).iterator().nextInt();
		}
	}
	
	public void draw(Graphics2D g2) {
		for(Rectangle glitchBox : glitchBoxes) {
			g2.setColor(Color.WHITE);
			g2.fillRect(glitchBox.x, glitchBox.y, glitchBox.width, glitchBox.height);
		}
		
		
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
		
		int rectX = Main.getInstance().getWidth()-(Settings.tileSize*3);
		int rectY = (Settings.tileSize);
		
		g2.setColor(selectedColor);
		g2.fillRect(rectX, rectY, Settings.tileSize*2, Settings.tileSize*2);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(rectX, rectY, (Settings.tileSize*2), (Settings.tileSize*2));
		if(Main.thePlayer.holdingItem != null) {
			g2.drawImage(Main.thePlayer.holdingItem.image, rectX, rectY, Settings.tileSize*2, Settings.tileSize*2, null);
		}
		
		if(message != null) {
			int frameHeight = Settings.tileSize;
			int frameY = 10;
			int textWidth = (int) g2.getFontMetrics(font).getStringBounds(message, g2).getWidth();
			int frameWidth = textWidth+Settings.tileSize;
			int frameX = (Main.getInstance().getWidth()/2)-(frameWidth/2);
			
			drawSubWindow(g2, frameX,frameY, frameWidth,frameHeight, 2);
			g2.setFont(font);
			g2.setColor(Color.white);
			g2.drawString(message,(Main.getInstance().getWidth()/2)-(textWidth/2), frameY+(int)(Settings.tileSize/1.5));
		}
	}

}
