package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.Sound;
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

	private boolean lockLeft = false;
	private boolean lockRight = false;
	private boolean lockUp = false;
	private boolean lockDown = false;
	private boolean lockEnter = false;
	
	public void tick() {
		if(Input.isKeyDown(Input.KEY_UP)) {
			if(!lockUp) {
				if(slotRow !=0) {
					slotRow--;
					Sound.playSFX("cursor");
				}
			}
			lockUp = true;
		} else {
			lockUp = false;
		}
		
		if(Input.isKeyDown(Input.KEY_DOWN)) {
			if(!lockDown) {
				if(slotRow !=3) {
					slotRow++;
					Sound.playSFX("cursor");
				}
			}
			lockDown = true;
		} else {
			lockDown = false;
		}
		
		if(Input.isKeyDown(Input.KEY_RIGHT)) {
			if(!lockRight) {
				if(slotCol != 4) {
					slotCol++;
					Sound.playSFX("cursor");
				}
			}
			lockRight = true;
		} else {
			lockRight = false;
		}
		
		if(Input.isKeyDown(Input.KEY_LEFT)) {
			if(!lockLeft) {
				if(slotCol != 0) {
					slotCol--;
					Sound.playSFX("cursor");
				}
			}
			lockLeft = true;
		} else {
			lockLeft = false;
		}
		
		if(Input.isKeyDown(Input.KEY_ENTER)) {
			if(!lockEnter) {
				Main.thePlayer.selectItem(getItemIndexOnSlot());
				if(Main.thePlayer.isItemAtIndex(getItemIndexOnSlot())) {
					
					Sound.playSFX("hitmonster");
				}
				
			}
			lockEnter = true;
		} else {
			lockEnter = false;
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(Main.getInstance().getX(),Main.getInstance().getY(),Main.getInstance().getWidth(),Main.getInstance().getHeight());
		this.drawCharacterScreen(g2);
		this.drawInventory(g2);
	}
	
	/**
	 * Draws the character stats from {@link Main#thePlayer}
	 * @param g2 Graphics
	 */
	public void drawCharacterScreen(Graphics2D g2) {
		//create frame :)
		final int frameX = Settings.tileSize*2;
		final int frameY = Settings.tileSize;
		final int frameWidth = Settings.tileSize*5;
		final int frameHeight = Settings.tileSize*11;
		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

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

		//values
		int tailX = (frameX + frameWidth) - 30;
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
		
		int offset = 5;
		
		g2.setColor(Color.DARK_GRAY);
		g2.fillRoundRect(frameWidth-(int)(Settings.tileSize*1.5)-offset, textY-offset, (Settings.tileSize*2)+offset*2, (Settings.tileSize*2)+offset*2, 1, 1);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(frameWidth-(int)(Settings.tileSize*1.5)-offset, textY-offset, (Settings.tileSize*2)+offset*2, (Settings.tileSize*2)+offset*2, 1, 1);
		g2.drawImage(Main.thePlayer.sprites[Main.thePlayer.spriteNum], frameWidth-(int)(Settings.tileSize*1.5), textY, Settings.tileSize*2, Settings.tileSize*2, null);
		if(Main.thePlayer.holdingItem != null) {
			g2.drawImage(Main.thePlayer.holdingItem.image, frameWidth-(int)(Settings.tileSize*1.5)+Settings.tileSize+Settings.tileSize/4, textY+Settings.tileSize, Settings.tileSize, Settings.tileSize, null);
		}
		//g2.drawImage(Main.thePlayer.currentWeapon.image, tailX - Settings.tileSize, textY-14, null);
		//textY += gp.tileSize;
		//g2.drawImage(Main.thePlayer.currentShield.image, tailX - Settings.tileSize, textY-14, null);
	}

	/**
	 * Draws the {@link Main#thePlayer}'s inventory
	 * @param g2 Graphics
	 */
	public void drawInventory(Graphics2D g2) {
		//frame
		int frameY = Settings.tileSize;
		int frameWidth = Settings.tileSize*6;
		int frameHeight = Settings.tileSize*5;
		int x = Settings.tileSize;
		int tailX = (x + Main.getInstance().getWidth()) - frameWidth/2;
		int frameX = tailX - frameWidth;


		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		//slot
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = Settings.tileSize+3;

		//cursor
		int cursorX = slotXstart + (slotSize * slotCol);
		int cursorY = slotYstart + (slotSize * slotRow);
		int cursorWidth = Settings.tileSize;
		int cursorHeight = Settings.tileSize;


		//draw cursor
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		g2.setColor(new Color(1f, 1f, 1f, 0.2f));
		g2.setStroke(new BasicStroke(0));
		g2.fillRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

		//draw players stuff
		for(int i = 0; i < Main.thePlayer.inventory.size(); i++) {
			//equip cursor (HOORAYY)
			/*if(Main.thePlayer.inventory.get(i) == Main.thePlayer.currentWeapon || Main.thePlayer.inventory.get(i) == Main.thePlayer.currentShield) {
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}*/
			//Main.thePlayer.inventory.get(i).update();
			g2.drawImage(Main.thePlayer.inventory.get(i).image, slotX, slotY, Settings.tileSize, Settings.tileSize, null);

			slotX += slotSize;
			if(i == 4 || i == 9 || i == 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
		}

		//descripton
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight + Settings.tileSize;
		int dFrameWidth = frameWidth;
		int dFrameHeight = Settings.tileSize*3;

		// draw desc text
		int textX = dFrameX + 13;
		int textY = dFrameY + (Settings.tileSize / 2) + 2;
		g2.setFont(g2.getFont().deriveFont(16F));

		int itemIndex = getItemIndexOnSlot();

		if(itemIndex < Main.thePlayer.inventory.size()) {
			drawSubWindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight);
			String name = Main.thePlayer.inventory.get(itemIndex).name;
			g2.drawString(name, textX, textY);
			textY += 32;
			g2.setFont(g2.getFont().deriveFont(15F));
			String desc = Main.thePlayer.inventory.get(itemIndex).description;
			for(String line : desc.split("\n")) {
				g2.drawString(line, textX, textY);
				textY += 16;
			}
		}
	}
	
	/** 
	 * Arraylist navigation using {@link #slotCol} and {@link #slotRow}
	 * @return {@link Integer} the arraylist position
	 */
	public int getItemIndexOnSlot() {
		return slotCol + (slotRow*5);
	}
}
