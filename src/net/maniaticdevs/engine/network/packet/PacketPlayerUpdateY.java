package net.maniaticdevs.engine.network.packet;

/**
 * Updates the Y Position of a Player
 * @author Oikmo
 */
public class PacketPlayerUpdateY {
	/** Active Connection ID of player to update */
	public int id;
	/** Y position to update to */
	public int y;
}
