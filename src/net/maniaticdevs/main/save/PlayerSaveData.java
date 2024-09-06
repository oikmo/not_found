package net.maniaticdevs.main.save;

import java.io.Serializable;

import net.maniaticdevs.main.entity.Player;

/**
 * Data for saving the Player
 * @author Oikmo
 */
public class PlayerSaveData implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** Name of player */
	public String name;
	/** Position X of player */
	public int posX;
	/** Position Y of player */
	public int posY;
	
	/**
	 * Loads data into class (to be saved)
	 * @param name Name of player
	 * @param player Player class for retrieving data from
	 */
	public PlayerSaveData(String name, Player player) {
		this.name = name;
		if(player != null) {
			this.posX = player.getPosition().x;
			this.posY = player.getPosition().y;
		}
	}
}
