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
	/**<b><i>KEY_MINUS</i></b>
	 *<pre>public static final int KEY_MINUS</pre> */
	public static int KEY_MINUS = KeyEvent.VK_MINUS;
	/**<b><i>KEY_EQUALS</i></b>
	 *<pre>public static final int KEY_EQUALS</pre> */
	public static int KEY_EQUALS = KeyEvent.VK_EQUALS;
	/**<b><i>KEY_E</i></b>
	 *<pre>public static final int KEY_E</pre> */
	public static int KEY_E = KeyEvent.VK_E;
	/**<b><i>KEY_ESC</i></b>
	 *<pre>public static final int KEY_ESC</pre> */
	public static int KEY_ESC = KeyEvent.VK_ESCAPE;
	/**<b><i>KEY_UP</i></b>
	 *<pre>public static final int KEY_UP</pre> */
	public static int KEY_UP = KeyEvent.VK_UP;
	/**<b><i>KEY_DOWN</i></b>
	 *<pre>public static final int KEY_DOWN</pre> */
	public static int KEY_DOWN = KeyEvent.VK_DOWN;
	/**<b><i>KEY_LEFT</i></b>
	 *<pre>public static final int KEY_LEFT</pre> */
	public static int KEY_LEFT = KeyEvent.VK_LEFT;
	/**<b><i>KEY_RIGHT</i></b>
	 *<pre>public static final int KEY_RIGHT</pre> */
	public static int KEY_RIGHT = KeyEvent.VK_RIGHT;
	/**<b><i>KEY_ENTER</i></b>
	 *<pre>public static final int KEY_ENTER</pre> */
	public static int KEY_ENTER = KeyEvent.VK_ENTER;
	
	/**
	 * Adds defined Keys to keyMap.
	 */
	public Input() {
		keyMap.put(KEY_A, false);
		keyMap.put(KEY_D, false);
		keyMap.put(KEY_E, false);
		keyMap.put(KEY_S, false);
		keyMap.put(KEY_W, false);
		keyMap.put(KEY_MINUS, false);
		keyMap.put(KEY_EQUALS, false);
		keyMap.put(KEY_ESC, false);
		keyMap.put(KEY_UP, false);
		keyMap.put(KEY_DOWN, false);
		keyMap.put(KEY_LEFT, false);
		keyMap.put(KEY_RIGHT, false);
		keyMap.put(KEY_ENTER, false);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(keyMap.get(code) != null) {
			keyMap.replace(code, true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int code = e.getKeyCode();
		if(keyMap.get(code) != null) {
			keyMap.replace(code, false);
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
