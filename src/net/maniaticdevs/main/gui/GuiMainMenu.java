package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.ResourceLoader;
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
public class GuiMainMenu extends GuiScreen {
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
	
	/** Player animation frames */
	private BufferedImage[] playerSprites;
	/** Shadow sprite */
	private BufferedImage playerShadow;
	/** Animation frames */
	private int spriteCounter, spriteNum;
	
	/**
	 * Load images
	 */
	public GuiMainMenu() {
		playerSprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
		playerShadow = ImageUtils.scaleImage(ResourceLoader.loadImage("/textures/player/shadow"), Settings.tileSize, Settings.tileSize);
	}
	
	public void tick() {
		spriteCounter++;
		if (spriteCounter > 4) { 
			if (spriteNum >= 0) { 
				spriteNum++; 
			} 
			if (spriteNum > 5) { 
				spriteNum = 0; 
			} 
			spriteCounter = 0; 
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
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,96F));
		int x = getXforCenteredText(g2, text);
		int y = Settings.tileSize*3;
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);

		g2.setColor(Color.white);
		g2.drawString(text, x, y);

		//System.out.println((gp.screenWidth/2) - (gp.tileSize + (gp.tileSize*2) + 16));
		x = (Main.getInstance().getWidth() - Settings.tileSize*5)/2;
		y += Settings.tileSize;
		g2.drawImage(playerShadow, ((Main.getInstance().getWidth()/2)-(Settings.tileSize*3)/2),(int) (y+(Settings.tileSize/2)), Settings.tileSize*3, Settings.tileSize*3, null);
		g2.drawImage(playerSprites[spriteNum], ((Main.getInstance().getWidth()/2)-(Settings.tileSize*3)/2),(int) (y - (Settings.tileSize/2)), Settings.tileSize*3, Settings.tileSize*3, null);

		//menu
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		text = LanguageHandler.getInstance().translateKey("gui.mainmenu.play");
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize*5;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-Settings.tileSize, y);
		}

		text = LanguageHandler.getInstance().translateKey("gui.mainmenu.options");
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-Settings.tileSize, y);

			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		}
		text = LanguageHandler.getInstance().translateKey("gui.mainmenu.quit");
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawString(">", x-Settings.tileSize, y);
		}
	}
}
