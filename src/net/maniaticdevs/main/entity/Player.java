package net.maniaticdevs.main.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;

/**
 * Player class! Does player things...
 * 
 * @author Oikmo
 *
 */
public class Player extends Entity {
	
	@Override
	protected void setDefaultValues() {
		position.set(25, 25);
		sprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
	}
	
	//float motionX, motionY;
	
	@Override
	public void tick() {
		animate();
		
		int xa = 0;
		int ya = 0;
		
		if(Input.isKeyDown(Input.KEY_D)) {
			xa += speed;
		} else if(Input.isKeyDown(Input.KEY_A)) {
			xa -= speed;
		} else if(Input.isKeyDown(Input.KEY_S)) {
			ya += speed;
		} else if(Input.isKeyDown(Input.KEY_W)) {
			ya -= speed;
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
		
		move(xa,ya);
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.drawImage(sprites[spriteNum], null, position.x, position.y);
	}
}
