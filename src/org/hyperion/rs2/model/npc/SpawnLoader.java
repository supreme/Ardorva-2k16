package org.hyperion.rs2.model.npc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.hyperion.application.ConsoleMessage;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.definitions.NPCDefinition;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.model.region.RegionCoordinates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Populates the world with NPCs.
 * @author Stephen Andrews
 */
public class SpawnLoader {

	/**
	 * Represents an NPC spawn.
	 * @author Stephen Andrews
	 */
	private class Spawn {
		
		/**
		 * The id of the NPC.
		 */
		private int id;
		
		/**
		 * The x position of the NPC.
		 */
		private int x;
		
		/**
		 * The y position of the NPC.
		 */
		private int y;
		
		/**
		 * The z position of the NPC.
		 */
		private int z;
		
		/**
		 * Gets the id of the NPC.
		 * @return The id of the NPC.
		 */
		private int getId() {
			return id;
		}
		
		/**
		 * Gets the spawn location of the NPC.
		 * @return The spawn location.
		 */
		private Location getLocation() {
			return Location.create(x, y, z);
		}
	}
	
	/**
	 * An array of NPC spawns.
	 */
	private static Spawn[] spawns;
	
	/**
	 * The location of the spawns file.
	 */
	private final static String SPAWN_FILE = "./data/world/npcs/spawns.json";
	
	/**
	 * Loads the NPC spawns.
	 */
	public static void loadSpawns() {
		try (BufferedReader reader = new BufferedReader(new FileReader(SPAWN_FILE))) {
            Gson gson = new GsonBuilder().create();
            spawns = gson.fromJson(reader, Spawn[].class);
            ConsoleMessage.info("Loaded " + spawns.length + " NPC spawns...");
    		populateWorld();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Populates the world with NPCs.
	 */
	private static void populateWorld() {
		for (Spawn spawn : spawns) {
			NPC npc = new NPC(NPCDefinition.forId(spawn.getId()));
			npc.setLocation(spawn.getLocation());
			npc.addToRegion(new Region(new RegionCoordinates(spawn.getLocation().getX(), spawn.getLocation().getY())));
			World.getWorld().register(npc);
		}
	}
}
