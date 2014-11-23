package org.hyperion.rs2.model.player;

import org.hyperion.rs2.content.combat.util.CombatData.Stance;
import org.hyperion.rs2.content.combat.util.CombatData.Style;

/**
 * Holds various variables essential to a player.
 * @author Stephen
 */
public class PlayerVariables {

	/**
	 * Represents values which show and hide the xp counter.
	 */
	private final int SHOW_COUNTER = 1, HIDE_COUNTER = 0;
	
	/**
	 * The player we're looking at.
	 */
	private Player player;
	
	/**
	 * The combat stance of the player.
	 * Set to accurate by default.
	 */
	private Stance combatStance = Stance.ACCURATE;
	
	/**
	 * The combat style of the player.
	 */
	private Style combatStyle;
	
	/**
	 * Whether or not the player has their run toggled on.
	 */
	private boolean isRunning;
	
	/**
	 * Whether or not the player is banking.
	 */
	private boolean banking;
	
	/**
	 * Constructs a player variables object.
	 * @param player The player to look at.
	 */
	public PlayerVariables(Player player) {
		this.player = player;
	}
	
	/**
	 * Toggles the xp counter.
	 */
	public void handleCounter() {
		int val = HIDE_COUNTER;
		player.getActionSender().sendConfig(555, val <= HIDE_COUNTER ? HIDE_COUNTER : SHOW_COUNTER);
	}
	
	/**
	 * Gets the player's combat stance.
	 * @return The combat stance.
	 */
	public Stance getCombatStance() {
		return combatStance;
	}
	
	/**
	 * Sets the player's combat stance.
	 * @param stance The stance to set it to.
	 */
	public void setCombatStance(Stance stance) {
		this.combatStance = stance;
	}
	
	/**
	 * Gets the player's combat style.
	 * @return The combat style.
	 */
	public Style getCombatStyle() {
		return combatStyle;
	}

	/**
	 * Whether or not the player is banking.
	 * @return <code>true</code> if so, </code>false<code> if not.
	 */
	public boolean isBanking() {
		return banking;
	}
	
	/**
	 * Sets the banking flag.
	 * @param banking The flag to set it to.
	 */
	public void setBanking(boolean banking) {
		this.banking = banking;
	}
	
}
