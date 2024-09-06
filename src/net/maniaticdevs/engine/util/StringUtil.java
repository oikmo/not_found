package net.maniaticdevs.engine.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.maniaticdevs.engine.ResourceLoader;

/**
 * Utility class for {@link String}
 * @author Oikmo
 */
public class StringUtil {
	
	/**
	 * Loads text file into {@link List}
	 * @param fileName File to load
	 * @return {@link List}&#60;{@link String}&#62;
	 * @throws IOException Exception for Input/Output
	 */
	public static List<String> loadTextFile(String fileName) throws IOException {
		Scanner s = new Scanner(ResourceLoader.class.getResourceAsStream("/"+fileName));
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNext()){
		    list.add(s.nextLine());
		}
		s.close();
		return list;
	}
	
}
