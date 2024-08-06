package net.maniaticdevs.engine.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.maniaticdevs.main.Main;

/**
 * Saves and Loads objects with any type as to make my life easier lmao
 * @author Oikmo
 */
public class SaveSystem {
	
	public static void save(String name, Object data) {
	    try {
	    	FileOutputStream fos = new FileOutputStream(Main.getWorkingDirectory() + "/" + name + ".dat");
	    	GZIPOutputStream gzo = new GZIPOutputStream(fos);
		    ObjectOutputStream oos = new ObjectOutputStream(gzo);
		    
			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	public static Object load(String name) {	
		File save = new File(Main.getWorkingDirectory() + "/" + name + ".dat");
		if(save.exists()) {
			ObjectInputStream obj;
			try {
				FileInputStream fis = new FileInputStream(save);
				GZIPInputStream gzi = new GZIPInputStream(fis);
				obj = new ObjectInputStream(gzi);
				Object data =  obj.readObject();
				obj.close();
				return data;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}
	
}
