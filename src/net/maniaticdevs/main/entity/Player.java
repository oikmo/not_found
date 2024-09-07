package net.maniaticdevs.main.entity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.maniaticdevs.engine.ResourceLoader;
import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.entity.Monster;
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
	
	/** If set, it will count down to 0 in which it removes the active notification from {@link GuiInGame} */
	private int notficationTicks = 0;
	
	/** Chat bubbles for player */
	public List<ChatMessage> messages = new ArrayList<>();
	
	/** When the player dies, this shows up */
	private BufferedImage deadSprite;

	@Override
	protected void setDefaultValues() {
		name = Main.playerName;
		level = 1;
		maxHealth = 6;
		health = maxHealth;
		strength = 1; // the higher the strength. the higher the damage
		dexterity = 1; // the higher the uh (what is it? oh dex- dexitrry? no? dexterity? oh ok.) dexterity the lesser the damage
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		screenPos = new Vector2((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
		position.set(Settings.tileSize*4, Settings.tileSize*4);
		sprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
		deadSprite = ImageUtils.scaleImage(ResourceLoader.loadImage("/textures/player/player_dead.png"), Settings.tileSize, Settings.tileSize);
	}
	
	/**
	 * If the player kills a monster, this is ran.
	 * If {@link #exp} is higher than (or equal to) {@link #nextLevelExp} then level up.
	 */
	@SuppressWarnings("unused")
	private void checkLevelUp() {
		if(exp >= nextLevelExp) {
			Main.sfxLib.play(SoundSFXEnum.powerUp);
			level++;
			nextLevelExp = nextLevelExp*2;
			maxHealth += 2;
			strength++;
			dexterity++;
			//attack = getAttack();
			//defense = getDefense();
		}
		
	}
	
	/** Updates the player center of screen */
	public void updateScreenPos() {
		screenPos.set((Main.getInstance().getWidth() / 2 - (Settings.tileSize / 2)), Main.getInstance().getHeight() / 2 - (Settings.tileSize / 2));
	}

	@Override
	public void tick() {
		
		Main.sfxLib.setListener(position.x, position.y);
		
		if (isInvince) { invinceCounter++; if (invinceCounter > 40) { isInvince = false; invinceCounter = 0; } }
		
		if(notficationTicks != 0 && GuiInGame.message != null) {
			notficationTicks--;
			if(notficationTicks <= 0 && GuiInGame.message != null) {
				GuiInGame.message = null;
			}
		}

		for(int i = 0; i < messages.size(); i++) {
			if(messages.size() > 5) {
				messages.remove(0);
			}
			messages.get(i).tick();
		}
		
		if((Main.currentScreen instanceof GuiInGame && ((GuiInGame)Main.currentScreen).chatScreen == null) && ((GuiInGame)Main.currentScreen).chatScreen == null && health != 0) {
			animate();
			
			if(Input.needsInput) {
				Input.clearInput();
			}
			direction = EntityDirection.IDLE;
			if(Input.isKeyDown(Input.KEY_S)) {
				direction = EntityDirection.SOUTH;
			} else if(Input.isKeyDown(Input.KEY_W)) {
				direction = EntityDirection.NORTH;
			}if(Input.isKeyDown(Input.KEY_D)) {
				direction = EntityDirection.EAST;
			} else if(Input.isKeyDown(Input.KEY_A)) {
				direction = EntityDirection.WEST;
			}
			
		} else if(Main.currentScreen instanceof GuiDialogue) {
			spriteNum = 0;
		}
		
		if(Input.isKeyDown(Input.KEY_MINUS)) {
			if(!lockTakeLife) {
				if(health != 0) {
					health--;
				}
			}
			lockTakeLife = true;
		} else {
			lockTakeLife = false;
		}

		if(Input.isKeyDown(Input.KEY_EQUALS)) {
			if(!lockGiveLife) {
				if(health != 0) {
					health++;
					if(health > maxHealth) {
						maxHealth = ((maxHealth/2)+1)*2;
					}
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
			
			if(ent instanceof Monster) {
				if(!isInvince) {
					((Monster)ent).react();
					changePlayerHealth(-((Monster)ent).attack);
					Main.sfxLib.play(SoundSFXEnum.recieveDmg);
					isInvince = true;
				}
				
				
			}
			
			OBJ contactOBJ = CollisionChecker.checkIfTouchingObj(this);
			if(Input.isKeyDown(Input.KEY_ENTER)) {
				
				if(ent != null) {
					if(ent instanceof NPC) {
						if(!((NPC)ent).lock) {
							((NPC)ent).react();
						}
					}
				}
				
				if(contactOBJ instanceof PickableObject) {
					inventory.add(((PickableObject)contactOBJ).getItem());
					notficationTicks = 180;
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
					 ((DataBuffer)obj).interact(this);
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
	 * Changes the player health
	 * @param amount Amount to change health by
	 */
	public void changePlayerHealth(int amount) {
		health += amount;
		if(health > maxHealth) {
			health = maxHealth;
		} else if(health < 0) {
			health = 0;
			isInvince = false;
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
		if (isInvince) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}
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
		if(health != 0) {
			g2.drawImage(sprites[spriteNum+getDirection().ordinal()*6], null, screenPos.x, screenPos.y);
		} else {
			g2.drawImage(deadSprite, null, screenPos.x, screenPos.y);
		}
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

	}

	/**
	 * Returns {@link #screenPos}
	 * @return {@link Vector2}
	 */
	public Vector2 getScreenPosition() {
		return screenPos;
	}
}
