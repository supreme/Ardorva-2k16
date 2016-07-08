package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.skills.prayer.BoneBurying;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Remove item options.
 * @author Graham Edgecombe
 * @author Stephen Andrews
 */
public class ItemOptionPacketHandler implements PacketHandler {
	
	/**
	 * Option 1 opcode.
	 */
	private static final int OPTION_1 = 145;
	
	/**
	 * Option 2 opcode.
	 */
	private static final int OPTION_2 = 117;
	
	/**
	 * Option 3 opcode.
	 */
	private static final int OPTION_3 = 43;
	
	/**
	 * Option 4 opcode.
	 */
	private static final int OPTION_4 = 129;
	
	/**
	 * Option 5 opcode.
	 */
	private static final int OPTION_5 = 135;
	
	/**
	 * Click 1 opcode.
	 */
	private static final int CLICK_1 = 101;

	
	/**
	 * The item dropping opcode.
	 */
	private static final int DROP_ITEM = 247;
	
	/**
	 * Picking up ground items opcode.
	 */
	private static final int PICKUP_GROUNDITEM = 216;
	
	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
		case OPTION_1:
			handleItemOption1(player, packet);
			break;
		case OPTION_2:
			handleItemOption2(player, packet);
			break;
		case OPTION_3:
			handleItemOption3(player, packet);
			break;
		case OPTION_4:
			handleItemOption4(player, packet);
			break;
		case OPTION_5:
			handleItemOption5(player, packet);
			break;
		case CLICK_1:
			handleItemOptionClick1(player, packet);
			break;
		case DROP_ITEM:
			handleDropItem(player, packet);
			break;
		case PICKUP_GROUNDITEM:
			handlePickupGroundItem(player, packet);
			break;
		}
	}

	/**
	 * Handles item option 1.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption1(Player player, Packet packet) {
		int interfaceId = packet.getShortA() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		case Equipment.INTERFACE:
			if(slot >= 0 && slot < Equipment.SIZE) { 
				if(!Container.transfer(player.getEquipment(), player.getInventory(), slot, id)) {
					// indicate it failed
				}
			}
			break;
		/*case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, 1);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, 1);
			}
			break;*/
		}
	}
	
	/**
	 * Handles item option 2.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption2(Player player, Packet packet) {
		int interfaceId = packet.getLEShortA() & 0xFFFF;
		int id = packet.getLEShortA() & 0xFFFF;
		int slot = packet.getLEShort() & 0xFFFF;
		
		switch(interfaceId) {
		/*case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, 5);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, 5);
			}
			break;*/
		}
	}
	
	/**
	 * Handles item option 3.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption3(Player player, Packet packet) {
		int interfaceId = packet.getLEShort() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		/*case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, 10);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, 10);
			}
			break;*/
		}
	}
	
	/**
	 * Handles item option 4.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption4(Player player, Packet packet) {
		int slot = packet.getShortA() & 0xFFFF;
		int interfaceId = packet.getShort() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		/*case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, player.getInventory().getCount(id));
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, player.getBank().getCount(id));
			}
			break;*/
		}
	}
	
	/**
	 * Handles item option 5.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption5(Player player, Packet packet) {
		int slot = packet.getLEShort() & 0xFFFF;
		int interfaceId = packet.getShortA() & 0xFFFF;
		int id = packet.getLEShort() & 0xFFFF;
		
		switch(interfaceId) {
		/*case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
			}
			break;*/
		}
	}
	
	/**
	 * Handles the click 1 opcode.
	 * @param player The player clicking the item.
	 * @param packet The packet.
	 */
	private void handleItemOptionClick1(Player player, Packet packet) {
		int interfaceId = packet.getInt1() >> 16;
		int slot = packet.getLEShort();
		int id = packet.getLEShort();
		Item item = player.getInventory().get(slot);
		if (player.isDead()) {
			return;
		}
		
		player.getActionSender().sendDebugPacket(
				packet.getOpcode(),
				"ItemClick1",
				new Object[] { "ID: " + id,
						"Interface: " + interfaceId + "Slot:" + slot });

		switch(interfaceId) {
		case Inventory.INTERFACE:
			BoneBurying.buryBone(player, slot);
			break;
		}
		
	}
	
	/**
	 * Creates a ground item at the player's location.
	 * @param player The player dropping the item.
	 * @param packet The packet.
	 */
	private void handleDropItem(Player player, Packet packet) {
		int interfaceId = packet.getInt2(); //Doesn't read right.
		int id = packet.getShortA();
		int slot = packet.getLEShort();
		
		if (Constants.DEV_MODE) {
			System.out.println(player.getName() + " dropped a ground item. Id: " + id);
		}
		
		Item item = player.getInventory().get(slot);
		if (item != null) {
			GroundItem groundItem = new GroundItem(item, player, player.getLocation(), player.getRegion(), false);
			player.getInventory().set(slot, null);
			player.getActionSender().sendGroundItem(groundItem);
			player.getRegion().addGroundItem(groundItem);
		}
	}
	
	/**
	 * Handles the pickup action of a ground item.
	 * @param player The player pickup up the item.
	 * @param packet The packet.
	 */
	private void handlePickupGroundItem(Player player, Packet packet) {
		int id = packet.getShort();
		int yPos = packet.getShortA();
		int xPos = packet.getLEShortA();
		
		if (Constants.DEV_MODE) {
			System.out.println(player.getName() + " attempted ground item pickup. Id: " + id + " [" + xPos + ", " + yPos + "]");
		}
		
		Location loc = Location.create(xPos, yPos, player.getLocation().getZ());
		for (GroundItem groundItem : player.getRegion().getGroundItems()) {
			if (groundItem.getItem().getId() == id && groundItem.getLocation().equals(loc)) {
				if (player.getInventory().add(groundItem.getItem())) {
					player.getRegion().removeGroundItem(groundItem);
				} else {
					player.getActionSender().sendMessage("You have no room in your inventory.");
				}
			}
		}
	}

}
