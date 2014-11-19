package org.hyperion.rs2.content.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.player.Player;

/**
 * Represents a shop.
 * @author Stephen Andrews
 */
public abstract class Shop {
	
	/**
	 * The interface id for a shop.
	 */
	public static final int SHOP_INTERFACE = 300; 
	
	/**
	 * The action of purchasing an item from a player.
	 * @param container The container of the shop.
	 * @param player The player selling the item to the shop.
	 */
	public void buyItem(Container container, Player player) {
		//TODO
	}
	
	/**
	 * The action of selling an item to a player.
	 * @param container The container to sell from.
	 * @param player The player to sell the item to.
	 */
	public void sellItem(Container container, Player player) {
		//TODO
	}
	
	/**
	 * Gets the name of the shop.
	 * @return The name of the shop or <code>null</code> if not overridden.
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * Gets the default contents of a shop.
	 * By default, this is set to null.
	 * @return The default contents of the shop or <code>null</code> if none.
	 */
	public Container getDefaultContents() {
		return null;
	} 
	
	/**
	 * Whether or not the shop automatically restocks its items.
	 * By default, this is set to false.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean selfReplenishing() {
		return false;
	}
	
	/**
	 * Whether or not the shop is a general store.
	 * By default, this is set to false.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isGeneralStore() {
		return false;
	}
	
	/**
	 * Gets the price of an item.
	 * @param item The item to look at.
	 * @param current The current quantity the shop has.
	 * @param max The max quantity the shop has at one time.
	 * @return The price of the item.
	 */
	public int getItemPrice(Item item, int current, int max) {
		int price = item.getDefinition().getValue();
		int difference = max - current;
		
		//Increase the price of the item based on the popularity (amount missing)
		if (difference != 0) {
			price += price * (difference/1000);
		}
		
		return price;
	}
}
