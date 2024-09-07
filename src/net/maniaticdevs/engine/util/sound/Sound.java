package net.maniaticdevs.engine.util.sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.maniaticdevs.main.Main;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryJavaSound;

/**
 * Sound Library using paulscode {@link SoundSystem}
 * @author Oikmo
 */
public class Sound {
	/** SoundSystem to play audio from */
	private SoundSystem soundSystem;
	
	/** Sets up {@link SoundSystemConfig} */
	public static void init() {
		try {
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch( SoundSystemException e ) {
			System.err.println("error linking with the plug-ins");
		}
	}
	
	/** Loaded sounds */
	private String soundURL[];
	/** Keeps track of tracks that loop (as to stop it) */
	private List<String> loopers = new ArrayList<>();
	
	/**
	 * Expects a URL list as to load into itself
	 * @param urls URL list expected to be loaded
	 */
	public Sound(String... urls) {
		soundSystem = new SoundSystem();
		soundURL = new String[urls.length];
		for(int i = 0; i < urls.length; i++) {
			soundURL[i] = "/sounds/"+urls[i]+".ogg";
		}
	}
	
	/** 
	 * Plays audio files without looping ever
	 * @param i Index of sound track from {@link #soundURL}
	 */
	public void play(int i) {
		play(i, false);
	}
	
	/**
	 * Plays audio files 
	 * @param i Index of sound track from {@link #soundURL}
	 * @param loop Should the track loop
	 */
	public void play(int i, boolean loop) {
		play(i, loop, 0.5f, 0, 0, 0, 0);
	}
	
	/**
	 * Set's the audio listener
	 * @param x X position
	 * @param y Y Position
	 */
	public void setListener(int x, int y) {
		soundSystem.setListenerPosition(x, 0, y);
		//soundSystem.set
	}
	
	/**
	 * Plays audio files 
	 * @param i Index of sound track from {@link #soundURL}
	 * @param loop Should the track loop
	 * @param volume Volume to play at
	 * @param x X position of the sound
	 * @param y Y position of the sound
	 * @param model The model to use for rolling off the audio
	 * @param rolloff The distance to rolloff the audio
	 */
	public void play(int i, boolean loop, float volume, int x, int y, int model, int rolloff) {
		String name = soundURL[i].split("/")[soundURL[i].split("/").length-1].replace(".ogg", "") + "" + new Random().nextInt();
		if(loop) {
			System.out.println(name);
			loopers.add(name);
		}
		soundSystem.newSource(false, name, Main.class.getResource(soundURL[i]), soundURL[i].split("/")[soundURL[i].split("/").length-1], loop, x, y, 0, model, rolloff);
		soundSystem.setTemporary(name, !loop);
		soundSystem.setVolume(name, volume);
		soundSystem.play(name);
	}
	
	/** 
	 * Returns true if the {@link #soundSystem} is playing anything
	 *  @return {@link Boolean}
	 */
	public boolean isPlaying() {
		return soundSystem.playing();
	}
	
	/** Stops all sources (and removes all temporary ones) */
	public void stop() {
		for(int i = 0; i < loopers.size(); i++) {
			String l = loopers.get(i);
			soundSystem.stop(l);
			soundSystem.removeSource(l);
			loopers.remove(i);
		}
		soundSystem.removeTemporarySources();
	}
}
