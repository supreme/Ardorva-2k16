package org.hyperion.rs2.content.combat.util;

import java.util.ArrayList;

import org.hyperion.rs2.LivingClasses;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.player.Player;

/**
 * Contains various information pertaining to combat.
 * @author Stephen
 */
public class CombatData {

	/**
	 * Represents the 3 different attack types a player can perform.
	 */
	public static enum AttackType {
		
		/**
		 * A melee-based attacks.
		 */
		MELEE,
		
		/**
		 * A projectile-based attacks.
		 */
		RANGED,
		
		/**
		 * A magic-based attacks.
		 */
		MAGIC;
	}
	
	/**
	 * Represents the stance the player is using.
	 */
	public static enum Stance {
		
		/**
		 * The accurate stance.
		 */
		ACCURATE(3),
		
		/**
		 * The aggressive stance.
		 */
		AGGRESSIVE(0),
		
		/**
		 * The defensive stance.
		 */
		DEFENSIVE(0),
		
		/**
		 * The controlled stance
		 */
		CONTROLLED(1);
		
		/**
		 * The accuracy bonus of the stance.
		 */
		private int accuracyIncrease;
		
		/**
		 * Constructs a combat stance.
		 * @param accuracyIncrease The accuracy bonus of the stance.
		 */
		Stance(int accuracyIncrease) {
			this.accuracyIncrease = accuracyIncrease;
		}
		
		/**
		 * Gets the accuracy bonus of the stance.
		 * @return The accuracy bonus.
		 */
		public int getAccuracyIncrease() {
			return accuracyIncrease;
		}
	}
	
	/**
	 * Represents the style of combat the player is using.
	 */
	public static enum Style {
		
		/**
		 * The stabbing style.
		 */
		STAB,
		
		/**
		 * The slashing style.
		 */
		SLASH,
		
		/**
		 * The crushing style.
		 */
		CRUSH,
		
		/**
		 * The magic style.
		 */
		MAGIC,
		
		/**
		 * The ranging accurate style.
		 */
		RANGE_ACCURATE,
		
		/**
		 * The ranging rapid style.
		 */
		RANGE_RAPID,
		
		/**
		 * The ranging defensive style.
		 */
		RANGE_DEFENSIVE;
	}
	
	/**
	 * The entity's bonuses.
	 */
	private static int[] bonuses = new int[13];
	
	/**
	 * Sets one of the mob's bonuses.
	 * @param index The bonus index.
	 * @param amount The bonus to set.
	 */
	public static void setBonus(int index, int amount) {
		bonuses[index] = amount;
	}

	/**
	 * Sets one of the mob's bonuses.
	 * @param index The bonus index.
	 * @param amount The bonus to set.
	 */
	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	/**
	 * Resets the mob's bonuses.
	 */
	public static void resetBonuses() {
		bonuses = new int[13];
	}

	/**
	 * Gets the mob's bonuses.
	 * @return The mob's bonuses.
	 */
	public int[] getBonuses() {
		return bonuses;
	}

	/**
	 * Gets a bonus by its index.
	 * @param index The bonus index.
	 * @return The bonus.
	 */
	public static int getBonus(int index) {
		return bonuses[index];
	}
	
	/**
	 * Calculates the bonuses.
	 */
	public static void calculateBonuses(Player player) {
		resetBonuses();
		for (Item item : player.getEquipment().toArray()) {
			if (item != null && LivingClasses.definitionLoader.getBonuses() != null) {
				for (int i = 0; i < 13; i++) {
					setBonus(i, getBonus(i) + new ArrayList<Integer>(LivingClasses.definitionLoader.getBonuses().keySet()).get(i));
				}
			}
		}
	}
	
}
