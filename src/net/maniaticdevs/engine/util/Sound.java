package net.maniaticdevs.engine.util;

import jaco.mp3.player.MP3Player;
import net.maniaticdevs.engine.ResourceLoader;

/**
 * Sound class, plays MP3s. fuck WAVs
 * @author Oikmo
 */
public class Sound {
	
	/**
	 * Plays MP3 files from the sound directory!
	 * @param fileName name and location of mp3
	 */
	public static void play(String fileName) {
		 new MP3Player(ResourceLoader.loadResource("/sounds/"+fileName+".mp3")).play();
	}
	
	/**
	 * Plays sfx from SFX directory (uses {@link #play(String)})
	 * @param name name of file
	 */
	public static void playSFX(String name) {
		play("sfx/"+name);
	}
}