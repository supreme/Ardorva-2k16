package org.hyperion.rs2.net.command;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.command.commands.DisplayInterface;
import org.hyperion.rs2.net.command.commands.EmptyInventory;
import org.hyperion.rs2.net.command.commands.MaxSkills;
import org.hyperion.rs2.net.command.commands.OpenBank;
import org.hyperion.rs2.net.command.commands.PlayAnimation;
import org.hyperion.rs2.net.command.commands.PlayGraphic;
import org.hyperion.rs2.net.command.commands.Position;
import org.hyperion.rs2.net.command.commands.SpawnItem;
import org.hyperion.rs2.net.command.commands.SpawnNPC;
import org.hyperion.rs2.net.command.commands.Teleport;

/**
 * Manages all the commands.
 * @author Stephen Andrews
 */
public class CommandManager {

	/**
	 * Represents an invokable command.
	 * @author Stephen Andrews
	 */
	private enum Invokable {
		
		/**
		 * Spawns an NPC into the world.
		 */
		SPAWN_NPC("npc", new SpawnNPC()),
		
		/**
		 * Spawns an item into a player's inventory.
		 */
		SPAWN_ITEM("item", new SpawnItem()),
		
		/**
		 * Teleports a player to the specified location.
		 */
		TELEPORT("tele", new Teleport()),
		
		/**
		 * Gets the current position of a player.
		 */
		POSITION("pos", new Position()),
		
		/**
		 * Plays an animation.
		 */
		PLAY_ANIMATION("anim", new PlayAnimation()),
		
		/**
		 * Plays a graphic.
		 */
		PLAY_GRAPHIC("gfx", new PlayGraphic()),
		
		/**
		 * Opens the bank of the specified player.
		 */
		OPEN_BANK("bank", new OpenBank()),
		
		/**
		 * Sets the player's skills to level 99.
		 */
		MAX_SKILLS("max", new MaxSkills()),
		
		/**
		 * Empties a player's inventory.
		 */
		EMPTY_INVENTORY("empty", new EmptyInventory()),
		
		/**
		 * Displays an interface.
		 */
		DISPLAY_INTERFACE("inter", new DisplayInterface());
		
		/**
		 * Identifies which command is being executed.
		 */
		private String identifier;
		
		/**
		 * The command to execute.
		 */
		private Command command;
		
		/**
		 * Constructs an invokable.
		 * @param identifier Identifies which command is being executed.
		 * @param command The command to execute.
		 */
		private Invokable(String identifier, Command command) {
			this.identifier = identifier;
			this.command = command;
		}
		
		/**
		 * Gets an invokable for the specified identifier.
		 * @param identifier The indentifier.
		 * @return The invokable.
		 */
		public static Invokable forIdentifier(String identifier) {
			for (Invokable invokable : Invokable.values()) {
				if (identifier.equalsIgnoreCase(invokable.getIdentifier())) {
					return invokable;
				}
			}
			
			return null;
		}
		
		/**
		 * Gets the identifier.
		 * @return The identifier.
		 */
		public String getIdentifier() {
			return identifier;
		}
		
		/**
		 * Gets the command.
		 * @return The command.
		 */
		public Command getCommand() {
			return command;
		}	
	}
	
	/**
	 * Invokes a command.
	 * @param player The player invoking the command.
	 * @param command The command to be invoked.
	 */
	public static void invoke(Player player, String command, String[] args) {
		String[] clientCommands = {"hd", "ld"};
		
		//Remove warning for client sided commands
		for (String s : clientCommands) {
			if (s.equals(command)) {
				return;
			}
		}
		
		if (command.length() == 0) {
			player.getActionSender().sendMessage("The command you have entered does not exist.");
			return;
		}
		
		Invokable invokable = Invokable.forIdentifier(command);
		if (invokable != null) {
			try {
				/*if (player.getRights().toInteger() < invokable.getCommand().getAccessLevel().ordinal()) {
					player.getActionSender().sendMessage("You do not have the required credentials to execute this command.");
					return;
				}*/
				invokable.getCommand().invoke(player, args);
			} catch (Exception ex) {
				if (Constants.DEV_MODE) {
					ex.printStackTrace();
				}
				player.getActionSender().sendMessage("Error while processing command.");
			}
		} else {
			player.getActionSender().sendMessage("The command you have entered does not exist.");
		}
	}
}
