package net.maniaticdevs.engine.network.packet;

/**
 * Chat message sent by client
 * @author Oikmo
 */
public class PacketChatMessage {
	/** Connection ID of Player that this packet was sent from */
	public int id;
	/** Message sent by Player */
	public String message;
}
