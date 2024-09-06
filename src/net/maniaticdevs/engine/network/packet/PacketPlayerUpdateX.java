package net.maniaticdevs.engine.network.packet;

/**
 * Updates the X Position of a Player
 * @author Oikmo
 */
public class PacketPlayerUpdateX {
	/** Active Connection ID of player to update */
	public int id;
	/** X position to update to */
	public int x;
}
