package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.content.combat.logic.CombatFormulas;
import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Displays a player's max hit to them.
 * @author Stephen Andrews
 */
public class MaxHit implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		player.getActionSender().sendMessage("Your melee max hit is: " + CombatFormulas.calculateMeleeRangeMaxHit(player, AttackType.MELEE));
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
