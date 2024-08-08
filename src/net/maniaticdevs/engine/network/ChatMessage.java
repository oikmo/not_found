package net.maniaticdevs.engine.network;

import java.util.List;

public class ChatMessage {
	
	private int timer = 60*6;
	private String message;
	private boolean special;
	
	private List<ChatMessage> list;
	
	public ChatMessage(List<ChatMessage> list, String message, boolean special) {
		this.message = message;
		this.special = special;
		this.list = list;
		this.list.add(this);
	}
	
	public void tick() {
		timer--;
		if(timer <= 0) {
			this.list.remove(this);
		}
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isSpecial() {
		return special;
	}
}
