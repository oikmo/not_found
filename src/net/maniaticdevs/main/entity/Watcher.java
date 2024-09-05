package net.maniaticdevs.main.entity;

import java.util.Random;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Monster;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiInGame;

public class Watcher extends Monster {
	public Watcher(Vector2 position) {
		super(position);
		
		name = "EYE";
		speed = 1;
		maxHealth = 8;
		health = maxHealth;
		attack = 5;
		defense = 0;
		exp = 4;
		sprites = ImageUtils.setupSheet("npc/watcher_sprite", 6, 5);
	}
	
	public void altTick() {
		try {
			float distance = (float) (Math.abs(Main.thePlayer.getPosition().magnitude() - this.position.magnitude())/Settings.tileSize);
			if(distance <= 2) {
				if(distance <= 1) {
					if(distance <= 0.5f) {
						GuiInGame.sizeGlitches = 20;
					} else {
						GuiInGame.sizeGlitches = 10;
					}
					
				} else {
					GuiInGame.sizeGlitches = 5;
				}
			} else {
				if(distance <= 4) {
					GuiInGame.sizeGlitches = 1;
				} else {
					GuiInGame.sizeGlitches = 0;
				}
				
			}
		} catch(NullPointerException e) {}
	}
	
	public void react() {
		actionLockCounter = 0;
		direction = Main.thePlayer.getDirection();
	}
	
	public void dropItem(OBJ droppedItem) {
		droppedItem.position.x = position.x;
		droppedItem.position.y = position.y;
		Main.currentLevel.addObject(droppedItem);
	}
	
	public void checkDrop() {
		//cast a die
		int i = new Random().nextInt(100)+1;
		
		//set the monster drop
		if(i < 50) {
			//dropItem(new OBJ_Coin_Bronze(gp));
		}
		if(i >= 50 && i < 50) {
			//dropItem(new OBJ_Heart(gp));
		}
		if(i >= 75 && i < 100) {
			//dropItem(new OBJ_Mana(gp));
		}
	}
}
