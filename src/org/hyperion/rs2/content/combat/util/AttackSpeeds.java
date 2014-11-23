package org.hyperion.rs2.content.combat.util;

import java.util.logging.Logger;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.combat.util.CombatData.Stance;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * Gets the attack speeds for different weapons.
 * @author Stephen
 */
public class AttackSpeeds {

	/**
	 * The logger for the class.
	 */
	private static Logger logger = Logger.getLogger(AttackSpeeds.class.getName());
	
	/**
	 * Gets the attack speed for the specified entity.
	 * @param entity The entity attacking.
	 * @return The attack speed.
	 */
	public static int getAttackSpeed(Entity entity) {
		/* Handle case for an NPC type entity */
		if (entity instanceof NPC) {
			NPC npc = (NPC) entity;
			return npc.getDefinition().getAttackSpeed();
		}
		
		/* Handle case for a player type entity */
		Player player = (Player) entity;
		int attackSpeed = 4;
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		
		/* In the case of a player not wearing a weapon */
		if (weapon == null) {
			return attackSpeed;
		}
		
		/* If weapon isn't null, get the proper attack speed */
		ItemDefinition definition = ItemDefinition.forId(weapon.getId());
		Stance stance = player.getPlayerVariables().getCombatStance();
		switch(stance) {
			case ACCURATE:
				attackSpeed = definition.getWeaponDefinition().getAttackStyles().getAccurateSpeed();
				break;
			case AGGRESSIVE:
				attackSpeed = definition.getWeaponDefinition().getAttackStyles().getAggressiveSpeed();
				break;
			case DEFENSIVE:
				attackSpeed = definition.getWeaponDefinition().getAttackStyles().getDefensiveSpeed();
				break;
			case CONTROLLED:
				attackSpeed = definition.getWeaponDefinition().getAttackStyles().getControlledSpeed();
				break;
		}
		
		if (Constants.DEV_MODE) {
			logger.info(player.getName() + " has an attack speed of " + attackSpeed + ", with weapon " + definition.getName());
		}
		
		return attackSpeed * 600;
	}
}
