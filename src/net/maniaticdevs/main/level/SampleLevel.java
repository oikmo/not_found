package net.maniaticdevs.main.level;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.level.Level;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.PickableObject;

/**
 * Sample level using {@link Level}
 * @author Oikmo
 */
public class SampleLevel extends Level {
	
	/**
	 * Loads sample map
	 */
	public SampleLevel() {
		super("sample", "Sample", 32, 32);
	}
	
	protected void loadEverything() {
		Key key1 = new Key("Room Key",Settings.tileSize*1,Settings.tileSize*1);
		this.objects.add(new PickableObject(key1,Settings.tileSize*1,Settings.tileSize*1));
		this.objects.add(new Door(key1, true, Settings.tileSize*24,Settings.tileSize*12));
	
	}

}
