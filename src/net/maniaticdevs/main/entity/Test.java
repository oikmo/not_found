package net.maniaticdevs.main.entity;

import java.io.IOException;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.util.ImageUtils;
import net.maniaticdevs.engine.util.StringUtil;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.gui.GuiDialogue;
import net.maniaticdevs.main.gui.GuiInGame;

public class Test extends NPC {
	
	public Test(Vector2 position) {
		super(position);
		//player/playerSheet
		sprites = ImageUtils.setupSheet("npc/watcher_sprite", 6, 5);
		try {
			dialogueToBeLoaded = StringUtil.loadTextFile("dialogues/test.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tickers() {
		float distance = (float) (Math.abs(Main.thePlayer.getPosition().magnitude() - getPosition().magnitude())/Settings.tileSize);
		if(distance <= 2) {
			GuiInGame.sizeGlitches = 5;
		} else {
			GuiInGame.sizeGlitches = 0;
		}
	}
	
	public void setDefaultValues() {
		speed = 2;
	}
	
	public void onInteract() {
		Main.currentScreen = new GuiDialogue(false, dialogueToBeLoaded, this);
	}

}
