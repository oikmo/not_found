package net.maniaticdevs.engine.entity;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.util.CollisionChecker;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

public class NPC extends Entity {
	
	public String networkID = UUID.randomUUID().toString();
	
	public List<String> dialogueToBeLoaded;
	
	protected Random random = new Random();
	
	public Vector2 defaultPosition = new Vector2(1,1);
	
	protected int actionLockCounter = 0;
	protected int maximumLockCount = 120;
	protected Vector2 playerPos = new Vector2(), playerScreenPos = new Vector2();

	public boolean lock;
	
	public NPC(Vector2 position) {
		this.position.set(position);
	}
	
	protected void setDefaultValues() {}
	
	public void onInteract() {}

	public void tick() {
		if((Main.theNetwork == null && Main.server == null) || Main.server != null) {
			actionLockCounter++;
			
			animate();
			
			if(actionLockCounter == maximumLockCount && !lock) {
				
				int i = random.nextInt(100)+1 + speed;
				direction = EntityDirection.IDLE;
				
				//pure random
				int vlow = random.nextInt(25);
				int low = random.nextInt(50);
				if(low < vlow) { low = vlow + 25; }
				int medium = random.nextInt(75);
				if(medium < low) { medium = low + 25; }
				int high = random.nextInt(100);
				if(high < medium) { high = medium + 25; }
				
				if(i <= vlow) {
					direction = EntityDirection.NORTH;
				}
				if(i > vlow && i <= low) {
					direction = EntityDirection.SOUTH;
				}
				if(i > low && i <= medium) {
					direction = EntityDirection.WEST;
				}
				if(i > medium && i <= high) {
					direction = EntityDirection.EAST;
				}
				actionLockCounter = 0;
			}
			
			if(actionLockCounter > maximumLockCount) {
				direction = EntityDirection.IDLE;
			}
			
			colliding = false;
			CollisionChecker.checkTile(this);
			
			if(!colliding) {
				/*if(getDirection() != EntityDirection.IDLE) {
					if(footSteps.isStopped()) {
						footSteps.play();
					}
				} else {
					footSteps.stop();
				}*/
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
	}
	
	public void update(Vector2 playerPos, Vector2 playerScreenPos) {
		this.playerPos.set(playerPos);
		this.playerScreenPos.set(playerScreenPos);
	}
	
	public void draw(Graphics2D g2) {
		int screenX = getPosition().x - playerPos.x + playerScreenPos.x;
		int screenY = getPosition().y - playerPos.y + playerScreenPos.y;

		if(getPosition().x + Settings.tileSize > playerPos.x - playerScreenPos.x &&
				getPosition().x - Settings.tileSize < playerPos.x + playerScreenPos.x &&
				getPosition().y + Settings.tileSize > playerPos.y - playerScreenPos.y &&
				getPosition().y - Settings.tileSize < playerPos.y + playerScreenPos.y) {
			g2.drawImage(sprites[spriteNum+direction.ordinal()*6], screenX, screenY, Settings.tileSize, Settings.tileSize, null);
		}
	}
	
	public void setNetworkID(String ID) {
		this.networkID = ID;
	}
}
