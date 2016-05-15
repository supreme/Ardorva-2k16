package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.content.magic.MagicBook;
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
		boolean isAncient = player.getPlayerConfiguration().getMagicBook() == MagicBook.ANCIENT;
		MagicBook set = isAncient ? MagicBook.MODERN : MagicBook.ANCIENT;
		player.getPlayerConfiguration().setMagicBook(set);
		player.getActionSender().sendTab(92, player.getPlayerConfiguration().getMagicBook().getInterfaceId());
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
