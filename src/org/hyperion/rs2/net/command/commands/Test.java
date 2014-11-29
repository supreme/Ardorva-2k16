package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Used for various testing applications.
 * @author Stephen Andrews
 */
public class Test implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		player.getSkills().detractLevel(Skills.HITPOINTS, 98);
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
