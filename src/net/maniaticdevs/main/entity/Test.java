package net.maniaticdevs.main.entity;

import java.io.IOException;

import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.StringUtil;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiDialogue;

public class Test extends NPC {
	
	public Test(Vector2 position) {
		super(position);
		sprites = ImageUtils.setupSheet("player/playerSheet", 6, 5);
		try {
			dialogueToBeLoaded = StringUtil.loadTextFile("dialogues/test.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDefaultValues() {
		speed = 2;
	}
	
	public void onInteract() {
		Main.currentScreen = new GuiDialogue(dialogueToBeLoaded, this);
	}

}
