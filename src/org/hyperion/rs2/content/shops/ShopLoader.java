package org.hyperion.rs2.content.shops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Container.Type;
import org.hyperion.rs2.model.player.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Loads the contents of a shop.
 * @author Stephen Andrews
 */
public class ShopLoader {

	/**
	 * The directory of the shops.
	 */
	private static String SHOP_DIRECTORY = "./data/world/shops/";
	
	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getLogger(ShopLoader.class.getName());
	
	/**
	 * The active shop.
	 */
	private static ShopContents shop;
	
	/**
	 * The contents of a shop.
	 * @author Stephen
	 */
	public class ShopContents {
		
		/**
		 * The name of the shop.
		 */
		private String name;
		
		/**
		 * The items in the shop.
		 */
		private ShopItem[] contents;
		
		/**
		 * Constructs a ShopContents object.
		 * @param items The items in the shop.
		 */
		public ShopContents(String name, ShopItem[] contents) {
			this.name = name;
			this.contents = contents;
		}
		
		/**
		 * Gets the name of the shop.
		 * @return The shop name.
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Gets the shop items.
		 * @return The shop items.
		 */
		public ShopItem[] getContents() {
			return contents;
		}
	}
	
	/**
	 * The structure of a shop item.
	 * @author Stephen
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
	
	/**
	 * Loads NPC drops from the drops.json file.
	 */
	public static void loadShop(Player player, String shopName) {
		if (parseJson(shopName)) {
			displayShop(player);
		} else {
			player.getActionSender().sendMessage("The shop you are trying to access does not exist.");
		}
	}
	
	/**
	 * Parses the json file.
	 * @param shopName The name of the NPC who owns the shop.
	 */
	private static boolean parseJson(String shopName) {
		shopName = shopName.toLowerCase();
		try (BufferedReader reader = new BufferedReader(new FileReader(SHOP_DIRECTORY + shopName + ".json"))) {
            Gson gson = new GsonBuilder().create();
            shop = gson.fromJson(reader, ShopContents.class);
            logger.info("Loaded " + shop.getContents().length + " shop items...");
            return true;
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Displays the shop to the player.
	 */
	private static void displayShop(Player player) {
		Container shopContents = new Container(Type.ALWAYS_STACK, shop.getContents().length);
		for (ShopItem item : shop.getContents()) {
			shopContents.add(new Item(item.getId(), item.getStock()));
		}
		player.getActionSender().sendUpdateItems(Shop.SHOP_INTERFACE, 75, 93, shopContents.toArray());
		player.getActionSender().sendUpdateItems(301, 0, 93, player.getInventory().toArray());
		player.getActionSender().sendInterface(300);
		player.getActionSender().sendInventoryInterface(301);
		player.getActionSender().sendString(300, 76, shop.getName());
	}
	
	/**
	 * Gets the active shop.
	 * @return The active shop.
	 */
	public ShopContents forName() {
		return null;
	}
}