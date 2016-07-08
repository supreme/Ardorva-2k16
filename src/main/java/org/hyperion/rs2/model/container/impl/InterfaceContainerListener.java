package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.player.Player;

/**
 * A ContainerListener which updates a client-side interface to match the
 * server-side copy of the container.
 * @author Graham Edgecombe
 *
 */
public class InterfaceContainerListener implements ContainerListener {
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * The interface id.
	 */
	private int interfaceId;
	private int childId;
	private int type;
	
	/**
	 * Creates the container listener.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 */
	public InterfaceContainerListener(Player player, int interfaceId, int childId, int type) {
		this.player = player;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.type = type;
	}

	@Override
	public void itemChanged(Container container, int slot) {
		Item item = container.get(slot);
		player.getActionSender().sendUpdateItem(interfaceId, childId, type, slot, item);
	}

	@Override
	public void itemsChanged(Container container) {
		player.getActionSender().sendUpdateItems(interfaceId, childId, type, container.toArray());
	}

	@Override
	public void itemsChanged(Container container, int[] slots) {
		player.getActionSender().sendUpdateItems(interfaceId, childId, type, slots, container.toArray());
	}

}
