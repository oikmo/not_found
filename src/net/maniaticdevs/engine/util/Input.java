package net.maniaticdevs.engine.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Key handling and maybe soon Mouse handling!!! OOOHHH!!!
 * @author Oikmo
 */
public class Input implements KeyListener {
	
	/** to keep track of what keys are pressed for  {@link #isKeyDown(int)} */
	private static Map<Integer, Boolean> keyMap = new HashMap<>();
	/**<b><i>KEY_W</i></b>
	 *<pre>public static final int KEY_W</pre> */
	public static int KEY_W = KeyEvent.VK_W;
	/**<b><i>KEY_A</i></b>
	 *<pre>public static final int KEY_A</pre> */
	public static int KEY_A = KeyEvent.VK_A;
	/**<b><i>KEY_S</i></b>
	 *<pre>public static final int KEY_S</pre> */
	public static int KEY_S = KeyEvent.VK_S;
	/**<b><i>KEY_D</i></b>
	 *<pre>public static final int KEY_D</pre> */
	public static int KEY_D = KeyEvent.VK_D;
	
	/**
	 * Adds defined Keys to keyMap.
	 */
	public Input() {
		keyMap.put(KEY_W, false);
		keyMap.put(KEY_A, false);
		keyMap.put(KEY_S, false);
		keyMap.put(KEY_D, false);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(keyMap.get(code) != null) {
			keyMap.replace(e.getKeyCode(), true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(keyMap.get(code) != null) {
			keyMap.replace(e.getKeyCode(), false);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 *	You give a key and it spews back if that key is currently being pressed
	 * @param keyToCheckFor - Key defined by Input class
	 * @return {@link Boolean}
	 */
	public static boolean isKeyDown(int keyToCheckFor) {
		return keyMap.get(keyToCheckFor);
	}

}
