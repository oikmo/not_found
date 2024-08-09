package net.maniaticdevs.engine.network;

import java.util.List;

/**
 * For chat bubbles (taken from BlockBase lol)
 * @author Oikmo
 */
public class ChatMessage {
	
	/** How long it should last for (6 seconds by default) */
	private int timer = 60*6;
	/** The message it self */
	private String message;
	/** What list to use for removing itself */
	private List<ChatMessage> list;
	
	/**
	 * Constructor to initalise {@link #ChatMessage(List, String)}
	 * @param list List for self use {@link #list}
	 * @param message Message itself {@link #message}
	 */
	public ChatMessage(List<ChatMessage> list, String message) {
		this.message = message;
		this.list = list;
		this.list.add(this);
	}
	
	/**
	 * Used to tick down the {@link #timer} variable and after it reaches 0 then it removes it self from {@link #list}
	 */
	public void tick() {
		timer--;
		if(timer <= 0) {
			this.list.remove(this);
		}
	}
	
	/**
	 * Returns the message
	 * @return {@link #message}
	 */
	public String getMessage() {
		return message;
	}
}
