package net.maniaticdevs.engine.util.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.maniaticdevs.main.Main;

/**
 * Easy loading and saving of options
 * @author Oikmo
 */
public class OptionsHandler {
	/** To easily retrieve the active instance */
	private static OptionsHandler instance = new OptionsHandler();
	/** Where all keys and values are stored */
	private Properties properties = new Properties();
	/** Path of options.txt */
	private String filePath;
	
	/** Loads {@link #filePath} into {@link #properties} */
	protected OptionsHandler() {
		try {
			this.filePath = Main.getWorkingDirectory()+"/options.txt";
			properties.load(new FileInputStream(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns {@link #instance} (the active instance)
	 * @return {@link #instance}
	 */
	public static OptionsHandler getInstance() {
		return instance;
	}
	
	/** 
	 * Adds to {@link #properties}
	 * @param name Key name
	 * @param value Key value
	 */
	public void insertKey(String name, String value) {
		this.properties.setProperty(name, value);
		try {
			this.properties.store(new FileOutputStream(filePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Translate a key to current language.
	 * @param key Key to translate
	 * @return {@link String}
	 */
	public String translateKey(String key) {
		return this.properties.getProperty(key, key);
	}
	
	/** Translate a key to current language applying String.format() 
	 * @param key Key to translate
	 * @param toFormat Format
	 * @return {@link String}
	 */
	public String translateKeyFormat(String key, Object toFormat) {
		String property = this.properties.getProperty(key, key);
		return String.format(property, toFormat);
	}
	
	/**
	 * Saves to {@link #filePath}
	 */
	public void save() {
		try {
			this.properties.store(new FileOutputStream(filePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
