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
			computerImage = ResourceLoader.loadImage("textures/computer");
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
			
			if(Input.getTextInput().contentEquals("ls")) {
				input.add("ls command?? no way");
			} else {
				input.add("command not found: "+ Input.getTextInput());
			}
			Input.clearInput();
			Input.needsInput = true;
			
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());
		int x = (Main.getInstance().getWidth()/2)-Main.getInstance().getHeight()/2;
		int y = 0;
		g2.drawImage(computerImage, x, y, Main.getInstance().getHeight(), Main.getInstance().getHeight(), null);
		String toShow = Input.getTextInput();
		int screenlength = (int)((Main.getInstance().getHeight()/1.3f)/GuiScreen.font.getSize())-1;
		if(Input.getTextInput().length() > screenlength) {
			toShow = toShow.substring(Input.getTextInput().length()-screenlength, Input.getTextInput().length());
		}
		this.drawString(g2, font, Color.green, "> "+toShow+(cursor ? "_" : ""), x+(int)(Main.getInstance().getHeight()/3.9f), y+(Main.getInstance().getHeight()/5)+ (int)(Main.getInstance().getHeight()/2.72f));
		int textY = 17;
		int size = input.size();
		
		screenlength = (int)((Main.getInstance().getHeight()/1.3f)/GuiScreen.font.getSize())-1;
		if(input.size() > (int)((Main.getInstance().getHeight()/2.41f)/17)-1) {
			for(int i = size-1; i >= input.size()-((int)((Main.getInstance().getHeight()/2.41f)/17)-1); i--) {
				String inp = input.get(i);
				drawString(g2, font, Color.green, inp.length() > screenlength ? inp.substring(0, screenlength)+" ->" : inp, x+(int)(Main.getInstance().getHeight()/3.9f), y+(Main.getInstance().getHeight()/5)+ (int)(Main.getInstance().getHeight()/2.72f)-textY);
				textY += 17;
			}
		} else {
			for(int i = size-1; i >= 0; i--) {
				String inp = input.get(i);
				drawString(g2, font, Color.green, inp.length() > screenlength ? inp.substring(0, screenlength)+" ->" : inp, x+(int)(Main.getInstance().getHeight()/3.9f), y+(Main.getInstance().getHeight()/5)+ (int)(Main.getInstance().getHeight()/2.72f)-textY);
				textY += 17;
			}
		}
		
		
		if(Main.debug) {
			g2.setColor(Color.WHITE);
			g2.drawRect(x+(int)(Main.getInstance().getHeight()/4.1f), y+(Main.getInstance().getHeight()/6), (int)(Main.getInstance().getHeight()/2.005f), (int)(Main.getInstance().getHeight()/2.41f));
		}
	}

}
