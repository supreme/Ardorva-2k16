package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.engine.task.listener.OnFireActionListener;
import org.hyperion.rs2.model.player.Player;

/**
 * A task which resets a player after an update cycle.
 * @author Graham Edgecombe
 *
 */
public class PlayerResetTask extends OnFireActionListener {

	private Player player;
	
	public PlayerResetTask(Player player) {
		this.player = player;
	}

	@Override
	public boolean cancelWhen() {
		return !player.getSession().isConnected();
	}

	@Override
	public void run() {
		player.getUpdateFlags().reset();
		player.setTeleporting(false);
		player.setMapRegionChanging(false);
		player.resetTeleportTarget();
		player.resetCachedUpdateBlock();
		player.reset();
	}

}
