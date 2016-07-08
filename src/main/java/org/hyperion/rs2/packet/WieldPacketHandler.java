package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.combat.util.CombatUtility;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Handles the 'wield' and 'remove' option on items.
 * @author Graham Edgecombe
 * @author Stephen Andrews
 */
public class WieldPacketHandler implements PacketHandler {

	/**
	 * Wielding packet.
	 */
	private static final int WIELD = 215;
	
	/**
	 * Unequipping packet.
	 */
	private static final int REMOVE = 177;
	
	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
			case WIELD:
				handleItemWield(player, packet);
				break;
			case REMOVE:
				handleItemUnequip(player, packet);
				break;
		}
	}
	
	/**
	 * Handles the wielding of an item.
	 * @param player The player attempting to wield the item.
	 * @param packet The wielding packet.
	 */
	private void handleItemWield(Player player, Packet packet) {
		int slot = packet.getLEShort();
		int interfaceId = packet.getLEInt() >> 16;
		int id = packet.getLEShort();
		
		if (interfaceId != 149) return;
		
		if(slot >= 0 && slot < Inventory.SIZE) {
			Item item = player.getInventory().get(slot);
			if(item != null && item.getId() == id) { 
				EquipmentType type = Equipment.getType(item);
				Item oldEquip = null;
				boolean stackable = false;
				if(player.getEquipment().isSlotUsed(type.getSlot()) && !stackable) {
					oldEquip = player.getEquipment().get(type.getSlot());
					player.getEquipment().set(type.getSlot(), null);
				}
				player.getInventory().set(slot, null);
				if(oldEquip != null) {
					player.getInventory().add(oldEquip);
				}
				if(!stackable) {
					player.getEquipment().set(type.getSlot(), item);
				} else {
					player.getEquipment().add(item);
				}
			}
		}
		
		player.getBonuses().refresh();
		player.getCombatUtility().refresh();
	}
	
	/**
	 * Handles the unequipping of an item.
	 * @param player The player attempting to wield the item.
	 * @param packet The wielding packet.
	 */
	private void handleItemUnequip(Player player, Packet packet) { //Thanks Nando ^.^
		int id = packet.getShort();
		int interfaceHash = packet.getInt1();
		int slot = packet.getShort();
		int interfaceId = interfaceHash >> 16;
		int child = interfaceHash & 0xff;
		
		if (Constants.DEV_MODE) {
			player.getActionSender().sendMessage("Id: " + id + " Slot: " + slot + " Interface ID: " + interfaceId + " Child: " + child);
		}
			
		if (slot >= 0 && slot <= 10) {
			Item item = player.getEquipment().get(slot);
			if (item != null && item.getId() == id) {
				if (player.getInventory().hasRoomFor(item)) {
					player.getInventory().add(item);
					player.getEquipment().set(slot, null);
				} else {
					player.getActionSender().sendMessage("You do not have any room in your inventory.");
				}
			}
		}
	}

}
