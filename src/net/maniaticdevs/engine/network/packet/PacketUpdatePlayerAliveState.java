package net.maniaticdevs.engine.network.packet;

/**
 * Updates alive state of the player
 * @author Oikmo
 */
public class PacketUpdatePlayerAliveState {
	/** Active Connection ID of player to update */
	public int id = -1;
	/** State update to */
	public boolean dead;
}
