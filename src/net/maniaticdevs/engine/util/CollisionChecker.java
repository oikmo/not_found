package net.maniaticdevs.engine.util;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.tile.Tile;
import net.maniaticdevs.main.Main;

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
}
