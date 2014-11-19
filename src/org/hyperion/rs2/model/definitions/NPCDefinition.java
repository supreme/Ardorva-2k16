package org.hyperion.rs2.model.definitions;

import org.hyperion.rs2.LivingClasses;

/**
 * Provides a structure for JSON serialization.
 * 
 * All the instance variables have to be strings cause I couldn't get a better way
 * to convert the definitions from XML to JSON without them :S.
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
}
