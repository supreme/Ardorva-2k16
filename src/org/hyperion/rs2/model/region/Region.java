package org.hyperion.rs2.model.region;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;

/**
 * Represents a single region.
 * @author Graham Edgecombe
 *
 */
public class Region {

	/**
	 * The region coordinates.
	 */
	private RegionCoordinates coordinate;
	
	/**
	 * A list of players in this region.
	 */
	private List<Player> players = new LinkedList<Player>();
	
	/**
	 * A list of NPCs in this region.
	 */
	private List<NPC> npcs = new LinkedList<NPC>();
	
	/**
	 * A list of objects in this region.
	 */
	private List<GameObject> objects = new LinkedList<GameObject>();
	
	/**
	 * A list of ground items in this region.
	 */
	private List<GroundItem> groundItems = new LinkedList<GroundItem>();
	
	/**
	 * Creates a region.
	 * @param coordinate The coordinate.
	 */
	public Region(RegionCoordinates coordinate) {
		this.coordinate = coordinate;
	}
	
	/**
	 * Gets the region coordinates.
	 * @return The region coordinates.
	 */
	public RegionCoordinates getCoordinates() {
		return coordinate;
	}

	/**
	 * Gets the list of players.
	 * @return The list of players.
	 */
	public Collection<Player> getPlayers() {
		synchronized(this) {
			return Collections.unmodifiableCollection(new LinkedList<Player>(players));
		}
	}
	
	/**
	 * Gets the list of NPCs.
	 * @return The list of NPCs.
	 */
	public Collection<NPC> getNpcs() {
		synchronized(this) {
			return Collections.unmodifiableCollection(new LinkedList<NPC>(npcs));
		}
	}
	
	/**
	 * Gets the list of objects.
	 * @return The list of objects.
	 */
	public Collection<GameObject> getGameObjects() {
		return objects;
	}

	/**
	 * Gets the list of ground items.
	 * @return The list of ground items.
	 */
	public Collection<GroundItem> getGroundItems() {
		return groundItems;
	}
	
	/**
	 * Adds a new player.
	 * @param player The player to add.
	 */
	public void addPlayer(Player player) {
		synchronized(this) {
			players.add(player);
		}
	}

	/**
	 * Removes an old player.
	 * @param player The player to remove.
	 */
	public void removePlayer(Player player) {
		synchronized(this) {
			players.remove(player);
		}
	}

	/**
	 * Adds a new NPC.
	 * @param npc The NPC to add.
	 */
	public void addNpc(NPC npc) {
		synchronized(this) {
			npcs.add(npc);
		}
	}

	/**
	 * Removes an old NPC.
	 * @param npc The NPC to remove.
	 */
	public void removeNpc(NPC npc) {
		synchronized(this) {
			npcs.remove(npc);
		}
	}
	
	/**
	 * Adds a new ground item.
	 * @param item The ground item to add.
	 */
	public void addGroundItem(GroundItem item) {
		synchronized(this) {
			groundItems.add(item);
			if (item.isGlobal()) {
				for (Player player : players) {
					player.getActionSender().sendGroundItem(item);
				}
			}
		}
	}
	
	/**
	 * Removes a ground item.
	 * @param item The ground item to remove.
	 */
	public void removeGroundItem(GroundItem item) {
		synchronized(this) {
			groundItems.remove(item);
			for (Player player : players) {
				player.getActionSender().clearGroundItem(item);
			}
		}
	}
}
