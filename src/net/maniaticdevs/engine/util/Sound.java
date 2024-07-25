package net.maniaticdevs.engine.util;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * Proof of concept class but once development goes in far enough this will be a functioning one
 * @author Oikmo
 */
public class Sound {
	
	/**
	 * Proof of concept, still yet to be properly done.
	 * @param fileName
	 */
	public static void play(String fileName) {
		new Thread(new Runnable() {
			public void run() {
				InputStream inputStream = this.getClass().getResourceAsStream("/"+fileName+".mp3"); //reads from res dir (since res is a source folder)
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				Player player = null;
				try {
					player = new Player(bufferedInputStream);
				} catch (JavaLayerException e1) {
					e1.printStackTrace();
				}
				//int totalLength = inputStream.available(); //if you REALLY want to know the length in bytes
		        try {
					player.play();
				} catch (JavaLayerException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}
}