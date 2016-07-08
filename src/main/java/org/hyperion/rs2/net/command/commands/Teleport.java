package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Teleports a player to the specified location.
 * @author Stephen Andrews
 */
public class Teleport implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		if(args.length == 2 || args.length == 3) {
			int x = Integer.parseInt(args[0]);
			int y = Integer.parseInt(args[1]);
			int z = player.getLocation().getZ();
			if(args.length == 3) {
				z = Integer.parseInt(args[2]);
			}
			player.setTeleportTarget(Location.create(x, y, z));
		} else {
			player.getActionSender().sendMessage("Syntax is ::tele [x] [y] [z].");
		}
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.MODERATOR;
	}

}
