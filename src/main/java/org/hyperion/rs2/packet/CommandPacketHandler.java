package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.command.CommandManager;

/**
 * Handles player commands (the ::words).
 * @author Graham Edgecombe
 * @author Stephen Andrews
 */
public class CommandPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		String commandString = packet.getRS2String();
		String[] input = commandString.split(" ");
		String command = input[0].toLowerCase();
		String[] args = new String[input.length - 1];
		for (int i = 1; i < input.length; i++) {
			args[i - 1] = input[i];
		}
		
		CommandManager.invoke(player, command, args);
	}

}
