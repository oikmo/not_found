package net.maniaticdevs.engine;

/**
 * Stores values for screen sizes and tile sizes. Options may be stored here.
 * @author Oikmo
 */
public class Settings {
	
	/** Size of our 1x1 tile images */
	private static final int originalTileSize = 16;
	/** The amount to scale up */
	private static final int scale = 3;
	
	/** Calculated tileSize from the {@link #originalTileSize} multiplied by the {@link #scale } */
	public static final int tileSize = originalTileSize * scale;
	/** Maximum tiles that can be shown at once on the screen horizontally */
	public static final int maxScreenCol = 17;
	/** Maximum tiles that can be shown at once on the screen vertically */
	public static final int maxScreenRow = 13;
	/** Window width calculated with {@link #tileSize} and {@link #maxScreenCol} */
	public static final int windowWidth = tileSize * maxScreenCol;
	/** Window width calculated with {@link #tileSize} and {@link #maxScreenRow} */
	public static final int windowHeight = tileSize * maxScreenRow;
	
}
