package net.maniaticdevs.engine.network.packet;

/**
 * Saves player position
 * @author Oikmo
 */
public class PacketSavePlayerPosition {
	/** Active connection ID */
	public int id;
	/** Username of player being saved */
	public String userName;
	/** Position to be stored */
	public float x, y;
}
