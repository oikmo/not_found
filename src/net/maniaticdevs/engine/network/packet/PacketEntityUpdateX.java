package net.maniaticdevs.engine.network.packet;

/**
 * Updates the X Position of an Entity
 * @author Oikmo
 */
public class PacketEntityUpdateX {
	/** Network ID of entity to update */
	public String networkID;
	/** X position to update to */
	public int x;
}
