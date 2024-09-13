package net.maniaticdevs.engine.objects;

import java.awt.image.BufferedImage;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.network.packet.PacketUpdateObjectAnimation;
import net.maniaticdevs.engine.network.server.MainServer;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.main.Main;

/**
 * A glimmer in the distance, could it hold something?
 * @author Oikmo
 */
public class PickableObject extends OBJ {
	
	/** OBJ it is holding */
	private OBJ item;
	/** Sprites */
	private BufferedImage[] images;
	
	/** if it reaches a second in ticks then {@link #flip} boolean */
	private int ticks = 0;
	/** if it reaches a second in {@link #ticks} then flip boolean */
	private boolean flip = false;
	
	/**
	 * PickableObject contructor
	 * @param item Object to be picked up
	 * @param x X position
	 * @param y Y position
	 */
	public PickableObject(OBJ item, int x, int y) {
		this.item = item;
		this.images = ImageUtils.fromSheet(ResourceLoader.loadImage("/textures/object/pickableobject"), 2, 2, 8, 8);
		this.image = images[0];
		position.set(x,y);
	}
	
	
	public void tick() {
		if(ticks < 60) {
			ticks++;
		} else {
			flip = !flip;
			this.image = images[flip ? 1 : 0];
			if(Main.server != null) {
				PacketUpdateObjectAnimation packet = new PacketUpdateObjectAnimation();
				packet.networkID = this.networkID;
				packet.anim = flip ? 1 : 0;
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
		image = images[index];
	}
}
