package org.hyperion.rs2.net.command;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;

/**
 * An interface for commands.
 * @author Stephen Andrews
 */
public interface Command {

	/**
	 * Invokes the command.
	 * @param player The player invoking the command.
	 * @param args The arguments of the command.
	 */
	public void invoke(Player player, String[] args);
	
	/**
	 * The rights a player has to have in order to invoke the command.
	 * @return The rights level.
	 */
	public Rights getAccessLevel();
}
