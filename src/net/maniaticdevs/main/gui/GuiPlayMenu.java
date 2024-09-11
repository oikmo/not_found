package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.concurrent.ThreadLocalRandom;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.web.WebServer;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;
import net.maniaticdevs.main.entity.Player;
import net.maniaticdevs.main.level.SampleLevel;

/**
 * When the player first opens the game or ragequits they go here :P
 * @author Oikmo
 */
public class GuiPlayMenu extends GuiScreen {
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockUp = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockDown = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEnter = false;

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
					Main.currentLevel = new SampleLevel(false);
					Main.thePlayer = new Player(); // would you look at that
					Main.currentScreen = new GuiInGame();
					Main.webThread = new Thread(new WebServer(ThreadLocalRandom.current().nextInt(12860, 72000)));
					Main.webThread.start();
					break;
				case 1:
					Main.currentScreen = new GuiMultiplayerChoice();
					break;
				case 2:
					Main.currentScreen  = new GuiMainMenu();
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
		drawPlayMenuScreen(g2);
	}
	
	/**
	 * Draws the title screen
	 * @param g2 Graphics
	 */
	public void drawPlayMenuScreen(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());
		
		g2.setColor(Color.WHITE);
		g2.setFont(font);
		//menu
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		String text = "You are " + Main.playerName + " and always will be";
		g2.drawString(text, getXforCenteredText(g2, text), 48);
		

		//System.out.println((gp.screenWidth/2) - (gp.tileSize + (gp.tileSize*2) + 16));
		int x = (Main.getInstance().getWidth()/2);
		int y = (Main.getInstance().getHeight()/2);
		//g2.drawImage(gp.player.shadow, x,(int) (y - gp.tileSize), gp.tileSize*5, gp.tileSize*5, null);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		text = "SOLO";
		x = getXforCenteredText(g2, text);
		g2.drawString(text, x, y);
		if(optionSelected == 0) {
			g2.drawString(">", x-Settings.tileSize, y);
		}

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		text = "MULTIPLAYER";
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		if(optionSelected == 1) {
			g2.drawString(">", x-Settings.tileSize, y);
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
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
