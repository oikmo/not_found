package net.maniaticdevs.engine.network.packet;

/**
 * Adds player
 * @author Oikmo
 */
public class PacketAddPlayer {
	/** Connection ID of Player that this packet is being sent to */
	public int id;
	/** Username of Player to be added */
	public String userName;
	
}
