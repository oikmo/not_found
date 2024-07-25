package net.maniaticdevs.engine;

public class Settings {
	
	private static final int originalTileSize = 16;
	private static final int scale = 3;
	
	public static final int tileSize = originalTileSize * scale;
	public static final int maxScreenCol = 16;
	public static final int maxScreenRow = 12;
	public static final int windowWidth = tileSize * maxScreenCol;
	public static final int windowHeight = tileSize * maxScreenRow;
	
}
