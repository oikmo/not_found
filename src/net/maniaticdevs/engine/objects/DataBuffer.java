package net.maniaticdevs.engine.objects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.StringUtil;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiDialogue;

/**
 * A source of information for the player...
 * @author Oikmo
 */
public class DataBuffer extends OBJ {
	
	/** All pieces of dialogue to be loaded */
	private List<String> buffer;
	/** For networking, to load buffer file on all clients */
	public String bufferName;
	/** Which way is it facing? */
	public int direction;
	
	/**
	 * Buffer constructor
	 * @param bufferText Text file to load
	 * @param direction 0 = down, 1 = up, 2 = right, 3 = left
	 * @param x X coordinate
	 * @param y X coordinate
	 */
	public DataBuffer(String bufferText, int direction, int x, int y) {
		name = "Data Buffer";
		this.bufferName = bufferText;
		this.direction = direction;
		try {
			buffer = StringUtil.loadTextFile("dialogues/buffer_"+bufferText+".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		image = ImageUtils.setupSheet("object/data-buffer-obj", 4,1)[direction];
		switch(direction) {
		case 0:
			hitBox = new Rectangle(0,0,48,12);
			break;
		case 1:
			hitBox = new Rectangle(0,36,48,12);
			break;
		case 2:
			hitBox = new Rectangle(36,0,12,48);
			break;
		case 3:
			hitBox = new Rectangle(0,0,12,48);
			break;
		}
		collision = true;
		position.set(x,y);
	}
	
	public void interact(Entity entity) {
		boolean interact = false;
		if(entity.getDirection() == EntityDirection.NORTH && direction == 0) {
			interact = true;
		} else if(entity.getDirection() == EntityDirection.SOUTH && direction == 1) {
			interact = true;
		} else if(entity.getDirection() == EntityDirection.EAST && direction == 2) {
			interact = true;
		}  else if(entity.getDirection() == EntityDirection.WEST && direction == 3) {
			interact = true;
		}
		
		if(interact) {
			Main.currentScreen = new GuiDialogue(true, getBuffer(), null);
		}
		
	}
	
	/**
	 * Returns buffer dialogues
	 * @return {@link List}	&#60;{@link String}&#62;	
	 */
	public List<String> getBuffer() {
		return buffer;
	}
}
