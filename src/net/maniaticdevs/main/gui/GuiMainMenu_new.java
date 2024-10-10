package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.save.SaveSystem;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.properties.LanguageHandler;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;
import net.maniaticdevs.main.save.PlayerSaveData;

/**
 * When the player first opens the game or ragequits they go here :P
 * @author Oikmo
 */
public class GuiMainMenu_new extends GuiScreen {
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockUp = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockDown = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEnter = false;

	/** Hovering option */
	private int commandNum = 0;

	/** Prevent action being done on the same frame as the last one to enter */
	private int tickDelay = 60;
	
	private BufferedImage[] selector, play, options, quit;
	private int animationTicks = 0, selectorFrame, playFrame, optionsFrame;
	
	private int point5 = (int)(Settings.tileSize/1.5f);
	
	/**
	 * Load images
	 */
	public GuiMainMenu_new() {
		selector = ImageUtils.scaleArray(ImageUtils.setupSheet("ui/option_selector", 1, 3, 16, 16), point5, point5);
		play = ImageUtils.scaleArray(ImageUtils.setupSheet("ui/playgameoption", 1, 3, 100, 22), Settings.tileSize*4, Settings.tileSize);
		options = ImageUtils.scaleArray(ImageUtils.setupSheet("ui/optionsoption", 1, 3, 88, 22), Settings.tileSize*3, Settings.tileSize);
		
	}
	
	public void tick() {
		
		animationTicks++;
		
		if(animationTicks >= 120) {
			animationTicks = 0;
		} else if(animationTicks % 20 == 1) {
			
			selectorFrame++;
			if(selectorFrame > 2) {
				selectorFrame = 0;
			}
		} else if(animationTicks % 18 == 1) {
			playFrame++;
			if(playFrame > 2) {
				playFrame = 0;
			}
		} else if(animationTicks % 19 == 1) {
			optionsFrame++;
			if(optionsFrame > 2) {
				optionsFrame = 0;
			}
		}
		
		
		if(GuiChat.originalMessages.size() != 0) {
			GuiChat.originalMessages.clear();
		}
		
		if(tickDelay != 0) { 
			tickDelay--;
		}

		if(Input.isKeyDownExplicit(Input.KEY_UP)) {
			if(!lockUp) {
				if(commandNum !=0) {
					commandNum--;
				} else {
					commandNum = 2;
				}
				Main.sfxLib.play(SoundSFXEnum.cursor);
			}
			lockUp = true;
		} else {
			lockUp = false;
		}

		if(Input.isKeyDownExplicit(Input.KEY_DOWN)) {
			if(!lockDown) {
				if(commandNum != 2) {
					commandNum++;
				} else {
					commandNum = 0;
				}
				Main.sfxLib.play(SoundSFXEnum.cursor);
			}
			lockDown = true;
		} else {
			lockDown = false;
		}

		if(Input.isKeyDownExplicit(Input.KEY_ENTER)) {
			if(!lockEnter) {
				switch(commandNum) {
				case 0:
					if(SaveSystem.load("player") != null) {
						PlayerSaveData data = (PlayerSaveData)SaveSystem.load("player");
						Main.playerName = data.name;
						Main.currentScreen = new GuiPlayMenu();
					} else {
						Main.currentScreen = new GuiSelectName();
					}
					
					//Main.currentScreen = new GuiSelectName();
					break;
				case 2:
					System.exit(0);
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
		drawTitleScreen(g2);
	}
	
	/**
	 * Draws the title screen
	 * @param g2 Graphics
	 */
	public void drawTitleScreen(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());

		String text = "not_found";

		g2.setFont(font);
		g2.setFont(g2.getFont().deriveFont(Font.ITALIC,96F));
		int x = Settings.tileSize;
		int y = Settings.tileSize*3;
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//menu
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		y = (Main.getInstance().getHeight()/2);
		g2.drawImage(play[playFrame], x, y-Settings.tileSize, null);
		if(commandNum == 0) {
			g2.drawImage(selector[selectorFrame], x-Settings.tileSize/2, y-(int)(Settings.tileSize/1.5f), null);
		}

		y += Settings.tileSize;
		g2.drawImage(options[optionsFrame], x, y-Settings.tileSize, null);
		if(commandNum == 1) {
			g2.drawImage(selector[selectorFrame], x-Settings.tileSize/2, y-point5, null);
		}
		text = LanguageHandler.getInstance().translateKey("gui.mainmenu.quit");
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawImage(selector[selectorFrame], x-Settings.tileSize/2, y-point5, null);
		}
	}
}
