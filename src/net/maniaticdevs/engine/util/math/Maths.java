package net.maniaticdevs.engine.util.math;

/**
 * Maths things, for like big stuff or something idk
 * @author Oikmo
 */
public class Maths {

	/**
	 * Lerp, allows you to transition numbers<br><br>
	 * 
	 * {@code public static int lerp(int start, int end, int amount)} 
	 * 
	 * @param start starting number to interpolate from
	 * @param end end number to interpolate to
	 * @param amount amount to interpolate to and from
	 * 
	 * @return {@link Integer}
	 */
	public static int ilerp(int start, int end, float amount) {
		return (int)flerp(start, end, amount);
	}
	
	/**
	 * Lerp, allows you to transition numbers<br><br>
	 * 
	 * {@code public static int lerp(int start, int end, int amount)} 
	 * 
	 * @param start starting number to interpolate from
	 * @param end end number to interpolate to
	 * @param amount amount to interpolate to and from
	 * 
	 * @return {@link Float}
	 */
	public static float flerp(float start, float end, float amount) {
		return (start + (amount)* (end - start));
	}
	
}
