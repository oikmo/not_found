package net.maniaticdevs.engine.network.packet;

/**
 * Updates the direction for the entity to all clients 
 * @author Oikmo
 */
public class PacketUpdateEntityDirection {
	/** Network ID of Entity to update */
	public String networkID;
	/** Direction to update to */
	public int dir;
	
}
