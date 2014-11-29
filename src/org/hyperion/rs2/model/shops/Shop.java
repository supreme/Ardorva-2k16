package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.player.Player;

/**
 * Represents a shop.
 * @author Stephen Andrews
 * @author Brendan Dodd
 */
public class Shop {
	
	
	/**
	 * Shop id
	 */
	private int id;
	
	/**
	 * Shop name
	 */
	private String name;
	
	/**
	 * Shop contents
	 */
	private ShopContents contents;
	
	/**
	 * Gets the shop id
	 * @return The shop id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name of the shop.
	 * @return The name of the shop or <code>null</code> if not overridden.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the shop contents
	 * @return The ShopContents object
	 */
	public ShopContents getContents() {
		return contents;
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
	 * Gets the price of an item.
	 * @param item The item to look at.
	 * @param current The current quantity the shop has.
	 * @param max The max quantity the shop has at one time.
	 * @return The price of the item.
	 */
	public int getItemPrice(int itemId) {
		int price = -1;
		
		for(ShopItem item : this.getContents().getItems()) {
			if(item.id == itemId) {
				int maxStock = item.max;
				int currentStock = item.stock;
				int difference = maxStock - currentStock;
				price = ItemDefinition.forId(item.id).getValue();
				if (difference != 0) {
					price += price * (difference/1000);
				}
				
				return price;
			}
		}
		return price;
	}
	
	/**
	 * The contents of a shop.
	 * @author Stephen
	 * @author Brendan Dodd
	 */
	public class ShopContents {
		
		/**
		 * The items in the shop.
		 */
		private ShopItem[] items;
		
		/**
		 * Constructs a ShopContents object.
		 * @param items The items in the shop.
		 */
		public ShopContents(ShopItem[] items) {
			this.items = items;
		}
		
		/**
		 * Gets the shop items.
		 * @return The shop items.
		 */
		public ShopItem[] getItems() {
			return items;
		}
		
		/**
		 * Gets the length of the contents array
		 * @return Length of contents array
		 */
		public int getLength() {
			return items.length;
		}
	}
	
	/**
	 * The structure of a shop item.
	 * @author Stephen
	 * @author Brendan Dodd
	 */
	public class ShopItem {
			
		/**
		 * The id of the item.
		 */
		private int id;
		
		/**
		 * The amount of the item in stock.
		 */
		private int stock;
		
		/**
		 * The max amount the shop can have of the item.
		 */
		private int max;
		
		/**
		 * Constructs an Item object.
		 * @param itemId The id of the item.
		 * @param stock The amount of the item in stock.
		 * @param cost The cost of the item per unit.
		 * @param max The max amount the shop can have of the item.
		 */
		public ShopItem(int id, int stock, int max) {
			this.id = id;
			this.stock = stock;
			this.max = max;
		}
		
		/**
		 * Gets the id of the item.
		 * @return The item id.
		 */
		public int getId() {
			return id;
		}
		
		/**
		 * Gets the amount of the item in stock.
		 * @return The stock of the item.
		 */
		public int getStock() {
			return stock;
		}
		
		/**
		 * Gets the max amount the shop can have of the item.
		 * @return The max amount the shop can have of the item.
		 */
		public int getMaxStock() {
			return max;
		}
	}
}
