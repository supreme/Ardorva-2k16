package org.hyperion.rs2.model.definitions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.hyperion.application.ConsoleMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	 * An array of item definitions.
	 */
	private ItemDefinition[] itemDefinitions;
	
	/**
	 * An array of NPC definitions.
	 */
	private NPCDefinition[] npcDefinitions;
		
	/**
	 * Loads the item definitions.
	 */
	public void loadItemDefinitions() {
		try (BufferedReader reader = new BufferedReader(new FileReader(DEFINITIONS_DIRECTORY + "itemdef.json"))) {
            Gson gson = new GsonBuilder().create();
            itemDefinitions = gson.fromJson(reader, ItemDefinition[].class);
            ConsoleMessage.info("Loaded " + itemDefinitions.length + " item definitions...");
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
            ConsoleMessage.info("Loaded " + npcDefinitions.length + " NPC definitions...");
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
}
