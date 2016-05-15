package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Opens the bank for the specified player.
 * @author Stephen Andrews
 */
public class OpenBank implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		if (args.length == 0 && player.getRights().equals(Rights.ADMINISTRATOR)) {
			Bank.open(player);
		} else {
			Player otherPlayer = World.getWorld().getPlayerByName(args[0]);
			if (otherPlayer == null) {
				player.getActionSender().sendMessage(args[0] + " doesn't appear to be online.");
				return;
			}
			
		}
		//TODO: Check other player's banks
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
