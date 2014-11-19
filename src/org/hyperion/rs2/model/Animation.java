package org.hyperion.rs2.model;

/**
 * Represents a single animation request.
 * @author Graham Edgecombe
 * @author Stephen Andrews
 */
public class Animation {

	/**
	 * All of the animations, in order (order is important!).
	 */
	public final static int[] ANIMS = { 855, 856, 858, 859, 857, 863, 2113, 862, 864, 861, 
										2109, 2111, 866, 2106, 2107, 2108, 860, 1368, 2105,
										2110, 865, 2112, 2127, 2128, 1131, 1130, 1129, 1128,
										-1, -1, -1, -1, -1, -1, -1, -1}; //Starts at idea
	
	/**
	 * Creates an animation with no delay.
	 * @param id The id.
	 * @return The new animation object.
	 */
	public static Animation create(int id) {
		return create(id, 0);
	}
	
	/**
	 * Creates an animation.
	 * @param id The id.
	 * @param delay The delay.
	 * @return The new animation object.
	 */
	public static Animation create(int id, int delay) {
		return new Animation(id, delay);
	}
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The delay.
	 */
	private int delay;
	
	/**
	 * Creates an animation.
	 * @param id The id.
	 * @param delay The delay.
	 */
	private Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

}
