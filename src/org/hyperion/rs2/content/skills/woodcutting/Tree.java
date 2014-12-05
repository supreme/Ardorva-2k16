package org.hyperion.rs2.content.skills.woodcutting;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents types of trees.
 * @author Graham Edgecombe
 * @author Stephen Andrews
 */
public enum Tree {
	
		/**
		 * Normal tree.
		 */
		NORMAL(1511, 1342, 15, 1, 50, new short[] { 1276, 1277, 1278, 1279, 1280,
			1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
			1319, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035,
			3036, 3881, 3882, 3883, 5902, 5903, 5904 }),
		
		/**
		 * Willow tree.
		 */
		WILLOW(1519, 7399, 15, 30, 135, new short[] { 1308, 5551, 5552, 5553 }),
		
		/**
		 * Oak tree.
		 */
		OAK(1521, 1356, 15, 15, 75, new short[] { 1281, 3037 }),
		
		/**
		 * Magic tree.
		 */
		MAGIC(1513, 1342, 15, 75, 500, new short[] { 1292, 1306 }),
		
		/**
		 * Maple tree.
		 */
		MAPLE(1517, 1342, 100, 45, 200, new short[] { 1307, 4677 }),
		
		/**
		 * Mahogany tree.
		 */
		MAHOGANY(6332, 1342, 15, 50, 250, new short[] { 9034 }),
		
		/**
		 * Teak tree.
		 */
		TEAK(6333, 1342, 15, 35, 170, new short[] { 9036 }),
		
		/**
		 * Achey tree.
		 */
		ACHEY(2862, 1342, 15, 1, 50, new short[] { 2023 }),
		
		/**
		 * Yew tree.
		 */
		YEW(1515, 1342, 15, 60, 350, new short[] { 1309 });
		
		/**
		 * A map of object ids to trees.
		 */
		private static Map<Short, Tree> trees = new HashMap<Short, Tree>();
		
		/**
		 * Gets a tree by an object id.
		 * @param object The object id.
		 * @return The tree, or <code>null</code> if the object is not a tree.
		 */
		public static Tree forId(int object) {
			return trees.get((short)object);
		}
		
		/**
		 * Populates the tree map.
		 */
		static {
			for(Tree tree : Tree.values()) {
				for(short object : tree.objects) {
					trees.put(object, tree);
				}
			}
		}
		
		/**
		 * The object ids of this tree.
		 */
		private short[] objects;
		
		/**
		 * The minimum level to cut this tree down.
		 */
		private byte level;
		
		/**
		 * The id of the tree.
		 */
		private int treeId;
		
		/**
		 * The log of this tree.
		 */
		private short log;
		
		/**
		 * The stump of this tree.
		 */
		private short stump;
		
		/**
		 * The amount of time the stump appears after the tree is cut down.
		 */
		private short stumpTime;
		
		/**
		 * The experience.
		 */
		private double experience;
		
		/**
		 * Creates the tree.
		 * @param log The log id.
		 * @param level The required level.
		 * @param experience The experience per log.
		 * @param objects The object ids.
		 */
		private Tree(int log, int stump, int stumpTime, int level, double experience, short[] objects) {
			this.objects = objects;
			this.level = (byte) level;
			this.experience = experience;
			this.log = (short) log;
			this.stump = (short) stump;
			this.stumpTime = (short) stumpTime;
		}
		
		/**
		 * Gets the log id.
		 * @return The log id.
		 */
		public int getLogId() {
			return log;
		}
		
		/**
		 * Gets the stump id.
		 * @return The stump id.
		 */
		public int getStump() {
			return stump;
		}
		
		/**
		 * Gets the time a stump will appear.
		 * @return The stump time.
		 */
		public int getStumpTime() {
			return stumpTime;
		}
		
		/**
		 * Gets the object ids.
		 * @return The object ids.
		 */
		public short[] getObjectIds() {
			return objects;
		}
		
		/**
		 * Gets the required level.
		 * @return The required level.
		 */
		public int getRequiredLevel() {
			return level;
		}
		
		/**
		 * Gets the experience.
		 * @return The experience.
		 */
		public double getExperience() {
			return experience;
		}
		
		/**
		 * Gets the tree id.
		 * @return The tree id.
		 */
		public int getTreeId() {
			return treeId;
		}
		
		/**
		 * Sets the id of the tree being cut.
		 */
		public void setTreeId(int id) {
			treeId = id;
		}
}
