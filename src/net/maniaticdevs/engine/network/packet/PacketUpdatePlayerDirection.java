package net.maniaticdevs.engine.network.packet;

/**
 * Updates the direction for a player to other players
 * @author Oikmo
 */
public class PacketUpdatePlayerDirection {
	/** Active connection ID of player to update */
	public int id;
	/** Direction of player to update to */
	public int dir;
	
}
