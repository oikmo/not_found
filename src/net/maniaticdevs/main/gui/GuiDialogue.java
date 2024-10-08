package net.maniaticdevs.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.gui.DialogueScenario;
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
	
	/** Position of the dialogue frame box */
	public static int x, y;
	/** Size of the dialogue frame box */
	public static final int width = Settings.tileSize*14, height = Settings.tileSize*5;
	/** Prevents player from going to the next dialogue if true */
	public static boolean lockEnter;
	
	/** Dialogue list to go through */
	private List<String> dialogues;
	/** currently displaying character */
	private String currentDialogue;
	/** To keep track of current dialogue in array */
	private int dialogueIndex = 0;
	/** To create that scrolling text effect */
	private int charIndex = 0;
	/** Result of scrolling text */
	private String combinedText = "";
	
	/** name supplied from dialogue file */
	private String name = null;
	
	/** Ticks */
	private int tickDelay = 15, ticks;
	/** loaded from {@link  net.maniaticdevs.engine.objects.DataBuffer} or nah */
	private boolean dataBuffer;
	
	/** Info for NPC that uses dialogues */
	public NPC npc;
	
	/** Default background */
	private static Color color = new Color(0,0,0,30);
	/** Background for DataBuffers */
	private static Color bufferColor = new Color(0,0,0,180);
	
	/** DataBuffer image */
	private static BufferedImage bufferImage;
	/** Cool background audio for DataBuffer */
	private static Sound blackbox;
	
	/** Lock to prevent sounds to play all at once */
	private boolean playBG = false;
	
	/** Loaded scenario */
	private DialogueScenario scenario;
	
	private boolean dialogueComplete = false;
	
	/** 
	 * Loads audios and images (and dialogues, scenarios and the like) 
	 * @param dataBuffer was this called from {@link net.maniaticdevs.engine.objects.DataBuffer}
	 * @param dialogue Dialogues to be loaded
	 * @param npc To lock and unlock when done
	 * @param scenario Scenario to load
	 */
	public GuiDialogue(boolean dataBuffer, List<String> dialogue, NPC npc, DialogueScenario scenario) {
		if(bufferImage == null) {
			try {
				bufferImage = ImageIO.read(ResourceLoader.class.getResourceAsStream("/textures/data-buffer.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(blackbox == null) {
			blackbox = new Sound("sfx/blackbox/00blackbox_start", "sfx/blackbox/01blackbox_end", "sfx/blackbox/02blackbox_loop", "sfx/blackbox/03blackbox_static");
		}
		this.dialogues = new ArrayList<>(dialogue);
		this.dataBuffer = dataBuffer;
		if(this.dataBuffer) {
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
		this.scenario = scenario;
	}
	
	public void draw(Graphics2D g2) {
		drawDialogueScreen(g2);
	}
	
	public void tick() {
		if(scenario != null) {
			scenario.tick();
		}
		if(tickDelay != 0) {
			tickDelay--;
		} else {
			if(dialogueIndex < dialogues.size()) {
				String dialogue = dialogues.get(dialogueIndex);
				if(dialogue.contains("%")) {
					String[] raw = dialogue.split("%");
					name = raw[0];
					dialogue = raw[1];
					
					if(scenario != null) {
						scenario.callback(name, dialogue);
						if(dialogues.size() > dialogueIndex+1) {
							String[] r = dialogues.get(dialogueIndex+1).split("%");
							scenario.callbackAhead(r[0], r[1]);
						}
						
					}
				}
				
				char[] diaChars = dialogue.toCharArray();
				
				ticks++;
				int tickInterval = dialogue.contentEquals("... ACCESSING DATA BUFFER ...") ? 2 : 1;
				if(ticks % tickInterval == 0 && charIndex < diaChars.length) {
					combinedText += diaChars[charIndex];
					charIndex++;
				}
				currentDialogue = combinedText;
				
				if(combinedText.contentEquals("... ACCESSING DATA BUFFER ...")) {
					if(!playBG && dataBuffer) {
						blackbox.play(2, true, 0.75f, 0, 0, 0, 0);
						//blackbox.play(3, true);
						playBG = true;
					}
				}
				
				if(Input.isKeyDownExplicit(Input.KEY_ESC)) {
					cancelDialogue();
					
					Main.currentScreen = new GuiInGame();
				}
				
				if(dialogue.contentEquals(combinedText)) {
					dialogueComplete = true;
				} else {
					dialogueComplete = false;
				}
				
				if(Input.isKeyDownExplicit(Input.KEY_ENTER) && !lockEnter) {
					if(dialogue.contentEquals(combinedText)) {
						
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
				}
			} else {
				if(dialogueIndex >= dialogues.size()) {
					Main.currentScreen = new GuiInGame();
					cancelDialogue();
				}
			}
		}
	}
	
	/**
	 * Draws the dialogue screen
	 * @param g2 Graphics
	 */
	public void drawDialogueScreen(Graphics2D g2) {
		x = (Main.getInstance().getWidth()/2)-(width/2);
		y = Main.getInstance().getHeight() - (Settings.tileSize/2) - height;
		
		g2.setColor(dataBuffer ? bufferColor : color);
		g2.fillRect(0, 0, Main.getInstance().getWidth(), Main.getInstance().getHeight());
		
		if(this.dataBuffer) {
			g2.drawImage(bufferImage, (Main.getInstance().getWidth()/2)-y/2, 0, y, y, null);
		} else {
			if(scenario != null) {
				scenario.draw(g2);
			}
		}
		
		drawSubWindow(g2, x, y, width, height);
		if(!lockEnter && dialogueComplete) {
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
			g2.drawString(">", x+ width-56, y+height-28);
		}
		
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
	
	/** To properly unlock the NPC in use (or stop all sounds from {@link net.maniaticdevs.engine.objects.DataBuffer} */
	public void cancelDialogue() {
		if(dataBuffer) {
			blackbox.stop();
			blackbox.play(1);
		}
		
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
