package net.maniaticdevs.engine.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.maniaticdevs.main.Main;

/**
 * Gui management, makes life easier and not a mess like the UI class from the original not_found
 * @author Oikmo
 */
public class GuiScreen {
	/** Global font */
	public static Font font;
	
	/** Sets up fonts */
	public static void init() {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, GuiScreen.class.getResourceAsStream("/fonts/VCR.TTF")).deriveFont(16F);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws string without centering
	 * @param g2 Graphics
	 * @param font To be used for text
	 * @param c To be used for text
	 * @param text Text to be shown
	 * @param x X position
	 * @param y Y position
	 */
	protected void drawString(Graphics2D g2, Font font, Color c, String text, int x, int y) {
		g2.setFont(font != null ? font : GuiScreen.font);
		g2.setColor(c);
		g2.drawString(text, x, y);
	}
	
	/**
	 * Draws string with centering
	 * @param g2 Graphics
	 * @param font To be used for text
	 * @param c To be used for text
	 * @param text Text to be shown
	 * @param x X position
	 * @param y Y position
	 */
	protected void drawStringCentered(Graphics2D g2, Font font, Color c, String text, int x, int y) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int height = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		drawString(g2, font, c, text, x-(length/2), y-(height/2));
	}
	
	/**
	 * Draws BufferedImages with centering
	 * @param g2 Graphics
	 * @param image Image to be drawn
	 * @param x X position
	 * @param y Y position
	 * @param width Width of image to be drawn
	 * @param height Height of image to be drawn
	 */
	protected void drawImage(Graphics2D g2, BufferedImage image, int x, int y, int width, int height) {
		g2.drawImage(image, x-(width/2), y-(height/2), width, height, null);
	}
	
	/**
	 * Gets right side of text
	 * @param g2 Graphics
	 * @param text Text to be measured
	 * @param tailX offset
	 * @return {@link Integer}
	 */
	protected int getXforAlignToRightText(Graphics2D g2, String text, int tailX) {   
	    int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
	/**
	 * Standard subwindow
	 * 
	 * @param g2 Graphics
	 * @param x X position
	 * @param y Y position
	 * @param width Width of window
	 * @param height Height of window
	 */
	public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
		Color c = new Color(0,0,0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);	
	}
	
	/**
	 * Subwindow with a control on the border thickness
	 * 
	 * @param g2 Graphics
	 * @param x X position
	 * @param y Y position
	 * @param width Width of window
	 * @param height Height of window
	 * @param stroke Thickness of border
	 */
	public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height, int stroke) {
		Color c = new Color(0,0,0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(stroke));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);	
	}
	/**
	 * Subwindow with colors
	 * 
	 * @param g2 Graphics
	 * @param x X position
	 * @param y Y position
	 * @param width Width of window
	 * @param height Height of window
	 * @param color Color of background
	 */
	public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height, Color color) {
		Color c = color;
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
		
	}
	
	/**
	 * Get X For Centered Text on whole screen
	 * @param g2 Graphics
	 * @param text String to be measured for
	 * @return centered x for text
	 */
	public int getXforCenteredText(Graphics2D g2, String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = (Main.getInstance().getWidth() - length)/2;
		return x;
	}
	
	/**
	 * Get X For Centered Text on specific width of screen
	 * @param g2 Graphics
	 * @param text String to be measured for
	 * @param width Specific width
	 * @return centered x for text
	 */
	public int getXforCenteredText(Graphics2D g2, String text, int width) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = (width - length)/2;
		return x;
	}
	
	/** Called on every tick by {@link Main} */
	public void tick() {}
	/** 
	 * Draw method for drawing gui
	 * @param g2 Graphics
	 */
	public void draw(Graphics2D g2) {}
}
