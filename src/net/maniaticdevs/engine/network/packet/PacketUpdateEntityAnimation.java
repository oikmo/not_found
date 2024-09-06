package net.maniaticdevs.engine.network.packet;

/**
 * Updates the animation frame for the entity to all clients 
 * @author Oikmo
 */
public class PacketUpdateEntityAnimation {
	/** Network ID of Entity to update */
	public String networkID;
	/** Frame of animation to update to */
	public int anim;
}
