package org.hyperion.rs2.event.impl;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.object.GameObject;

/**
 * An <code>ObjectReplacementEvent</code> takes an existing object in the game world
 * and temporarily replaces it with another object for a set delay. Once the delay
 * is up, the object reverts back to the original.
 * @author Stephen Andrews
 */
public class ObjectReplacementEvent extends Event {

	/**
	 * The original object already existing in the game world, to be replaced.
	 */
	private GameObject original;
	
	/**
	 * The replacement object.
	 */
	private GameObject replacement;
	
	/**
	 * Whether or not the object has already been replaced with its temporary replacement.
	 */
	private boolean replacementVisible;
	
	/**
	 * Creates an <code>ObjectReplacementEvent</code>.
	 * @param delay The delay before the original object respawns.
	 */
	public ObjectReplacementEvent(GameObject original, GameObject replacement, long delay) {
		super(delay, true);
		this.original = original;
		this.replacement = replacement;
		this.replacementVisible = false;
	}

	@Override
	public void execute() {
		if (!replacementVisible) {
			original.getRegion().removeGameObject(original);
			original.getRegion().addGameObject(replacement);
			replacementVisible = true;
		} else {
			replacement.getRegion().removeGameObject(replacement);
			replacement.getRegion().addGameObject(original);
			this.stop();
		}
	}
}
