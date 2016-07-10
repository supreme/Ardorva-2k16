package org.hyperion.rs2.net;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.impl.EquipmentContainerListener;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.container.impl.WeaponContainerListener;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.PlayerConfiguration;
import org.hyperion.rs2.model.region.Mapdata;
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

		sendMessage("<img=1></img> Welcome to " + Constants.SERVER_NAME + ".");

		sendInteractionOption("null", 1, true); // null or attack
		sendInteractionOption("null", 2, false); // challenge = duel arena only
		sendInteractionOption("Follow", 3, false);
		sendInteractionOption("Trade with", 4, false);

//		player.getPlayerVariables().handleCounter(); TODO

		sendSkills();
		sendSidebarInterfaces();
		//sendPlayerConfiguration();

		InterfaceContainerListener inventoryListener = new InterfaceContainerListener(player, Inventory.INTERFACE, 0, 93); //client side -> type 2
		player.getInventory().addListener(inventoryListener);

		InterfaceContainerListener equipmentListener = new InterfaceContainerListener(player, Equipment.INTERFACE, 28, 94); //-1, 64208
		player.getEquipment().addListener(equipmentListener);
		player.getEquipment().addListener(new EquipmentContainerListener(player));
		player.getEquipment().addListener(new WeaponContainerListener(player));

		//player.getBonuses().refresh();
		//player.getCombatUtility().refresh();
		
		

		//player.getEquipment().set(Equipment.SLOT_CAPE, new Item(6570, 1));


		if (player.getName().equals("Stephen")) {
			player.setRights(Player.Rights.ADMINISTRATOR);
		}
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
	 * Sends the player's configuration.
	 */
	private ActionSender sendPlayerConfiguration() {
		PlayerConfiguration config = player.getPlayerConfiguration();
		if (config == null) {
			player.getActionSender().sendMessage("We're sorry, we were unable to load your previous configuration.");
			return this;
		}

		sendString(config.getWeaponTabInterface(), 0, config.getWeaponName());
		sendTab(86, config.getWeaponTabInterface());
		return this;
	}

	/**
	 * Saves the player's configuration.
	 */
	private ActionSender savePlayerConfiguration() {
		PlayerConfiguration config = player.getPlayerConfiguration();
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);

		config.setWeaponName(weapon != null ? weapon.getDefinition().getName() : "Unarmed");
		//Weapon tab id is set in wield packet handler
		return this;
	}

	/**
	 * Sends a debug message.
	 * @param message The message to send.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendDebugMessage(String message) {
		return Constants.DEV_MODE ? sendMessage("<col=ff0000>" + message) : this;
	}

	/**
	 * Sends a debug message.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendDebugPacket(int opCode, String description, Object[] params) {
		String paramString = "";
		for(Object object : params) {
			paramString += object.toString() + "    ";
		}
		return sendDebugMessage("------------------------------------------------------------------------------------------")
				.sendDebugMessage("Pkt            " + opCode + "  " + description)
				.sendDebugMessage("------------------------------------------------------------------------------------------")
				.sendDebugMessage("Params    " + paramString)
				.sendDebugMessage("------------------------------------------------------------------------------------------");
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
		if(skill == Skills.PRAYER) {
			bldr.put((byte) Math.ceil(player.getSkills().getPrayerPoints()));
		} else {
			bldr.put((byte) player.getSkills().getLevel(skill));
		}
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

	/**
	 * Moves a component's position.
	 * @param interfaceId The interface id of the component.
	 * @param childId The component child id.
	 * @param x The x position to move to.
	 * @param y The y position to move to.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendComponentPosition(int interfaceId, int childId, int x, int y) {
		player.getSession().write(new PacketBuilder(201).putLEInt(interfaceId << 16 | childId).putShortA(y).putShort(x).toPacket());
		return this;
	}

	public ActionSender sendWindowPane(int pane) {
		player.getSession().write(new PacketBuilder(77).putLEShortA(pane).toPacket());
		return this;
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
				int region = yCalc + (xCalc << 8);
				int[] data = Mapdata.getData(region);
				//int[] data = XTEALoader.xteas.get(region);
				pb.putInt(data[0]);
				pb.putInt(data[1]);
				pb.putInt(data[2]);
				pb.putInt(data[3]);
			}
		}
		pb.putLEShort(player.getLocation().getRegionX());
		pb.putShort(player.getLocation().getLocalX());
		pb.put((byte) player.getLocation().getZ());
		pb.putShort(player.getLocation().getLocalY());
		player.getSession().write(pb.toPacket());

		/* Send all the ground items in the region */
		for (GroundItem item : player.getRegion().getGroundItems()) {
			if (item.isGlobal() || item.getOwner().equals(player)) {
				sendGroundItem(item);
			}
		}

		/* Send all the objects in the region */
		for (GameObject object : player.getRegion().getGameObjects()) {
			if (object.getAction() == GameObject.ADD) {
				sendObject(object);
			} else if (object.getAction() == GameObject.REMOVE) {
				removeObject(object);
			}
		}

		return this;
	}

	/**
	 * Sends all the sidebar interfaces.
	 * @return The action sender instance, for chaining.
	 */
	/**
	 * Sends all the sidebar interfaces.
	 * 
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendSidebarInterfaces() {
		int magic_book = 218;
		sendInterface(Constants.MAIN_WINDOW, 128, 137, true);//player name on chatbox
		int start_pos = 135;
		sendSidebarTab(start_pos++, 593);//attack 
		sendSidebarTab(start_pos++, 320);//skills
		sendSidebarTab(start_pos++, 274);//quests
		sendSidebarTab(start_pos++, 149);//inv
		sendSidebarTab(start_pos++, 387);//equipment
		sendSidebarTab(start_pos++, 271);//prayers
		sendSidebarTab(start_pos++, magic_book);//MAGIC
		sendSidebarTab(start_pos++, 589);//cc
		sendSidebarTab(start_pos++, 429);//friends
		sendSidebarTab(start_pos++, 432);//ignores
		sendSidebarTab(start_pos++, 182);//logout
		sendSidebarTab(start_pos++, 261);//settings
		sendSidebarTab(start_pos++, 216);//emotes
		sendSidebarTab(start_pos++, 239);//music
		return this;
	}
	
	/**
	 * Sends a sidebar tab
	 * @param pos
	 * @param child
	 * @return
	 */
	public ActionSender sendSidebarTab(int pos, int child) {
		sendInterface(Constants.MAIN_WINDOW, pos, child, true);
		return this;
	}

	public ActionSender sendTab(int tabId, int childId) {
		sendInterface(548, tabId, childId, true);
		return this;
	}

	/**
	 * Sends an interface to the player's screen.
	 * @param interfaceId The id of the interface to display.
	 */
	public ActionSender sendInterface(int interfaceId) {
		sendInterface(548, 104, interfaceId, false);
		return this;
	}

	/**
	 * Sends an interface to the player's inventory.
	 * @param childId The child id of the interface to display.
	 */
	public ActionSender sendInventoryInterface(int childId) {
		sendInterface(548, 84, childId, false);
		return this;
	}

	/**
	 * Sends an interface to the player's chat box.
	 * @param childId The child id of the interface to display.
     */
	public void sendChatBoxInterface(int childId) {
		sendInterface(548, 79, childId, false);
	}

	/**
	 * Sends an interface.
	 * @param windowId The id of the window.
	 * @param position The position.
	 * @param interfaceId The interface id.
	 * @param walkable Whether or not the interface is walkable.
	 */
	public ActionSender sendInterface(int windowId, int position, int interfaceId, boolean walkable) {
		PacketBuilder pb = new PacketBuilder(238);
		pb.putInt1((windowId << 16) | position);
		pb.putShort(interfaceId);
		pb.putByteC(walkable ? 1 : 0);
		player.getSession().write(pb.toPacket());
		return this;
	}

	/**
	 * Displays an interface.
	 */
	public ActionSender displayInterface(int id) {
		player.getInterfaceState().interfaceOpened(id);
		sendInterface(548, 104, id, false);
		return this;
	}

	/**
	 * Sends the logout packet.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendLogout() {
		player.getSession().write(new PacketBuilder(167).toPacket()).addListener(future -> {
            savePlayerConfiguration();
            PlayerConfiguration.serialize(player, player.getPlayerConfiguration());
            future.getSession().close(false);
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
	 * Sends the enter amount interface.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendEnterAmountInterface() {
		player.getActionSender().sendRunScript(Constants.NUMERICAL_INPUT_INTERFACE, new Object[] { "Enter amount:" }, "s");
		return this;
	}

	/**
	 * Sends the enter amount interface.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendEnterTextInterface(String question) {
		player.getActionSender().sendRunScript(Constants.ALPHA_NUMERICAL_INPUT_INTERFACE, new Object[] { question }, "s");
		return this;
	}

	/**
	 * Sends a clientscript to the client.
	 * @param id The id.
	 * @param params Any parameters in the scrips.
	 * @param types The script types
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendRunScript(int id, Object[] params, String types) {
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
		return this;
	}

	/**
	 * Not sure why this one is needed as well...
	 * @param scriptId
	 * @param params
	 */
	public void sendRunScript(int scriptId, Object[] params) {
		PacketBuilder bldr = new PacketBuilder(69, Type.VARIABLE_SHORT);
		String parameterTypes = "";
		for(int count = params.length-1; count >= 0; count--) {
			if(params[count] instanceof String)
				parameterTypes += "s"; //string
			else
				parameterTypes += "i"; //integer
		}
		bldr.putRS2String(parameterTypes);
		int index = 0;
		for (int count = parameterTypes.length() - 1;count >= 0;count--) {
			if (parameterTypes.charAt(count) == 's')
				bldr.putRS2String((String) params[index++]);
			else
				bldr.putInt((Integer) params[index++]);
		}
		bldr.putInt(scriptId);
		player.write(bldr.toPacket());
	}

	/**
	 * Sends a system update.
	 * @param time The time until the update.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendSystemUpdate(int time) {
		player.getSession().write(new PacketBuilder(30).putShortA(time).toPacket());
		return this;
	}

	/**
	 * Sends an animation of an interface.
	 * @param emoteId The emote id.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendInterfaceAnimation(int emoteId, int interfaceId, int childId) {
		player.getSession().write(new PacketBuilder(63).putInt2(interfaceId <<  16 | childId).putLEShort(emoteId).toPacket());
		return this;
	}

	/**
	 * Sends the player's head onto an interface.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendPlayerHead(int interfaceId, int childId) {
		player.getSession().write(new PacketBuilder(8).putLEInt(interfaceId << 16 | childId).toPacket());
		return this;
	}

	/**
	 * Sends an NPC's head onto an interface.
	 * @param npcId The NPC's id.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendNPCHead(int npcId, int interfaceId, int childId) {
		player.getSession().write(new PacketBuilder(207).putLEShortA(npcId).putInt(interfaceId << 16 | childId).toPacket());
		return this;
	}

	/**
	 * Sends a projectile to a location.
	 * @param start The starting location.
	 * @param finish The finishing location.
	 * @param id The graphic id.
	 * @param delay The delay before showing the projectile.
	 * @param angle The angle the projectile is coming from.
	 * @param speed The speed the projectile travels at.
	 * @param startHeight The starting height of the projectile.
	 * @param endHeight The ending height of the projectile.
	 * @param lockon The lockon index of the projectile, so it follows them if they move.
	 * @param slope The slope at which the projectile moves.
	 * @param radius The radius from the centre of the tile to display the projectile from.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendProjectile(Location start, Location finish, int id,
									   int delay, int angle, int speed, int startHeight, int endHeight,
									   int lockon, int slope, int radius) {

		int offsetX = (start.getX() - finish.getX()) * -1;
		int offsetY = (start.getY() - finish.getY()) * -1;
		sendArea(start, -3, -2);

		PacketBuilder bldr = new PacketBuilder(218);
		bldr.put((byte) angle);
		bldr.put((byte) offsetX);
		bldr.put((byte) offsetY);
		bldr.putShort(lockon);
		bldr.putShort(id);
		bldr.put((byte) startHeight);
		bldr.put((byte) endHeight);
		bldr.putShort(delay);
		bldr.putShort(speed);
		bldr.put((byte) slope);
		bldr.put((byte) radius);
		player.getSession().write(bldr.toPacket());
		return this;
	}

	/**
	 * Removes the chatbox interface.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender removeChatboxInterface() {
		player.getSession().write(new PacketBuilder(137).putInt(Constants.MAIN_WINDOW << 16 | Constants.CHAT_BOX).toPacket()); //chat box screen
		player.getActionSender().sendRunScript(Constants.REMOVE_INPUT_INTERFACE, new Object[] { "" }, "");
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
	 * Creates an object in the game world.
	 * @param object The object to create.
	 */
	public ActionSender sendObject(GameObject object) {
		sendArea(object.getLocation());
		PacketBuilder bldr = new PacketBuilder(17);
		bldr.putByteA((byte) 0);
		bldr.putLEShort(object.getId());
		bldr.putByteA((byte)((object.getType() << 2) + (object.getFace() & 3)));
		player.getSession().write(bldr.toPacket());
		return this;
	}

	/**
	 * Removes an object from the game world.
	 * @param object The object to remove.
	 */
	public ActionSender removeObject(GameObject object) {
		sendArea(object.getLocation());
		PacketBuilder bldr = new PacketBuilder(16);
		int ot = ((object.getType() << 2) + (object.getFace() & 3));
		bldr.putByteA((byte) ot);
		bldr.putByteA((byte) 0);
		player.getSession().write(bldr.toPacket());
		return this;
	}

	/**
	 * Sends a location to the client.
	 * @param location The location to send.
	 */
	public ActionSender sendArea(Location location) {
		PacketBuilder bldr = new PacketBuilder(132);
		int regionX = player.getLastKnownRegion().getRegionX();
		int regionY = player.getLastKnownRegion().getRegionY();
		bldr.put((byte) ((location.getY() - ((regionY-6) * 8))));
		bldr.put((byte) ((location.getX() - ((regionX-6) * 8))));
		player.getSession().write(bldr.toPacket());
		return this;
	}

	/**
	 * Sends your location to the client.
	 * @param location The location.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendArea(Location location, int xOffset, int yOffset) {
		PacketBuilder bldr = new PacketBuilder(132);
		int regionX = player.getLastKnownRegion().getRegionX(), regionY = player.getLastKnownRegion().getRegionY();
		bldr.put((byte) ((location.getY() - ((regionY-6) * 8)) + yOffset));
		bldr.put((byte) ((location.getX() - ((regionX-6) * 8)) + xOffset));
		player.write(bldr.toPacket());
		return this;
	}

	/**
	 * Removes a ground item from the world.
	 * @param item The item to remove.
	 */
	public ActionSender clearGroundItem(GroundItem item) {
		if (item != null) {
			sendArea(item.getLocation());
			PacketBuilder bldr = new PacketBuilder(39);
			bldr.putShortA(item.getItem().getId());
			bldr.putByteS((byte) 0);
			player.getSession().write(bldr.toPacket());
		}
		return this;
	}

	/**
	 * Sends a ground item to the world.
	 * @param item The item to send.
	 */
	public ActionSender sendGroundItem(GroundItem item) {
		if (item != null) {
			sendArea(item.getLocation());
			PacketBuilder bldr = new PacketBuilder(112);
			bldr.putShort(item.getItem().getId());
			bldr.putLEShort(item.getItem().getCount());
			bldr.putByteS((byte) 0);
			player.getSession().write(bldr.toPacket());
		}
		return this;
	}

	/**
	 * Assuming it clears the red flags on minimap for walking queue?
	 */
	public ActionSender clearMapFlag() {
		player.getSession().write(new PacketBuilder(68).toPacket());
		return this;
	}

	/**
	 * Sends a sound to the client.
	 * @param sound The sound to play.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender playSound(Sound sound) {
		PacketBuilder bldr = new PacketBuilder(40);
		bldr.putShort(sound.getId()).put(sound.getVolume()).putShort(sound.getDelay());
		player.getSession().write(bldr.toPacket());
		return this;
	}

	public ActionSender turnCameraLocation(int localX, int localY, int height, int constantSpeed, int variableSpeed) {
		PacketBuilder bldr = new PacketBuilder(82);
		bldr.put((byte)localX);
		bldr.put((byte)localY);
		bldr.putShort(height);
		bldr.put((byte)constantSpeed);
		bldr.put((byte)variableSpeed);
		return this;
	}

	public ActionSender moveCameraLocation(int localX, int localY, int height, int constantSpeed, int variableSpeed) {
		PacketBuilder bldr = new PacketBuilder(113);
		bldr.put((byte)localX);
		bldr.put((byte)localY);
		bldr.putShort(height);
		bldr.put((byte)constantSpeed);
		bldr.put((byte)variableSpeed);
		return this;
	}

	/**
	 * Not sure?
	 * @param set
	 * @param interfaceId
	 * @param window
	 * @param start
	 * @param end
	 */
	public ActionSender sendAccessMask(int set, int interfaceId, int window, int start, int end) {
		PacketBuilder bldr = new PacketBuilder(254);
		bldr.putLEShort(end);
		bldr.putInt(interfaceId << 16 | window);
		bldr.putShortA(start);
		bldr.putInt1(set);
		player.getSession().write(bldr.toPacket());
		return this;
	}

	/**
	 * Displays the enter amount interface in the chatbox.
	 * @param text The player's inputted text.
	 */
	public ActionSender displayEnterAmount(String text) {
		Object[] o = {text};
		sendClientScript(108, o, "s");
		return this;
	}

	/**
	 * Invokes a client script.
	 * @param id The script id.
	 * @param params
	 * @param types
	 */
	public ActionSender sendClientScript(int id, Object[] params, String types) {
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
		return this;
	}

	public ActionSender sendConfig(int id, int value) {
		if(value < 128 && value > -128) {
			PacketBuilder bldr = new PacketBuilder(245);
			bldr.putShortA(id);
			bldr.put((byte) value);
			player.getSession().write(bldr.toPacket());
		} else {
			PacketBuilder bldr = new PacketBuilder(37);
			bldr.putShortA(id);
			bldr.putLEInt(value);
			player.getSession().write(bldr.toPacket());
		}
		return this;
	}
}