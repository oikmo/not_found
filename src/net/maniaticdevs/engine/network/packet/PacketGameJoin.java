package net.maniaticdevs.engine.network.packet;

/**
 * Info for players that just joined.
 * @author Oikmo
 *
 */
public class PacketGameJoin {
	/** What map to load... */
	public String map;
	/** Where to spawn... */
	public int x, y;
	
}
