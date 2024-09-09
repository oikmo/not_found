package net.maniaticdevs.engine.objects;

import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.main.Main;

/**
 * GIF player for the funnies
 * @author Oikmo
 */
public class MovingImage extends OBJ {
	
	/** IMAGES */
	private BufferedImage[] images;
	
	/** Current animation frame index */
	private int imageIndex;
	/** When to reset the ticks and increment {@link #imageIndex} */
	private int imageTicksLimit;
	/** Image ticks as to progress to the next frame */
	private int imageTicks = 0;
	/** Audio track */
	private int audioTrack = -1;
	/** Start on first replay */
	private boolean hasPlayed = false;
	
	/** For networking reasons, this is stored */
	private String imagePath;
	/** For networking reasons, this is stored */
	private int rows, cols;
	
	/**
	 * MovingImage constructor
	 * @param audio Audio index
	 * @param imagePath File path of image
	 * @param rows Amount of rows in the image
	 * @param cols Amount of columns in the image
	 * @param imageTicksLimit For X tick go to the next frame
	 * @param x X position
	 * @param y Y position
	 */
	public MovingImage(int audio, String imagePath, int rows, int cols, int imageTicksLimit, int x, int y) {
		this.audioTrack = audio;
		this.imagePath = imagePath;
		this.rows = rows;
		this.cols = cols;
		this.imageTicksLimit = imageTicksLimit;
		this.images = ImageUtils.setupSheet(imagePath, rows, cols, 64, 64);
		this.image = images[imageIndex];
		position.set(x,y);
	}
	
	public void tick() {
		if(imageTicks < imageTicksLimit) {
			imageTicks++;
			
			image = images[imageIndex];
		} else {
			imageTicks = 0;
			imageIndex++;
			if(imageIndex >= images.length) {
				imageIndex = 0;
				if(audioTrack != -1 && !hasPlayed) {
					Main.sfxLib.play(audioTrack, true, 0.05f, position.x, position.y, 2, Settings.worldTileSize*4);
					hasPlayed = true;
				}
			}
		}
	}

	/**
	 * Sets the current image to index
	 * @param index Index of image to be set to
	 */
	public void setImageIndex(int index) {
		this.imageIndex = index;
		image = images[imageIndex];
	}
	
	/**
	 * Returns the path of the images
	 * @return {@link String}
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Returns the rows of the image
	 * @return {@link Integer}
	 */
	public int getImageRows() {
		return rows;
	}

	/**
	 * Returns the columns of the image
	 * @return {@link Integer}
	 */
	public int getImageColumns() {
		return cols;
	}
	
	/**
	 * Returns the {@link #imageTicksLimit}
	 * @return {@link Integer}
	 */
	public int getImageTicksLimit() {
		return imageTicksLimit;
	}
	
	/**
	 * Returns the {@link #audioTrack}
	 * @return {@link Integer}
	 */
	public int getAudioTrack() {
		return audioTrack;
	}
	
	/**
	 * Returns the {@link #imageIndex}
	 * @return {@link Integer}
	 */
	public int getImageIndex() {
		return imageIndex;
	}
}
