package net.maniaticdevs.engine.network.packet;

/**
 * Adds entity to client.
 * @author Oikmo
 */
public class PacketAddEntity {
	/** Connection ID of Player that this packet is being sent to */
	public int id;
	/** Network ID of entity being added */
	public String networkID;
	/** Class of entity being added */
	public String entityClass;
	/** Position of entity being added */
	public int x, y;
}
