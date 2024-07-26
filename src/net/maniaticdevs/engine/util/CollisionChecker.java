package net.maniaticdevs.engine.util;

import java.awt.Rectangle;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
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
		entity.colliding = false;
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
	 * Check if contact is made with object
	 * @return {@link OBJ}
	 */
	public static OBJ checkIfTouchingObj(Entity entity) {
		Rectangle entityChecker = new Rectangle();
		Rectangle objChecker = new Rectangle();
		
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
		return null;
	}
}
