package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.model.Item;
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

		int equipId = Integer.valueOf(args[0]);
		int slot = Integer.valueOf(args[1]);
		player.getEquipment().set(slot, new Item(equipId, 1));
		//player.getEquipment().remove();
//		for (int i = 0; i < 593; i++) {
//			player.getActionSender().sendMessage("Displaying interface: " + i);
//			player.getActionSender().displayInterface(i);
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		int id = Integer.valueOf(args[0]);
//		player.getActionSender().sendTab(id, 149);
//		for (int i = 70; i < 130; i++) {
//			player.getActionSender().sendMessage("Sending tab: " + i);
//			player.getActionSender().sendTab(i, 239);
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
