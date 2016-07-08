package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Used for various testing applications.
 * @author Stephen Andrews
 */
public class SpawnObject implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		int id = Integer.parseInt(args[0]);
		player.getActionSender().sendObject(new GameObject(id, player.getLocation(), 0, 10));
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
