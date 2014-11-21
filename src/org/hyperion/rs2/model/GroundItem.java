package org.hyperion.rs2.model;

import org.hyperion.rs2.event.impl.GroundItemEvent;
import org.hyperion.rs2.model.region.Region;

/**
 * Represents a ground item.
 * @author Stephen Andrews
 */
public class GroundItem {

	/**
	 * The item on the ground.
	 */
	private Item item;
	
	/**
	 * The owner of the item.
	 */
	private Entity owner;
	
	/**
	 * The location of the ground item.
	 */
	private Location location;
	
	/**
	 * The region the ground item is in.
	 */
	private Region region;
	
	/**
	 * Whether or not the item is a globally visible.
	 */
	private boolean global;
	
	/**
	 * Constructs a ground item object.
	 * @param itemId The id of the item.
	 * @param quantity The quantity of the item.
	 * @param owner The owner of the item.
	 * @param location The location of the item.
	 * @param region The region the item is in.
	 * @param global Whether or not the item is a globally visible.
	 */
	public GroundItem(Item item, Entity owner, Location location, Region region, boolean global) {
		this.item = item;
		this.owner = owner;
		this.location = location;
		this.region = region;
		this.global = global;
		World.getWorld().submit(new GroundItemEvent(this));
	}
	
	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Gets the owner of the ground item.
	 * @return The owner.
	 */
	public Entity getOwner() {
		return owner;
	}
	
	/**
	 * Gets the location of the ground item.
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the region of the ground item.
	 * @return The region.
	 */
	public Region getRegion() {
		return region;
	}
	
	/**
	 * Determines whether or not the ground item is globally visible.
	 * @return <code>true</code> if so, or <code>false</code> if not.
	 */
	public boolean isGlobal() {
		return global;
	}
	
	/**
	 * Sets the global boolean.
	 * @param flag The state of the global boolean.
	 */
	public void setGlobal(boolean flag) {
		global = flag;
	}
}
