package org.hyperion.rs2.net;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Palette;
import org.hyperion.rs2.model.Palette.PaletteTile;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.impl.EquipmentContainerListener;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.container.impl.WeaponContainerListener;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet.Type;

/**
 * A utility class for sending packets.
 * @author Graham Edgecombe
 *
 */
public class ActionSender {
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * Creates an action sender for the specified player.
	 * @param player The player to create the action sender for.
	 */
	public ActionSender(Player player) {
		this.player = player;
	}
	
	/**
	 * Sends all the login packets.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendLogin() {
		player.setActive(true);
		sendMapRegion();
		
		sendWindowPane(548);
		
		sendMessage("Welcome to " + Constants.SERVER_NAME + ".");
		sendMessage("We are currently in a pre-alpha state.");
		
		sendSkills();
		
		sendSideBarInterfaces();
		
		InterfaceContainerListener inventoryListener = new InterfaceContainerListener(player, Inventory.INTERFACE, 0, 93);
		player.getInventory().addListener(inventoryListener);
		
		InterfaceContainerListener equipmentListener = new InterfaceContainerListener(player, Equipment.INTERFACE, 28, 94);
		player.getEquipment().addListener(equipmentListener);
		player.getEquipment().addListener(new EquipmentContainerListener(player));
		player.getEquipment().addListener(new WeaponContainerListener(player));
		
		return this;
	}

	/**
	 * Sends the player's skills.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendSkills() {
		for(int i = 0; i < Skills.SKILL_COUNT; i++) {
			sendSkill(i);
		}
		return this;
	}
	
	/**
	 * Sends a specific skill.
	 * @param skill The skill to send.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendSkill(int skill) {
		PacketBuilder bldr = new PacketBuilder(190);
		bldr.putByteS((byte) skill);
		bldr.putLEInt((int) player.getSkills().getExperience(skill));
		bldr.put((byte) player.getSkills().getLevel(skill));
		player.write(bldr.toPacket());
		return this;
	}

	/**
	 * Sends a message.
	 * @param message The message to send.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendMessage(String message) {
		player.write(new PacketBuilder(108, Type.VARIABLE).putRS2String(message).toPacket());
		return this;
	}
	
	public void sendWindowPane(int pane) {
		player.getSession().write(new PacketBuilder(77).putLEShortA(pane).toPacket());
	}
	
	/**
	 * Sends the map region load command.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendMapRegion() {
		player.setLastKnownRegion(player.getLocation());
		PacketBuilder pb = new PacketBuilder(221, Type.VARIABLE_SHORT);
		pb.putShortA(player.getLocation().getRegionY());
		for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= (player.getLocation().getRegionX() + 6) / 8; xCalc++) {
			for (int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= (player.getLocation().getRegionY() + 6) / 8; yCalc++) {
				pb.putInt1(0);
				pb.putInt1(0);
				pb.putInt1(0);
				pb.putInt1(0);
			}
		}
		pb.putLEShort(player.getLocation().getRegionX());
		pb.putShort(player.getLocation().getLocalX());
		pb.put((byte) player.getLocation().getZ());
		pb.putShort(player.getLocation().getLocalY());
		player.getSession().write(pb.toPacket());
		
		for (GroundItem item : player.getRegion().getGroundItems()) {
			if (item.isGlobal() || item.getOwner().equals(player)) {
				sendGroundItem(item);
			}
		}
		return this;
	}
	
	public void sendSideBarInterfaces() {
		sendTab(77, 137);//chatbox
		sendTab(86, 92);
		sendTab(87, 320);
		sendTab(88, 274);
		sendTab(89, 149);
		sendTab(90, 387);
		sendTab(91, 271);
		sendTab(92, 192);
		sendTab(94, 550);
		sendTab(95, 551);
		sendTab(96, 182);
		sendTab(97, 261);
		sendTab(98, 464);
		sendTab(99, 239);
	}
	
	public void sendTab(int tabId, int childId) {
		sendInterface(548, tabId, childId, true);
	}
	
	/**
	 * Sends an interface to the player's screen.
	 * @param interfaceId The id of the interface to display.
	 */
	public void sendInterface(int interfaceId) {
		sendInterface(548, 64, interfaceId, false);
	}
	
	/**
	 * Sends an interface to the player's inventory.
	 * @param childId The child id of the interface to display.
	 */
	public void sendInventoryInterface(int childId) {
		sendInterface(548, 84, childId, false);
	}
	
	/**
	 * Displays an interface.
	 * @param id The id of the interface to display.
	 * TODO: The difference between send and display? 2nd param is 62 as opposed to 64.
	 */
	public void displayInterface(int id) {
		player.getInterfaceState().interfaceOpened(id);
		sendInterface(548, 62, id, false);
    }
	
	/**
	 * Sends an interface.
	 * @param windowId The id of the window.
	 * @param position The position.
	 * @param interfaceId The interface id.
	 * @param walkable Whether or not the interface is walkable.
	 */
	public void sendInterface(int windowId, int position, int interfaceId, boolean walkable) {
		PacketBuilder pb = new PacketBuilder(238);
		pb.putInt1((windowId << 16) | position);
		pb.putShort(interfaceId);
		pb.putByteC(walkable ? 1 : 0);
		player.getSession().write(pb.toPacket());
	}
	
	/**
	 * Sends the logout packet.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendLogout() {
		player.getSession().write(new PacketBuilder(167).toPacket()).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture arg0) {
				arg0.getSession().close(false);
			}
		});
		return this;
	}
	
	/**
	 * Sends a packet to update a group of items.
	 * @param interfaceId The interface id.
	 * @param items The items.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendUpdateItems(int interfaceId, int child, int type, Item[] items) {
		PacketBuilder bldr = new PacketBuilder(92, Type.VARIABLE_SHORT);
		bldr.putInt(interfaceId << 16 | child);
		bldr.putShort(type);
		bldr.putShort(items.length);
		for(Item item : items) {
			if(item != null) {
				int count = item.getCount();
				if(count > 254) {
					bldr.putByteC((byte) 255);
					bldr.putInt(count);
				} else {
					bldr.putByteC((byte) count);
				}
				bldr.putLEShort(item.getId() + 1);
			} else {
				bldr.putByteC((byte) 0);
				bldr.putLEShort(0);
			}
		}
		player.write(bldr.toPacket());
		return this;
	}

	/**
	 * Sends a packet to update a single item.
	 * @param interfaceId The interface id.
	 * @param slot The slot.
	 * @param item The item.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendUpdateItem(int interfaceId, int child, int type, int slot, Item item) {
		PacketBuilder bldr = new PacketBuilder(120, Type.VARIABLE_SHORT);
		bldr.putInt(interfaceId << 16 | child).putShort(type).putSmart(slot);
		if(item != null) {
			bldr.putShort(item.getId() + 1);
			int count = item.getCount();
			if(count > 254) {
				bldr.put((byte) 255);
				bldr.putInt(count);
			} else {
				bldr.put((byte) count);
			}
		} else {
			bldr.putShort(0);
			bldr.put((byte) -1);
		}
		player.write(bldr.toPacket());
		return this;
	}
	
	/**
	 * Sends a packet to update multiple (but not all) items.
	 * @param interfaceId The interface id.
	 * @param slots The slots.
	 * @param items The item array.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendUpdateItems(int interfaceId, int child, int type, int[] slots, Item[] items) {
		PacketBuilder bldr = new PacketBuilder(120, Type.VARIABLE_SHORT);
		bldr.putInt(interfaceId << 16 | child).putShort(type);
		for(int i = 0; i < slots.length; i++) {
			Item item = items[slots[i]];
			bldr.putSmart(slots[i]);
			if(item != null) {
				bldr.putShort(item.getId() + 1);
				int count = item.getCount();
				if(count > 254) {
					bldr.put((byte) 255);
					bldr.putInt(count);
				} else {
					bldr.put((byte) count);
				}
			} else {
				bldr.putShort(0);
				bldr.put((byte) -1);
			}
		}
		player.write(bldr.toPacket());
		return this;
	}

	/**
	 * Sends the player an option.
	 * @param slot The slot to place the option in the menu.
	 * @param top Flag which indicates the item should be placed at the top.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendInteractionOption(String option, int slot, boolean top) {
		PacketBuilder bldr = new PacketBuilder(72, Type.VARIABLE);
		bldr.putRS2String(option);
		bldr.putByteS((byte) slot);
		bldr.putByteC(top ? (byte) 0 : (byte) 1);
		player.write(bldr.toPacket());
		return this;
	}

	/**
	 * Sends a string.
	 * @param id The interface id.
	 * @param string The string.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendString(int id, int child, String string) {
		PacketBuilder bldr = new PacketBuilder(47, Type.VARIABLE_SHORT);
		bldr.putInt1(id << 16 | child);
		bldr.putRS2String(string);
		player.write(bldr.toPacket());
		return this;
	}
	
	/**
	 * Sends an area.
	 * @param location
	 */
	public void sendArea(Location location) {
		PacketBuilder bldr = new PacketBuilder(132);
		int regionX = player.getLastKnownRegion().getRegionX();
		int regionY = player.getLastKnownRegion().getRegionY();
		bldr.put((byte) ((location.getY() - ((regionY-6) * 8))));
		bldr.put((byte) ((location.getX() - ((regionX-6) * 8))));
		player.getSession().write(bldr.toPacket());
	}
	
	/**
	 * Removes a ground item from the world.
	 * @param item The item to remove.
	 */
	public void clearGroundItem(GroundItem item) {
		if (item != null) {
			sendArea(item.getLocation());
			PacketBuilder bldr = new PacketBuilder(39);
			bldr.putShortA(item.getItem().getId());
			bldr.putByteS((byte) 0);
			player.getSession().write(bldr.toPacket());
		}
	}
	
	/**
	 * Sends a ground item to the world.
	 * @param item The item to send.
	 */
	public void sendGroundItem(GroundItem item) {
		if (item != null) {
			sendArea(item.getLocation());
			PacketBuilder bldr = new PacketBuilder(112);
			bldr.putShort(item.getItem().getId());
			bldr.putLEShort(item.getItem().getCount());
			bldr.putByteS((byte) 0);
			player.getSession().write(bldr.toPacket());
		}
	}
	
	/**
	 * Submits an action.
	 */
	public void submitAction() {
		//
	}
	
	public void displayEnterAmount(String text) {
		Object[] o = {text};
		sendClientScript(108, o, "s");
	}
	
	public void sendClientScript(int id, Object[] params, String types) {
		PacketBuilder bldr = new PacketBuilder(69, Type.VARIABLE_SHORT);
		bldr.putRS2String(types);
		if(params.length > 0) {
			int j = 0;
			for (int i = types.length() - 1; i >= 0; i--, j++) {
				if (types.charAt(i) == 115) {
					bldr.putRS2String((String) params[j]);
				} else {
					bldr.putInt((Integer) params[j]);
				}
			}
		}
		bldr.putInt(id);
		player.write(bldr.toPacket());
	}
	
}