package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.main.Main;

/**
 * Shows character stats and inventory
 * @author Oikmo
 */
public class GuiStatsScreen extends GuiScreen {
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
		int frameHeight = Settings.tileSize*11;
		int x = Settings.tileSize;
		int tailFrameX = (x + Main.getInstance().getWidth()) - (frameWidth/2);
		int tailX = tailFrameX - 20;
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
		g2.drawString("Stats", frameX + getXforCenteredText(g2, "Stats", frameWidth), textY);
		textY += lineHeight * 1.25f;

		g2.setFont(g2.getFont().deriveFont(24f));

		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 20;
		
		//reset textY
		textY = (int) (frameY + (Settings.tileSize * 1.5f) + 16);
		String value;

		value = String.valueOf(Main.thePlayer.level);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.health + "/" + Main.thePlayer.maxHealth);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.mana + "/" + Main.thePlayer.maxMana);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.strength);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.dexterity);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.attack);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.defense);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.exp);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.nextLevelExp);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(Main.thePlayer.coin);
		textX = getXforAlignToRightText(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		//g2.drawImage(Main.thePlayer.currentWeapon.image, tailX - Settings.tileSize, textY-14, null);
		//textY += gp.tileSize;
		//g2.drawImage(Main.thePlayer.currentShield.image, tailX - Settings.tileSize, textY-14, null);
	}
	
	
}
