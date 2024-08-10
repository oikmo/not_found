package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.network.client.NetworkHandler;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.Input.InputType;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;
import net.maniaticdevs.main.entity.Player;

/**
 * To join a server you must ip
 * @author Oikmo
 */
public class GuiJoinMenu extends GuiScreen {
	
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockUp = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockDown = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEnter = false;
	/** To prevent action to be done repeatedly in a short period of time for {@link Input}*/
	private boolean hasSet = false;

	/** Hovering option */
	private int optionSelected = 0;

	/** Prevent action being done on the same frame as the last one to enter */
	private int tickDelay = 30;

	public void tick() {
		if(tickDelay != 0) { 
			tickDelay--;
		}

		if(Input.isKeyDownExplicit(Input.KEY_UP)) {
			if(!lockUp) {
				if(optionSelected !=0) {
					optionSelected--;
				} else {
					optionSelected = 2;
				}
				Main.sfxLib.play(SoundSFXEnum.cursor);
			}
			lockUp = true;
		} else {
			lockUp = false;
		}

		if(Input.isKeyDownExplicit(Input.KEY_DOWN)) {
			if(!lockDown) {
				if(optionSelected != 2) {
					optionSelected++;
				} else {
					optionSelected = 0;
				}
				Main.sfxLib.play(SoundSFXEnum.cursor);
			}
			lockDown = true;
		} else {
			lockDown = false;
		}

		if(Input.isKeyDownExplicit(Input.KEY_ENTER)) {
			if(!lockEnter) {
				switch(optionSelected) {
				case 0:
					Input.needsInput = false;
					hasSet = false;
					if(!Input.getTextInput().isEmpty()) {
						Main.thePlayer = new Player();
						Main.currentScreen = new GuiInGame();
						try {
							Main.theNetwork = new NetworkHandler(Input.getTextInput().trim());
						} catch (Exception e) {
							Main.disconnect(false, "Server doesn't exist!");
						}
					}
					break;
				case 1:
					if(!Input.getTextInput().isEmpty()) {
						Main.thePlayer = new Player();
						Main.currentScreen = new GuiInGame();
						try {
							Main.theNetwork = new NetworkHandler(Input.getTextInput().trim());
						} catch (Exception e) {
							Main.disconnect(false, "Server doesn't exist!");
						}
					}
					break;
				case 2:
					Main.currentScreen = new GuiPlayMenu();
					break;
				}
				Main.sfxLib.play(SoundSFXEnum.hit);
			}
			lockEnter = true;
		} else {
			lockEnter = false;
		}
	}

	public void draw(Graphics2D g2) {
		if(!Input.needsInput && !hasSet) {
			Input.needsInput = true;
			hasSet = true;
		}
		
		if(this.optionSelected != 0) {
			Input.needsInput = false;
		} else {
			Input.needsInput = true;
		}
		
		if(Input.lengthInput != 30) {
			Input.lengthInput = 30;
		}
		
		Input.inputType = InputType.IP;
		
		drawSelectNameScreen(g2);
	}
	
	/**
	 * Draws the menu
	 * @param g2 Graphics
	 */
	public void drawSelectNameScreen(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());

		//System.out.println((gp.screenWidth/2) - (gp.tileSize + (gp.tileSize*2) + 16));
		int x = (Main.getInstance().getWidth()/2);
		int y = (Main.getInstance().getHeight()/2);
		//g2.drawImage(gp.player.shadow, x,(int) (y - gp.tileSize), gp.tileSize*5, gp.tileSize*5, null);
		
		g2.setColor(Color.WHITE);
		g2.setFont(font);
		//menu
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		String text = "Enter IP...";
		x = getXforCenteredText(g2, text);
		g2.drawString(text, x, (int) (y-g2.getFontMetrics().getStringBounds(text, g2).getHeight()*2));
		
		text = Input.getTextInput();
		x = getXforCenteredText(g2, text);
		g2.drawString(text, x+(optionSelected == 0 ? 15 : 0), y);
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		
		
		int width = (int)(Settings.tileSize*8.5f);
		if(Input.getTextInput().length() > 20) {
			width = (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth() + Settings.tileSize*1.15f);
		}
		int height = (int) (Settings.tileSize*1.2f);
		int subX = ((Main.getInstance().getWidth()/2)-width/2);
		
		g2.drawRoundRect(subX+5, (y-height+20), width-10, height-10, 25, 25);	
		if(optionSelected == 0) {
			g2.drawString(">",subX+15, y);
		}
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		text = "JOIN";
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		if(optionSelected == 1) {
			g2.drawString(">", x-Settings.tileSize, y);
		}

		text = "BACK";
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		if(optionSelected == 2) {
			g2.drawString(">", x-Settings.tileSize, y);
		}
	}
}
