package net.maniaticdevs.engine.objects;

import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.ResourceLoader;

/**
 * A glimmer in the distance, could it hold something?
 * @author Oikmo
 */
public class PickableObject extends OBJ {
	
	/** OBJ it is holding */
	private OBJ item;
	/** Unrotated image */
	private BufferedImage image1;
	/** Rotated image */
	private BufferedImage image2;

	/**
	 * PickableObject contructor
	 * @param item Object to be picked up
	 * @param x X position
	 * @param y Y position
	 */
	public PickableObject(OBJ item, int x, int y) {
		this.item = item;
		this.image1 = ResourceLoader.loadImage("/textures/object/pickable_object.png");
		this.image2 = ResourceLoader.loadImage("/textures/object/pickable_object_rotated.png");
		this.image = image1;
		position.set(x,y);
	}
	
	/** if it reaches a second in ticks then {@link #flip} boolean */
	private int ticks = 0;
	/** if it reaches a second in {@link #ticks} then flip boolean */
	private boolean flip = false;
	public void tick() {
		if(ticks < 60) {
			ticks++;
		} else {
			flip = !flip;
			if(!flip) {
				image = image1;
			} else {
				image = image2;
			}
			ticks = 0;
		}
	}
	
	/** Return stored item
	 * @return {@link OBJ} */
	public OBJ getItem() {
		return item;
	}
}
