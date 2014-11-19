package org.hyperion.rs2.event.impl;

import org.hyperion.Server;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.player.Player;

/**
 * An event which runs periodically and performs tasks such as garbage
 * collection.
 * @author Graham Edgecombe
 *
 */
public class ControlPanelUpdationEvent extends Event {

	/**
	 * The delay in milliseconds between consecutive cleanups.
	 */
	public static final int UPDATION_INTERVAL = 5000;
	
	/**
	 * Creates the cleanup event to run every 5 minutes.
	 */
	public ControlPanelUpdationEvent() {
		super(UPDATION_INTERVAL);
	}

	private void updatePlayersOnline() {
		//Update the label
		Server.controlPanel.playersOnlineLabel.setText("Players online: " + World.getWorld().getPlayers().size());
		
		//Update the list
		//Need to do this another way, individually removing players because if you have a selected player and the list updates, it unselects it.
		for (Player player : World.getWorld().getPlayers()) {
			Server.controlPanel.playersOnline.clear();
			Server.controlPanel.playersOnline.addElement(player.getName());
		}
	}
	
	@Override
	public void execute() {
		updatePlayersOnline();
	}

}
