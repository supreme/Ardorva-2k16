package org.hyperion.rs2.content.skills.firemaking;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds various information pertaining to the firemaking skill.
 * Data has been collected from <url>http://runescape.wikia.com/wiki/Firemaking</url>
 * @author Stephen
 */
public class LogData {

	public enum Log {
		
		/**
		 * Normal logs.
		 */
		NORMAL(1511, 1, 40),
		
		/**
		 * Oak logs.
		 */
		OAK(1521, 15, 60),
		
		/**
		 * Willow logs.
		 */
		WILLOW(1519, 30, 90),
		
		/**
		 * Maple logs.
		 */
		MAPLE(1517, 45, 141),
		
		/**
		 * Yew logs.
		 */
		YEW(1515, 60, 203),
		
		/**
		 * Magic logs.
		 */
		MAGIC(1513, 75, 304);
		
		/**
		 * The id of the log.
		 */
		private int id;
		
		/**
		 * The required level to burn the log.
		 */
		private int level;
		
		/**
		 * The xp rewarded for a successful light.
		 */
		private int xp;
		
		/**
		 * Constructs a log object.
		 * @param id The id of the log.
		 * @param level The required level to burn the log.
		 * @param xp The xp rewarded for a successful light.
		 */
		Log(int id, int level, int xp) {
			this.id = id;
			this.level = level;
			this.xp = xp;
		}
		
		/**
		 * Map of log data.
		 */
		private static Map<Integer, Log> logs = new HashMap<Integer, Log>();
		
		/**
		 * Populate the map.
		 */
		static {
			for(Log log : Log.values()) {
				logs.put(log.id, log);
			}
		}
		
		/**
		 * Gets the log object for the specified id.
		 * @return The log.
		 */
		public static Log forId(int id) {
			return logs.get(id);
		}
		
		/**
		 * Gets the log's id.
		 * @return The id.
		 */
		public int getId() {
			return id;
		}
		
		/**
		 * Gets the log's required level.
		 * @return The level.
		 */
		public int getLevel() {
			return level;
		}
		
		/**
		 * Gets the log's xp.
		 * @return The xp.
		 */
		public int getXp() {
			return xp;
		}
	}
}
