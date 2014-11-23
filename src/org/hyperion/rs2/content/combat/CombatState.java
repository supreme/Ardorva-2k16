package org.hyperion.rs2.content.combat;

import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.content.combat.util.CombatData.Stance;
import org.hyperion.rs2.content.combat.util.CombatData.Style;

/**
 * The combat state class should be assigned to entities in order
 * to track variations in their combat behavior.
 * TODO: Save/load
 * @author Stephen Andrews
 */
public class CombatState {

	/**
	 * The attack type in use.
	 */
	private AttackType attackType;
	
	/**
	 * The combat style in use.
	 */
	private Style combatStyle;
	
	/**
	 * The combat stance in use.
	 */
	private Stance combatStance;
	
	/**
	 * Whether or not the player is currently engaged in combat.
	 */
	private boolean inCombat;
	
	/**
	 * Whether or not the player has changed weapons since the last attack.
	 */
	private boolean pendingWeaponChange;
	
	/**
	 * Constructs a combat state object.
	 */
	public CombatState() {
		attackType = null;
		combatStyle = null;
		combatStance = Stance.ACCURATE;
		inCombat = false;
		pendingWeaponChange = false;
	}
	
	/**
	 * Gets the attack type in use.
	 * @return The combat type.
	 */
	public AttackType getAttackType() {
		return attackType;
	}
	
	/**
	 * Sets the attack type.
	 * @param attackType The new combat type.
	 */
	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}
	
	/**
	 * Gets the combat style in use.
	 * @return The combat style.
	 */
	public Style getCombatStyle() {
		return combatStyle;
	}
	
	/**
	 * Sets the combat style.
	 * @param combatStyle The new combat style.
	 */
	public void setCombatStyle(Style combatStyle) {
		this.combatStyle = combatStyle;
	}
	
	/**
	 * Gets the combat stance in use.
	 * @return The combat stance.
	 */
	public Stance getCombatStance() {
		return combatStance;
	}
	
	/**
	 * Sets the combat stance.
	 * @param combatStance The new combat stance.
	 */
	public void setCombatStance(Stance combatStance) {
		this.combatStance = combatStance;
	}
	
	/**
	 * Determines whether or not the player is in combat.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isInCombat() {
		return inCombat;
	}
	
	/**
	 * Sets the in combat flag.
	 * @param inCombat The new flag.
	 */
	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}
	
	/**
	 * Determines whether or not the player has changed weapons since the last attack.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isPendingWeaponChange() {
		return pendingWeaponChange;
	}
	
	/**
	 * Flags a pending weapon change.
	 * @param flag The flag to set.
	 */
	public void setPendingWeaponChange(boolean flag) {
		pendingWeaponChange = flag;
	}
}