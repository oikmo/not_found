package net.maniaticdevs.main.level;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.level.Level;
import net.maniaticdevs.engine.objects.DataBuffer;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.MovingImage;
import net.maniaticdevs.engine.objects.PickableObject;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.SoundSFXEnum;
import net.maniaticdevs.main.entity.Test;
import net.maniaticdevs.main.entity.Watcher;

/**
 * Sample level using {@link Level}
 * @author Oikmo
 */
public class SampleLevel extends Level {
	
	/**
	 * Loads sample map
	 * @param dontLoad if true then don't call {@link #loadEverything()}
	 */
	public SampleLevel(boolean dontLoad) {
		super("sample", "Sample", 32, 32, dontLoad);
	}
	
	protected void loadEverything() {
		Key key1 = new Key("Room Key",Settings.worldTileSize*1,Settings.worldTileSize*1);
		this.objects.add(new PickableObject(key1,Settings.worldTileSize*1,Settings.worldTileSize*1));
		this.objects.add(new Door(key1, true, Settings.worldTileSize*24,Settings.worldTileSize*12));
		this.objects.add(new DataBuffer("test", 2, Settings.worldTileSize*14,Settings.worldTileSize));
		this.entities.add(new Test(new Vector2(Settings.worldTileSize*5, Settings.worldTileSize*7)));
		this.entities.add(new Watcher(new Vector2(Settings.worldTileSize*10, Settings.worldTileSize*18)));
		this.objects.add(new MovingImage(SoundSFXEnum.catgif, "catgif", 6, 5, 5, Settings.worldTileSize*29, Settings.worldTileSize*29));
	}

}
