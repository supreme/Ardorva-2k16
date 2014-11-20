package org.hyperion.rs2.net.command.commands;

import org.hyperion.rs2.engine.task.TaskManager;
import org.hyperion.rs2.engine.task.listener.OnFireActionListener;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.Player.Rights;
import org.hyperion.rs2.net.command.Command;

/**
 * Used for various testing applications.
 * @author Stephen Andrews
 */
public class Test  implements Command {

	@Override
	public void invoke(Player player, String[] args) {
		TaskManager.submit(new OnFireActionListener(2) {
			int i = 0;
			long last = System.currentTimeMillis();
			
			@Override
			public boolean cancelWhen() {
				return i == 10;
			}

			@Override
			public void run() {
				System.out.println(System.currentTimeMillis() - last);
				last = System.currentTimeMillis();
				player.getActionSender().sendMessage("" + i);
				i = i + 1;
			}
			
		});
	}

	@Override
	public Rights getAccessLevel() {
		return Rights.PLAYER;
	}

}
