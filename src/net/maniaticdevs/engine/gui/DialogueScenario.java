package net.maniaticdevs.engine.gui;

import java.awt.Graphics2D;

/**
 * Easy handling of epic cool things in the dialogue scenario
 * @author Oikmo
 *
 */
public abstract class DialogueScenario {
	
	/** Logic function to do logic things */
	public abstract void tick();
	/** 
	 * Draws the required things on the screen
	 *  @param g2 Graphics
	 */
	public abstract void draw(Graphics2D g2);
	/** 
	 * When a new dialogue is called, this is also called
	 * @param name Name of person speaking
	 * @param dialogue The speech itself
	 */
	public abstract void callback(String name, String dialogue);
	/** 
	 * When a new dialogue is called, this is also called but another line ahead
	 * @param name Name of person speaking
	 * @param dialogue The speech itself
	 */
	public abstract void callbackAhead(String name, String dialogue);
	
	
}
