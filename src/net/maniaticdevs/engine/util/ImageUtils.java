package net.maniaticdevs.engine.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.main.Main;

/**
 * Handles BufferedImage in ways of scaling and slicing sprite sheets into image arrays
 * @author Oikmo
 */
public class ImageUtils {
	
	/**
	 * Scales the image by its intened params
	 * @param originalImage - image to be scaled
	 * @param width - width to scale to
	 * @param height - height to scale to
	 * @return {@link BufferedImage}
	 */
	public static BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
		if(originalImage == null) {
			System.err.println("[ERROR] Image could not be scaled!");
			return null;
		}
		BufferedImage scaledImage = toCompatibleImage(new BufferedImage(width, height, originalImage.getType()));
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(originalImage, 0, 0, width, height, null);
		g2.dispose();
		return scaledImage;
	}
	
	/**
	 * Fuck if I know what the hell this is
	 * @param image - image to be optimized
	 * @return {@link BufferedImage}
	 */
	private static BufferedImage toCompatibleImage(BufferedImage image) {
	    // obtain the current system graphical settings
	    GraphicsConfiguration gfxConfig = GraphicsEnvironment.
	        getLocalGraphicsEnvironment().getDefaultScreenDevice().
	        getDefaultConfiguration();
 
	    //if image is already compatible and optimized for current system settings, simply return it
	    if (image.getColorModel().equals(gfxConfig.getColorModel()))
	        return image;

	    // image is not optimized, so create a new image that is
	    BufferedImage newImage = gfxConfig.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

	    // get the graphics context of the new image to draw the old image on
	    Graphics2D g2d = newImage.createGraphics();

	    // actually draw the image and dispose of context no longer needed
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();

	    // return the new optimized image
	    return newImage; 
	}
	
	/**
	 * Creates an empty BufferedImage array (of size specified by the rows and cols) in which it creates an empty sub image in that array that then draws from the given BufferedImage into that sub image.
	 * <br><br>
	 * <b>But this time the size is capped at 16.</b><br>
	 * 
	 * @param image - image to be split into arrays
	 * @param rows - amount of rows in said image
	 * @param cols - amount of columns in said image
	 * @return {@link BufferedImage}[]
	 */
	public static BufferedImage[] fromSheet_16(BufferedImage image, int rows, int cols) {
		return fromSheet(image, rows, cols, 16, 16);
	}
	
	/**
	 * Creates an empty BufferedImage array (of size specified by the rows and cols) in which it creates an empty sub image in that array that then draws from the given BufferedImage into that sub image.
	 * 
	 * @param image - image to be split into arrays
	 * @param rows - amount of rows in said image
	 * @param cols - amount of columns in said image
	 * @return {@link BufferedImage}[]
	 */
	public static BufferedImage[] fromSheet(BufferedImage image, int rows, int cols) {
		return fromSheet(image, rows, cols, image.getWidth() / cols, image.getHeight() / rows);
	}
	
	/**
	 * Full fucking thang
	 * <br>
	 * Creates an empty BufferedImage array (of size specified by the rows and cols) in which it creates an empty sub image in that array that then draws from the given BufferedImage into that sub image.
	 * 
	 * @param image - image to be split into arrays
	 * @param rows - amount of rows in said image
	 * @param cols - amount of columns in said image
	 * @param chunkWidth - sub image width
	 * @param chunkHeight - sub image height
	 * @return {@link BufferedImage}[]
	 */
	public static BufferedImage[] fromSheet(BufferedImage image, int rows, int cols, int chunkWidth, int chunkHeight) {
		int chunks = rows * cols;
	    
		int count = 0;
		BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				//Initialize the image array with image chunks
				imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
				Graphics2D g2 = imgs[count++].createGraphics();
				g2.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				g2.dispose();
			}
		}

		return imgs;
	}
	
	/**
	 * Creates an empty BufferedImage 2D array (of size specified by the rows and cols) in which it creates an empty sub image in that array that then draws from the given BufferedImage into that sub image.
	 * <br><br>
	 * <b>But this time the size is capped at 16.</b><br>
	  * @param image - image to be split into arrays
	 * @param rows - amount of rows in said image
	 * @param cols - amount of columns in said image
	 * @return {@link BufferedImage}[][]
	 */
	public static BufferedImage[][] fromSheet2D_16(BufferedImage image, int rows, int cols) {
		return fromSheet2D(image, rows, cols, 16, 16);
	}
	/**
	 * Creates an empty BufferedImage 2D array (of size specified by the rows and cols) in which it creates an empty sub image in that array that then draws from the given BufferedImage into that sub image.
	 * 
	 * @param image - image to be split into arrays
	 * @param rows - amount of rows in said image
	 * @param cols - amount of columns in said image
	 * @return {@link BufferedImage}[][]
	 */
	public static BufferedImage[][] fromSheet2D(BufferedImage image, int rows, int cols) {
		System.out.println("image is" + image);
		return fromSheet2D(image, rows, cols, image.getWidth()/cols,image.getHeight()/rows);
	}
	
	/**
	 * Full fucking thang
	 * <br>
	 * Creates an empty BufferedImage 2D array (of size specified by the rows and cols) in which it creates an empty sub image in that array that then draws from the given BufferedImage into that sub image.
	 * 
	 * @param image image to be split into arrays
	 * @param rows amount of rows in said image
	 * @param cols amount of columns in said image
	 * @param chunkWidth sub image width
	 * @param chunkHeight sub image height
	 * @return {@link BufferedImage}[][]
	 */
	private static BufferedImage[][] fromSheet2D(BufferedImage image, int rows, int cols, int chunkWidth, int chunkHeight) {
		int chunks = rows * cols;
		BufferedImage imgs[][] = new BufferedImage[chunks][chunks]; //Image array to hold image chunks
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				//Initialize the image array with image chunks
				imgs[x][y] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
				Graphics2D g2 = imgs[x][y].createGraphics();
				g2.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				g2.dispose();
			}
		}

		return imgs;
	}
	
	/**
	 * 
	 * Loads sheet using {@link #fromSheet(BufferedImage, int, int, int, int)} and scales said sheet using {@link #scaleArray(BufferedImage[], int, int)}
	 * 
	 * @param filePath file at in jar /textures/ folder
	 * @param row amount of rows in said image
	 * @param col amount of columns in said image
	 * @return {@link BufferedImage }[]
	 */
	public static BufferedImage[] setupSheet(String filePath, int row, int col) {
		return setupSheet(filePath, row, col, -1, -1);
	}
	
	/**
	 * 
	 * Loads sheet using {@link #fromSheet(BufferedImage, int, int, int, int)} and scales said sheet using {@link #scaleArray(BufferedImage[], int, int)}
	 * 
	 * @param filePath file at in jar /textures/ folder
	 * @param row amount of rows in said image
	 * @param col amount of columns in said image
	 * @param width sub image width
	 * @param height sub image height
	 * @return {@link BufferedImage }[]
	 */
	public static BufferedImage[] setupSheet(String filePath, int row, int col, int width, int height) {
		BufferedImage spriteSheet = null;
		try {
			spriteSheet = ImageIO.read(Main.class.getResourceAsStream("/textures/" + filePath +".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage[] result = new BufferedImage[row * col];
		if(width == -1 && height == -1) {
			result = ImageUtils.fromSheet(spriteSheet, row, col, 16, 16);
			result = scaleArray(result, Settings.tileSize, Settings.tileSize);
		} else {
			result = ImageUtils.fromSheet(spriteSheet, row, col, width, height);
			result = scaleArray(result, width, height);
		}
		
		return result;
	}
	
	/**
	 * 
	 * Scales array using {@link #scaleImage(BufferedImage, int, int)} with a default size of {@link Settings#tileSize} on both sides
	 * 
	 * @param array BufferedImage array to be scaled
	 * @return {@link BufferedImage }[]
	 */
	public static BufferedImage[] scaleArray(BufferedImage[] array) {
		return scaleArray(array, Settings.tileSize, Settings.tileSize);
	}
	
	/**
	 * 
	 * Scales array using {@link #scaleImage(BufferedImage, int, int)} with a default size of {@link Settings#tileSize} on both sides
	 * 
	 * @param array BufferedImage array to be scaled
	 * @param width width to scale up to
	 * @param height height to scale up to
	 * @return {@link BufferedImage }[]
	 */
	public static BufferedImage[] scaleArray(BufferedImage[] array, int width, int height) {
		BufferedImage[] result = new BufferedImage[array.length];
		
	    for (int i = 0; i < array.length; i++) {
	        result[i] = ImageUtils.scaleImage(array[i], width, height);
	    }
		
	    return result;
	}
}
