package net.maniaticdevs.engine.network.packet;

/**
 * Updates the animation frame for the object to all clients 
 * @author Oikmo
 */
public class PacketUpdateObjectAnimation {
	/** Network ID of Object to update */
	public String networkID;
	/** Frame of animation to update to */
	public int anim;
}
