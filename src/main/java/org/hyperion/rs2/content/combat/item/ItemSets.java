package org.hyperion.rs2.content.combat.item;

import org.hyperion.rs2.model.player.Player;

/**
 * Items sets are a collection of items which act as a whole. When a 
 * player has a full set, certain bonuses are granted based on the set.
 * @author Stephen Andrews
 */
public enum ItemSets {

	/**
	 * Full dharok set.
	 */
	DHAROK(new int[] {4716, 4718, 4720, 4722}),
	
	/**
	 * Void knight melee set.
	 */
	VOID_MELEE(new int[] {8839, 8840, 8842, 11665}),
	
	/**
	 * Void knight range set.
	 */
	VOID_RANGE(new int[] {1, 1, 1, 1}),
	
	/**
	 * Non-existing set
	 */
	NONE(null);
	
	/**
	 * All of the items that make up the set.
	 */
	private int[] setItems;
	
	/**
	 * Constructs an item set.
	 * @param setItems The items that make up the set.
	 */
	ItemSets(int[] setItems) {
		this.setItems = setItems;
	}
	
	/**
	 * Gets all the items in the set.
	 * @return The items in the set.
	 */
	public int[] getSetItems() {
		return setItems;
	}
	
	/**
	 * Determines whether or not a player has an item set equipped, 
	 * and if so returns the set.
	 * @param player The player to check.
	 */
	public static ItemSets get(Player player) {
		for (ItemSets set : ItemSets.values()) {
			if (set.getSetItems() == null) {
				continue;
			}
			
			for (int id : set.getSetItems()) {
				if (!player.getEquipment().contains(id)) {
					System.out.println("Looking at set " + set.toString() + " item " + id);
					break;
				}
				
				return set;
			}
		}
		
		return NONE;
	}
}
