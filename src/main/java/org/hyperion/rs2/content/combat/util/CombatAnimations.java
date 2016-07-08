package org.hyperion.rs2.content.combat.util;

import java.util.logging.Logger;

import org.hyperion.rs2.content.combat.util.CombatData.Stance;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.definitions.ItemDefinition.ShieldDefinition;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * Gets the combat animations for a player based on their weapon/shield and attack stance.
 * @author Stephen Andrews
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
		Item shield = player.getEquipment().get(Equipment.SLOT_SHIELD);
		int animation = 424;
		
		/* In the case of a player not wearing a shield */
		if (shield == null) {
			return animation;
		}
				
		/* If shield isn't null, get the proper animation */
		ShieldDefinition definition = ItemDefinition.forId(shield.getId()).getEquipmentDefinition().getShieldDefinition();
		if (definition != null) { //Sometimes there's a null for the shield definition even though it's a shield
			animation = definition.getBlockAnimation();
		}
		
		return animation;
	}
}
