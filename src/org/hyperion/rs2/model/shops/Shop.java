package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.content.shops.ShopHandler;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Container;
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
	 * Buy an item from a shop
	 * @param container The container of the shop.
	 * @param player The player selling the item to the shop.
	 */
	synchronized public void buyItem(Player player, Item buyItem) {
		ShopItem shopItem = getContents().getShopItem(buyItem.getId());
		if(shopItem == null || shopItem.getStock() <= 0)
			return;
		
		//Check shop stock levels
		if(shopItem.getStock() < buyItem.getCount())
			buyItem = new Item(buyItem.getId(), Math.min(buyItem.getCount(), shopItem.getStock()));
		
		//Check players inventory for room
		if(!player.getInventory().hasRoomFor(buyItem)) {
			buyItem = new Item(buyItem.getId(), player.getInventory().freeSlots());
		}
		
		Item coinsRequired = new Item(995, shopItem.getValue()*buyItem.getCount());
		
		//Adjust buy amount for players coins
		if(player.getInventory().hasItem(new Item(995, shopItem.getValue()))) {
			//Player can afford 1 of item
			if(!player.getInventory().hasItem(coinsRequired)) {
				//Player cannot afford all of items requested
				Item playerCoins = player.getInventory().getById(995);
				int canAfford = playerCoins.getCount() / shopItem.getValue();
				buyItem = new Item(buyItem.getId(), canAfford);
				coinsRequired = new Item(995, shopItem.getValue()*buyItem.getCount());
			}
		} else {
			player.getActionSender().sendMessage("You don't have enough coins to purchase that item.");
			return;
		}
		
		//Purchase
		if(player.getInventory().hasItem(coinsRequired)) { //Player has required coins to purchase item
			player.getInventory().remove(coinsRequired);
			player.getInventory().add(buyItem);
			shopItem.setItem(new Item(shopItem.getItem().getId(), (shopItem.getStock()-buyItem.getCount())));
			ShopHandler.refreshShop(player, this);
		}
	}
	
	/**
	 * Sell an item to a shop
	 * @param container The container to sell from.
	 * @param player The player to sell the item to.
	 */
	synchronized public void sellItem(Player player, Item sellItem) {
		
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
		
		public ShopItem getShopItem(int itemId) {
			try {
				for(ShopItem shopItem : items) {
					if(shopItem.getItem().getId() == itemId) {
						return shopItem;
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		
		public ShopItem getShopItem(Item item) {
			for(ShopItem shopItem : items) {
				if(shopItem.getItem().getId() == item.getId()) {
					return shopItem;
				}
			}
			return null;
		}
	}
	
	/**
	 * The structure of a shop item.
	 * @author Stephen
	 * @author Brendan Dodd
	 */
	public class ShopItem {
			
		/**
		 * The item
		 */
		private Item item;
		
		/**
		 * The max amount the shop can have of the item.
		 */
		private int defaultItemQuantity;
		
		/**
		 * Constructs an Item object.
		 * @param Item - item object
		 * @param Int - Default max item quantity of shop item
		 */
		public ShopItem(Item item, int defaultItemQuantity) {
			this.item = item;
			this.defaultItemQuantity = defaultItemQuantity;
		}
		
		/**
		 * Gets the child Item object
		 * @return The item.
		 */
		public Item getItem() {
			return item;
		}
		
		/**
		 * Sets the child Item object
		 */
		public void setItem(Item item) {
			this.item = item;
		}
		
		/**
		 * Gets default quantity of item when shop is new
		 * @return The quantity of the item
		 */
		public int getDefaultItemQuantity() {
			return defaultItemQuantity;
		}
		
		/**
		 * Gets current stock levels of item
		 * @return Current item quantity
		 */
		public int getStock() {
			return item.getCount();
		}
		
		/**
		 * Gets calculated value of Item in respect to stock levels
		 * @return Calculated value of Item
		 */
		public int getValue() {
			float difference = defaultItemQuantity - item.getCount();
			int price = item.getDefinition().getValue();
			if (difference != 0) {
				price += (price * (difference/1000));
			}
			
			return price;
		}
		
	}
}
