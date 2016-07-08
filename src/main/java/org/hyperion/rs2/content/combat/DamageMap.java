package org.hyperion.rs2.content.combat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hyperion.rs2.model.Entity;

/**
 * Maintains the total amount of damage done to an NPC and which entities
 * provided the damage.
 * @author Stephen
 */
public class DamageMap {
	
	/**
	 * A map of the total damage dealt for each entity that participated in the assault.
	 */
	private Map<Entity, Integer> totalDamage;
	
	/**
	 * Constructs a damage map.
	 */
	public DamageMap() {
		totalDamage = new LinkedHashMap<Entity, Integer>();
	}
	
	/**
	 * Adds damage to the map.
	 * @param source The entity providing the damage.
	 * @param damage The damage dealt.
	 */
	public void appendDamage(Entity source, int damage) {
		/* Check to see if there is an existing entry for the source of damage */
		if (totalDamage.get(source) == null) {
			totalDamage.put(source, damage);
		} else {	
		/* Overwrite existing entries */
			int damageDealt = totalDamage.get(source);
			damageDealt += damage;
			totalDamage.put(source, damageDealt);
		}
	}
	
	/**
	 * Removes the invalid entries in the damage map. The invalid entries are
	 * all those who did less than the max amount of damage.
	 */
	private void removeInvalidEntries() {
		Entity winningAttacker = null;
		for (Entity attacker : totalDamage.keySet()) {
			if (winningAttacker == null) {
				winningAttacker = attacker;
			}
			
			if (totalDamage.get(attacker) > totalDamage.get(winningAttacker)) {
				winningAttacker = attacker;
				totalDamage.remove(winningAttacker);
			}
		}
	}
	
	/**
	 * Gets the entity who dealt the most damage.
	 * @return The entity.
	 */
	public Entity getWinner() {
		removeInvalidEntries();
		return (new ArrayList<Entity>(totalDamage.keySet()).get(0));
	}
}

