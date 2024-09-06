package net.maniaticdevs.engine.entity;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import net.maniaticdevs.engine.util.math.Vector2;

/**
 * oooh aaahh scary monster that hurts player!!!
 * @author Oikmo
 */
public class Monster extends NPC {
	/** Search area for player */
	public Rectangle patrolBox;
	
	/**
	 * Monster constructor
	 * @param position Position to be placed at
	 */
	public Monster(Vector2 position) {
		super(position);
	}
	
	protected void setDefaultValues() {}
	
	/** 
	 * Creates {@link #patrolBox}
	 * @param range Size of box
	 * @param screenX Position X of monster on screen
	 * @param screenY Position Y of monster on screen
	 */
	public void createRange(int range, int screenX, int screenY) {	    
	    double centerX = hitBox.getCenterX() + screenX;
	    double centerY = hitBox.getCenterY() + screenY;
	    double radius = range * 10;

	    Ellipse2D circle = new Ellipse2D.Double();
	    circle.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
		
	    patrolBox = new Rectangle((int)circle.getX(),(int)circle.getY(), (int) circle.getWidth(), (int)circle.getHeight());
	}
}
