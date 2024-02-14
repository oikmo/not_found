package net.maniaticdevs.engine.entity;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import net.maniaticdevs.engine.util.math.Vector2;

public class Monster extends NPC {
	public Rectangle patrolBox;
	
	public Monster(Vector2 position) {
		super(position);
	}
	
	protected void setDefaultValues() {}
	
	public void createRange(int range, int screenX, int screenY) {	    
	    double centerX = hitBox.getCenterX() + screenX;
	    double centerY = hitBox.getCenterY() + screenY;
	    double radius = range * 10;

	    Ellipse2D circle = new Ellipse2D.Double();
	    circle.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
		
	    patrolBox = new Rectangle((int)circle.getX(),(int)circle.getY(), (int) circle.getWidth(), (int)circle.getHeight());
	}
}
