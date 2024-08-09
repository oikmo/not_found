package net.maniaticdevs.engine.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.maniaticdevs.main.Main;

/**
 * Universal language handler. Incase I get bored.
 * @author Oikmo
 */
public class LanguageHandler {
	/** Instance :D */
	private static LanguageHandler instance = new LanguageHandler();
	/** Where all keys and values are stored */
	private Properties properties = new Properties();
	/** Where to load?? <code>"/en_GB.lang"</code>*/
	private InputStream filePath;
	
	/**
	 * Loads  <code>"/en_GB.lang"</code> into {@link #properties}
	 */
	protected LanguageHandler() {
		try {
			this.filePath = Main.class.getResourceAsStream("/en_GB.lang");
			properties.load(filePath);
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}
	
	/** 
	 * Returns active instance :D 
	 * @return {@link #instance} 
	 */
	public static LanguageHandler getInstance() {
		return instance;
	}
	
	/**
	 * Returns value from key
	 * @param key To get value from
	 * @return Value
	 */
	public String translateKey(String key) {
		return this.properties.getProperty(key, key);
	}
}
