package org.hyperion.rs2.content.skills.mining;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents types of axes.
 * @author Graham Edgecombe
 */
public enum Pickaxe {
	
	/**
	 * Rune pickaxe.
	 */
	RUNE(1275, 41, 624),
	
	/**
	 * Adamant pickaxe.
	 */
	ADAMANT(1271, 31, 628),
	
	/**
	 * Mithril pickaxe.
	 */
	MITHRIL(1273, 21, 629),
	
	/**
	 * Steel pickaxe.
	 */
	STEEL(1269, 11, 627),
	
	/**
	 * Iron pickaxe.
	 */
	IRON(1267, 5, 626),
	
	/**
	 * Bronze pickaxe.
	 */
	BRONZE(1265, 1, 625);
	
	/**
	 * The id.
	 */
	private short id;
	
	/**
	 * The level.
	 */
	private byte level;
	
	/**
	 * The animation.
	 */
	private short animation;
	
	/**
	 * A map of object ids to axes.
	 */
	private static Map<Short, Pickaxe> pickaxes = new HashMap<Short, Pickaxe>();
	
	/**
	 * Gets a axe by an object id.
	 * @param object The object id.
	 * @return The axe, or <code>null</code> if the object is not a axe.
	 */
	public static Pickaxe forId(int object) {
		return pickaxes.get(object);
	}
	
	/**
	 * Populates the tree map.
	 */
	static {
		for(Pickaxe pickaxe : Pickaxe.values()) {
			pickaxes.put(pickaxe.id, pickaxe);
		}
	}
	
	/**
	 * Creates the axe.
	 * @param id The id.
	 * @param level The required level.
	 * @param animation The animation id.
	 */
	private Pickaxe(int id, int level, int animation) {
		this.id = (short) id;
		this.level = (byte) level;
		this.animation = (short) animation;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the required level.
	 * @return The required level.
	 */
	public int getRequiredLevel() {
		return level;
	}
	
	/**
	 * Gets the animation id.
	 * @return The animation id.
	 */
	public int getAnimation() {
		return animation;
	}
}
