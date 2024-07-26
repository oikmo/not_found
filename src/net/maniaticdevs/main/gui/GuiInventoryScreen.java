package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.Sound;
import net.maniaticdevs.main.Main;

/**
 * Shows inventory screen and item stats
 * @author Oikmo
 */
public class GuiInventoryScreen extends GuiScreen {
	/** Inventory slot column position */
	public int slotCol = 0;
	/** Inventory slot column position */
	public int slotRow = 0;

	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockLeft = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockRight = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockUp = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockDown = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEnter = false;
	
	private int tickDelay = 30;

	public void tick() {
		if(tickDelay != 0) { 
			tickDelay--;
		}
		
		if(Input.isKeyDown(Input.KEY_UP)) {
			if(!lockUp) {
				if(slotRow !=0) {
					slotRow--;
					Sound.playSFX("cursor");
				}  else {
					slotRow = 3;
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
				} else {
					slotRow = 0;
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
				} else {
					if(slotRow != 3) {
						slotCol = 0;
						slotRow++;
						Sound.playSFX("cursor");
					} else {
						slotCol = 0;
						slotRow = 0;
						Sound.playSFX("cursor");
					}
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
				} else {
					if(slotRow != 0) {
						slotCol = 4;
						slotRow--;
						Sound.playSFX("cursor");
					} else {
						slotCol = 4;
						slotRow = 3;
						Sound.playSFX("cursor");
					}
				}
			}
			lockLeft = true;
		} else {
			lockLeft = false;
		}

		if(Input.isKeyDown(Input.KEY_ENTER)) {
			if(!lockEnter) {
				if(tickDelay <= 0) {
					Main.thePlayer.selectItem(getItemIndexOnSlot());
					if(Main.thePlayer.isItemAtIndex(getItemIndexOnSlot())) {
						Sound.playSFX("hitmonster");
					}
				}
			}
			lockEnter = true;
		} else {
			lockEnter = false;
		}
	}

	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0,0,0,50));
		g2.fillRect(Main.getInstance().getX(),Main.getInstance().getY(),Main.getInstance().getWidth(),Main.getInstance().getHeight());
		this.drawInventory(g2);
	}
	
	/**
	 * Draws the {@link Main#thePlayer}'s inventory
	 * @param g2 Graphics
	 */
	public void drawInventory(Graphics2D g2) {
		//frame
		int frameY = Settings.tileSize;
		int frameWidth = (int) (Settings.tileSize*6.075);
		int frameHeight = Settings.tileSize*5;
		int x = Settings.tileSize;
		int tailX = (x + Main.getInstance().getWidth()) - frameWidth/2;
		int frameX = tailX - frameWidth;

		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight, 3);

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

		//draw players stuff
		for(int i = 0; i < 5*4; i++) {
			//equip cursor (HOORAYY)
			/*if(Main.thePlayer.inventory.get(i) == Main.thePlayer.currentWeapon || Main.thePlayer.inventory.get(i) == Main.thePlayer.currentShield) {
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}*/
			//Main.thePlayer.inventory.get(i).update();
			
			g2.setColor(Color.gray);
			if(Main.thePlayer.isItemAtIndex(i)) {
				if(Main.thePlayer.inventory.get(i) == Main.thePlayer.holdingItem) {
					g2.setStroke(new BasicStroke(2));
					g2.drawRoundRect(slotX, slotY, Settings.tileSize, Settings.tileSize, 10, 10);
					g2.setColor(new Color(1f, 1f, 1f, 0.4f));
					g2.setStroke(new BasicStroke(0));
					g2.fillRoundRect(slotX, slotY, Settings.tileSize, Settings.tileSize, 10, 10);
				} else {
					g2.setStroke(new BasicStroke(1));
					g2.drawRoundRect(slotX, slotY, Settings.tileSize, Settings.tileSize, 10, 10);
					g2.setColor(new Color(1f, 1f, 1f, 0.1f));
					g2.setStroke(new BasicStroke(0));
					g2.fillRoundRect(slotX, slotY, Settings.tileSize, Settings.tileSize, 10, 10);
				}
				g2.drawImage(Main.thePlayer.inventory.get(i).image, slotX, slotY, Settings.tileSize, Settings.tileSize, null);
			} else {
				g2.setStroke(new BasicStroke(1));
				g2.drawRoundRect(slotX, slotY, Settings.tileSize, Settings.tileSize, 10, 10);
				g2.setColor(new Color(1f, 1f, 1f, 0.1f));
				g2.setStroke(new BasicStroke(0));
				g2.fillRoundRect(slotX, slotY, Settings.tileSize, Settings.tileSize, 10, 10);
			}

			slotX += slotSize;
			if(i == 4 || i == 9 || i == 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
		}

		//draw cursor
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		g2.setColor(new Color(1f, 1f, 1f, 0.2f));
		g2.setStroke(new BasicStroke(0));
		g2.fillRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

		//descripton
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight + Settings.tileSize/4;
		int dFrameWidth = frameWidth;
		int dFrameHeight = Settings.tileSize*3;

		// draw desc text
		int textX = dFrameX + 13;
		int textY = dFrameY + (Settings.tileSize / 2) + 2;
		g2.setFont(g2.getFont().deriveFont(16F));

		int itemIndex = getItemIndexOnSlot();

		if(itemIndex < Main.thePlayer.inventory.size()) {
			int prevTextY = textY;
			textY += 32;
			
			String name = Main.thePlayer.inventory.get(itemIndex).name;
			String desc = Main.thePlayer.inventory.get(itemIndex).description;
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

	/** 
	 * Arraylist navigation using {@link #slotCol} and {@link #slotRow}
	 * @return {@link Integer} the arraylist position
	 */
	public int getItemIndexOnSlot() {
		return slotCol + (slotRow*5);
	}
}
