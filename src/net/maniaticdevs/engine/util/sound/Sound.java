package net.maniaticdevs.engine.util.sound;

import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

/**
 * Sound class! Plays WAVs D:<
 * @author Oikmo
 */
public class Sound {
	
	private Clip clip;
	private URL soundURL[];
	public int selectedTrack;
	private FloatControl control;
	private int volumeScale = 3;
	private float volume;
	
	/**
	 * Expects a URL list as to load into itself
	 * @param urls URL list expected to be loaded
	 */
	public Sound(URL... urls) {
		soundURL = urls;
	}
	
	/**
	 * Loads URL as an audio clip and selects to {@link #selectedTrack} and {@link #clip}
	 * @param i Index of URL
	 */
	public void setFile(int i) {
		try {
			selectedTrack = i;
			AudioInputStream soundIn = AudioSystem.getAudioInputStream(soundURL[selectedTrack]);
			AudioFormat format = soundIn.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(soundIn);
			control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			checkVolume();
			control.setValue(volume);
		} catch(Exception e) {}
	}
	
	public void play() {
		if(clip != null) {
			if(clip.isOpen()) {
				clip.start();
			}
		}	
	}
	
	public void loop() {
		if(clip != null) {
			if(clip.isOpen()) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
	}
	
	public void stop() {
		if(clip != null) {
			if(clip.isOpen()) {
				clip.stop();
			}
		}
	}
	
	public boolean isPlaying() {
		boolean playing = false;
		if(clip != null) {
			if(clip.isOpen()) {
				playing = clip.isRunning();
			}
		}
		
		return playing;
	}
	
	public int getCurrentFile() {
		return selectedTrack;
	}
	
	public void checkVolume() {
		switch(volumeScale) {
		case 0: volume = -80f; break;
		case 1: volume = -20f; break;
		case 2: volume = -12f; break;
		case 3: volume = -5f; break;
		case 4: volume = 1f; break;
		case 5: volume = 6f; break;
		}
	}
	
	public void checkVolume(int scale) {
		switch(scale) {
		case 0: volume = -80f; break;
		case 1: volume = -20f; break;
		case 2: volume = -12f; break;
		case 3: volume = -5f; break;
		case 4: volume = 1f; break;
		case 5: volume = 6f; break;
		}
	}
	
	public void play(int i) {
		this.setFile(i);
		this.play();
	}
}