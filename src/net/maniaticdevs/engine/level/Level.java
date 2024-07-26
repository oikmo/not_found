package net.maniaticdevs.engine.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.tile.Tile;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.entity.Player;

/**
 * Stores tile data, entities and name of map
 * @author Oikmo
 */
public class Level {
	
	/** For like a loading screen or something */
	private String name;
	/** For loading map file */
	private String internalName;
	
	/** Active entities */
	private List<Entity> entities = new ArrayList<>();
	/** Active objects */
	private List<OBJ> objects = new ArrayList<>();
	/** Fuck no I ain't using tile based rendering because that shit lags.<br>Here's a better solution that's tiny in memory and is easy to render! */
	private BufferedImage mapImage;
	/** Tile data from map file */
	private Tile[][] tiles;
	/** Dimensions of map */
	private int width, height;
	
	/**
	 * Level contructor, used by {@link LevelLoader} only.
	 * @param name given from map file
	 * @param width given from map file
	 * @param height given from map file
	 */
	public Level(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
	}
	
	/**
	 * Draws {@link #mapImage} based on {@link Player} coordinates.
	 * @param g2 Renderer for mapImage
	 */
	public void draw(Graphics2D g2) {
		int screenX = 0 - Main.thePlayer.getPosition().x + Main.thePlayer.getScreenPosition().x;
		int screenY = 0 - Main.thePlayer.getPosition().y + Main.thePlayer.getScreenPosition().y;
		g2.drawImage(mapImage, screenX, screenY, width*Settings.tileSize, height*Settings.tileSize, null);
	}
	
	/**
	 * Updates {@link #mapImage} with a new one
	 * @param image to update {@link #mapImage} with
	 */
	public void setImage(BufferedImage image) {
		this.mapImage = image;
	}
	
	/**
	 * Sets a tile from {@link LevelLoader#tiles} on specified grid position
	 * 
	 * @param x tile coord X
	 * @param y tile coord Y
	 * @param type given from {@link LevelLoader#tiles}[]
	 */
	public void setTileAt(int x, int y, int type) {
		setTileAt(x,y, LevelLoader.tiles[type]);
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
	
	
}
