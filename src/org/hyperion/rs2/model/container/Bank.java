package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.player.Player;

/**
 * Banking utility class.
 * TODO: Premium members have 400 spaces while f2p have 200.
 * @author Graham Edgecombe
 * @author Stephen Andrews
 */
public class Bank {

	/**
	 * The bank size.
	 */
	public static final int SIZE = 400;

	/**
	 * The bank inventory interface.
	 */
	public static final int BANK_INVENTORY_INTERFACE = 15;
	
	/**
	 * The bank interface.
	 */
	public static final int BANK_INTERFACE = 12;
	
	/**
	 * Opens the bank for the specified player.
	 * @param player The player to open the bank for.
	 */
	public static void open(Player player) {
		player.getBank().shift();
		player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, 15, 0, 93));
		player.getInventory().getContainerInterface().setInterfaceId(15);
		player.getInventory().getContainerInterface().setChildId(0);
		player.getInventory().getContainerInterface().setType(93);
		player.getActionSender().sendInventoryInterface(BANK_INVENTORY_INTERFACE);
		player.getActionSender().displayInterface(BANK_INTERFACE);
		player.getPlayerVariables().setBanking(true);
		refresh(player);
	}
	
	/**
	 * Refreshes a player's bank updating the item count and items in the container.
	 * @param player The player whose bank we're refreshing.
	 */
	private static void refresh(Player player) {
		player.getBank().refresh();
		player.getInventory().refresh();
		player.getActionSender().sendString(BANK_INTERFACE, 23, "" + player.getBank().freeSlots());
	}
	
	/**
	 * Withdraws an item.
	 * @param player The player.
	 * @param slot The slot in the player's inventory.
	 * @param id The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void withdraw(Player player, int slot, int id, int amount) {
		Item item = player.getBank().get(slot);
		if(item == null) {
			return; // invalid packet, or client out of sync
		}
		if(item.getId() != id) {
			return; // invalid packet, or client out of sync
		}
		
		int transferAmount = item.getCount();
		if(transferAmount >= amount) {
			transferAmount = amount;
		} else if(transferAmount == 0) {
			return; // invalid packet, or client out of sync
		}
		
		int withdrawingId = item.getId(); // TODO deal with withdraw as notes!
		if(player.getSettings().isWithdrawingAsNotes()) {
			if(item.getDefinition().getNotedId() != -1) {
				withdrawingId = item.getDefinition().getNotedId();
			}
		}
		
		ItemDefinition def = ItemDefinition.forId(withdrawingId);
		if(def.isStackable()) {
			if(player.getInventory().freeSlots() <= 0 && player.getInventory().getById(withdrawingId) == null) {
				player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
			}
		} else {
			int free = player.getInventory().freeSlots();
			if(transferAmount > free) {
				player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
				transferAmount = free;
			}
		}
		
		//Adding the item to the inventory
		if(player.getInventory().add(new Item(withdrawingId, transferAmount))) {
			// all items in the bank are stacked, makes it very easy!
			int newAmount = item.getCount() - transferAmount;
			if(newAmount <= 0) {
				player.getBank().set(slot, null);
			} else {
				player.getBank().set(slot, new Item(item.getId(), newAmount));
			}
		} else {
			player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
		}
		
		//Lastly, refresh the interfaces
		refresh(player);
	}
	
	/**
	 * Deposits an item.
	 * @param player The player.
	 * @param slot The slot in the player's inventory.
	 * @param id The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void deposit(Player player, int slot, int id, int amount) {
		Item item = player.getInventory().get(slot);
		if(item == null) {
			return; // invalid packet, or client out of sync
		}
		if(item.getId() != id) {
			return; // invalid packet, or client out of sync
		}
		
		int transferAmount = player.getInventory().getCount(id);
		if(transferAmount >= amount) {
			transferAmount = amount;
		} else if(transferAmount == 0) {
			return; // invalid packet, or client out of sync
		}
		
		boolean noted = item.getDefinition().isNoted();
		if(item.getDefinition().isStackable() || noted) {
			int bankedId = noted ? item.getDefinition().getUnNotedId() : item.getId();
			if(player.getBank().freeSlots() < 1 && player.getBank().getById(bankedId) == null) {
				player.getActionSender().sendMessage("You don't have enough space in your bank account.");
				return;
			}
			
			// we only need to remove from one stack
			int newInventoryAmount = item.getCount() - transferAmount;
			Item newItem;
			if(newInventoryAmount <= 0) {
				newItem = null;
			} else {
				newItem = new Item(item.getId(), newInventoryAmount);
			}
			
			if(!player.getBank().add(new Item(bankedId, transferAmount))) {
				player.getActionSender().sendMessage("You don't have enough space in your bank account.");
				return;
			} else {
				if (newItem == null) {
					player.getInventory().set(slot, null);
				} else {
					player.getInventory().remove(slot, newItem);
				}
			}
		} else {
			if(player.getBank().freeSlots() < transferAmount) {
				player.getActionSender().sendMessage("You don't have enough space in your bank account.");
				return;
			}
			
			if(!player.getBank().add(new Item(item.getId(), transferAmount))) {
				player.getActionSender().sendMessage("You don't have enough space in your bank account.");
				return;
			} else {
				// we need to remove multiple items
				for(int i = 0; i < transferAmount; i++) {
					if(i == 0) {
						player.getInventory().set(slot, null);
					} else { 
						player.getInventory().set(player.getInventory().getSlotById(item.getId()), null);
					}
				}
			}
		}
		
		refresh(player);
	}

}
