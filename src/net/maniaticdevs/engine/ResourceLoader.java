package net.maniaticdevs.engine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Loads files into memory as to not use a lot of disk usage (also easy file management)
 * @author Oikmo
 */
public class ResourceLoader {
	
	/** Where files are stored */
	private static Map<String, FileData> files = new HashMap<>();
	
	/**
	 * Returns {@link FileData} from {@link #files}, if it isn't found within then it will be loaded into it.
	 * @param loc Location of file in jar
	 * @return {@link FileData}
	 */
	private static FileData loadFile(String loc) {
		if(files.get(loc) == null) {
			FileData data = new FileData();
			data.resource = ResourceLoader.class.getResource(loc);
			data.stream = ResourceLoader.class.getResourceAsStream(loc);
			
			files.put(loc, data);
		}
		return files.get(loc);
	}
	
	/**
	 * {@link URL}  version of {@link #loadFile(String)}
	 * @param loc Location of file in jar
	 * @return {@link URL}
	 */
	public static URL loadResource(String loc) {
		return loadFile(loc).resource;
	}
	
	/**
	 * {@link InputStream} version of {@link #loadFile(String)}
	 * @param loc Location of file in jar
	 * @return  {@link InputStream}
	 */
	public static InputStream loadStream(String loc) {
		return loadFile(loc).stream;
	}
	
	/**
	 * {@link BufferedImage} version of {@link #loadFile(String)}
	 * @param loc Location of file in jar
	 * @return  {@link BufferedImage}
	 */
	public static BufferedImage loadImage(String loc) {
		try {
			return ImageIO.read(ResourceLoader.class.getResourceAsStream("/"+loc+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Just easy file management
	 * 
	 * @author Oikmo
	 */
	private static class FileData {
		/** URL version of file */
		public URL resource;
		/** InputStream version of file */
		public InputStream stream;
	}
}
