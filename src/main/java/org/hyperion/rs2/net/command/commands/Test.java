package org.hyperion.rs2.net.command.commands;

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
//		boolean isAncient = player.getPlayerConfiguration().getMagicBook() == MagicBook.ANCIENT;
//		MagicBook set = isAncient ? MagicBook.MODERN : MagicBook.ANCIENT;
//		player.getPlayerConfiguration().setMagicBook(set);
//		player.getActionSender().sendTab(92, player.getPlayerConfiguration().getMagicBook().getInterfaceId());

		//249 errors, 526, 547
		for (int i = 549; i < 593; i++) {
			player.getActionSender().sendMessage("Displaying interface: " + i);
			player.getActionSender().displayInterface(i);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
