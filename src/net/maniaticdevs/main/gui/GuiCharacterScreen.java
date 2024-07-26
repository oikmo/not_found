package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.main.Main;

/**
 * Shows character stats and inventory
 * @author Oikmo
 */
public class GuiCharacterScreen extends GuiScreen {
	/** Inventory slot column position */
	public int slotCol = 0;
	/** Inventory slot column position */
	public int slotRow = 0;

	private int spriteCounter;
	private int spriteNum;

	public void tick() {
		spriteCounter++;
		if (spriteCounter > 8) { 
			if (spriteNum >= 0) { 
				spriteNum++; 
			} 
			if (spriteNum > 5) { 
				spriteNum = 0; 
			} 
			spriteCounter = 0; 
		}
	}

	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0,0,0,50));
		g2.fillRect(Main.getInstance().getX(),Main.getInstance().getY(),Main.getInstance().getWidth(),Main.getInstance().getHeight());
		this.drawCharacterScreen(g2);
	}

	/**
	 * Draws the character stats from {@link Main#thePlayer}
	 * @param g2 Graphics
	 */
	public void drawCharacterScreen(Graphics2D g2) {
		//create frame :)
		int frameY = Settings.tileSize;
		int frameWidth = Settings.tileSize*6;
		int frameHeight = (int) (Settings.tileSize*6.7f);
		int x = Settings.tileSize;
		int tailFrameX = (x + Main.getInstance().getWidth()) - (frameWidth/2);
		int frameX = tailFrameX - frameWidth;
		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight, 3);

		//text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(24f));
		int textX = frameX + 20;
		int textY = frameY + Settings.tileSize;
		final int lineHeight = 32;

		//names
		g2.setFont(g2.getFont().deriveFont(30f));
		g2.drawString("Character", frameX + getXforCenteredText(g2, "Character", frameWidth), textY);
		textY += lineHeight/2;
		int offset = 5;

		g2.setColor(Color.DARK_GRAY);
		g2.fillRoundRect(tailFrameX-(int)(Settings.tileSize*5.25)-offset, textY-offset, (int)((Settings.tileSize*4)+offset*6.25), (int)((Settings.tileSize*4)+offset*6.25), 1, 1);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(tailFrameX-(int)(Settings.tileSize*5.25)-offset, textY-offset,(int)((Settings.tileSize*4)+offset*6.25), (int)((Settings.tileSize*4)+offset*6.25), 1, 1);

		g2.drawImage(Main.thePlayer.sprites[spriteNum], tailFrameX-(int)(Settings.tileSize*4), textY+Settings.tileSize, Settings.tileSize*2, Settings.tileSize*2, null);
		
		g2.setColor(new Color(255,255,255, 50));
		g2.fillRoundRect(tailFrameX-(int)(Settings.tileSize*2)-offset, textY+(Settings.tileSize*3)-offset, Settings.tileSize+offset, Settings.tileSize+offset, 1, 1);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(tailFrameX-(int)(Settings.tileSize*2)-offset, textY+(Settings.tileSize*3)-offset, Settings.tileSize+offset, Settings.tileSize+offset, 1, 1);

		
		if(Main.thePlayer.holdingItem != null) {
			g2.drawImage(Main.thePlayer.holdingItem.image, tailFrameX-(int)(Settings.tileSize*2)-offset, textY+(Settings.tileSize*3)-offset/2, Settings.tileSize, Settings.tileSize, null);
		}
		
		//descripton
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight + Settings.tileSize/4;
		int dFrameWidth = frameWidth;
		int dFrameHeight = Settings.tileSize*3;

		// draw desc text
		textX = dFrameX + 13;
		textY = dFrameY + (Settings.tileSize / 2) + 2;
		g2.setFont(g2.getFont().deriveFont(16F));
		
		if(Main.thePlayer.holdingItem != null) {
			String name = Main.thePlayer.holdingItem.name;
			int prevTextY = textY;
			
			textY += 32;
			
			String desc = Main.thePlayer.holdingItem.description;
			ArrayList<String> increments = new ArrayList<>();
			for(String line : desc.split("\n")) {
				increments.add(line);
				textY += 16;
			}
			
			dFrameHeight = textY- (dFrameY + 2) + 6;
			drawSubWindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight, 3);
			textY = prevTextY + 32;
			
			g2.drawString(name, textX, prevTextY);
			g2.setFont(g2.getFont().deriveFont(15F));
			for(String line : increments) {
				g2.drawString(line, textX, textY);
				textY += 16;
			}
		}
	}


}
