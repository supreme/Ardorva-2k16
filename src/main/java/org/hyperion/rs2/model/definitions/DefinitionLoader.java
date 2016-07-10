package org.hyperion.rs2.model.definitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.shops.Shop;
import org.hyperion.util.Logger;
import org.hyperion.util.Logger.Level;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

/**
 * Loads definitions for items and NPCs.
 * @author Stephen Andrews
 */
public class DefinitionLoader {

	/**
	 * The location of the item definitions.
	 */
	private final static String DEFINITIONS_DIRECTORY = "./data/world/definitions/";
	
	/**
	 * The location of the shop definitions.
	 */
	private final static String SHOP_DIRECTORY = "./data/world/shops/";
	
	/**
	 * An array of item definitions.
	 */
	private ItemDefinition[] itemDefinitions;
	
	/**
	 * An array of NPC definitions.
	 */
	private NPCDefinition[] npcDefinitions;
	
	/**
	 * An array of shops
	 */
	
	private Shop[] shops;
	
	/**
	 * A map of item bonuses.
	 */
	private static HashMap<Integer, int[]> itemBonuses;

	/**
	 * Gets an item from the item bonuses map based on the item id.
	 * @param itemId The id of the item.
	 * @return The bonuses for the item.
	 */
	public final int[] getBonuses(int itemId) {
		return itemBonuses.get(itemId);
	}
	
	/**
	 * Gets the bonuses map.
	 * @return The bonuses map.
	 */
	public HashMap<Integer, int[]> getBonuses() {
		return itemBonuses;
	}
	
	/**
	 * Sets the bonuses map.
	 * @param bonuses The item bonuses.
	 */
	public void setBonuses(HashMap<Integer, int[]> bonuses) {
		itemBonuses = bonuses;
	}

	/**
	 * Initializes the definition loader to load all of the required
	 * external definitions.
	 */
	public void init() {

	}
	/**
	 * Loads the item bonuses.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void loadBonusDefinitions() throws FileNotFoundException, IOException {
		final String PACKED_PATH = "data/world/items/bonuses.ib";
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,channel.size());
			setBonuses(new HashMap<>(buffer.remaining() / 26));
			int loaded = 0;
			while (buffer.hasRemaining()) {
				int itemId = buffer.getShort() & 0xffff;
				int[] bonuses = new int[12];
				for (int index = 0; index < bonuses.length; index++) {
					bonuses[index] = buffer.getShort();
				}
				getBonuses().put(itemId, bonuses);
				loaded++;
			}
			channel.close();
			in.close();
			Logger.log(Level.CORE, "Loaded " + loaded + " item bonuses.");
		} catch (Throwable e) {
			World.getWorld().handleError(e);
		}
	}
	
	/**
	 * Loads the item definitions.
	 */
	public void loadItemDefinitions() {
		try (BufferedReader reader = new BufferedReader(new FileReader(DEFINITIONS_DIRECTORY + "itemdef.json"))) {
            Gson gson = new GsonBuilder().create();
            itemDefinitions = gson.fromJson(reader, ItemDefinition[].class);
            for (ItemDefinition def : itemDefinitions) {
            	String itemName = def.getName();
            	if (def.getEquipmentDefinition() != null && itemName != null) {
            		Equipment.getEquipmentTypes().put(def.getId(), Equipment.forSlot(itemName, def.getEquipmentDefinition().getSlot()));
            	}
            }
			Logger.log(Level.CORE, "Loaded " + itemDefinitions.length + " item definitions...");
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the NPC definitions.
	 */
	public void loadNPCDefinitions() {
		try (BufferedReader reader = new BufferedReader(new FileReader(DEFINITIONS_DIRECTORY + "npcdef.json"))) {
            Gson gson = new GsonBuilder().create();
            npcDefinitions = gson.fromJson(reader, NPCDefinition[].class);
            Logger.log(Level.CORE, "Loaded " + npcDefinitions.length + " NPC definitions...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the shops from json file
	 */
	public void loadShops() {
		try (BufferedReader reader = new BufferedReader(new FileReader(SHOP_DIRECTORY + "shops.json"))) {
            Gson gson = new GsonBuilder().create();
            shops = gson.fromJson(reader, Shop[].class);
            Logger.log(Level.CORE, "Loaded " + shops.length + " shops");
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the item definitions.
	 * @return The item definitions.
	 */
	public ItemDefinition[] getItemDefinitions() {
		return itemDefinitions;
	}
	
	/**
	 * Gets the NPC definitions.
	 * @return The NPC definitions.
	 */
	public NPCDefinition[] getNPCDefinitions() {
		return npcDefinitions;
	}
	
	/**
	 * Gets the Shops
	 * @return The shops
	 */
	public Shop[] getShops() {
		return shops;
	}
	
}
