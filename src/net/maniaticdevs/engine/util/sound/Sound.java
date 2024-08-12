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

public class Sound {
	
	private SoundSystem soundSystem;
	
	public static void init() {
		try {
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch( SoundSystemException e ) {
			System.err.println("error linking with the plug-ins");
		}
	}
	
	private String soundURL[];
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
	
	public void play(int i) {
		play(i, false);
	}
	
	public void play(int i, boolean loop) {
		String name = soundURL[i].split("/")[soundURL[i].split("/").length-1].replace(".ogg", "") + "" + new Random().nextInt();
		if(loop) {
			System.out.println(name);
			loopers.add(name);
		}
		soundSystem.newSource(false, name, Main.class.getResource(soundURL[i]), soundURL[i].split("/")[soundURL[i].split("/").length-1], loop, 0, 0, 0, 0, 0);
		soundSystem.setTemporary(name, !loop);
		soundSystem.setVolume(name, 1f);
		soundSystem.play(name);
	}
	
	public boolean isPlaying() {
		return soundSystem.playing();
	}
	
	public void stop() {
		for(int i = 0; i < loopers.size(); i++) {
			String l = loopers.get(i);
			System.out.println(l);
			soundSystem.stop(l);
			soundSystem.removeSource(l);
			loopers.remove(i);
		}
		soundSystem.removeTemporarySources();
	}
}
