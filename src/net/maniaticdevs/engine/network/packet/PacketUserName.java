package net.maniaticdevs.engine.network.packet;

/**
 * Username packet for joining player
 * @author Oikmo
 */
public class PacketUserName {
	/** Active connection ID of player joining */
	public int id;
	/** True only if they actually joined at all, not when they are being replicated */
	public boolean firstJoin;
	/** Username of joining player */
	public String userName;
	
}
