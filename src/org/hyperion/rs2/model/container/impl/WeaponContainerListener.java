package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.player.Player;

/**
 * A listener which updates the weapon tab.
 * @author Graham Edgecombe
 *
 */
public class WeaponContainerListener implements ContainerListener {
	
	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Creates the listener.
	 * @param player The player.
	 */
	public WeaponContainerListener(Player player) {
		this.player = player;
	}

	@Override
	public void itemChanged(Container container, int slot) {
		if(slot == Equipment.SLOT_WEAPON) {
			sendWeapon();
		}
	}

	@Override
	public void itemsChanged(Container container, int[] slots) {
		for(int slot : slots) {
			if(slot == Equipment.SLOT_WEAPON) {
				sendWeapon();
				return;
			}
		}
	}

	@Override
	public void itemsChanged(Container container) {
		sendWeapon();
	}
	
	/**
	 * Sends weapon information.
	 */
	private void sendWeapon() {
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int id = -1;
		String name = null;
		if(weapon == null) {
			name = "Unarmed";
		} else {
			name = weapon.getDefinition().getName();
			id = weapon.getId();
		}
		String genericName = filterWeaponName(name).trim();
		sendWeapon(id, name, genericName);
	}

	/**
	 * Sends weapon information.
	 * @param id The id.
	 * @param name The name.
	 * @param genericName The filtered name.
	 */
	private void sendWeapon(int id, String name, String genericName) {
	}

	/**
	 * Filters a weapon name.
	 * @param name The original name.
	 * @return The filtered name.
	 */
	private String filterWeaponName(String name) {
		final String[] filtered = new String[] {
			"Iron", "Steel", "Scythe", "Black", "Mithril", "Adamant",
			"Rune", "Granite", "Dragon", "Crystal", "Bronze"
		};
		for(String filter : filtered) {
			name = name.replaceAll(filter, "");
		}
		return name;
	}

}
