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
 * Gets the combat animation for a player based on their weapon and attack stance.
 * @author Stephen
 */
public class CombatAnimations {

	/**
	 * The logger for the class.
	 */
	private static Logger logger = Logger.getLogger(CombatAnimations.class.getName());
	
	/**
	 * Gets the attacking animation for the specified entity.
	 * @param entity The entity engaged in combat.
	 * @return The attacking animation.
	 */
	public static int getAttackingAnimation(Entity entity) {
		/* Handle case for an NPC type entity */
		if (entity instanceof NPC) {
			NPC npc = (NPC) entity;
			return npc.getDefinition().getAttackAnimation();
		}
		
		/* Handle case for a player type entity */
		Player player = (Player) entity;
		Stance stance = player.getPlayerVariables().getCombatStance();
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int animation = -1;
		
		/* In the case of a player not wearing a weapon */
		if (weapon == null) {
			switch(stance) {
				case ACCURATE:
				case DEFENSIVE:
					animation = 422;
					break;
				case AGGRESSIVE:
					animation = 423;
					break;
				default:
					animation = -1;
					break;
			}
			
			return animation;
		}
				
		/* If weapon isn't null, get the proper animation */
		ItemDefinition definition = ItemDefinition.forId(weapon.getId());
		switch(stance) {
			case ACCURATE:
				animation = definition.getWeaponDefinition().getAttackStyles().getAccurateAnimation();
				break;
			case AGGRESSIVE:
				animation = definition.getWeaponDefinition().getAttackStyles().getAggressiveAnimation();
				break;
			case DEFENSIVE:
				animation = definition.getWeaponDefinition().getAttackStyles().getDefensiveAnimation();
				break;
			case CONTROLLED:
				animation = definition.getWeaponDefinition().getAttackStyles().getControlledAnimation();
				break;
		}
		
		if (Constants.DEV_MODE) {
			logger.info(player.getName() + " has a combat animation of " + animation + ", with attack stance " + stance.toString());
		}
		
		return animation;
	}
	
	/**
	 * Gets the defensive animation for the specified entity.
	 * @param entity The entity engaged in combat.
	 * @return The defensive animation.
	 */
	public static int getDefensiveAnimation(Entity entity) {
		/* Handle case for an NPC type entity */
		if (entity instanceof NPC) {
			NPC npc = (NPC) entity;
			return npc.getDefinition().getDefenceAnimation();
		}
		
		/* Handle case for a player type entity */
		Player player = (Player) entity;
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int animation = 424;
		
		/* In the case of a player not wearing a weapon */
		if (weapon == null) { //TODO: Shield
			return animation;
		}
				
		/* If weapon isn't null, get the proper animation */
		ItemDefinition definition = ItemDefinition.forId(weapon.getId());
		animation = definition.getWeaponDefinition().getBlockAnimation();
		
		return animation;
	}
}
