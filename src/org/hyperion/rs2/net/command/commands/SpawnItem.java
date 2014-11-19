package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Spawns an item a the player's inventory.
 * @author Stephen Andrews
 */
public class SpawnItem implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		if(args.length == 1 || args.length == 2) {
			int id = Integer.parseInt(args[0]);
			int count = 1;
			if(args.length == 2) {
				count = Integer.parseInt(args[1]);
			}
			Item item = new Item(id, count);
			if (item != null) {
				player.getInventory().add(new Item(id, count));
			} else {
				player.getActionSender().sendMessage("The item for id: " + id + ", does not exist.");
			}
		} else {
			player.getActionSender().sendMessage("Syntax is ::item [id] [count].");
		}
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
