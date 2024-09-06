package net.maniaticdevs.engine.entity;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.network.packet.PacketEntityUpdateX;
import net.maniaticdevs.engine.network.packet.PacketEntityUpdateY;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityDirection;
import net.maniaticdevs.engine.network.server.MainServer;
import net.maniaticdevs.engine.util.CollisionChecker;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

/**
 * Runs around doing stupid stuff (and may give out quests to players)
 * @author Oikmo
 *
 */
public class NPC extends Entity {
	
	/** For {@link net.maniaticdevs.main.gui.GuiDialogue} */
	public List<String> dialogueToBeLoaded;
	
	/** Completely random decision making */
	protected Random random = new Random();
	
	/** If it reaches {@link #maximumLockCount} then it does an action */
	protected int actionLockCounter = 0;
	/** Action time */
	protected int maximumLockCount = 120;
	/** To represent NPC on screen from player position */
	protected Vector2 playerPos = new Vector2(), playerScreenPos = new Vector2();
	/** Networking only, for keeping track of position as to update when needed */
	protected Vector2 lastPos = new Vector2(position);
	
	/** Networking only, for keeping track of animation frames as to update when needed */
	protected int lastFrame = spriteNum;
	/** Networking only, for keeping track of direction as to update when needed */
	protected int lastDirection = direction.ordinal();
	
	/** Freeze */
	public boolean lock;
	
	/**
	 * NPC Constructor
	 * @param position Where to spawn at
	 */
	public NPC(Vector2 position) {
		this.position.set(position);
	}
	
	protected void setDefaultValues() {}
	
	/** if NPC gets damaged, do something. */
	public void react() {}
	
	/** To not overwrite the tick loop */
	protected void altTick() {}
	
	public void tick() {
		
		altTick();
		
		actionLockCounter++;
		
		lastFrame = spriteNum;
		animate();
		if(lastFrame != spriteNum && Main.server != null) {
			PacketUpdateEntityAnimation packet = new PacketUpdateEntityAnimation();
			packet.anim = spriteNum;
			packet.networkID = this.networkID;
			MainServer.server.sendToAllUDP(packet);
		}
		
		if(actionLockCounter >= maximumLockCount && !lock) {
			lastPos.set(getPosition());
			int i = random.nextInt(100)+1 + speed;
			direction = EntityDirection.IDLE;
			if(Main.server != null) {
				PacketUpdateEntityDirection packet = new PacketUpdateEntityDirection();
				packet.dir = EntityDirection.IDLE.ordinal();
				packet.networkID = this.networkID;
				MainServer.server.sendToAllUDP(packet);
			}
			
			//pure random
			int vlow = random.nextInt(25);
			int low = random.nextInt(50);
			if(low < vlow) { low = vlow + 25; }
			int medium = random.nextInt(75);
			if(medium < low) { medium = low + 25; }
			int high = random.nextInt(100);
			if(high < medium) { high = medium + 25; }
			
			lastDirection = direction.ordinal();
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
			
			if(lastDirection != direction.ordinal() && Main.server != null) {
				PacketUpdateEntityDirection packet = new PacketUpdateEntityDirection();
				packet.dir = direction.ordinal();
				packet.networkID = this.networkID;
				MainServer.server.sendToAllUDP(packet);
			}
		}
		if(actionLockCounter > maximumLockCount) {
			direction = EntityDirection.IDLE;
			if(Main.server != null) {
				PacketUpdateEntityDirection packet = new PacketUpdateEntityDirection();
				packet.dir = EntityDirection.IDLE.ordinal();
				packet.networkID = this.networkID;
				MainServer.server.sendToAllUDP(packet);
			}
		}
		
		colliding = false;
		if(Main.currentLevel != null) {
			CollisionChecker.checkTile(this);
			CollisionChecker.checkObject(this);
			CollisionChecker.checkEntity(this);
			if(Main.server != null) {
				CollisionChecker.checkOtherPlayer(this);
			} else {
				CollisionChecker.checkPlayer(this);
			}
			
		} else {
			return;
		}
		
		if(!colliding && !lock) {
			
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
			if(Main.server != null) {
				PacketUpdateEntityDirection packet = new PacketUpdateEntityDirection();
				packet.dir = EntityDirection.IDLE.ordinal();
				packet.networkID = this.networkID;
				MainServer.server.sendToAllUDP(packet);
			}
			
		}
		
		if(Main.server != null) {
			if(lastPos.x != position.x) {
				PacketEntityUpdateX packetX = new PacketEntityUpdateX();
				packetX.networkID = this.networkID;
				packetX.x = position.x;
				MainServer.server.sendToAllTCP(packetX);
			}
			if(lastPos.y != position.y) {
				PacketEntityUpdateY packetY = new PacketEntityUpdateY();
				packetY.networkID = this.networkID;
				packetY.y = position.y;
				MainServer.server.sendToAllTCP(packetY);
			}
		}
		
	}
	
	/**
	 * Drawing purposes
	 * @param playerPos Position of player
	 * @param playerScreenPos Screen position of player
	 */
	public void update(Vector2 playerPos, Vector2 playerScreenPos) {
		this.playerPos.set(playerPos);
		this.playerScreenPos.set(playerScreenPos);
	}
	
	public void draw(Graphics2D g2) {
		int screenX = getPosition().x - playerPos.x + playerScreenPos.x;
		int screenY = getPosition().y - playerPos.y + playerScreenPos.y;

		if(getPosition().x + Settings.worldTileSize > playerPos.x - playerScreenPos.x &&
				getPosition().x - Settings.worldTileSize < playerPos.x + playerScreenPos.x &&
				getPosition().y + Settings.worldTileSize > playerPos.y - playerScreenPos.y &&
				getPosition().y - Settings.worldTileSize < playerPos.y + playerScreenPos.y) {
			g2.drawImage(sprites[spriteNum+direction.ordinal()*6], screenX, screenY, Settings.worldTileSize, Settings.worldTileSize, null);
		}
	}
}
