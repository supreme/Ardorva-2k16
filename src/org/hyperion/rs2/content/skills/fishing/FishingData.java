package org.hyperion.rs2.content.skills.fishing;

import java.util.HashMap;
import java.util.Map;

/**
 * The data pertaining to the fishing skill.
 * @author phil
 * @author Stephen
 */
public class FishingData {

	public enum Spots {
		
		LURE_BAIT(315, Fish.TROUT, Fish.SARDINES),
		
		NET_BAIT(320, Fish.SHRIMP, Fish.HERRING),
		
		LURE_BAIT2(311, Fish.SALMON, Fish.COD),
		
		CAGE_HARPOON(312, Fish.LOBSTER, Fish.TUNA),
		
		NET_HARPOON(313, Fish.MONKFISH, Fish.SHARK);
		
		/**
		 * The id of the fishing spot.
		 */
		private short id;
		
		/**
		 * The possible fish to catch.
		 */
		public Fish[] options = new Fish[2];
		
		/**
		 * Constructs a fishing spot.
		 * @param id The id of the fishing spot.
		 * @param a One possible fish to catch.
		 * @param b The other possible fish to catch.
		 */
		Spots(int id, Fish a, Fish b) {
			this.id = (short) id;
			options[0] = a;
			options[1] = b;
		}
		
		/**
		 * A map of fishing spots.
		 */
		private static Map<Short, Spots> spot = new HashMap<Short, Spots>();
		
		/**
		 * Populate the map.
		 */
		static {
			for(Spots s : Spots.values())
				spot.put(s.id, s);
		}
		
		/**
		 * Gets the map of fishing spots.
		 * @param id The id of the spot.
		 * @return The fishing spot.
		 */
		public static Spots getSpot(int id) {
			return spot.get(id);
		}
	}
	
	public enum Fish {
		
		SHRIMP(317, 1, 10, 0.50, new short[] {303}, 620),
		
		SARDINES(327, 5, 20, 0.46, new short[] {307, 313}, 622),
		
		HERRING(345, 10, 30, 0.43, new short[] {307, 313}, 622),
		
		ANCHOVIES(321, 15, 40, 0.41, new short[] {303}, 620),
		
		COD(341, 23, 45, 0.40, new short[] {305}, 620),
		
		SALMON(331, 30, 70, 0.35, new short[] {309, 314}, 622),
		
		TROUT(335, 20, 50, 0.44, new short[] {309, 314}, 622),
		
		LOBSTER(377, 40, 90, 0.33, new short[] {301}, 619),
		
		SHARK(383, 76, 110, 0.23, new short[] {311}, 618),
		
		SWORDFISH(371, 50, 100, 0.27, new short[] {311}, 618),
		
		MONKFISH(7944, 62, 120, 0.35, new short[] {303}, 620),
		
		TUNA(359, 55, 80, 0.35, new short[] {311}, 618),
		
		SEA_TURTLE(395, 79, 38, 0.20, new short[] {303}, 620),
		
		MANTA(389, 81, 46, 0.15, new short[] {303}, 620);
		
		/**
		 * The id of the fish being caught.
		 */
		private short fish;
		
		/**
		 * The required level to catch the fish.
		 */
		private byte level;
		
		/**
		 * The experience rewarded for a successful catch.
		 */
		private double xp;
		
		/**
		 * The chance of catching the fish.
		 */
		private double factor;
		
		/**
		 * The materials required to catch the fish.
		 */
		private short materials[];
		
		/**
		 * The emote for the fishing style.
		 */
		private short emote;

		/**
		 * Constructs a fish.
		 * @param id The id of the fish being caught.
		 * @param lv The required level to catch the fish.
		 * @param xp The experience rewarded for a successful catch.
		 * @param catchChance The chance of catching the fish.
		 * @param materials The materials required to catch the fish.
		 * @param anim The emote for the fishing style.
		 */
		Fish(int id, int lv, double xp, double catchChance, short[] materials, int anim) {
			fish = (short) id;
			level = (byte) lv;
			this.xp = xp;
			factor = catchChance;
			emote = (short) anim;
			this.materials = materials;
		}
		
		/**
		 * Gets the fish being caught.
		 * @return The fish
		 */
		public short getFish() {
			return fish;
		}
		
		/**
		 * Gets the level required to catch the fish.
		 * @return The required level.
		 */
		public byte getLevel() {
			return level;
		}
		
		/**
		 * Gets the xp for a successful catch.
		 * @return The xp.
		 */
		public double getXp() {
			return xp;
		}
		
		/**
		 * Gets the chance of catching the fish.
		 * @return 
		 */
		public double getFactor() {
			return factor;
		}
		
		/**
		 * Gets the materials required to catch the fish.
		 * @return The materials.
		 */
		public short[] getMaterials() {
			return materials;
		}
		
		/**
		 * Gets the emote of the fishing style.
		 * @return The emote.
		 */
		public short getEmote() {
			return emote;
		}
	}
}
