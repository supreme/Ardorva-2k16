package org.hyperion.rs2.content.skills.prayer;

import org.hyperion.rs2.model.Skills;

/**
 * Enumeration of all the prayer spells that can be activated.
 * @author Stephen Andrews
 */
public enum PrayerSpell {
	
	THICK_SKIN(5, new int[] {Skills.DEFENCE}),
	BURST_OF_STRENGTH(7, new int[] {Skills.STRENGTH}),
	CLARITY_OF_THOUGHT(9, new int[] {Skills.ATTACK}),
	SHARP_EYE(11, new int[] {Skills.RANGE}),
	MYSTIC_WILL(13, new int[] {Skills.MAGIC}),
	ROCK_SKIN(15, new int[] {Skills.DEFENCE}),
	SUPERHUMAN_STRENGTH(17, new int[] {Skills.STRENGTH}),
	IMPROVED_REFLEXES(19, new int[] {Skills.ATTACK}),
	RAPID_RESTORE(21, new int[] {PrayerManager.UNIQUE_SPELL}),
	RAPID_HEAL(23, new int[] {Skills.HITPOINTS}),
	PROTECT_ITEM(25, new int[] {PrayerManager.UNIQUE_SPELL}),
	HAWK_EYE(27, new int[] {Skills.RANGE}),
	MYSTIC_LORE(29, new int[] {Skills.MAGIC}),
	STEEL_SKIN(31, new int[] {Skills.DEFENCE}),
	ULTIMATE_STRENGTH(33, new int[] {Skills.STRENGTH}),
	INCREDIBLE_REFLEXES(35, new int[] {Skills.ATTACK}),
	PROTECT_FROM_MAGIC(37, new int[] {PrayerManager.PROTECTION_SPELL}),
	PROTECT_FROM_MISSILES(39, new int[] {PrayerManager.PROTECTION_SPELL}),
	PROTECT_FROM_MELEE(41, new int[] {PrayerManager.PROTECTION_SPELL}),
	EAGLE_EYE(43, new int[] {Skills.RANGE}),
	MYSTIC_MIGHT(45, new int[] {Skills.MAGIC}),
	RETRIBUTION(47, new int[] {PrayerManager.UNIQUE_SPELL}),
	REDEMPTION(49, new int[] {Skills.HITPOINTS}),
	SMITE(51, new int[] {PrayerManager.UNIQUE_SPELL}),
	CHIVALRY(53, new int[] {Skills.DEFENCE, Skills.STRENGTH, Skills.ATTACK}),
	PIETY(55, new int[] {Skills.DEFENCE, Skills.STRENGTH, Skills.ATTACK}),
	NON_EXISITING_SPELL(-1, null);
	
	/**
	 * The head icon indicies of spells that show a headicon.
	 */
	public static final int MELEE = 0;
	
	/**
	 * The action button of the spell.
	 */
	private int actionButton;
	
	/**
	 * The skills modified by the spell.
	 */
	private int[] modifiedSkills;
	
	/**
	 * Constructs a <code>PrayerSpell</code>.
	 * @param actionButton The action button of the spell.
	 * @param modifiedSkills The skills modified by the spell.
	 */
	PrayerSpell(int actionButton, int[] modifiedSkills) {
		this.actionButton = actionButton;
		this.modifiedSkills = modifiedSkills;
	}
	
	/**
	 * Gets a <code>PrayerSpell</code> for the specified action button.
	 * @return The <code>PrayerSpell</code>.
	 */
	public static PrayerSpell forId(int actionButton) {
		for (PrayerSpell spell : PrayerSpell.values()) {
			if (actionButton == spell.getActionButton()) {
				return spell;
			}
		}
		
		return NON_EXISITING_SPELL;
	}
	
	/**
	 * Gets the action button of the spell.
	 * @return The action button.
	 */
	public int getActionButton() {
		return actionButton;
	}
	
	/**
	 * Gets the skill indicies the spell modifies.
	 * @return The skills modified.
	 */
	public int[] getModifiedSkills() {
		return modifiedSkills;
	}
	
}
