package org.hyperion.rs2.content.combat.logic;

import org.hyperion.rs2.content.combat.item.ItemSets;
import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.content.combat.util.CombatData.Stance;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.item.Bonuses;
import org.hyperion.rs2.model.player.Player;

/**
 * Contains max hit forumlas for melee, range, and mage. I found a post on the RuneScape wiki
 * containing an article player's put together attempting to recreate RuneScape's max hit
 * formula. All the data can be found here - http://services.runescape.com/m=rswiki/en/Maximum_Hit_Formula.
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
		ItemSets itemSet = ItemSets.NONE; //ItemSets.get(player);
		
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
		
		//TODO: Calculate other bonuses, like special, full dharok.
		switch(type) {
			case MELEE:
				if (itemSet == ItemSets.DHAROK) {
					System.out.println("Full dharok set bonus");
					double currentHealth = player.getSkills().getLevel(Skills.HITPOINTS);
					double maxHealth = player.getSkills().getLevelForExperience(Skills.HITPOINTS);
					double multiplier = 1 - (currentHealth / maxHealth); //Percentage of health missing
					multiplier += 1; //Since the other value is a decimal, we need to multiply by 100% + the missing health %
					maxHit *= multiplier;
				}
				break;
		}
		
		return maxHit/10; //Divide by 10 because this formula is for x10 damage
	}
}
