package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Plays the specified graphic.
 * @author Stephen Andrews
 */
public class PlayGraphic implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		if(args.length == 1 || args.length == 2) {
			int id = Integer.parseInt(args[0]);
			int delay = 0;
			if(args.length == 2) {
				delay = Integer.parseInt(args[1]);
			}
			player.playGraphics(Graphic.create(id, delay));
		}
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
