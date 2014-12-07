package org.hyperion.rs2.content.skills.prayer;

import org.hyperion.rs2.model.Skills;

/**
 * Enumeration of all the prayer spells that can be activated.
 * @author Stephen Andrews
 */
public enum PrayerSpell {
	
	THICK_SKIN(5, 1, new int[] {Skills.DEFENCE}),
	BURST_OF_STRENGTH(7, 4, new int[] {Skills.STRENGTH}),
	CLARITY_OF_THOUGHT(9, 7, new int[] {Skills.ATTACK}),
	SHARP_EYE(11, 8, new int[] {Skills.RANGE}),
	MYSTIC_WILL(13, 9, new int[] {Skills.MAGIC}),
	ROCK_SKIN(15, 10, new int[] {Skills.DEFENCE}),
	SUPERHUMAN_STRENGTH(17, 13, new int[] {Skills.STRENGTH}),
	IMPROVED_REFLEXES(19, 16, new int[] {Skills.ATTACK}),
	RAPID_RESTORE(21, 19, new int[] {PrayerManager.UNIQUE_SPELL}),
	RAPID_HEAL(23, 22, new int[] {Skills.HITPOINTS}),
	PROTECT_ITEM(25, 25, new int[] {PrayerManager.UNIQUE_SPELL}),
	HAWK_EYE(27, 26, new int[] {Skills.RANGE}),
	MYSTIC_LORE(29, 27, new int[] {Skills.MAGIC}),
	STEEL_SKIN(31, 28, new int[] {Skills.DEFENCE}),
	ULTIMATE_STRENGTH(33, 31, new int[] {Skills.STRENGTH}),
	INCREDIBLE_REFLEXES(35, 34, new int[] {Skills.ATTACK}),
	PROTECT_FROM_MAGIC(37, 37, new int[] {PrayerManager.PROTECTION_SPELL}),
	PROTECT_FROM_MISSILES(39, 40, new int[] {PrayerManager.PROTECTION_SPELL}),
	PROTECT_FROM_MELEE(41, 43, new int[] {PrayerManager.PROTECTION_SPELL}),
	EAGLE_EYE(43, 44, new int[] {Skills.RANGE}),
	MYSTIC_MIGHT(45, 45, new int[] {Skills.MAGIC}),
	RETRIBUTION(47, 46, new int[] {PrayerManager.UNIQUE_SPELL}),
	REDEMPTION(49, 49, new int[] {Skills.HITPOINTS}),
	SMITE(51, 52, new int[] {PrayerManager.UNIQUE_SPELL}),
	CHIVALRY(53, 60, new int[] {Skills.DEFENCE, Skills.STRENGTH, Skills.ATTACK}),
	PIETY(55, 70, new int[] {Skills.DEFENCE, Skills.STRENGTH, Skills.ATTACK}),
	NON_EXISITING_SPELL(-1, -1, null);
	
	/**
	 * The head icon indicies of spells that show a headicon.
	 */
	public static final int MELEE = 0;
	
	/**
	 * The action button of the spell.
	 */
	private int actionButton;
	
	/**
	 * The required level to activate the spell.
	 */
	private int requiredLevel;
	
	/**
	 * The skills modified by the spell.
	 */
	private int[] modifiedSkills;
	
	/**
	 * Constructs a <code>PrayerSpell</code>.
	 * @param actionButton The action button of the spell.
	 * @param requiredLevel The required level to activate the spell.
	 * @param modifiedSkills The skills modified by the spell.
	 */
	PrayerSpell(int actionButton, int requiredLevel, int[] modifiedSkills) {
		this.actionButton = actionButton;
		this.requiredLevel = requiredLevel;
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
	 * Gets the required level to activate the spell.
	 * @return The required level to activate the spell.
	 */
	public int getRequiredLevel() {
		return requiredLevel;
	}
	
	/**
	 * Gets the skill indicies the spell modifies.
	 * @return The skills modified.
	 */
	public int[] getModifiedSkills() {
		return modifiedSkills;
	}
	
}
