package net.maniaticdevs.engine.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.tile.Tile;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.main.Main;

/**
 * Handles map files to give as {@link Level}s
 * @author Oikmo
 */
public class MapLoader  {
	/**
	 * Tile data
	 */
	public static Tile[] tiles;
	/**
	 * Images gathered from atlas to be used for {@link #tiles}
	 */
	private static BufferedImage[] images = new BufferedImage[256];
	
	/**
	 * Sets up values and loads atlas to be split into {@link #images} in which it calls {@link System#gc()}
	 */
	public static void init() {
		tiles = new Tile[16];
		try {
			images = ImageUtils.fromSheet(ImageIO.read(Main.class.getResourceAsStream("/textures/defaultPack.png")), 16, 16);
		} catch(IOException e) {
			System.err.println("[ERROR] \"/res/defaultPack.png\" could not be loaded!");
		}
		
		getTileImage();
		System.gc();
	}
	
	/**
	 * Setup tiles
	 */
	private static void getTileImage() {
		setup(0, false); //ground
		setup(1, true); //wall
		setup(2, false);
		setup(3, false);
		setup(4, false);
		setup(5, false);
		setup(6, false);
		setup(7, false);
		setup(8, false);
		setup(9, false);
		setup(10, false);
		setup(11, false);
	}
	
	/**
	 * Sets tile data at given index
	 * @param index where to be stored in {@link #tiles}
	 * @param collision if it can be collided with
	 */
	private static void setup(int index, boolean collision) {
			tiles[index] = new Tile();
			tiles[index].index = index;
			tiles[index].image = images[index];
			tiles[index].image = ImageUtils.scaleImage(tiles[index].image, Settings.tileSize, Settings.tileSize);
			tiles[index].collision = collision;
	}
	
	/**
	 * Loads map resources from /maps/<br><br>
	 * 
	 * @param mapPath what map to load
	 * @param width Width of map
	 * @param height Height of map
	 * @return {@link LevelData}
	 */
	public static LevelData loadMap(String mapPath, int width, int height) {
		LevelData level = null;
		try {
			InputStream is = MapLoader.class.getResourceAsStream("/maps/"+mapPath+".map");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			BufferedImage map = new BufferedImage(width*16, height*16, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = map.createGraphics();
			Tile[][] tiles = new Tile[width][height];
			int col = 0;
			int row = 0;
			
			while(col < width && row < height) {
				String line = br.readLine();
				//System.out.println(line);
				while (col < width) {
					if(line.trim().isEmpty()) {
						col++;
						if(col == width) {
							row--;
						}
						continue;
					}
					String numbers[] = line.split(" ");
					
					int worldX = col*16;
					int worldY = row*16;
					
					g2.drawImage(MapLoader.tiles[Integer.parseInt(numbers[col])].image, worldX, worldY, 16, 16, null);
					tiles[col][row] = MapLoader.tiles[Integer.parseInt(numbers[col])];
					col++;
				}
				if(col == width) {
					col = 0;
					row++;
				}
			}
			g2.dispose();
			level = new LevelData(map, tiles);
			
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.gc();
		return level;
	}
	
	/**
	 * Easy passing of map information
	 * @author Oikmo
	 */
	public static class LevelData {
		/** Rendered map */
		private BufferedImage mapImage;
		/** Tiles from map */
		private Tile[][] tiles;
		
		/**
		 * Data constructor
		 * @param mapImage rendered map
		 * @param tiles tile data
		 */
		public LevelData(BufferedImage mapImage, Tile[][] tiles) {
			this.mapImage = mapImage;
			this.tiles = tiles;
		}
		
		/**
		 * Get rendered map
		 * @return {@link BufferedImage}
		 */
		public BufferedImage getMapImage() {
			return mapImage;
		}

		/**
		 * Get tile data
		 * @return {@link Tile}[][]
		 */
		public Tile[][] getTiles() {
			return tiles;
		}
	}
}
