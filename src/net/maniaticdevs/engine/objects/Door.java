package net.maniaticdevs.engine.objects;

import net.maniaticdevs.engine.ResourceLoader;

/**
 * A barrier to the player (unless they have a key :P)
 * @author Oikmo
 */
public class Door extends OBJ {

	/** The key required to remove the door */
	private Key keyToUnlockWith;
	/** Should the key be removed from the inventory? */
	private boolean removesKey;
	
	/**
	 * Door constructor
	 * @param x X coordinate
	 * @param y X coordinate
	 * @param key Key required to open door
	 * @param removesKey {@link #removesKey}
	 */
	public Door(Key key, boolean removesKey, int x, int y) {
		this.removesKey = removesKey;
		name = "Door";
		image = ResourceLoader.loadImage("/textures/object/door");
		collision = true;
		position.set(x,y);
		this.keyToUnlockWith = key;
	}
	
	/**
	 * Returns the key required to remove the door
	 * @return {@link Key}
	 */
	public Key getRequiredKey() {	
		return keyToUnlockWith;
	}
	
	/**
	 * Returns a boolean for if the key should be removed from the players inventory
	 * @return {@link Boolean}
	 */
	public boolean removeKeyAfterUse() {
		return removesKey;
	}
}
