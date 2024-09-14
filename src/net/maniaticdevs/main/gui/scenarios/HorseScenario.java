package net.maniaticdevs.main.gui.scenarios;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.gui.DialogueScenario;
import net.maniaticdevs.engine.util.math.Maths;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiDialogue;

public class HorseScenario extends DialogueScenario {
	
	private boolean horse = false, horseComplete = false, playerActivate = false;
	private BufferedImage horseImg, playerImg;
	private float horseX;
	private float horseTransparency = 0;
	private int playerRunTicks, spriteCounter, spriteNum;
	
	public HorseScenario() {
		horseImg = ResourceLoader.loadImage("/textures/horse");
		playerImg = Main.thePlayer.sprites[0];
	}
	
	@Override
	public void tick() {
		if(playerActivate) {
			playerRunTicks++;
			if(playerRunTicks >= 45) {
				spriteCounter++;
				if (spriteCounter > 4) { 
					if (spriteNum >= 0) { 
						spriteNum++; 
					} 
					if (spriteNum > 5) { 
						spriteNum = 0; 
					} 
					spriteCounter = 0; 
				}
				playerImg = Main.thePlayer.sprites[spriteNum+EntityDirection.WEST.ordinal()*6];
			}
		}
		
		GuiDialogue.lockEnter = !horseComplete && horse;
		
		if(horse) {
			
			
			if(!horseComplete) {
				horseX = Maths.flerp(horseX, (Main.getInstance().getWidth()/2)+Settings.tileSize*2, 0.01f);
			} else {
				horseX = (Main.getInstance().getWidth()/2)+Settings.tileSize*2;
			}
			if((int)horseX <= ((Main.getInstance().getWidth()/2)+Settings.tileSize*2) + 10) {
				horseComplete = true;
			}
			horseTransparency = Maths.flerp(horseTransparency, 1f, 0.009f);
		} else {
			horseX = Main.getInstance().getWidth();
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		
		g2.drawImage(playerImg, GuiDialogue.x-64, GuiDialogue.y-(256), 256, 256, null);
		if(horse) {
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, horseTransparency));
			g2.drawImage(horseImg, (int)horseX, GuiDialogue.y-(256), 256, 256, null);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
		
	}

	@Override
	public void callback(String name, String dialogue) {
		if(name.contentEquals("Horse") && dialogue.contains("Go on")) {
			playerActivate = true;
		}
	}

	@Override
	public void callbackAhead(String name, String dialogue) {
		if(name.contentEquals("Horse") && dialogue.contains("Go on")) {
			horse = true;
		}
	}

}
