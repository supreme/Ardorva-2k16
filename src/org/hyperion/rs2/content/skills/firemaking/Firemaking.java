package org.hyperion.rs2.content.skills.firemaking;

import java.util.ArrayList;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Location;

/**
 * The main class for firemaking. 
 * @author Stephen
 */
public class Firemaking {

	/**
	 * A list of existing fires.
	 */
	private ArrayList<Location> existingFires = new ArrayList<Location>();
	
	/**
	 * The item id for a tinder box.
	 */
	public final static int TINDERBOX = 590;
	
	/**
	 * The animation for lighting a fire.
	 */
	public final Animation LIGHTING_ANIM = Animation.create(733);
	
	/**
	 * Gets the existing fires.
	 * @return The existing fires.
	 */
	public ArrayList<Location> getExistingFires() {
		return existingFires;
	}
}
