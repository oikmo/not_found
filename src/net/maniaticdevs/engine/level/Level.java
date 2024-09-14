package net.maniaticdevs.engine.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.level.MapLoader.LevelData;
import net.maniaticdevs.engine.network.packet.PacketRemoveObject;
import net.maniaticdevs.engine.objects.MovingImage;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.tile.Tile;
import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.entity.Player;

/**
 * Stores tile data, entities and name of map
 * @author Oikmo
 */
public class Level {
	
	/** For like a loading screen or something */
	private String name;
	
	/** Active entities */
	protected List<Entity> entities = new ArrayList<>();
	/** Active objects */
	protected List<OBJ> objects = new ArrayList<>();
	/** Fuck no I ain't using tile based rendering because that shit lags.<br>Here's a better solution that's tiny in memory and is easy to render! */
	private BufferedImage mapImage;
	/** Tile data from map file */
	private Tile[][] tiles;
	/** Dimensions of map */
	private int width, height;
	
	/**
	 * Level contructor, used by {@link MapLoader} only.
	 * @param internalName for loading map file
	 * @param name Name of level
	 * @param width Width of level
	 * @param height Height of level
	 * @param dontLoad For network reasons, this exists. If true then {@link #loadEverything()} is not called.
	 */
	public Level(String internalName, String name, int width, int height, boolean dontLoad) {
		this.name = name;
		this.width = width;
		this.height = height;
		LevelData data = MapLoader.loadMap(internalName, width, height);
		this.tiles = data.getTiles();
		this.mapImage = data.getMapImage();
		if(!dontLoad) {
			loadEverything();
		}
		
	}
	
	/**
	 * Called on construction, this can be redefined by abstracted classes so that they can set their own objects and enemies easily.
	 */
	protected void loadEverything() {}
	
	/**
	 * Draws {@link #mapImage} based on {@link Player} coordinates.
	 * @param g2 Renderer for mapImage
	 * @param position Player position
	 * @param screenPosition Player's screen position
	 */
	public void draw(Graphics2D g2, Vector2 position, Vector2 screenPosition) {
		int screenX = 0 - position.x + screenPosition.x;
		int screenY = 0 - position.y + screenPosition.y;
		g2.drawImage(mapImage, screenX, screenY, width*Settings.worldTileSize, height*Settings.worldTileSize, null);
		
		try {
			for(OBJ obj : objects) {
				obj.draw(g2, position, screenPosition);
			}
			
			for(Entity e : entities) {
				if(e instanceof NPC) {
					((NPC)e).update(position, screenPosition);
				}
				e.draw(g2);
			}
			
		} catch(Exception e) {
			Logger.log(LogLevel.ERROR, "I don't like drawing...");
		}
		
	}
	
	/** 
	 * Logic function
	 * @param server So that no entities tick on client side, only server.
	 */
	public void tick(boolean server) {
		try {
			for(OBJ obj : objects) {
				if(Main.theNetwork == null || server) {
					if(!(obj instanceof MovingImage)) {
						obj.tick();
					}
					
				}
				if(obj instanceof MovingImage) {
					obj.tick();
				}
			}
			
			
			if(Main.theNetwork == null || server) {
				for(Entity e : entities) {
					e.tick();
				}
			}
		} catch(java.util.ConcurrentModificationException e) {}
	}
	
	/**
	 * Removes {@link OBJ} from {@link #objects} network edition
	 * @param obj Object to be removed
	 */
	public void removeObject(OBJ obj) {
		if(Main.theNetwork != null) {
			PacketRemoveObject packet = new PacketRemoveObject();
			packet.networkID = obj.networkID;
			Main.theNetwork.client.sendTCP(packet);
		}
		removeObjectNoNet(obj);
	}
	
	/**
	 * To not cause stackoverflow on the network
	 * @param obj Object to be removed
	 */
	public void removeObjectNoNet(OBJ obj) {
		objects.remove(obj);
	}
	
	/**
	 * Sets a tile from {@link MapLoader#tiles} on specified grid position
	 * 
	 * @param x tile coord X
	 * @param y tile coord Y
	 * @param type given from {@link MapLoader#tiles}[]
	 */
	public void setTileAt(int x, int y, int type) {
		setTileAt(x,y, MapLoader.tiles[type]);
	}
	
	/**
	 * Sets a tile on specified grid position
	 * 
	 * @param x tile coord X
	 * @param y tile coord Y
	 * @param tile to set point with
	 */
	public void setTileAt(int x, int y, Tile tile) {
		tiles[x][y] = tile;
	}
	
	/**
	 * Returns a tile at specified grid point
	 * @param x coordinates
	 * @param y coordinates
	 * @return {@link Tile}
	 */
	public Tile getTileAt(int x, int y) {
		return tiles[x][y];
	}
	
	/**
	 * Returns map name
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the objects in memory
	 * @return {@link List} {@link OBJ}
	 */
	public List<OBJ> getObjects() {
		return objects;
	}
	
	/**
	 * Adds {@link OBJ} to {@link #objects}
	 * @param obj Object to be added
	 */
	public void addObject(OBJ obj) {
		objects.add(obj);
	}
	
	/**
	 * Returns the entities in memory
	 * @return {@link List} {@link Entity}
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Adds entity to {@link #entities}
	 * @param ent Entity to be added
	 */
	public void addEntity(Entity ent) {
		entities.add(ent);
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
		
	}
}
