package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.sun.xml.internal.ws.api.ResourceLoader;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.network.packet.PacketNPCLock;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.sound.Sound;
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
	private boolean dataBuffer;
	
	public NPC npc;
	
	private static Color color = new Color(0,0,0,180);
	private static BufferedImage bufferImage;
	private static Sound blackbox;
	
	private boolean playBG = false;
	
	/** 
	 * Constructor 
	 * @param dialogue Dialogues to be loaded
	 * @param npc To re-enable...
	 */
	public GuiDialogue(boolean dataBuffer, List<String> dialogue, NPC npc) {
		if(bufferImage == null) {
			try {
				bufferImage = ImageIO.read(ResourceLoader.class.getResourceAsStream("/textures/data-buffer.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(blackbox == null) {
			blackbox = new Sound("sfx/blackbox/00blackbox_start", "sfx/blackbox/01blackbox_loop", "sfx/blackbox/02blackbox_static");
		}
		this.dialogues = new ArrayList<>(dialogue);
		this.dataBuffer = dataBuffer;
		if(this.dataBuffer) {
			blackbox.checkVolume(3);
			blackbox.play(0);
			this.dialogues.add(0, "... ACCESSING DATA BUFFER ...");
		}
		
		if(npc != null) {
			this.npc = npc;
			if(Main.theNetwork != null) {
				PacketNPCLock packet = new PacketNPCLock();
				packet.networkID = npc.networkID;
				packet.lock = true;
				Main.theNetwork.client.sendTCP(packet);
			} else {
				this.npc.lock = true;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		drawDialogueScreen(g2);
	}
	
	int musicTicks = 0;
	
	public void tick() {
		if(tickDelay != 0) {
			tickDelay--;
		} else {
			if(dialogueIndex < dialogues.size()) {
				String dialogue = dialogues.get(dialogueIndex);
				if(dialogue.contains("%")) {
					String[] raw = dialogues.get(dialogueIndex).split("%");
					name = raw[0];
					dialogue = raw[1];
				}
				
				char[] diaChars = dialogue.toCharArray();
				
				ticks++;
				int tickInterval = dialogue.contentEquals("... ACCESSING DATA BUFFER ...") ? 2 : 1;
				if(ticks % tickInterval == 0 && charIndex < diaChars.length) {
					combinedText += diaChars[charIndex];
					charIndex++;
				}
				currentDialogue = combinedText;
				
				if(Input.isKeyDownExplicit(Input.KEY_ENTER)) {
					//wait for blackbox start to end
					if(dialogue.contentEquals("... ACCESSING DATA BUFFER ...") && playBG) {
						if(combinedText.contentEquals("... ACCESSING DATA BUFFER ...")) {
							charIndex = 0;
							combinedText = "";
							dialogueIndex++;
							Main.sfxLib.play(SoundSFXEnum.hit);
						}
					} else {
						charIndex = 0;
						combinedText = "";
						dialogueIndex++;
						Main.sfxLib.play(SoundSFXEnum.hit);
					}
					
				}
			} else {
				if(dialogueIndex >= dialogues.size()) {
					Main.currentScreen = new GuiInGame();
					cancelDialogue();
				}
			}
		}
		if(!playBG && !blackbox.isPlaying() && dataBuffer) {
			blackbox.checkVolume(1);
			blackbox.setFile(1);
			blackbox.play();
			blackbox.loop();
			playBG = true;
		}
	}
	
	public void drawDialogueScreen(Graphics2D g2) {
		int width = (Settings.tileSize*14);
		int x = (Main.getInstance().getWidth()/2)-(width/2);
		int height = Settings.tileSize*5;
		int y = Main.getInstance().getHeight() - (Settings.tileSize/2) - height;
		
		if(this.dataBuffer) {
			g2.setColor(color);
			g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());
			g2.drawImage(bufferImage, (Main.getInstance().getWidth()/2)-y/2, 0, y, y, null);
		}
		
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
	
	public void cancelDialogue() {
		blackbox.stop();
		if(npc != null) {
			if(Main.theNetwork != null) {
				PacketNPCLock packet = new PacketNPCLock();
				packet.networkID = npc.networkID;
				packet.lock = false;
				Main.theNetwork.client.sendTCP(packet);
			} else {
				this.npc.lock = false;
			}
		}
	}
}
