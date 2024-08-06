package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;

/**
 * I paused my game to be here.
 * <br>Gives options to player as to change loadout, dump inventory, quitting and the alike
 * @author Oikmo
 */
public class GuiPauseScreen extends GuiScreen {
	
	/** Sub {@link GuiScreen} to be rendered */
	private GuiScreen subScreen;

	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockUp = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockDown = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEnter = false;
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEscape = false;
	
	/** Hovering option */
	private int optionSelection = 0;
	/** Selected option */
	private int optionSelected = -1;
	
	/** Prevent action being done on the same frame as the last one to enter */
	private int tickDelay = 30;

	public void tick() {
		if(tickDelay != 0) { 
			tickDelay--;
		}

		if(Input.isKeyDownExplicit(Input.KEY_ESC)) {
			if(!lockEscape) {
				if(optionSelected != -1) {
					optionSelected = -1;
				} else {
					if(tickDelay <= 0) {
						Main.currentScreen = new GuiInGame();
					}
				}
				Main.sfxLib.play(SoundSFXEnum.cursor);
			}
			lockEscape = true;
		} else {
			lockEscape = false;
		}
		if(optionSelected == -1) {
			if(Input.isKeyDownExplicit(Input.KEY_UP)) {
				if(!lockUp) {
					if(optionSelection !=0) {
						optionSelection--;
					} else {
						optionSelection = 4;
					}
					Main.sfxLib.play(SoundSFXEnum.cursor);
				}
				lockUp = true;
			} else {
				lockUp = false;
			}

			if(Input.isKeyDownExplicit(Input.KEY_DOWN)) {
				if(!lockDown) {
					if(optionSelection !=4) {
						optionSelection++;
					} else {
						optionSelection = 0;
					}
					Main.sfxLib.play(SoundSFXEnum.cursor);
				}
				lockDown = true;
			} else {
				lockDown = false;
			}

			if(Input.isKeyDownExplicit(Input.KEY_ENTER)) {
				if(!lockEnter) {
					optionSelected = optionSelection;
					Main.sfxLib.play(SoundSFXEnum.hit);
				}
				lockEnter = true;
			} else {
				lockEnter = false;
			}
		}
		if(subScreen != null) {
			subScreen.tick();
		}
	}

	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(Main.getInstance().getX(),Main.getInstance().getY(),Main.getInstance().getWidth(),Main.getInstance().getHeight());
		g2.setFont(GuiScreen.font);
		drawPauseMenu(g2);
		
		if(optionSelected != -1) {
			switch(optionSelected) {
			case 0:
				if(subScreen != null) {
					if(subScreen.getClass() != GuiCharacterScreen.class) {
						subScreen = new GuiCharacterScreen();
					}
				} else {
					subScreen = new GuiCharacterScreen();
				}
				break;
			case 1:
				if(subScreen != null) {
					if(subScreen.getClass() != GuiInventoryScreen.class) {
						subScreen = new GuiInventoryScreen();
					}
				} else {
					subScreen = new GuiInventoryScreen();
				}
				break;
			case 2:
				if(subScreen != null) {
					if(subScreen.getClass() != GuiStatsScreen.class) {
						subScreen = new GuiStatsScreen();
					}
				} else {
					subScreen = new GuiStatsScreen();
				}
				break;
			case 4:
				System.exit(0);
				break;
			}
		} else {
			if(subScreen != null) {
				subScreen = null;
			}
			drawTimeStamp(g2);
			if(Main.theNetwork != null) {
				drawOnlinePlayers(g2);
			}
		}
		if(subScreen != null) {
			subScreen.draw(g2);
		}
	}

	/**
	 * Draws the pause menu showing all options.
	 * @param g2 Graphics
	 */
	public void drawPauseMenu(Graphics2D g2) {
		//create frame :)
		final int frameX = Settings.tileSize*2;
		final int frameY = Settings.tileSize;
		final int frameWidth = Settings.tileSize*5;
		final int frameHeight = Settings.tileSize*11;
		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight, 3);

		//text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(24f));

		int textX = frameX + Settings.tileSize;

		int textY = frameY + Settings.tileSize;
		final int lineHeight = 32;

		//names
		g2.setFont(g2.getFont().deriveFont(30f));
		g2.drawString("Pause Menu", frameX + getXforCenteredText(g2, "Pause Menu", frameWidth), textY);
		textY += lineHeight * 1.25f;

		int textXStart = frameX + 20;
		int textYStart = (int) (frameY + Settings.tileSize + (lineHeight * 1.4f));

		g2.setFont(g2.getFont().deriveFont(24f));

		g2.drawString("Character", textX, textY);
		textY += lineHeight;
		g2.drawString("Inventory", textX, textY);
		textY += lineHeight;
		g2.drawString("Stats", textX, textY);
		textY += lineHeight;
		g2.drawString("Settings", textX, textY);
		textY += lineHeight;
		g2.drawString("Save & Quit", textX, textY);
		textY += lineHeight;

		g2.drawString("*", textXStart, textYStart+(optionSelection*lineHeight));

		//g2.drawImage(Main.thePlayer.currentWeapon.image, tailX - Settings.tileSize, textY-14, null);
		//textY += gp.tileSize;
		//g2.drawImage(Main.thePlayer.currentShield.image, tailX - Settings.tileSize, textY-14, null);
	}
	
	/**
	 * Draws a sub window showing how long the player has been playing for from {@link Main#startedPlaying}
	 * @param g2 Graphics
	 */
	public void drawTimeStamp(Graphics2D g2) {
		//frame
		int frameY = Settings.tileSize;
		int frameWidth = (int) (Settings.tileSize*6.075);
		int frameHeight = Settings.tileSize*2;
		int x = Settings.tileSize;
		int tailX = (x + Main.getInstance().getWidth()) - frameWidth/2;
		int frameX = tailX - frameWidth;

		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight, 3);
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(16f));

		int textX = frameX + Settings.tileSize/2;
		int textY = (int) (frameY + Settings.tileSize/1.5);
		
		final int lineHeight = 32;
		
		g2.drawString("You have been playing for:", textX, textY);
		textY += lineHeight;
		g2.setFont(g2.getFont().deriveFont(20f));
		long totalTime = ((System.currentTimeMillis()-Main.startedPlaying)/1000);
		int seconds = (int) (totalTime % 60);
		int minutes = (int) (totalTime / 60);
		int hours = minutes / 60;
		String time = "";
		
		if(hours != 0) {
			time += hours + "hrs ";
		}
		if(minutes != 0) {
			time += minutes + "m ";
		}
		time += seconds + "s";
		
		g2.drawString(time, textX, textY);
	}
	
	/**
	 * Draws a sub window showing how long the player has been playing for from {@link Main#startedPlaying}
	 * @param g2 Graphics
	 */
	public void drawOnlinePlayers(Graphics2D g2) {
		//frame
		int frameY = (int) (Settings.tileSize*2 + Settings.tileSize*1.5f);
		int frameWidth = (int) (Settings.tileSize*6.075);
		int frameHeight = (int) (Settings.tileSize*8.35f);
		
		int x = Settings.tileSize;
		int tailX = (x + Main.getInstance().getWidth()) - frameWidth/2;
		int frameX = tailX - frameWidth;

		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight, 3);
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(16f));

		int textX = frameX + Settings.tileSize/2;
		int textY = (int) (frameY + Settings.tileSize/1.5);
		
		final int lineHeight = 32;
		
		g2.drawString("Online Players", (int) (textX+this.getXforCenteredText(g2, "Online Players", frameWidth)/1.5), textY);
		g2.setFont(g2.getFont().deriveFont(22f));
		
		textY += lineHeight;
		
		if(Main.theNetwork.players.values().size() != 0) {
			try {
				for(OtherPlayer p : Main.theNetwork.players.values()) { 
					g2.drawString(p.userName, textX, textY);
					textY += lineHeight;
				}
			} catch(Exception e) {}
		} else {
			g2.drawString("It's just you!", textX, textY);	
		}
		
	}
}
