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

/**
 * Represents players on a server
 * @author Oikmo
 */
public class OtherPlayer {
	
	/** Active connection */
	public Connection c;
	/** Active connection id */
	public int id = -1;
	
	/** Player name */
	public String userName;
	/** Current animation frame */
	public int anim;
	/** Current facing direction */
	public int direction;
	
	/** Chat bubbles */
	public List<ChatMessage> messages = new ArrayList<>();
	
	/** World position */
	public int x, y;
	
	/** Is the player dead or not */
	public boolean dead = false;

	/** Used for collision checking */
	protected Rectangle hitBox = new Rectangle(8, 1, 32, 46);
	
	/**
	 * Sets the position to given parameters
	 * @param x X position
	 * @param y Y position
	 */
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * To remove messages after a while (or remove extra ones)
	 */
	public void tick() {
		for(int i = 0; i < messages.size(); i++) {
			if(messages.size() > 5) {
				messages.remove(0);
			}
			messages.get(i).tick();
		}
	}

	/**
	 * Draws player, if is not seen by player then it is not drawn.
	 * <br>Draws username and chat bubbles
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

		if(x + Settings.worldTileSize > playerPos.x - playerScreenPos.x &&
				x - Settings.worldTileSize < playerPos.x + playerScreenPos.x &&
				y + Settings.worldTileSize > playerPos.y - playerScreenPos.y &&
				y - Settings.worldTileSize < playerPos.y + playerScreenPos.y) {
			if(!dead) {
				g2.drawImage(Main.thePlayer.sprites[anim+direction*6], screenX, screenY, Settings.worldTileSize, Settings.worldTileSize, null);
			} else {
				g2.drawImage(Main.thePlayer.deadSprite, screenX, screenY, Settings.worldTileSize, Settings.worldTileSize, null);
			}
			
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
				int bubbleWidth = width+15;
				int x = screenX+(hitBox.x/2)+15;
				g2.setColor(Color.WHITE);
				g2.fillRoundRect((x)-(bubbleWidth/2), (int) (screenY-height*2.5f)-offsetY, bubbleWidth, height+15, 15, 15);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(3));
				g2.drawRoundRect((x)-(bubbleWidth/2), (int) (screenY-height*2.5f)-offsetY, bubbleWidth, height+15, 5, 5);
				g2.setColor(Color.BLACK);
				g2.drawString(msg, (x-((bubbleWidth)/2)+(15/2)), screenY-height-(offsetY));
				offsetY += change;
			}
		}
	}
	
	/**
	 * Returns {@link #hitBox}
	 * @return {@link Rectangle}
	 */
	public Rectangle getHitBox() {
		return hitBox;
	}
}
