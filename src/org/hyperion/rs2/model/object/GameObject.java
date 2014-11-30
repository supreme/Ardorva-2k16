package org.hyperion.rs2.model.object;

import org.hyperion.rs2.model.Location;

/**
 * Represents a game object in the world.
 * @author Stephen Andrews
 */
public class GameObject {
	
	/**
	 * The action variables.
	 */
	public static int REMOVE = 0, ADD = 1;
	
	/**
	 * The id of the object.
	 */
	private int id;
	
	/**
	 * The x coordinate of the object.
	 */
	private int x;
	
	/**
	 * The y coordinate of the object.
	 */
	private int y;

	/**
	 * The z coordinate of the object.
	 */
	private int z;
	
	/**
	 * The facing direction of the object.
	 */
	private int face;
	
	/**
	 * The type of the object.
	 * 5 - wall decorations, 10 - solid objects, 22 - walkable objects, 0 & 1 - doors.
	 */
	private int type;
	
	/**
	 * The action to perform - deleting existing objects or spawning new ones.
	 */
	private String action;
	
	/**
	 * Constructs a game object.
	 */
	public GameObject(int id, Location location, int face, int type) {
		this.id = id;
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.face = face;
		this.type = type;
	}
	
	/**
	 * Gets the object's id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the object's location.
	 * @return The location.
	 */
	public Location getLocation() {
		return Location.create(x, y, z);
	}
	
	/**
	 * Gets the object's facing direction.
	 * @return The facing direction.
	 */
	public int getFace() {
		return face;
	}
	
	/**
	 * Gets the object's type.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Gets the object's z value.
	 * @return The object's z value.
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * Gets the action to perform - deleting existing objects or spawning new ones.
	 * @return The action or -1 if we can't decipher the action.
	 */
	public int getAction() {
		if (action.equalsIgnoreCase("delete")) {
			return 0;
		} else if (action.equalsIgnoreCase("add")) {
			return 1;
		}
		
		return -1;
	}
}
