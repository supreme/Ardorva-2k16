package org.hyperion.rs2.content.skills.thieving;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.model.Item;

/**
 * Contains information pertaining to the thieving skill.
 * @author Stephen
 */
public class ThievingData {

	public enum Mob {
		
		MAN(1, 1, 83, new Item[] { new Item(995, 5) }, 2);
		
		/**
		 * The id of the NPC being stolen from.
		 */
		private int npc;
		
		/**
		 * The required level to steal.
		 */
		private int level;
		
		/**
		 * The xp rewarded on a successful attempt.
		 */
		private int xp;
		
		/**
		 * The rewards for a successful attempt.
		 */
		private Item[] rewards;
		
		/**
		 * The hit damage.
		 */
		private int hit;
		
		Mob(int npc, int level, int xp, Item[] rewards, int hit) {
			this.npc = npc;
			this.level = level;
			this.xp = xp;
			this.rewards = rewards;
			this.hit = hit;
		}
		
		/**
		 * A map of thievable NPCs.
		 * @return The thievable NPCs.
		 */
		private static Map<Integer, Mob> data = new HashMap<Integer, Mob>();
		
		/**
		 * Populate the map.
		 */
		static {
			for(Mob npc : Mob.values()) {
				data.put(npc.getNpc(), npc);
			}
		}
		
		/**
		 * Gets a mob for a specified NPC id.
		 * @param id The id of the NPC.
		 * @return The mob.
		 */
		public static Mob forId(int id) {
			return data.get(id);
		}
		
		/**
		 * Gets the NPC id of the mob.
		 * @return The NPC id.
		 */
		public int getNpc() {
			return npc;
		}
		
		/**
		 * Gets the required level to steal from the mob.
		 * @return The required level.
		 */
		public int getLevel() {
			return level;
		}
		
		/**
		 * Gets the xp rewarded for a successful theft.
		 * @return The xp.
		 */
		public int getXp() {
			return xp;
		}
		
		/**
		 * Gets the rewards.
		 * @return The rewards, or -1 if there are none.
		 */
		public Item[] getRewards() {
			return rewards;
		}
		
		/**
		 * Gets the amount a player is hit for when being caught.
		 * @return The amount a player is hit for when being caught.
		 */
		public int getHit() {
			return hit;
		}
	}
	
	public enum Stall {
		
		BAKER(2561, 5, 16, new Item[] { new Item(4151, 1) }, 5);
		
		private int id;
		
		private int level;
		
		private int xp;
		
		private Item[] rewards;
		
		private int delay;
		
		Stall(int id, int level, int xp, Item[] rewards, int delay) {
			this.id = id;
			this.level = level;
			this.xp = xp;
			this.rewards = rewards;
			this.delay = delay;
		}
		
		private static Map<Integer, Stall> stalls = new HashMap<Integer, Stall>();
		
		static {
			for (Stall stall : Stall.values()) {
				stalls.put(stall.getId(), stall);
			}
		}
		
		public static Stall forId(int id) {
			return stalls.get(id);
		}
		
		public int getId() {
			return id;
		}
		
		public int getLevel() {
			return level;
		}
		
		public int getXp() {
			return xp;
		}
		
		public Item[] getRewards() {
			return rewards;
		}
		
		public int getDelay() {
			return delay;
		}
	}
}
