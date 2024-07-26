package net.maniaticdevs.engine.util.os;

/**
 * Allows identification of the users OS.
 * @author Oikmo
 */
public class EnumOSMappingHelper {
	
	/** Easy storage of the OS */
	public static final int os[];

	static {
		os = new int[EnumOS.values().length];
		try {
			os[EnumOS.linux.ordinal()] = 1;
		} catch(NoSuchFieldError e) {}
		
		try {
			os[EnumOS.solaris.ordinal()] = 2;
		} catch(NoSuchFieldError e) {}
		
		try {
			os[EnumOS.windows.ordinal()] = 3;
		} catch(NoSuchFieldError e) {}
		
		try {
			os[EnumOS.macos.ordinal()] = 4;
		} catch(NoSuchFieldError e) {}
	}
}