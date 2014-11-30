package org.hyperion.rs2.action.impl;

import org.hyperion.Server;
import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.player.Player;

/**
 * An <code>Action</code> which prevents player's from invoking
 * objects, NPCs, etc from an incorrect distance. Once the player
 * is within the interaction distance, the pending action is added
 * to their action queue.
 * @author Stephen Andrews
 */
public class DistancedAction extends Action {
	
	/**
	 * The action put on hold.
	 */
	private Action pendingAction;
	
	/**
	 * The location the player is trying to interact with.
	 */
	private Location target;
	
	/**
	 * Creates the distanced action.
	 * @param player The player attempting an action.
	 * @param target The location the player is trying to interact with.
	 * @param pendingAction The action put on hold.
	 */
	public DistancedAction(Player player, Location target, Action pendingAction) {
		super(player, Server.CYCLE_TIME, false);
		this.target = target;
		this.pendingAction = pendingAction;
	}

	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.NEVER;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public void execute() {
		if (getPlayer().getLocation().isWithinInteractionDistance(target)) {
			this.stop();
			getPlayer().getActionQueue().addAction(pendingAction);
		}	
	}
}
