package org.hyperion.rs2.model.player;

/**
 * Contains all the variables belonging to a <code>Player</code>.
 * @author Stephen Andrews
 */
public class PlayerVariables {

	/**
	 * The banking flag.
	 */
	private boolean banking;
	
	/**
	 * Whether or not the player is banking.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isBanking() {
		return banking;
	}
	
	/**
	 * Sets the banking flag.
	 * @param banking The flag to set.
	 */
	public void setBanking(boolean banking) {
		this.banking = banking;
	}
}
