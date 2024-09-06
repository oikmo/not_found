package net.maniaticdevs.engine.network.packet;

/**
 * Updates the animation frame for a player to other players
 * @author Oikmo
 */
public class PacketUpdatePlayerAnimation {
	/** Active connection ID of player to update */
	public int id;
	/** Frame of animation to update to */
	public int anim;
}
