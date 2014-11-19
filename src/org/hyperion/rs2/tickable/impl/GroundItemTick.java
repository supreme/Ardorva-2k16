package org.hyperion.rs2.tickable.impl;

import org.hyperion.application.ConsoleMessage;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.tickable.Tickable;

/**
 * Handles the properties of a ground item.
 * @author Stephen Andrews
 */
public class GroundItemTick extends Tickable {

	/**
	 * The amount of ticks before a ground item becomes globally visible.
	 */
	private static final int GLOBAL_DELAY = 120;
	
	/**
	 * The amount of ticks before a ground item is removed from the region after it has
	 * become globally visible.
	 */
	private static final int REMOVAL_DELAY = 120;
	
	/**
	 * The ground item that has been created.
	 */
	private GroundItem groundItem;
	
	/**
	 * Constructs a ground item tick.
	 */
	public GroundItemTick(GroundItem groundItem) {
		super(GLOBAL_DELAY);
		this.groundItem = groundItem;
	}

	@Override
	public void execute() {
		final Player owner = (Player) groundItem.getOwner();

		if (!owner.getRegion().getGroundItems().contains(groundItem)) stop();
		
		groundItem.setGlobal(true);
		owner.getActionSender().clearGroundItem(groundItem);
		
		for (Player player : groundItem.getRegion().getPlayers()) {
			player.getActionSender().sendGroundItem(groundItem);
		}
		
		World.getWorld().submit(new Tickable(REMOVAL_DELAY) {

			@Override
			public void execute() {
				groundItem.getRegion().removeGroundItem(groundItem);
				
				for (Player player : groundItem.getRegion().getPlayers()) {
					player.getActionSender().clearGroundItem(groundItem);
				}
				owner.getActionSender().clearGroundItem(groundItem);
				
				if (Constants.DEV_MODE) {
					ConsoleMessage.info("Ground item: " + groundItem.getItem().getId() + " has expired.");
				}
				stop();
			}
			
		});
		
		if (Constants.DEV_MODE) {
			ConsoleMessage.info("Ground item: " + groundItem.getItem().getId() + " has become globally visible.");
		}
		stop();
	}
	
}
