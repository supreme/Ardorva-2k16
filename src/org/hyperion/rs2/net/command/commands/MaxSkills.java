package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.net.command.Command;

/**
 * Sets all of the player's skills to level 99.
 * @author Stephen Andrews
 */
public class MaxSkills implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		for(int i = 0; i <= Skills.SKILL_COUNT; i++) {
			player.getSkills().setLevel(i, 99);
			player.getSkills().setExperience(i, 14000000);
		}
		
		player.getActionSender().sendMessage("Your skills have been maxed.");
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.ADMINISTRATOR;
	}

}
