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
	//private List<GameObject> objects = new LinkedList<GameObject>();
	
	/**
	 * A list of ground items in this region.
	 */
	private List<GroundItem> groundItems = new LinkedList<GroundItem>();
	
	public static final int[] OBJECT_SLOTS = new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	public static final int OBJECT_SLOT_FLOOR = 2;
    private boolean loadedObjectSpawns;
    protected List<GameObject> spawnedObjects = new CopyOnWriteArrayList<GameObject>();
    protected List<GameObject> removedOriginalObjects = new CopyOnWriteArrayList<GameObject>();
    protected GameObject[][][][] objects;
	
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
	public GameObject[][][][] getGameObjects() {
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
	
	public void spawnObject(GameObject object, int plane, int localX, int localY, boolean original) {
		if (objects == null)
			objects = new GameObject[4][64][64][4];
		int slot = OBJECT_SLOTS[object.getType()];
		if (original) {
			objects[plane][localX][localY][slot] = object;
			//clip(object, localX, localY);
		} else {
			GameObject spawned = getSpawnedObjectWithSlot(plane, localX,
					localY, slot);
			// found non original object on this slot. removing it since we
			// replacing with a new non original
			if (spawned != null) {
				spawnedObjects.remove(spawned);
				// unclips non orignal old object which had been cliped so can
				// clip the new non original
				//unclip(spawned, localX, localY);
			}
			GameObject removed = getRemovedObjectWithSlot(plane, localX,
					localY, slot);
			// there was a original object removed. lets readd it
			if (removed != null) {
				object = removed;
				removedOriginalObjects.remove(object);
				// adding non original object to this place
			} else if (objects[plane][localX][localY][slot] != object) {
				spawnedObjects.add(object);
				// unclips orignal old object which had been cliped so can clip
				// the new non original
				if (objects[plane][localX][localY][slot] != null) {
					//unclip(objects[plane][localX][localY][slot], localX, localY);
				}
			} else if (spawned == null) {
				//if (Settings.DEBUG)
					//Logger.log(this,"Requested object to spawn is already spawned.(Shouldnt happen)");
				return;
			}
			// clips spawned object(either original or non original)
			//clip(object, localX, localY);
			for (Player player : World.getWorld().getPlayers()) {
				//if (player == null || !player.getMapRegionsIds().contains(Location.create(coordinate.getX(), coordinate.getY(), 0).getRegionId()))
					//continue;
				player.getActionSender().createObject(object);
			}
		}
	}
	
	public void removeObject(GameObject object, int plane, int localX, int localY) {
		if (objects == null)
			objects = new GameObject[4][64][64][4];
		int slot = OBJECT_SLOTS[object.getType()];
		GameObject removed = getRemovedObjectWithSlot(plane, localX, localY, slot);
		if (removed != null) {
			removedOriginalObjects.remove(object);
			//clip(removed, localX, localY);
		}
		GameObject original = null;
		// found non original object on this slot. removing it since we
		// replacing with real one or none if none
		GameObject spawned = getSpawnedObjectWithSlot(plane, localX, localY, slot);
		if (spawned != null) {
			object = spawned;
			spawnedObjects.remove(object);
			//unclip(object, localX, localY);
			if (objects[plane][localX][localY][slot] != null) {// original
				// unclips non original to clip original above
				//System.out.println("orig: " + objects[plane][localX][localY][slot].toString());
				//clip(objects[plane][localX][localY][slot], localX, localY);
				original = objects[plane][localX][localY][slot];
			}
			// found original object on this slot. removing it since requested
		} else if (objects[plane][localX][localY][slot].getId() == object.getId()) { // removes original
			GameObject original_spawned = objects[plane][localX][localY][slot];
			if (original_spawned.getType() == object.getType()) {
				if (original_spawned.getFace() == object.getFace()) {
					if (original_spawned.getLocation().equals(object.getLocation())) {
						//unclip(object, localX, localY);
						removedOriginalObjects.add(object);
					}
				}
			}
		} else {
			//System.out.println("Requested object to remove wasnt found.(Shouldnt happen): ");
			//if (Settings.DEBUG)
				//Logger.log(this,"Requested object to remove wasnt found.(Shouldnt happen)");
			return;
		}
		for (Player p2 : World.getWorld().getPlayers()) {
			if (p2 == null || !p2.getMapRegionsIds().contains(Location.create(coordinate.getX(), coordinate.getY(), 0).getRegionId()))
				continue;
			if (original != null) {
				p2.getActionSender().createObject(original);
			} else {
				p2.getActionSender().removeObject(object);
			}
		}
	}
	
	public GameObject getSpawnedObjectWithSlot(int plane, int x, int y, int slot) {
		for (GameObject object : spawnedObjects) {
			if (object.getLocation().getXInRegion() == x
					&& object.getLocation().getYInRegion() == y
					&& object.getZ() == plane
					&& OBJECT_SLOTS[object.getType()] == slot)
				return object;
		}
		return null;
	}

	public GameObject getRemovedObjectWithSlot(int plane, int x, int y,
			int slot) {
		for (GameObject object : removedOriginalObjects) {
			if (object.getLocation().getXInRegion() == x && object.getLocation().getYInRegion() == y
					&& object.getZ() == plane
					&& OBJECT_SLOTS[object.getType()] == slot)
				return object;
		}
		return null;
	}
}
