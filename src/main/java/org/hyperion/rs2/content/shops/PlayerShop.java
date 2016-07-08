package org.hyperion.rs2.content.shops;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.shops.Shop;

/**
 * Represents a player owned shop.
 * @author Stephen Andrews
 */
public class PlayerShop extends Shop {
	
	/**
	 * The owner of the shop.
	 */
	private String owner;
	
	/**
	 * The contents of the shop.
	 */
	private Container contents;
	
	/**
	 * The maximum amount of items the player can have in a shop.
	 */
	private int shopSize;
	
	/**
	 * Constructs a new player shop.
	 * @param owner The owner of the shop.
	 * @param contents The contents of the shop.
	 * @param shopSize The maximum amount of items the player can have in a shop.
	 */
	public PlayerShop(String owner, Container contents, int shopSize) {
		this.owner = owner;
		this.contents = contents;
		this.shopSize = shopSize;
	}

/*	@Override
	public void buyItem(Container container, Player player) {
		player.getActionSender().sendMessage("You cannot sell items to a player owned shop.");
	}*/
	
	/**
	 * Saves a shop to a dat file.
	 * @param shop The shop to save.
	 */
	public void saveShop(PlayerShop shop) {
		if (shop == null) { return; }
		
		try {
			File data = new File("./data/world/shops/player/" + owner + ".dat");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(data, true));	
			out.writeObject(shop);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads a player shop.
	 * @param owner The owner of the shop to load.
	 */
	public static PlayerShop loadShop(String owner) {
		try {
			File data = new File("./data/world/shops/player/" + owner + ".dat");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(data));
			PlayerShop shop = null;
			shop = (PlayerShop) in.readObject();
			in.close();
			
			return shop;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}

}
