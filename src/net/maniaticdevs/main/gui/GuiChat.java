package net.maniaticdevs.main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.network.ChatMessage;
import net.maniaticdevs.engine.network.packet.PacketChatMessage;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.Input.InputType;
import net.maniaticdevs.main.Main;

/**
 * For send messages to others! (or yourself...)
 * @author Oikmo
 */
public class GuiChat extends GuiScreen {
	
	/** Transparent Black **/
	private Color transBlack = new Color(0,0,0, 210);
	/** Used to store all messages recieved as to sort and process */
	public static List<String> originalMessages = new ArrayList<>();
	/** Processed messages from {@link #recompileMessages()} */
	private List<String> messages;
	/** Reversed {@link #messages} from {@link #recompileMessages()} */
	private List<String> sortedMessages = new ArrayList<>();
	
	/** Constructor */
	public GuiChat() {
		recompileMessages();
	}
	
	public void draw(Graphics2D g2) {
		if(!Input.needsInput) {
			Input.clearInput();
			Input.needsInput = true;
			Input.lengthInput = 62;
			Input.inputType = InputType.Chat;
		}
		int frameX = Settings.tileSize;
		int frameHeight = Settings.tileSize*5;
		int frameY = Main.getInstance().getHeight() - frameHeight - Settings.tileSize;
		int frameWidth = Settings.tileSize * 6;
		
		int frameMainY = frameY - Settings.tileSize;
		Color c = transBlack;
		g2.setColor(c);
		g2.fillRoundRect(frameX, frameMainY, frameWidth, frameHeight, 15, 15);
		
		c = Color.WHITE;
		g2.setColor(c);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(frameX+5, frameMainY+5, frameWidth-10, frameHeight-10, 5, 5);
		
		c = transBlack;
		g2.setColor(c);
		g2.fillRoundRect(frameX, Main.getInstance().getHeight() - Settings.tileSize*2, frameWidth, (int)(Settings.tileSize*0.9F), 15, 15);
		
		c = Color.WHITE;
		g2.setColor(c);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(frameX+5, Main.getInstance().getHeight() - Settings.tileSize*2+5, frameWidth-10, (int)(Settings.tileSize*0.9F)-10, 5, 5);
		
		float text = 20f;
		
		g2.setFont(font.deriveFont(text));
		g2.drawString(">", frameX+(text/2), Main.getInstance().getHeight() - ((Settings.tileSize*2f)-25)+5);
		String input = Input.getTextInput();
		if(input.length() > 21) {
			input = input.substring(input.length()-21, input.length());
		}
		g2.drawString(input, frameX+(text), Main.getInstance().getHeight() - ((Settings.tileSize*2f)-25)+5);
		int offsetY = 0;
		text = 14.5f;
		g2.setFont(font.deriveFont(text));
		for(String s : sortedMessages) {
			g2.drawString(s, frameX+10, Main.getInstance().getHeight() - (int)(Settings.tileSize*2.25f)-offsetY);
			offsetY += text;
		}
		
		if(Input.isKeyDownExplicit(Input.KEY_ENTER) && !input.isEmpty()) {
			originalMessages.add("<"+Main.playerName+"> " + Input.getTextInput());
			recompileMessages();
			new ChatMessage(Main.thePlayer.messages, Input.getTextInput());
			if(Main.theNetwork != null) {
				PacketChatMessage packet = new PacketChatMessage();
				packet.id = Main.theNetwork.client.getID();
				packet.message = Input.getTextInput();
				Main.theNetwork.client.sendTCP(packet);
			}
			Input.clearInput();
			Input.needsInput = true;
		}
	}
	
	/**
	 * Rebuilds visual messages from {@link #originalMessages} and uses {@link #messages} for processing and {@link #sortedMessages} for the final visual chats
	 */
	public void recompileMessages() {
		messages = new ArrayList<>(originalMessages);
		
		for(int i = 0; i < messages.size(); i++) {
			String str = messages.get(i);
			List<String> parts = this.getParts(str, 31);
			for(int j = 0; j < parts.size(); j++) {
				if(j == 0) {
					messages.remove(i);
				} 
				messages.add(i+j, parts.get(j));
			}
		}
		
		if(messages.size() > 16) {
			int toSubtract = messages.size() - 16;
			
			for(int i = 0; i < toSubtract; i++) {
				messages.remove(0);
			}
		}
		
		sortedMessages = new ArrayList<>(messages);
		Collections.reverse(sortedMessages);
		
		for(int i = sortedMessages.size()-1; i > 0; i--) {
			if(sortedMessages.get(i).startsWith("<")) {
				break;
			} else {
				if(!sortedMessages.get(i).contains(" left the game")) {
					break;
				}
				sortedMessages.remove(i);
			}
		}
	}
	
	/**
	 * Partitions string to specific size
	 * @param string String to partition
	 * @param partitionSize Size of each partition
	 * @return {@link List} {@link String}
	 */
	private List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }
}
