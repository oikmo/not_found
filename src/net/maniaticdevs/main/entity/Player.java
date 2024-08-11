package net.maniaticdevs.main.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.network.ChatMessage;
import net.maniaticdevs.engine.objects.DataBuffer;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.objects.PickableObject;
import net.maniaticdevs.engine.util.CollisionChecker;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.SoundSFXEnum;
import net.maniaticdevs.main.gui.GuiDialogue;
import net.maniaticdevs.main.gui.GuiInGame;

/**
 * Player class! Does player things...
 * 
 * @author Oikmo, LYCNK
 *
 */
public class Player extends Entity {

	/*float motionX, motionY; //Smooth Movement 
	float dist = xa * xa + ya * ya;
	if(dist >= 0.01F) {
		dist = speed / (float)Math.sqrt((double)dist);
		xa *= dist;
		ya *= dist;
		this.motionX += xa;
		this.motionY += ya ;
	}

	this.motionX *= 0.91F;
	this.motionY *= 0.91F;*/

	/** Position data for player to be at the center of the screen */
	private Vector2 screenPos = new Vector2();

	/** Player inventory for storing {@link OBJ}s */
	public List<OBJ> inventory = new ArrayList<>();
	/** Currently holding item */
	public OBJ holdingItem;
	/** You died, so it goes. */
	public boolean died = false;

	/** To prevent action to be done repeatedly in a short period of time */
	boolean lockTakeLife = false;
	/** To prevent action to be done repeatedly in a short period of time */
	boolean lockGiveLife = false;

	private int ticks = 0;

	public List<ChatMessage> messages = new ArrayList<>();

	@Override
	protected void setDefaultValues() {
		name = Main.playerName;
		maxHealth = 6;
		health = maxHealth;
		screenPos = new Vector2((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
		position.set(Settings.tileSize*4, Settings.tileSize*4);
		sprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
	}

	public void updateScreenPos() {
		screenPos.set((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
	}

	@Override
	public void tick() {
		if(ticks != 0 && GuiInGame.message != null) {
			ticks--;
			if(ticks <= 0 && GuiInGame.message != null) {
				GuiInGame.message = null;
			}
		}

		animate();

		for(int i = 0; i < messages.size(); i++) {
			if(messages.size() > 5) {
				messages.remove(0);
			}

			messages.get(i).tick();
		}

		direction = EntityDirection.IDLE;
		if((Main.currentScreen instanceof GuiInGame && ((GuiInGame)Main.currentScreen).chatScreen == null) && ((GuiInGame)Main.currentScreen).chatScreen == null) {
			if(Input.needsInput) {
				Input.clearInput();
			}
			
			if(Input.isKeyDown(Input.KEY_S)) {
				direction = EntityDirection.SOUTH;
			} else if(Input.isKeyDown(Input.KEY_W)) {
				direction = EntityDirection.NORTH;
			}if(Input.isKeyDown(Input.KEY_D)) {
				direction = EntityDirection.EAST;
			} else if(Input.isKeyDown(Input.KEY_A)) {
				direction = EntityDirection.WEST;
			}
		}
		
		if(Input.isKeyDown(Input.KEY_MINUS)) {
			if(!lockTakeLife) {
				health--;
				if(health <= 0) {
					System.exit(0);
				}
			}
			lockTakeLife = true;
		} else {
			lockTakeLife = false;
		}

		if(Input.isKeyDown(Input.KEY_EQUALS)) {
			if(!lockGiveLife) {
				health++;
				if(health > maxHealth) {
					maxHealth = ((maxHealth/2)+1)*2;
				}
			}
			lockGiveLife = true;
		} else {
			lockGiveLife = false;
		}

		if(Main.currentLevel != null) {
			colliding = false;
			CollisionChecker.checkTile(this);
			Entity ent = CollisionChecker.checkEntity(this);
			OBJ obj = CollisionChecker.checkObject(this);
			if(Main.theNetwork != null) {
				CollisionChecker.checkOtherPlayer(this);
			}

			
			
			OBJ contactOBJ = CollisionChecker.checkIfTouchingObj(this);
			if(Input.isKeyDown(Input.KEY_ENTER)) {
				
				if(ent != null) {
					if(ent instanceof NPC) {
						if(!((NPC)ent).lock) {
							((NPC)ent).onInteract();
						}
					}
				}
				
				if(contactOBJ instanceof PickableObject) {
					inventory.add(((PickableObject)contactOBJ).getItem());
					ticks = 180;
					GuiInGame.message = "You picked up: "+((PickableObject)contactOBJ).getItem().name;
					if(((PickableObject)contactOBJ).getItem() instanceof Key) {
						Main.sfxLib.play(SoundSFXEnum.key);
					}
					Main.currentLevel.removeObject(contactOBJ);
				}
				
				if(obj instanceof Door) {
					if(holdingItem != null) {
						if(holdingItem.networkID.contentEquals(((Door)obj).getRequiredKey().networkID)) {
							if(((Door)obj).removeKeyAfterUse()) {
								inventory.remove(((Door)obj).getRequiredKey());
							}
							Main.currentLevel.removeObject(obj);
							//Sound.playSFX("door");
							Main.sfxLib.play(SoundSFXEnum.door);
						}
					}
				} else if(obj instanceof DataBuffer) {
					Main.currentScreen = new GuiDialogue(true, ((DataBuffer)obj).getBuffer(), null);
				}
			}
		}


		if(!colliding) {
			/*if(getDirection() != EntityDirection.IDLE) {
				if(footSteps.isStopped()) {
					footSteps.play();
				}
			} else {
				footSteps.stop();
			}*/
			switch(getDirection()) {
			case EAST:
				position.x += speed;
				break;
			case NORTH:
				position.y -= speed;
				break;
			case SOUTH:
				position.y += speed;
				break;
			case WEST:
				position.x -= speed;
				break;
			default:
				break;

			}
		} else {
			direction = EntityDirection.IDLE;
		}
	}

	/**
	 * Checks if index of a soon to be selected item is valid
	 * @param index Index to be checked
	 * @return {@link Boolean}
	 */
	public boolean isItemAtIndex(int index) {
		if(index+1 <= inventory.size()) {
			if(inventory.get(index) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Selects item to be used for {@link #holdingItem}
	 * @param index Index for item to be chosen
	 */
	public void selectItem(int index) {
		if(index+1 <= inventory.size()) {
			holdingItem = inventory.get(index);
		} else {
			if(holdingItem != null) {
				Main.sfxLib.play(SoundSFXEnum.hit);
				holdingItem = null;
			}
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		//if debug flag is enabled
		if(Main.debug) {
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(2));
			//shows hitbox
			g2.drawRect(screenPos.x+getHitBox().x, screenPos.y+getHitBox().y, getHitBox().width, getHitBox().height);
			//prints info on screen
			g2.drawString("direction:"+direction.name(), 0, 10);
			g2.drawString("KEY_W:"+Input.isKeyDown(Input.KEY_W), 0, 25);
			g2.drawString("KEY_A:"+Input.isKeyDown(Input.KEY_A), 0, 50);
			g2.drawString("KEY_S:"+Input.isKeyDown(Input.KEY_S), 0, 75);
			g2.drawString("KEY_D:"+Input.isKeyDown(Input.KEY_D), 0, 100);
		}
		g2.setFont(GuiScreen.font.deriveFont(18.0F));
		int height = (int)g2.getFontMetrics().getStringBounds("cock", g2).getHeight();
		int change = height + 20;
		int offsetY = 0;
		for(int i = messages.size(); i > 0; i--) {
			int j = i - 1;
			ChatMessage chatmessage = messages.get(j);
			String msg = chatmessage.getMessage(); 

			int width = (int)g2.getFontMetrics().getStringBounds(msg, g2).getWidth();
			g2.setColor(Color.WHITE);
			g2.fillRoundRect((Main.getInstance().getWidth()/2)-((width+15)/2), (int) (screenPos.y-height*2.5f)-offsetY, width+15, height+15, 15, 15);
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect((Main.getInstance().getWidth()/2)-((width+15)/2), (int) (screenPos.y-height*2.5f)-offsetY, width+15, height+15, 5, 5);
			g2.setColor(Color.BLACK);
			g2.drawString(msg, (Main.getInstance().getWidth()/2)-((width)/2), screenPos.y-height-(offsetY));
			offsetY += change;
		}
		g2.drawImage(sprites[spriteNum+getDirection().ordinal()*6], null, screenPos.x, screenPos.y);

	}

	/**
	 * Returns {@link #screenPos}
	 * @return {@link Vector2}
	 */
	public Vector2 getScreenPosition() {
		return screenPos;
	}
}
