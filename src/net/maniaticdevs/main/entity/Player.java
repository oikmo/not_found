package net.maniaticdevs.main.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.util.CollisionChecker;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

/**
 * Player class! Does player things...
 * 
 * @author Oikmo
 *
 */
public class Player extends Entity {
	
	/** Position data for player to be at the center of the screen */
	private Vector2 screenPos = new Vector2();
	
	@Override
	protected void setDefaultValues() {
		screenPos = new Vector2((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
		position.set(Settings.tileSize*2, Settings.tileSize*2);
		sprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
	}
	
	//float motionX, motionY;
	
	@Override
	public void tick() {
		screenPos.set((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
		
		animate();
		
		if(Input.isKeyDown(Input.KEY_D)) {
			direction = EntityDirection.EAST;
		} else if(Input.isKeyDown(Input.KEY_A)) {
			direction = EntityDirection.WEST;
		} else if(Input.isKeyDown(Input.KEY_S)) {
			direction = EntityDirection.SOUTH;
		} else if(Input.isKeyDown(Input.KEY_W)) {
			direction = EntityDirection.NORTH;
		} else {
			direction = EntityDirection.IDLE;
		}
		
		/*float dist = xa * xa + ya * ya;
		if(dist >= 0.01F) {
			dist = speed / (float)Math.sqrt((double)dist);
			xa *= dist;
			ya *= dist;
			this.motionX += xa;
			this.motionY += ya ;
		}
		
		this.motionX *= 0.91F;
		this.motionY *= 0.91F;*/
		colliding = false;
		CollisionChecker.checkTile(this);
		if(!colliding) {
			switch(getDirection()) {
			case EAST:
				position.x += speed;
				break;
			case NORTH:
				position.y -= speed;
				break;
			case SOUTH:
				position.y += speed;
				break;
			case WEST:
				position.x -= speed;
				break;
			default:
				break;
			
			}
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		if(Main.debug) {
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(2));
			g2.drawRect(screenPos.x+getHitBox().x, screenPos.y+getHitBox().y, getHitBox().width, getHitBox().height);
		}
		g2.drawImage(sprites[spriteNum+(getDirection().ordinal()*6)], null, screenPos.x, screenPos.y);
		
	}
	
	/**
	 * Returns {@link #screenPos}
	 * @return {@link Vector2}
	 */
	public Vector2 getScreenPosition() {
		return screenPos;
	}
}
