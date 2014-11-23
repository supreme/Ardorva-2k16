package org.hyperion.rs2.content.combat.util;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.player.Player;

/**
 * Serves to provide attack distances for the various types of combat.
 * @author Stephen Andrews
 */
public class AttackDistance {

	/**
	 * Gets the appropriate melee attack distance for the weapon in hand.
	 * @param entity The entity to get an attack distance for.
	 * @return The attack distance.
	 */
	public static int getMeleeDistancec(Entity entity) {
		int attackDistance = 1; //By default usually a 1 tile difference suffices
		if (entity instanceof Player) {
			Player player = (Player) entity;
			Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
			if (weapon != null) {
				ItemDefinition def = weapon.getDefinition();
				
				/* Halberds are the only melee weapon that have an attack distance of 2 */
				if (def.getName().contains("halberd")) {
					attackDistance = 2;
				}
			}
			
			return attackDistance;
		} else {
			//We can assume this is an NPC since there are no other entity types
			return 1; //Not sure what do to for this yet
		}
	}
	
}
