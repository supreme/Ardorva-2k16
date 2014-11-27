package org.hyperion.rs2.event.impl;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.npc.NPC;

/**
 * Handles the respawning of dead NPCs throughout the world.
 * @author Stephen Andrews
 */
public class NPCRespawnEvent extends Event {

	/**
	 * The NPC waiting to respawn.
	 */
	private NPC npc;
	
	/**
	 * Constructs an NPC respawn tick.
	 * @param npc The NPC waiting to respawn.
	 * @param delay The delay in ms.
	 */
	public NPCRespawnEvent(NPC npc, int delay) {
		super(delay);
		this.npc = npc;
	}

	@Override
	public void execute() {
		npc.setVisible(true);
		npc.setDead(false);	
		this.stop();
	}

}
