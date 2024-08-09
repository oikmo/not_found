package net.maniaticdevs.main.gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;

/**
 * So you can chat to whatever!
 * @author Oikmo
 */
public class GuiDialogue extends GuiScreen {
	
	/** Dialogue list to go through */
	private List<String> dialogues;
	private String currentDialogue;
	private int dialogueIndex = 0;
	private int charIndex = 0;
	private String combinedText = "";
	
	private String name = null;
	
	private int tickDelay = 15, ticks;
	
	public NPC npc;
	
	/** 
	 * Constructor 
	 * @param dialogue Dialogues to be loaded
	 * @param npc To re-enable...
	 */
	public GuiDialogue(List<String> dialogue, NPC npc) {
		this.dialogues = dialogue;
		this.npc = npc;
		this.npc.lock = true;
	}
	
	public void draw(Graphics2D g2) {
		drawDialogueScreen(g2);
	}
	
	public void tick() {
		if(tickDelay != 0) {
			tickDelay--;
		} else {
			if(dialogueIndex < dialogues.size()) {
				String[] raw = dialogues.get(dialogueIndex).split("%");
				name = raw[0];
				String dialogue = raw[1];
				char[] diaChars = dialogue.toCharArray();
				
				ticks++;
				if(ticks % 3 == 0 && charIndex < diaChars.length) {
					combinedText += diaChars[charIndex];
					charIndex++;
				}
				currentDialogue = combinedText;
				
				if(Input.isKeyDownExplicit(Input.KEY_ENTER)) {
					charIndex = 0;
					combinedText = "";
					dialogueIndex++;
					Main.sfxLib.play(SoundSFXEnum.hit);
				}
			} else {
				if(dialogueIndex >= dialogues.size()) {
					Main.currentScreen = new GuiInGame();
				}
			}
		}
	}
	
	public void drawDialogueScreen(Graphics2D g2) {
		int width = (Settings.tileSize*14);
		int x = (Main.getInstance().getWidth()/2)-(width/2);
		int height = Settings.tileSize*5;
		int y = Main.getInstance().getHeight() - (Settings.tileSize/2) - height;
		
		drawSubWindow(g2, x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
		
		
		if(name != null) {
			g2.drawString(name, x+Settings.tileSize/4+1, y + Settings.tileSize/ 2);
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
			x += Settings.tileSize;
			y += Settings.tileSize;
			for(String line : currentDialogue.split("\n")) {
				g2.drawString(line, x, y);
				y += 30;
			}
		} else {
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
			x += Settings.tileSize;
			y += Settings.tileSize;
			if(currentDialogue != null) {
				for(String line : currentDialogue.split("\n")) {
					g2.drawString(line, x, y);
					y += 30;
				}
			}
			
		}
	}
}
