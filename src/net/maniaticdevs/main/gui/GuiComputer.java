/**
 * 
 */
package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.Main;

/**
 * To interface with <b>it.</b>
 * @author Oikmo
 */
public class GuiComputer extends GuiScreen {
	
	private static BufferedImage computerImage;
	/** Background */
	private static Color color = new Color(0,0,0,180);
	/** Ticks counter */
	private int cursorTicks = 0;
	/** Every 30 ticks that is reached by {@link #cursorTicks}, this will flip and if true the "_" will show */
	private boolean cursor = true;
	
	private List<String> input = new ArrayList<>();
	
	/**
	 * Computer GUI constructor
	 */
	public GuiComputer() {
		if(computerImage == null) {
			computerImage = ResourceLoader.loadImage("/textures/computer");
		}
		Input.inputType = Input.InputType.Chat;
		Input.needsInput = true;
		Input.lengthInput = 99;
	}
	
	public void tick() {
		if(cursorTicks > 30) {
			cursor = !cursor;
			cursorTicks = 0;
		} else {
			cursorTicks++;
		}
		if(Input.isKeyDownExplicit(Input.KEY_ENTER) && Input.getTextInput().length() != 0 && !Input.getTextInput().replace(" ", "").isEmpty()) {
			
			handleCommand(Input.getTextInput());
			Input.clearInput();
			Input.needsInput = true;
			
		}
	}
	
	private void handleCommand(String cmd) {
		if(cmd.contentEquals("ls")) {
			input.add("ls command?? no way");
		} else {
			input.add("command not found: "+ Input.getTextInput());
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		int currentMeasurement = Main.getInstance().getHeight();
		if(Main.getInstance().getHeight() > Main.getInstance().getWidth()) {
			currentMeasurement = Main.getInstance().getWidth();
		}
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());
		int x = (Main.getInstance().getWidth()/2)-currentMeasurement/2;
		int y = 0;
		g2.drawImage(computerImage, x, y, currentMeasurement, currentMeasurement, null);
		String toShow = Input.getTextInput();
		int screenlength = (int)((currentMeasurement/1.3f)/GuiScreen.font.getSize())-1;
		if(Input.getTextInput().length() > screenlength) {
			toShow = toShow.substring(Input.getTextInput().length()-screenlength, Input.getTextInput().length());
		}
		this.drawString(g2, font, Color.green, "> "+toShow+(cursor ? "_" : ""), x+(int)(currentMeasurement/3.9f), y+(currentMeasurement/5)+ (int)(currentMeasurement/2.72f));
		int textY = 17;
		int size = input.size();
		
		screenlength = (int)((currentMeasurement/1.3f)/GuiScreen.font.getSize())-1;
		if(input.size() > (int)((currentMeasurement/2.41f)/17)-1) {
			for(int i = size-1; i >= input.size()-((int)((currentMeasurement/2.41f)/17)-1); i--) {
				String inp = input.get(i);
				drawString(g2, font, Color.green, inp.length() > screenlength ? inp.substring(0, screenlength)+" ->" : inp, x+(int)(currentMeasurement/3.9f), y+(currentMeasurement/5)+ (int)(currentMeasurement/2.72f)-textY);
				textY += 17;
			}
		} else {
			for(int i = size-1; i >= 0; i--) {
				String inp = input.get(i);
				drawString(g2, font, Color.green, inp.length() > screenlength ? inp.substring(0, screenlength)+" ->" : inp, x+(int)(currentMeasurement/3.9f), y+(currentMeasurement/5)+ (int)(currentMeasurement/2.72f)-textY);
				textY += 17;
			}
		}
		
		
		if(Main.debug) {
			g2.setColor(Color.WHITE);
			g2.drawRect(x+(int)(currentMeasurement/4.1f), y+(currentMeasurement/6), (int)(currentMeasurement/2.005f), (int)(currentMeasurement/2.41f));
		}
	}

}
