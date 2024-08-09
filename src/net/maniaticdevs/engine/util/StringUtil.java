package net.maniaticdevs.engine.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.maniaticdevs.engine.ResourceLoader;

public class StringUtil {
	
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
