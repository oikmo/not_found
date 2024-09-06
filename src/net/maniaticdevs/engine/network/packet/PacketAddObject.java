package net.maniaticdevs.engine.network.packet;

/**
 * Adds object to client.
 * @author Oikmo
 */
public class PacketAddObject {
	/** Connection ID of Player that this packet is being sent to */
	public int id;
	/** Network ID of object being added */
	public String networkID;
	/** Class of object being added */
	public String objClass;
	/** Class of Sub Object */
	public String subObj;
	/** Name of Sub Object */
	public String subObjName;
	/** Network ID of Sub Object */
	public String subNetworkID;
	/** Position of object being added */
	public int x, y;
}
