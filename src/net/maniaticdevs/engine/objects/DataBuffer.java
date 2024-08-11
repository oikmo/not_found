package net.maniaticdevs.engine.objects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.StringUtil;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiDialogue;

/**
 * A barrier to the player (unless they have a key :P)
 * @author Oikmo
 */
public class DataBuffer extends OBJ {
	
	private List<String> buffer;
	private int direction;
	
	/**
	 * Door constructor
	 * @param bufferText Text file to load
	 * @param direction 0 = down, 1 = up, 2 = left, 3 = right
	 * @param x X coordinate
	 * @param y X coordinate
	 */
	public DataBuffer(String bufferText, int direction, int x, int y) {
		name = "Data Buffer";
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
		this.direction = direction;
		collision = true;
		position.set(x,y);
	}
	
	public void interact(Entity entity) {
		Main.currentScreen = new GuiDialogue(true, getBuffer(), null);
	}
	
	public List<String> getBuffer() {
		return buffer;
	}
}
