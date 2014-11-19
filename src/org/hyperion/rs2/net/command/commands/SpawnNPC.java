package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.definitions.NPCDefinition;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Spawns an NPC into the world.
 * @author Stephen Andrews
 */
public class SpawnNPC implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		int id = Integer.parseInt(args[0]);
		
		if (id < 1) {
			player.getActionSender().sendMessage("The NPC for id " + id + " does not exist.");
		}
		
		NPC npc = new NPC(NPCDefinition.forId(id));
		npc.setLocation(player.getLocation());
		npc.addToRegion(player.getRegion());
		World.getWorld().register(npc);
		
		player.getActionSender().sendMessage("You spawned a " + NPCDefinition.forId(id).getName() + ".");
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
