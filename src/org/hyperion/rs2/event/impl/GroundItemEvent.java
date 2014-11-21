package org.hyperion.rs2.event.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.player.Player;

/**
 * Handles the ground items in the game world.
 * @author Stephen Andrews
 */
public class GroundItemEvent extends Event {

	/**
	 * The amount of ms before a ground item becomes globally visible.
	 */
	private static final int GLOBAL_DELAY = 72000;
	
	/**
	 * The amount of ms before a ground item is removed from the region after it has
	 * become globally visible.
	 */
	private static final int REMOVAL_DELAY = 72000;
	
	/**
	 * The ground item that has been created.
	 */
	private GroundItem groundItem;
	
	/**
	 * Constructs a ground item event.
	 * @param groundItem The ground item belonging to the event.
	 */
	public GroundItemEvent(GroundItem groundItem) {
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
		
		World.getWorld().submit(new Event(REMOVAL_DELAY) {

			@Override
			public void execute() {
				groundItem.getRegion().removeGroundItem(groundItem);
				
				for (Player player : groundItem.getRegion().getPlayers()) {
					player.getActionSender().clearGroundItem(groundItem);
				}
				owner.getActionSender().clearGroundItem(groundItem);
				
				if (Constants.DEV_MODE) {
					System.out.println("Ground item: " + groundItem.getItem().getId() + " has expired.");
				}
				stop();
			}
			
		});
		
		if (Constants.DEV_MODE) {
			System.out.println("Ground item: " + groundItem.getItem().getId() + " has become globally visible.");
		}
		stop();		
	}

}
