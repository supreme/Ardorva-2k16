package org.hyperion.rs2.model.definitions;

import org.hyperion.rs2.LivingClasses;

/**
 * Provides a structure for JSON serialization of NPC definitions.
 * @author Stephen Andrews
 */
public class NPCDefinition {
	
	/**
	 * The id of the NPC.
	 */
	private int id;
	
	/**
	 * The name of the NPC.
	 */
	private String name;
	
	/**
	 * The examine of the NPC.
	 */
	private String examine;
	
	/**
	 * The combat level of the NPC.
	 */
	private int combat;
	
	/**
	 * The size of the NPC.
	 */
	private int size;
	
	/**
	 * Whether or not the NPC is attackable.
	 */
	private boolean attackable;
	
	/**
	 * Whether or not the NPC is aggressive.
	 */
	private boolean aggressive;
	
	/**
	 * Whether or not the NPC retreats.
	 */
	private boolean retreats;
	
	/**
	 * Whether or not the NPC is poisonous.
	 */
	private boolean poisonous;
	
	/**
	 * The respawn of the NPC in server ticks.
	 */
	private int respawn;
	
	/**
	 * The max hit of the NPC.
	 */
	private int maxHit;
	
	/**
	 * The hitpoints of the NPC.
	 */
	private int hitpoints;
	
	/**
	 * The attack speed of the NPC.
	 */
	private int attackSpeed;
	
	/**
	 * The attack animation of the NPC.
	 */
	private int attackAnim;
	
	/**
	 * The defence animation of the NPC.
	 */
	private int defenceAnim;
	
	/**
	 * The death animation of the NPC.
	 */
	private int deathAnim;
	
	/**
	 * The attack bonus of the NPC.
	 */
	private int attackBonus;
	
	/**
	 * The melee defence bonus of the NPC.
	 */
	private int defenceMelee;
	
	/**
	 * The range defence bonus of the NPC.
	 */
	private int defenceRange;
	
	/**
	 * The mage defence bonus of the NPC.
	 */
	private int defenceMage;
	
	/**
	 * Gets an NPC definition for the specified id.
	 * @return The NPC definition.
	 */
	public static NPCDefinition forId(int id) {
		for (NPCDefinition def : LivingClasses.definitionLoader.getNPCDefinitions()) {
			if (def.getId() == id) {
				return def;
			}
		}
		return null;
	}
	
	/**
	 * Gets the NPC's id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the NPC's name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the NPC's examine info.
	 * @return The examine info.
	 */
	public String getExamine() {
		return examine;
	}
	
	/**
	 * Gets the NPC's combat level.
	 * @return The combat level.
	 */
	public int getCombatLevel() {
		return combat;
	}
	
	/**
	 * Gets the NPC's size.
	 * @return The size of the NPC.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Whether or not the NPC is attackable.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isAttackable() {
		return attackable;
	}
	
	/**
	 * Whether or not the NPC is aggressive.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isAggressive() {
		return aggressive;
	}
	
	/**
	 * Whether or not the NPC retreats.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean retreats() {
		return retreats;
	}
	
	/**
	 * Whether or not the NPC is poisonous.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isPoisonous() {
		return poisonous;
	}
	
	/**
	 * Gets the NPC's respawn delay in server ticks.
	 * @return The respawn delay.
	 */
	public int getRespawn() {
		return respawn;
	}
	
	/**
	 * Gets the max hit of the NPC.
	 * @return The max hit.
	 */
	public int getMaxHit() {
		return maxHit;
	}
	
	/**
	 * Gets the NPC's hitpoints.
	 * @return The hitpoints.
	 */
	public int getHitpoints() {
		return hitpoints;
	}
	
	/**
	 * Gets the attack speed of the NPC.
	 * @return The attack speed.
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	/**
	 * Gets the NPC's attack animation.
	 * @return The attack animation.
	 */
	public int getAttackAnimation() {
		return attackAnim;
	}
	
	/**
	 * Gets the NPC's defence animation.
	 * @return The defense animation.
	 */
	public int getDefenceAnimation() {
		return defenceAnim;
	}
	
	/**
	 * Gets the NPC's death animation.
	 * @return The death animation.
	 */
	public int getDeathAnimation() {
		return deathAnim;
	}
	
	/**
	 * Gets the NPC's attack bonus.
	 * @return The attack bonus.
	 */
	public int getAttackBonus() {
		return attackBonus;
	}
	
	/**
	 * Gets the NPC's melee defence bonus.
	 * @return The melee defence bonus.
	 */
	public int getDefenceMelee() {
		return defenceMelee;
	}
	
	/**
	 * Gets the NPC's range defence bonus.
	 * @return The range defence bonus.
	 */
	public int getDefenceRange() {
		return defenceRange;
	}
	
	/**
	 * Gets the NPC's mage defence bonus.
	 * @return The mage defence bonus.
	 */
	public int getDefenceMage() {
		return defenceMage;
	}
}
