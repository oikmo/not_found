package net.maniaticdevs.engine.network.packet;

/**
 * Packet to lock the NPC when in dialogue
 * @author Oikmo
 */
public class PacketNPCLock {
	/** Network ID of NPC */
	public String networkID;
	/** Boolean to decide the lock */
	public boolean lock;
}
