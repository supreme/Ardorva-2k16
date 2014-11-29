package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.World;
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
		World.getWorld().submit(new Event(1200, false) {

			int i = 3;
			private long last = System.currentTimeMillis();
			
			@Override
			public void execute() {
				if (i == 0) {
					player.getActionSender().sendMessage("Boom!");
					stop();
					return;
				}
				
				player.getActionSender().sendMessage("" + i);
				i--;
				System.out.println(System.currentTimeMillis() - last);
				last = System.currentTimeMillis();
			}

		});
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
