package net.maniaticdevs.main.save;

import java.io.Serializable;

import net.maniaticdevs.main.entity.Player;

public class PlayerSaveData implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public int posX;
	public int posY;
	
	public PlayerSaveData(String name, Player player) {
		this.name = name;
		if(player != null) {
			this.posX = player.getPosition().x;
			this.posY = player.getPosition().y;
		}
	}
}
