package net.maniaticdevs.engine.objects;

import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.network.packet.PacketUpdateObjectAnimation;
import net.maniaticdevs.engine.network.server.MainServer;
import net.maniaticdevs.main.Main;

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
	/** Networking reasons */
	private int imageIndex = 1;
	public void tick() {
		if(ticks < 60) {
			ticks++;
		} else {
			flip = !flip;
			if(!flip) {
				image = image1;
				imageIndex = 1;
			} else {
				image = image2;
				imageIndex = 2;
			}
			if(Main.server != null) {
				PacketUpdateObjectAnimation packet = new PacketUpdateObjectAnimation();
				packet.networkID = this.networkID;
				packet.anim = imageIndex;
				MainServer.server.sendToAllTCP(packet);
			}
			ticks = 0;
		}
	}
	
	/** Return stored item
	 * @return {@link OBJ} */
	public OBJ getItem() {
		return item;
	}
	
	/**
	 * Sets the current image to index
	 * @param index Index of image to be set to
	 */
	public void setImageIndex(int index) {
		if(index == 1) {
			image = image1;
		} else {
			image = image2;
		}
	}
}
