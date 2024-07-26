package net.maniaticdevs.main.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import jaco.mp3.player.MP3Player;
import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.util.CollisionChecker;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.Sound;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiInGame;

/**
 * Player class! Does player things...
 * 
 * @author Oikmo, LYCNK
 *
 */
public class Player extends Entity {
	
	/** Position data for player to be at the center of the screen */
	private Vector2 screenPos = new Vector2();
	/** Footsteps sound effect */
	MP3Player footSteps = new MP3Player(ResourceLoader.loadResource("/sounds/sfx/bfsl-minifigfoots1.mp3"));
	
	/** Player inventory for storing {@link OBJ}s */
	public List<OBJ> inventory = new ArrayList<>();
	/** Currently holding item */
	public OBJ holdingItem;
	
	@Override
	protected void setDefaultValues() {
		name = "Player";
		maxHealth = 6;
		health = maxHealth;
		screenPos = new Vector2((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
		position.set(Settings.tileSize*4, Settings.tileSize*4);
		sprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
	}
	
	//float motionX, motionY;
	/** To prevent action to be done repeatedly in a short period of time */
	boolean lockTakeLife = false;
	/** To prevent action to be done repeatedly in a short period of time */
	boolean lockGiveLife = false;
	@Override
	public void tick() {
		screenPos.set((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
		
		animate();
		
		direction = EntityDirection.IDLE;
		
		if(Main.currentScreen instanceof GuiInGame) {
			if(Input.isKeyDown(Input.KEY_D)) {
				direction = EntityDirection.EAST;
			} else if(Input.isKeyDown(Input.KEY_A)) {
				direction = EntityDirection.WEST;
			} else if(Input.isKeyDown(Input.KEY_S)) {
				direction = EntityDirection.SOUTH;
			} else if(Input.isKeyDown(Input.KEY_W)) {
				direction = EntityDirection.NORTH;
			}
		}
		
		
		if(Input.isKeyDown(Input.KEY_MINUS)) {
			if(!lockTakeLife) {
				health--;
				if(health <= 0) {
					System.exit(0);
				}
			}
			lockTakeLife = true;
		} else {
			lockTakeLife = false;
		}
		
		if(Input.isKeyDown(Input.KEY_EQUALS)) {
			if(!lockGiveLife) {
				health++;
				if(health > maxHealth) {
					maxHealth = ((maxHealth/2)+1)*2;
				}
			}
			lockGiveLife = true;
		} else {
			lockGiveLife = false;
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
		OBJ obj = CollisionChecker.checkObject(this);
		if(obj != null) {
			if(obj instanceof Key) {
				inventory.add(obj);
				Main.currentLevel.getObjects().remove(obj);
				Sound.playSFX("key");
			}
			if(obj instanceof Door) {
				if(holdingItem == ((Door)obj).getRequiredKey()) {
					if(((Door)obj).removeKeyAfterUse()) {
						inventory.remove(((Door)obj).getRequiredKey());
					}
					Main.currentLevel.getObjects().remove(obj);
					Sound.playSFX("door");
				}
			}
		}
		
		if(!colliding) {
			if(getDirection() != EntityDirection.IDLE) {
				if(footSteps.isStopped()) {
					footSteps.play();
				}
			} else {
				footSteps.stop();
			}
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
		} else {
			direction = EntityDirection.IDLE;
		}
	}
	
	/**
	 * Checks if index of a soon to be selected item is valid
	 * @param index Index to be checked
	 * @return {@link Boolean}
	 */
	public boolean isItemAtIndex(int index) {
		if(index+1 <= inventory.size()) {
			if(inventory.get(index) != null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Selects item to be used for {@link #holdingItem}
	 * @param index Index for item to be chosen
	 */
	public void selectItem(int index) {
		if(index+1 <= inventory.size()) {
			holdingItem = inventory.get(index);
		} else {
			holdingItem = null;
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		if(Main.debug) {
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(2));
			g2.drawRect(screenPos.x+getHitBox().x, screenPos.y+getHitBox().y, getHitBox().width, getHitBox().height);
			g2.drawString("direction:"+direction.name(), 0, 10);
			g2.drawString("KEY_W:"+Input.isKeyDown(Input.KEY_W), 0, 25);
			g2.drawString("KEY_A:"+Input.isKeyDown(Input.KEY_A), 0, 50);
			g2.drawString("KEY_S:"+Input.isKeyDown(Input.KEY_S), 0, 75);
			g2.drawString("KEY_D:"+Input.isKeyDown(Input.KEY_D), 0, 100);
		}
		g2.setColor(Color.WHITE);
		g2.setFont(GuiScreen.font);
		//int length = (int)g2.getFontMetrics(GuiScreen.font).getStringBounds(name, g2).getWidth();
		//int height = (int)g2.getFontMetrics(GuiScreen.font).getStringBounds(name, g2).getHeight();
		//g2.drawString(name, (Main.getInstance().getWidth()/2)-(length/2), screenPos.y-(height/2));
		g2.drawImage(sprites[spriteNum+getDirection().ordinal()*6], null, screenPos.x, screenPos.y);
		
	}
	
	/**
	 * Returns {@link #screenPos}
	 * @return {@link Vector2}
	 */
	public Vector2 getScreenPosition() {
		return screenPos;
	}
}
