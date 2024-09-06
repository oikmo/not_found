package net.maniaticdevs.engine.network.packet;

/**
 * Removes player from Server (by Server)
 * @author Oikmo
 */
public class PacketRemovePlayer {
	/** The once active connection ID of the player being removed */
	public int id;
	/** Was this a kick from the server? */
	public boolean kick;
	/** The message given by server for disconnection */
	public String message;	
}
