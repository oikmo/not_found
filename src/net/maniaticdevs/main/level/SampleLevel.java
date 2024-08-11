package net.maniaticdevs.main.level;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.level.Level;
import net.maniaticdevs.engine.objects.DataBuffer;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.PickableObject;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.entity.Test;

/**
 * Sample level using {@link Level}
 * @author Oikmo
 */
public class SampleLevel extends Level {
	
	/**
	 * Loads sample map
	 */
	public SampleLevel(boolean dontLoad) {
		super("sample", "Sample", 32, 32, dontLoad);
	}
	
	protected void loadEverything() {
		Key key1 = new Key("Room Key",Settings.tileSize*1,Settings.tileSize*1);
		this.objects.add(new PickableObject(key1,Settings.tileSize*1,Settings.tileSize*1));
		this.objects.add(new Door(key1, true, Settings.tileSize*24,Settings.tileSize*12));
		this.objects.add(new DataBuffer("test", 2, Settings.tileSize*14,Settings.tileSize));
		this.entities.add(new Test(new Vector2(Settings.tileSize*5, Settings.tileSize*7)));
	}

}
