package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;

/**
 * When the player first opens the game or ragequits they go here :P
 * @author Oikmo
 */
public class GuiDisconnected extends GuiScreen {
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEnter = false;

	/** Prevent action being done on the same frame as the last one to enter */
	private int tickDelay = 30;

	private String fullMessage;
	
	public GuiDisconnected(boolean kick, String message) {
		if(!kick) {
			fullMessage = "Disconnected: " + message;
		} else {
			fullMessage = "Kicked: " + message;
		}
	}
	
	public void tick() {
		if(tickDelay != 0) { 
			tickDelay--;
		}

		if(tickDelay <= 0) {
			if(Input.isKeyDownExplicit(Input.KEY_ENTER)) {
				if(!lockEnter) {
					Main.currentScreen = new GuiPlayMenu();
					Main.sfxLib.play(SoundSFXEnum.hit);
				}
				lockEnter = true;
			} else {
				lockEnter = false;
			}
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

		int x = (Main.getInstance().getWidth()/2);
		int y = (Main.getInstance().getHeight()/2);
		
		g2.setColor(Color.WHITE);
		g2.setFont(font);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		String text = fullMessage;
		x = getXforCenteredText(g2, text);
		g2.drawString(text, x, y);

		text = "BACK";
		x = getXforCenteredText(g2, text);
		y += Settings.tileSize;
		g2.drawString(text, x, y);
		g2.drawString(">", x-Settings.tileSize, y);
	}
}
