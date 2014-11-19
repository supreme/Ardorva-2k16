package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Gets the current position of the player.
 * @author Stephen Andrews
 */
public class Position implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		player.getActionSender().sendMessage("You are at: " + player.getLocation());
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
