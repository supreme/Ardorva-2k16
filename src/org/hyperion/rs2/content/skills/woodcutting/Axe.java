package org.hyperion.rs2.content.skills.woodcutting;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents types of axes.
 * @author Graham Edgecombe
 */
public enum Axe {

		/**
		 * Dragon axe.
		 */
		DRAGON(6739, 61, 2846),
	
		/**
		 * Rune axe.
		 */
		RUNE(1359, 41, 867),
		
		/**
		 * Adamant axe.
		 */
		ADAMANT(1357, 31, 869),
		
		/**
		 * Mithril axe.
		 */
		MITHRIL(1355, 21, 871),
		
		/**
		 * Black axe.
		 */
		BLACK(1361, 6, 873),
		
		/**
		 * Steel axe.
		 */
		STEEL(1353, 6, 875),
		
		/**
		 * Iron axe.
		 */
		IRON(1349, 1, 877),
		
		/**
		 * Bronze axe.
		 */
		BRONZE(1351, 1, 879);
		
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
		private static Map<Short, Axe> axes = new HashMap<Short, Axe>();
		
		/**
		 * Gets a axe by an object id.
		 * @param object The object id.
		 * @return The axe, or <code>null</code> if the object is not a axe.
		 */
		public static Axe forId(int object) {
			return axes.get(object);
		}
		
		/**
		 * Populates the tree map.
		 */
		static {
			for(Axe axe : Axe.values()) {
				axes.put(axe.id, axe);
			}
		}
		
		/**
		 * Creates the axe.
		 * @param id The id.
		 * @param level The required level.
		 * @param animation The animation id.
		 */
		private Axe(int id, int level, int animation) {
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
