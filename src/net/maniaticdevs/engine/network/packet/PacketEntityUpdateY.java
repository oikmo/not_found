package net.maniaticdevs.engine.network.packet;

/**
 * Updates the Y Position of an Entity
 * @author Oikmo
 */
public class PacketEntityUpdateY {
	/** Network ID of entity to update */
	public String networkID;
	/** Y position to update to */
	public int y;
}
