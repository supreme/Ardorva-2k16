package org.hyperion.rs2.content.combat.logic;

import java.util.HashMap;

import org.hyperion.rs2.content.combat.item.ItemSets;
import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.content.combat.util.CombatData.Stance;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.definitions.ItemDefinition.EquipmentDefinition;
import org.hyperion.rs2.model.item.Bonuses;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * An attempt to recreate realisitic combat resembling RuneScape. Formula's for max hits,
 * accuracy, etc can be found in this class.
 * 
 * Forumlas for melee, range, and mage -
 * I found a post on the RuneScape wiki containing an article player's put together attempting 
 * to recreate RuneScape's max hit formula. All the data for max hit formulas can be found 
 * here - http://services.runescape.com/m=rswiki/en/Maximum_Hit_Formula.
 * 
 * For the accuracy formulas, I found a fairly extensive explanation of how accuracy works.
 * All of the information used can be found here - http://runescape.wikia.com/wiki/Hit_chance.
 * @author Stephen Andrews
 */
public class CombatFormulas {

	/**
	 * According to the article, both melee and range max hits are
	 * calculating the same way. This method calculates a melee or range
	 * max hit based on the entity and the combat type.
	 * @param player The player we are calculating the max hit for.
	 * @param type The type of combat.
	 */
	public static int calculateMeleeRangeMaxHit(Player player, AttackType type) {
		int maxHit;
		int strengthLevel = player.getSkills().getLevel(Skills.STRENGTH);
		int rangeLevel = player.getSkills().getLevel(Skills.RANGE);
		ItemSets itemSet = player.getCombatUtility().getItemSet();
		
		/* Calculate the effective level first depending on the combat type */
		int effectiveLevel = type == AttackType.MELEE ? strengthLevel : rangeLevel;
		//TODO: Possibly, if we're going to have curses take into account
		//leech strength and leech range
		
		/* Prayers and curses section to modify the max hit */
		int prayerBonus = 1;
		//TODO: Modify based on active prayers
		
		/* Reassign the effective level after combinining the previous effective level
		 * and prayer bonuses.
		 */
		effectiveLevel = (effectiveLevel * prayerBonus) + 8;
		effectiveLevel = Math.round(effectiveLevel);
		
		/* Now we take into consideration combat style bonuses */
		Stance stance = player.getCombatState().getCombatStance();
		if (type == AttackType.MELEE) {
			switch(stance) {
				case ACCURATE:
				case CONTROLLED:
					effectiveLevel += stance.getAccuracyIncrease();
					break;
				default:
					//Remove warning
					break;
			}
		} else if (type == AttackType.RANGED) {
			if (stance == Stance.ACCURATE) {
				effectiveLevel += stance.getAccuracyIncrease();
			}
		}
		
		/* Void knight set bonus modifies effective strength/range */
		switch (type) {
			case MELEE:
				if (itemSet == ItemSets.VOID_MELEE) {
					effectiveLevel = (int) Math.round(effectiveLevel * 1.1);
					player.getActionSender().sendMessage("You gain the void melee set bonus");
				}
				break;
			case RANGED:
				if (itemSet == ItemSets.VOID_RANGE) {
					effectiveLevel = (int) Math.round(effectiveLevel * 1.1);
					effectiveLevel = (int) Math.round(effectiveLevel * 1.1);
				}
				break;
			default:
				//Remove warning
				break;
		}
		
		/* Now all the effective bonus is done, so we calculate base damage */
		int strengthBonus = player.getBonuses().getBonus(Bonuses.STRENGTH);
		int rangeBonus = player.getBonuses().getBonus(Bonuses.RANGED);
		int typeBonus = type == AttackType.MELEE ? strengthBonus : rangeBonus;
		maxHit = 5 + effectiveLevel * (typeBonus + 64) / 64;
		maxHit = Math.round(maxHit);
		
		//TODO: Calculate other bonuses, like special, full dharok
		switch(type) {
			case MELEE:
				if (itemSet == ItemSets.DHAROK) {
					player.getActionSender().sendMessage("Full dharok set bonus");
					double currentHealth = player.getSkills().getLevel(Skills.HITPOINTS);
					double maxHealth = player.getSkills().getLevelForExperience(Skills.HITPOINTS);
					double multiplier = 1 - (currentHealth / maxHealth); //Percentage of health missing
					multiplier += 1; //Since the other value is a decimal, we need to multiply by 100% + the missing health %
					maxHit *= multiplier;
				}
				break;
			default:
				//Remove warning
				break;
		}
		
		return maxHit/10; //Divide by 10 because this formula is for x10 damage
	}
	
	/**
	 * Runs the specified level through the accuracy function.
	 * @param level The level to run.
	 * @return The accuracy function output.
	 */
	private static double runAccuracyFunction(int level) {
		return (.0008 * Math.pow(level, 3)) + (4 * level) + 40;
	}
	
	/**
	 * 
	 */
	private static double calculateEntityAccuracy(Entity entity, AttackType type) {
		if (entity instanceof NPC) {
			//TODO
			return -1;
		} else {
			Player player = (Player) entity;
			double accuracy = 0;
			int skillLevel = player.getSkills().getLevel(type.getSkillNumber());
			Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
			int requiredWeaponLevel = 1; //Required level to wield the weapon the player has equipped
			
			if (weapon != null) { //If player doesn't have a weapon move on, otherwise get the requirement
				requiredWeaponLevel = weapon.getDefinition().getEquipmentDefinition().getRequirements()[type.getSkillNumber()];
			}
			
			accuracy = runAccuracyFunction(skillLevel) + (2.5 * runAccuracyFunction(requiredWeaponLevel));
			return accuracy;
		}
	}
	
	
	/**
	 * Calculates a player's offensive accuracy.
	 * @param player The player to calcuate the accuracy for.
	 * @param type The player's attack type.
	 * @return The accuracy.
	 */
	public static double calculateOffensiveAccuracy(Player player, AttackType type) {
		double skillAccuracyBonus;
		double weaponAccuracy;
		int skillLevel = player.getSkills().getLevel(type.getSkillNumber());
		
		//Calculate the accuracy bonus we receive from our skill level
		skillAccuracyBonus = (Math.pow(skillLevel, 3) / 1250) + (4 * skillLevel) + 40;
		
		//Calculate the accuracy of the player's weapon
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int requiredWeaponLevel = 1; //Required level to wield the weapon the player has equipped
		
		if (weapon != null) { //If player doesn't have a weapon move on, otherwise get the requirement
			requiredWeaponLevel = weapon.getDefinition().getEquipmentDefinition().getRequirements()[type.getSkillNumber()];
		}
		
		//Calculate the weapon accuracy
		weaponAccuracy = 2.5 * ((Math.pow(requiredWeaponLevel, 3) + (4 * requiredWeaponLevel) + 40));
		
		//Perform the final calculations
		double finalAccuracy = skillAccuracyBonus + weaponAccuracy;
		
		return finalAccuracy;
	}
	
	/**
	 * Calculates a player's defensive bonus.
	 * @param player The player to calculate the defensive bonus for.
	 * @return The defensive bonus.
	 */
	public static double calculateDefensiveBonus(Entity entity) {
		double totalBonus = 0;
		if (entity instanceof Player) {
			Player player = (Player) entity;
			HashMap<Integer, Item> armor = new HashMap<Integer, Item>();
			
			/* Loop through player's equipment an add it to armor hashmap */
			for (int i = 0; i < player.getEquipment().toArray().length; i++) {
				if (player.getEquipment().toArray()[i] == null) {
					continue;
				}
				
				armor.put(i, player.getEquipment().toArray()[i]);
			}
			
			/* Equate the bonus for each piece of armor */
			for (int key : armor.keySet()) {
				EquipmentDefinition def = armor.get(key).getDefinition().getEquipmentDefinition();
				totalBonus += runDefensiveFormula(def.getRequirements()[Skills.DEFENCE], key);
			}
		} else {
			NPC npc = (NPC) entity;
			totalBonus = runDefensiveFormula(npc.getDefinition().getDefenceMelee(), -1);
		}

		return totalBonus;
	}
	
	/**
	 * Calculates the defensive bonus of a piece of equipment.
	 * @param tier The tier of the equipment.
	 * @param type The type of the equipment (helm, body, etc).
	 * @return The defensive bonus.
	 */
	private static double runDefensiveFormula(int tier, int type) {
		double bonus;
		
		bonus = 2.5 * ((Math.pow(tier, 3)/1250 + (4*tier) + 40));
		switch(type) {
		case Equipment.SLOT_HELM:
			bonus = bonus * 0.2;
			break;
		case Equipment.SLOT_CHEST:
			bonus = bonus * 0.23;
			break;
		case Equipment.SLOT_BOTTOMS:
			bonus = bonus * 0.22;
			break;
		case Equipment.SLOT_SHIELD:
			bonus = bonus * 0.2;
			break;
		case Equipment.SLOT_GLOVES:
			bonus = bonus * 0.05;
			break;
		case Equipment.SLOT_BOOTS:
			bonus = bonus * 0.05;
			break;
		case Equipment.SLOT_CAPE:
			bonus = bonus * 0.03;
			break;
		case Equipment.SLOT_RING:
			bonus = bonus * 0.02;
			break;
		default:
			//Nothing, assume NPC
			break;
		}
		
		return bonus;
	}
}
