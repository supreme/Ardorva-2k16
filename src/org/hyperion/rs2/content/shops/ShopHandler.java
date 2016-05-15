package org.hyperion.rs2.content.shops;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Container.Type;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.shops.Shop;
import org.hyperion.rs2.model.shops.Shop.ShopItem;

import java.util.logging.Logger;

/**
 * Loads the contents of a shop.
 * @author Stephen Andrews
 * @author Brendan Dodd
 */
public class ShopHandler {

	
	/**
	 * The interface id for a shop.
	 */
	public static final int SHOP_INTERFACE = 300;
		
	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getLogger(ShopHandler.class.getName());
	
	/**
	 * Displays the shop to the player.
	 */
	public static void displayShop(Player player, Shop shop) {
		Container shopContents = new Container(Type.ALWAYS_STACK, shop.getContents().getLength());
		for (ShopItem item : shop.getContents().getItems()) {
			shopContents.add(item.getItem());
		}
		player.getActionSender().sendUpdateItems(SHOP_INTERFACE, 75, 93, shopContents.toArray());
		player.getActionSender().sendUpdateItems(301, 0, 93, player.getInventory().toArray());
		player.getActionSender().sendInterface(300);
		player.getInterfaceState().interfaceOpened(300);
		player.getActionSender().sendInventoryInterface(301);
		player.getActionSender().sendString(300, 76, shop.getName());
	}
	
	public static void refreshShop(Player player, Shop shop) {
		Container shopContents = new Container(Type.ALWAYS_STACK, shop.getContents().getLength());
		for (ShopItem item : shop.getContents().getItems()) {
			shopContents.add(item.getItem());
		}
		player.getActionSender().sendUpdateItems(SHOP_INTERFACE, 75, 93, shopContents.toArray());
		player.getActionSender().sendUpdateItems(301, 0, 93, player.getInventory().toArray());
	}
	
	/**
	 * Gets the Shop object for the npc that owns it
	 * @param npcId
	 * @return Shop or <code>null</code>.
	 */
	public static Shop getShopForNpc(int npcId) {
		try {
			for(Shop shop : World.getWorld().getDefinitionLoader().getShops()) {
				if(shop.getId() == npcId) {
					return shop;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}