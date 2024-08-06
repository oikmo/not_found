package net.maniaticdevs.engine.util.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.maniaticdevs.main.Main;

public class OptionsHandler {
	private static OptionsHandler instance = new OptionsHandler();
	private Properties properties = new Properties();
	private String filePath;
	
	protected OptionsHandler() {
		try {
			this.filePath = Main.getWorkingDirectory()+"/options.txt";
			properties.load(new FileInputStream(filePath));
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}
	
	public static OptionsHandler getInstance() {
		return instance;
	}
	
	public void insertKey(String name, String value) {
		this.properties.setProperty(name, value);
		try {
			this.properties.store(new FileOutputStream(filePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String translateKey(String key) {
		return this.properties.getProperty(key, key);
	}
	
	public String translateKeyFormat(String key, Object toFormat) {
		String property = this.properties.getProperty(key, key);
		return String.format(property, toFormat);
	}

	public String translateNamedKey(String key) {
		return this.properties.getProperty(key + ".name", "");
	}
	
	public void save() {
		try {
			this.properties.store(new FileOutputStream(filePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
