package org.hyperion.rs2.content.combat;

import org.hyperion.rs2.model.Entity;

/**
 * A combat session between two entities.
 * @author Stephen Andrews
 */
public class CombatSession {

	/**
	 * The aggressor entity.
	 */
	private Entity aggressor;
	
	/**
	 * The opponent entity.
	 */
	private Entity opponent;
	
	/**
	 * Creates a combat session between two entites.
	 * @param aggressor The aggressor entity.
	 * @param opponent The opponent entity.
	 */
	public CombatSession(Entity aggressor, Entity opponent) {
		this.aggressor = aggressor;
		this.opponent = opponent;
	}
	
	/**
	 * Gets the aggressor entity.
	 * @return The aggressor entity.
	 */
	public Entity getAggressor() {
		return aggressor;
	}
	
	/**
	 * Gets the opponent entity.
	 * @return The opponent entity.
	 */
	public Entity getOpponent() {
		return opponent;
	}
}
