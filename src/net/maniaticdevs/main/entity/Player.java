package net.maniaticdevs.main.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.Main;

/**
 * Player class! Does player things...
 * 
 * @author Oikmo
 *
 */
public class Player extends Entity {
	
	@Override
	protected void setDefaultValues() {
		try {
			image = ImageUtils.scaleImage(ImageIO.read(Main.class.getResourceAsStream("/textures/player/jim.png")), Settings.tileSize, Settings.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		position.set(25, 25);
	}
	
	//float motionX, motionY;
	
	@Override
	public void tick() {
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
		//g2.drawRect(position.x, position.y, Settings.tileSize, Settings.tileSize);
		g2.drawImage(image, null, position.x, position.y);
	}
}
