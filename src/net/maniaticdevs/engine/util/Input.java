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
	public static final int KEY_W = KeyEvent.VK_W;
	/**<b><i>KEY_A</i></b>
	 *<pre>public static final int KEY_A</pre> */
	public static final int KEY_A = KeyEvent.VK_A;
	/**<b><i>KEY_S</i></b>
	 *<pre>public static final int KEY_S</pre> */
	public static final int KEY_S = KeyEvent.VK_S;
	/**<b><i>KEY_D</i></b>
	 *<pre>public static final int KEY_D</pre> */
	public static final int KEY_D = KeyEvent.VK_D;
	/**<b><i>KEY_MINUS</i></b>
	 *<pre>public static final int KEY_MINUS</pre> */
	public static final int KEY_MINUS = KeyEvent.VK_MINUS;
	/**<b><i>KEY_EQUALS</i></b>
	 *<pre>public static final int KEY_EQUALS</pre> */
	public static final int KEY_EQUALS = KeyEvent.VK_EQUALS;
	/**<b><i>KEY_E</i></b>
	 *<pre>public static final int KEY_E</pre> */
	public static final int KEY_E = KeyEvent.VK_E;
	/**<b><i>KEY_ESC</i></b>
	 *<pre>public static final int KEY_ESC</pre> */
	public static final int KEY_ESC = KeyEvent.VK_ESCAPE;
	/**<b><i>KEY_UP</i></b>
	 *<pre>public static final int KEY_UP</pre> */
	public static final int KEY_UP = KeyEvent.VK_UP;
	/**<b><i>KEY_DOWN</i></b>
	 *<pre>public static final int KEY_DOWN</pre> */
	public static final int KEY_DOWN = KeyEvent.VK_DOWN;
	/**<b><i>KEY_LEFT</i></b>
	 *<pre>public static final int KEY_LEFT</pre> */
	public static final int KEY_LEFT = KeyEvent.VK_LEFT;
	/**<b><i>KEY_RIGHT</i></b>
	 *<pre>public static final int KEY_RIGHT</pre> */
	public static final int KEY_RIGHT = KeyEvent.VK_RIGHT;
	/**<b><i>KEY_ENTER</i></b>
	 *<pre>public static final int KEY_ENTER</pre> */
	public static final int KEY_ENTER = KeyEvent.VK_ENTER;
	/**<b><i>KEY_T</i></b>
	 *<pre>public static final int KEY_T</pre> */
	public static final int KEY_T = KeyEvent.VK_T;
	
	public static int lastFuckingKey = -1;
	
	public static int lengthInput = 15;
	
	public static enum InputType {
		Username,
		IP,
		Chat
	}
	
	public static InputType inputType = InputType.Username;
	public static boolean needsInput = false;
	private static String inputFull = "";
	
	
	/**
	 * Adds defined Keys to keyMap.
	 */
	public Input() {
		keyMap.put(KEY_A, false);
		keyMap.put(KEY_D, false);
		keyMap.put(KEY_E, false);
		keyMap.put(KEY_S, false);
		keyMap.put(KEY_T, false);
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
		
		if(needsInput) {
			if(Character.isAlphabetic(e.getKeyChar()) || Character.isDigit(e.getKeyChar()) || (e.getKeyChar() == '.' && inputType == InputType.IP) && inputType != InputType.Chat) {
				if(inputFull.length() < lengthInput) {
					inputFull += e.getKeyChar();
				}
				
			} else if(inputType == InputType.Chat && this.isValidCharacter(e.getKeyChar())) {
				if(inputFull.length() < lengthInput) {
					inputFull += e.getKeyChar();
				}
			} else {
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if(inputFull.length() != 0) {
						inputFull = inputFull.substring(0, inputFull.length()-1);
					}
					
				}
			}	
		}
		
		lastFuckingKey = code;
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
		boolean key = keyMap.get(keyToCheckFor);
		return key;
	}
	
	/**
	 *	You give a key and it spews back if that key is currently being pressed
	 * @param keyToCheckFor - Key defined by Input class
	 * @return {@link Boolean}
	 */
	public static boolean isKeyDownExplicit(int keyToCheckFor) {
		boolean key = keyMap.get(keyToCheckFor);
		keyMap.replace(keyToCheckFor, false);
		return key;
	}
	
	public static int getLastKeyPressed() {
		return lastFuckingKey;
	}

	public static String getTextInput() {
		return inputFull;
	}

	public static void clearInput() {
		inputFull = "";
		needsInput = false;
	}
	
	private boolean isValidCharacter(char c) {
		return " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\307\374\351\342\344\340\345\347\352\353\350\357\356\354\304\305\311\346\306\364\366\362\373\371\377\326\334\370\243\330\327\u0192\341\355\363\372\361\321\252\272\277\256\254\275\274\241\253\273".indexOf(c) >= 0;
	}
}
