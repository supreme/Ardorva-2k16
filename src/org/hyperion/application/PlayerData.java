package org.hyperion.application;

import org.hyperion.Server;
import org.hyperion.rs2.model.player.Player;

/**
 * Handles some of the functions the control panel performs.
 * @author Stephen Andrews
 */
public class PlayerData {

	/**
	 * Local instance of the control panel.
	 */
	private ControlPanel cp = Server.controlPanel;
	
	/**
	 * The player whose data is being looked at.
	 */
	private Player player;
	
	/**
	 * Constructs a player data object.
	 * @param player The player whose data is to be looked at.
	 */
	public PlayerData(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the information pertaining to the player.
	 * @param index The index of the player in the world.
	 */
	public void getPlayerInfo() {
		cp.usernameField.setText(player.getName());
		
		
	}
}
