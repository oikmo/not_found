package net.maniaticdevs.engine.network;

import java.util.List;
import java.util.Locale;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;

/**
 * For chat bubbles (taken from BlockBase lol)
 * @author Oikmo
 */
public class ChatMessage {

	/** How long it should last for (6 seconds by default) */
	private int timer = 60*6;
	/** The message it self */
	private String message;
	/** What list to use for removing itself */
	private List<ChatMessage> list;
	/** Talk box for TTS */
	private static Synthesizer synthesizer;
	
	/** Creates and allocates the {@link #synthesizer} */
	public static void init() {
		try {
			SynthesizerModeDesc desc = new SynthesizerModeDesc(Locale.US);
			synthesizer = Central.createSynthesizer(desc);
			synthesizer.allocate();
			synthesizer.getSynthesizerProperties().setVolume(0.92f);
			synthesizer.getSynthesizerProperties().setPitch(100);
			Logger.log(LogLevel.INFO, "Allocating synthesizer!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor to initalise {@link #ChatMessage(List, String)}
	 * @param list List for self use {@link #list}
	 * @param message Message itself {@link #message}
	 */
	public ChatMessage(List<ChatMessage> list, String message) {
		this.message = message;
		this.list = list;
		this.list.add(this);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					synthesizer.speakPlainText(message, null);
					synthesizer.wait();
				} catch(Exception e) {}
			}
		}).start();
	}
	
	/**
	 * Deallocates {@link #synthesizer}
	 */
	public static void cleanup() {
		Logger.log(LogLevel.INFO, "Deallocating synthesizer!");
		try {
			synthesizer.deallocate();
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EngineStateError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Used to tick down the {@link #timer} variable and after it reaches 0 then it removes it self from {@link #list}
	 */
	public void tick() {
		timer--;
		if(timer <= 0) {
			this.list.remove(this);
		}
	}

	/**
	 * Returns the message
	 * @return {@link #message}
	 */
	public String getMessage() {
		return message;
	}
}
