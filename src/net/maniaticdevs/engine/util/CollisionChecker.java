package net.maniaticdevs.engine.util;

import java.awt.Rectangle;
import java.util.ConcurrentModificationException;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.server.MainServer;
import net.maniaticdevs.engine.network.server.MainServerListener;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.tile.Tile;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.entity.Player;

/**
 * Detects collision on entities.
 * @author Oikmo
 */
public class CollisionChecker {
	/**
	 * Checks to see if entity is colliding with a tile from {@link Main#currentLevel}
	 * @param entity subject to collision testing
	 */
	public static void checkTile(Entity entity) {
		int entityLeftWorldX = entity.getPosition().x + entity.getHitBox().x;
		int entityRightWorldX = entity.getPosition().x + entity.getHitBox().x + entity.getHitBox().width;
		int entityTopWorldY = entity.getPosition().y + entity.getHitBox().y;
		int entityBottomWorldY = entity.getPosition().y + entity.getHitBox().y + entity.getHitBox().height;
		
		int entityLeftCol = entityLeftWorldX/Settings.tileSize;
		int entityRightCol = entityRightWorldX/Settings.tileSize;
		int entityTopRow = entityTopWorldY/Settings.tileSize;
		int entityBottomRow = entityBottomWorldY/Settings.tileSize;
		
		Tile tileNum1, tileNum2;
		
		switch(entity.getDirection()) {
		case NORTH:
			entityTopRow = (entityTopWorldY - entity.getSpeed())/Settings.tileSize;
			tileNum1 = Main.currentLevel.getTileAt(entityLeftCol,entityTopRow);
			tileNum2 = Main.currentLevel.getTileAt(entityRightCol,entityTopRow);
			if(tileNum1.collision || tileNum2.collision) {
				entity.colliding = true;
			}
			break;
		case SOUTH:
			entityBottomRow = (entityBottomWorldY + entity.getSpeed())/Settings.tileSize;
			tileNum1 = Main.currentLevel.getTileAt(entityLeftCol,entityBottomRow);
			tileNum2 = Main.currentLevel.getTileAt(entityRightCol,entityBottomRow);
			if(tileNum1.collision || tileNum2.collision) {
				entity.colliding = true;
			}
			break;
		case WEST:
			entityLeftCol = (entityLeftWorldX - entity.getSpeed())/Settings.tileSize;
			tileNum1 = Main.currentLevel.getTileAt(entityLeftCol,entityTopRow);
			tileNum2 = Main.currentLevel.getTileAt(entityLeftCol,entityBottomRow);
			if(tileNum1.collision || tileNum2.collision) {
				entity.colliding = true;
			}
			break;
		case EAST:
			entityRightCol = (entityRightWorldX + entity.getSpeed())/Settings.tileSize;
			tileNum1 = Main.currentLevel.getTileAt(entityRightCol,entityTopRow);
			tileNum2 = Main.currentLevel.getTileAt(entityRightCol,entityBottomRow);
			if(tileNum1.collision || tileNum2.collision) {
				entity.colliding = true;
			}
			break;
		default:
			break;
		}
	}
	/**
	 * Checks to see if entity is colliding with an object from {@link Main#currentLevel}<br>
	 * If entity is player, then the collided object will be returned.
	 * 
	 * @param entity subject to collision testing
	 * @return {@link OBJ}
	 */
	public static OBJ checkObject(Entity entity) {
		boolean player = entity instanceof Player;
		
		OBJ interactedOBJ = null;
		
		Rectangle entityChecker = new Rectangle();
		Rectangle objChecker = new Rectangle();
		
		for(OBJ object : Main.currentLevel.getObjects()) {
			int solidEntityX = entity.getPosition().x + entity.getHitBox().x;
			int solidEntityY = entity.getPosition().y + entity.getHitBox().y;
			entityChecker.x = solidEntityX;
			entityChecker.y = solidEntityY;
			entityChecker.width = entity.getHitBox().width;
			entityChecker.height = entity.getHitBox().height;
			
			int solidObjX = object.position.x + object.getHitBox().x;
			int solidObjY = object.position.y + object.getHitBox().y;
			
			objChecker.x = solidObjX;
			objChecker.y = solidObjY;
			objChecker.width = object.getHitBox().width;
			objChecker.height = object.getHitBox().height;
			switch(entity.getDirection()) {
			case EAST:
				entityChecker.x += entity.getSpeed();
				if(entityChecker.intersects(objChecker)) {
					if(object.collision) {
						entity.colliding = true;
					}
					if(player) {
						interactedOBJ = object;
					}
				}
				break;
			case NORTH:
				entityChecker.y -= entity.getSpeed();
				if(entityChecker.intersects(objChecker)) {
					if(object.collision) {
						entity.colliding = true;
					}
					if(player) {
						interactedOBJ = object;
					}
				}
				break;
			case SOUTH:
				entityChecker.y += entity.getSpeed();
				if(entityChecker.intersects(objChecker)) {
					if(object.collision) {
						entity.colliding = true;
					}
					if(player) {
						interactedOBJ = object;
					}
				}
				break;
			case WEST:
				entityChecker.x -= entity.getSpeed();
				if(entityChecker.intersects(objChecker)) {
					if(object.collision) {
						entity.colliding = true;
					}
					if(player) {
						interactedOBJ = object;
					}
				}
				break;
			default:
				break;
			}
		}
		return interactedOBJ;
	}
	
	/**
	 * Checks to see if entity is colliding with an entity from {@link Main#currentLevel}<br>
	 * If entity is player, then the collided entity will be returned.
	 * 
	 * @param entity subject to collision testing
	 * @return {@link Entity}
	 */
	public static Entity checkEntity(Entity entity) {
		boolean player = entity instanceof Player;
		
		Entity interactedEntity = null;
		
		Rectangle selfEntityChecker = new Rectangle();
		Rectangle entityChecker = new Rectangle();
		Iterable<Entity> entities = Main.currentLevel.getEntities();
		
		if(Main.server != null) {
			entities = MainServer.currentLevel.getEntities();
		}
		
		for(Entity ent : entities) {
			if(ent != entity) {
				int solidSelfEntityX = entity.getPosition().x + entity.getHitBox().x;
				int solidSelfEntityY = entity.getPosition().y + entity.getHitBox().y;
				selfEntityChecker.x = solidSelfEntityX;
				selfEntityChecker.y = solidSelfEntityY;
				selfEntityChecker.width = entity.getHitBox().width;
				selfEntityChecker.height = entity.getHitBox().height;
				
				int solidEntityX = ent.getPosition().x + ent.getHitBox().x;
				int solidEntityY = ent.getPosition().y + ent.getHitBox().y;
				
				entityChecker.x = solidEntityX;
				entityChecker.y = solidEntityY;
				entityChecker.width = ent.getHitBox().width;
				entityChecker.height = ent.getHitBox().height;
				switch(entity.getDirection()) {
				case EAST:
					selfEntityChecker.x += entity.getSpeed();
					if(selfEntityChecker.intersects(entityChecker)) {
						entity.colliding = true;
						if(player) {
							interactedEntity = ent;
						}
					}
					break;
				case NORTH:
					selfEntityChecker.y -= entity.getSpeed();
					if(selfEntityChecker.intersects(entityChecker)) {
						entity.colliding = true;
						if(player) {
							interactedEntity = ent;
						}
					}
					break;
				case SOUTH:
					selfEntityChecker.y += entity.getSpeed();
					if(selfEntityChecker.intersects(entityChecker)) {
						entity.colliding = true;
						if(player) {
							interactedEntity = ent;
						}
					}
					break;
				case WEST:
					selfEntityChecker.x -= entity.getSpeed();
					if(selfEntityChecker.intersects(entityChecker)) {
						entity.colliding = true;
						if(player) {
							interactedEntity = ent;
						}
					}
					break;
				default:
					break;
				}
			}
			
		}
		return interactedEntity;
	}
	
	/**
	 * Checks to see if entity is colliding with an entity from {@link Main#currentLevel}<br>
	 * If entity is player, then the collided entity will be returned.
	 * 
	 * @param entity subject to collision testing
	 * @return {@link Entity}
	 */
	public static void checkPlayer(Entity entity) {
		
		Rectangle selfEntityChecker = new Rectangle();
		Rectangle entityChecker = new Rectangle();
		
		Entity ent = Main.thePlayer;
		
		int solidSelfEntityX = entity.getPosition().x + entity.getHitBox().x;
		int solidSelfEntityY = entity.getPosition().y + entity.getHitBox().y;
		selfEntityChecker.x = solidSelfEntityX;
		selfEntityChecker.y = solidSelfEntityY;
		selfEntityChecker.width = entity.getHitBox().width;
		selfEntityChecker.height = entity.getHitBox().height;
		
		int solidEntityX = ent.getPosition().x + ent.getHitBox().x;
		int solidEntityY = ent.getPosition().y + ent.getHitBox().y;
		
		entityChecker.x = solidEntityX;
		entityChecker.y = solidEntityY;
		entityChecker.width = ent.getHitBox().width;
		entityChecker.height = ent.getHitBox().height;
		switch(entity.getDirection()) {
		case EAST:
			selfEntityChecker.x += entity.getSpeed();
			if(selfEntityChecker.intersects(entityChecker)) {
				entity.colliding = true;
			}
			break;
		case NORTH:
			selfEntityChecker.y -= entity.getSpeed();
			if(selfEntityChecker.intersects(entityChecker)) {
				entity.colliding = true;
			}
			break;
		case SOUTH:
			selfEntityChecker.y += entity.getSpeed();
			if(selfEntityChecker.intersects(entityChecker)) {
				entity.colliding = true;
			}
			break;
		case WEST:
			selfEntityChecker.x -= entity.getSpeed();
			if(selfEntityChecker.intersects(entityChecker)) {
				entity.colliding = true;
			}
			break;
		default:
			break;
		}
	}
	
	/** Checks to see if entity is colliding with another player from NetworkHandler<br>
	 * If entity is player, then the collided player will be returned.
	 * 
	 * @param entity subject to collision testing
	 * @return {@link OtherPlayer}
	 */
	public static OtherPlayer checkOtherPlayer(Entity entity) {
		boolean player = entity instanceof Player;
		
		OtherPlayer interactedPlayer = null;
		
		Rectangle selfEntityChecker = new Rectangle();
		Rectangle entityChecker = new Rectangle();
		
		for(OtherPlayer otherplayer : player ? Main.theNetwork.players.values() : MainServerListener.players.values()) {
			int solidSelfEntityX = entity.getPosition().x + entity.getHitBox().x;
			int solidSelfEntityY = entity.getPosition().y + entity.getHitBox().y;
			selfEntityChecker.x = solidSelfEntityX;
			selfEntityChecker.y = solidSelfEntityY;
			selfEntityChecker.width = entity.getHitBox().width;
			selfEntityChecker.height = entity.getHitBox().height;
			
			int solidEntityX = otherplayer.x + otherplayer.getHitBox().x;
			int solidEntityY = otherplayer.y + otherplayer.getHitBox().y;
			
			entityChecker.x = solidEntityX;
			entityChecker.y = solidEntityY;
			entityChecker.width = otherplayer.getHitBox().width;
			entityChecker.height = otherplayer.getHitBox().height;
			switch(entity.getDirection()) {
			case EAST:
				selfEntityChecker.x += entity.getSpeed();
				if(selfEntityChecker.intersects(entityChecker)) {
					entity.colliding = true;
					if(player) {
						interactedPlayer = otherplayer;
					}
				}
				break;
			case NORTH:
				selfEntityChecker.y -= entity.getSpeed();
				if(selfEntityChecker.intersects(entityChecker)) {
					entity.colliding = true;
					if(player) {
						interactedPlayer = otherplayer;
					}
				}
				break;
			case SOUTH:
				selfEntityChecker.y += entity.getSpeed();
				if(selfEntityChecker.intersects(entityChecker)) {
					entity.colliding = true;
					if(player) {
						interactedPlayer = otherplayer;
					}
				}
				break;
			case WEST:
				selfEntityChecker.x -= entity.getSpeed();
				if(selfEntityChecker.intersects(entityChecker)) {
					entity.colliding = true;
					if(player) {
						interactedPlayer = otherplayer;
					}
				}
				break;
			default:
				break;
			}
		}
		return interactedPlayer;
	}
	
	/**
	 * Check if contact is made with object
	 * @param entity Entity subject to collision
	 * @return {@link OBJ}
	 */
	public static OBJ checkIfTouchingObj(Entity entity) {
		Rectangle entityChecker = new Rectangle();
		Rectangle objChecker = new Rectangle();
		
		try {
			for(OBJ obj : Main.currentLevel.getObjects()) {
				int solidEntityX = entity.getPosition().x + entity.getHitBox().x;
				int solidEntityY = entity.getPosition().y + entity.getHitBox().y;
				entityChecker.x = solidEntityX;
				entityChecker.y = solidEntityY;
				entityChecker.width = entity.getHitBox().width;
				entityChecker.height = entity.getHitBox().height;
				
				int solidObjX = obj.position.x + obj.getHitBox().x;
				int solidObjY = obj.position.y + obj.getHitBox().y;
				
				objChecker.x = solidObjX;
				objChecker.y = solidObjY;
				objChecker.width = obj.getHitBox().width;
				objChecker.height = obj.getHitBox().height;
				
				int xDist = Math.abs(entityChecker.x - objChecker.x);
				int yDist = Math.abs(entityChecker.y - objChecker.y);
				int dist = Math.max(xDist, yDist);
				
				if(dist < Settings.tileSize) {
					if(entityChecker.intersects(objChecker)) {
						return obj;
					}
				}
			}
		} catch(ConcurrentModificationException e) {
			
		}
		
		return null;
	}
}
