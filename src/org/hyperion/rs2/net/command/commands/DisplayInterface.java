package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Displays an interface for the player.
 * @author Stephen Andrews
 */
public class DisplayInterface implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		if (args.length > 1) {
			player.getActionSender().sendMessage("Syntax is ::inter [interfaceId]");
			return;
		}
		
		int interfaceId = Integer.valueOf(args[0]);
		player.getActionSender().displayInterface(interfaceId);
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
