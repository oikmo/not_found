package net.maniaticdevs.engine.network;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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

	public List<ChatMessage> messages = new ArrayList<>();

	public int x, y;

	/** Used for collision checking */
	protected Rectangle hitBox = new Rectangle(8, 1, 32, 46);

	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void tick() {
		for(int i = 0; i < messages.size(); i++) {
			if(messages.size() > 5) {
				messages.remove(0);
			}
			messages.get(i).tick();
		}
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

			g2.setFont(GuiScreen.font.deriveFont(18.0F));
			int change = height + 20;
			int offsetY = 0;
			for(int i = messages.size(); i > 0; i--) {
				int j = i - 1;
				ChatMessage chatmessage = messages.get(j);
				String msg = chatmessage.getMessage().trim().replace("\n", ""); 

				int width = (int)g2.getFontMetrics().getStringBounds(msg, g2).getWidth();
				g2.setColor(Color.WHITE);
				g2.fillRoundRect((screenX), (int) (screenY-height*2.5f)-offsetY, width+15, height+15, 15, 15);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(3));
				g2.drawRoundRect((screenX), (int) (screenY-height*2.5f)-offsetY, width+15, height+15, 5, 5);
				g2.setColor(Color.BLACK);
				g2.drawString(msg, (screenX+(15/2)), screenY-height-(offsetY));
				offsetY += change;
			}
		}
	}

	public Rectangle getHitBox() {
		return hitBox;
	}
}
