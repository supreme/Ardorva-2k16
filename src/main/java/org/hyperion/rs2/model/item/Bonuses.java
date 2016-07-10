package org.hyperion.rs2.model.item;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.player.Player;

/**
 * Handles a player's equipment bonuses.
 * @author Stephen Andrews
 */
public class Bonuses {
	
	/**
	 * Constants for each individual bonus.
	 */
	public static final int STAB = 0, SLASH = 1, CRUSH = 2, MAGIC = 3, RANGED = 4, STAB_DEFENSE = 5, SLASH_DEFENSE = 6,
			CRUSH_DEFENSE = 7, MAGIC_DEFENSE = 8, RANGED_DEFENSE = 9, STRENGTH = 10, PRAYER = 11;
	
	/**
	 * The bonus names.
	 */
	public static final String[] BONUS_NAMES = new String[] { "Stab", "Slash", "Crush", "Magic", "Ranged", 
		"Stab", "Slash", "Crush", "Magic", "Ranged", "Strength", "Prayer"};
	
	public static final int[] BONUS_STRING = {
		108, 109, 110, 111, 112, 114, 115, 116, 117, 118, 120, 121
	};
	
	/**
	 * The amount of bonuses.
	 */
	public static final int SIZE = 12;
	
	/**
	 * The player object.
	 */
	private Player player;
	
	/**
	 * The bonuses array.
	 */
	private int[] bonuses = new int[SIZE];
	
	/**
	 * Constructs a bonuses object.
	 * @param player The player owning the bonuses.
	 */
	public Bonuses(Player player) {
		this.player = player;
	}

	/**
	 * Sets the bonuses array with the correct bonuses for the player's
	 * current equipment.
	 */
	private void setBonuses() {
		/* Reset the bonuses array */
		for (int i = 0; i < bonuses.length; i++) {
			bonuses[i] = 0;
		}
		
		/* Set the new bonuses */
		for(Item item : player.getEquipment().toArray()) {
			if (item != null) {
				int[] bonuses = ItemDefinition.getBonuses(item.getId());
				
				/* If for some reason the bonuses for the item are null, continue the loop */
				if (bonuses == null) {
					continue;
				}
				
				/* Append each bonus in the bonuses array to the existing bonueses */
				for (int bonusIndex = 0; bonusIndex < bonuses.length; bonusIndex++) {
					this.bonuses[bonusIndex] += bonuses[bonusIndex];
				}
			}
		}
	}
	
	/**
	 * Sends the interface text for each bonus.
	 */
	private void updateInterface() {
		for (int index = 0; index < 12; index++) {
			String string = (BONUS_NAMES[index] + ": " + (bonuses[index] >= 0 ? "+" : "") + bonuses[index]);
			int child_id = 108 + index;
			if (child_id > 117) {
				child_id ++;
			}
			player.getActionSender().sendString(465, child_id, string);
		}
	}
	
	/**
	 * Refreshes the player's bonus array as well as updates the interface
	 * with the correct data.
	 */
	public void refresh() {
		//setBonuses();
		//updateInterface();
	}
	
	/**
	 * Gets theb bonuses array.
	 * @return The bonuses.
	 */
	public int[] getBonuses() {
		return bonuses;
	}
	
	/**
	 * Gets a bonus in the bonuses array.
	 * @param i The index of the bonus to get.
	 * @return The bonus.
	 */
	public int getBonus(int i) {
		return bonuses[i];
	}
}
