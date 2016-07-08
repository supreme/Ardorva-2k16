package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Empties the player's inventory.
 * @author Stephen Andrews
 */
public class EmptyInventory implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		player.getInventory().clear();
		player.getActionSender().sendMessage("Your inventory has been emptied.");
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
