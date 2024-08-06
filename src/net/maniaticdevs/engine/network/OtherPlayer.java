package net.maniaticdevs.engine.network;

import java.awt.Color;
import java.awt.Graphics2D;

import com.esotericsoftware.kryonet.Connection;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

public class OtherPlayer {
	
	public Connection c;
	public int id = -1;
	
	public String userName;
	public int anim;
	public int direction;
	
	public int x, y;
	
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Draws player, if is not seen by player then it is not drawn
	 * @param g2 Graphics
	 * @param playerPos Player world position
	 * @param playerScreenPos Player screen position
	 */
	public void draw(Graphics2D g2, Vector2 playerPos, Vector2 playerScreenPos) {
		if(userName == null) {
			return;
		}
		int screenX = x - playerPos.x + playerScreenPos.x;
		int screenY = y - playerPos.y + playerScreenPos.y;

		if(x + Settings.tileSize > playerPos.x - playerScreenPos.x &&
				x - Settings.tileSize < playerPos.x + playerScreenPos.x &&
				y + Settings.tileSize > playerPos.y - playerScreenPos.y &&
				y - Settings.tileSize < playerPos.y + playerScreenPos.y) {
			g2.drawImage(Main.thePlayer.sprites[anim+direction*6], screenX, screenY, Settings.tileSize, Settings.tileSize, null);
			g2.setColor(Color.WHITE);
			g2.setFont(GuiScreen.font.deriveFont(18.0F));
			int height = (int)g2.getFontMetrics(GuiScreen.font).getStringBounds(userName, g2).getHeight();
			g2.drawString(userName, screenX, screenY-(height/2));
		}
	}
}
