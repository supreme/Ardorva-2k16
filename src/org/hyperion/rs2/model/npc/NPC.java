package org.hyperion.rs2.model.npc;

import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.definitions.NPCDefinition;
import org.hyperion.rs2.model.region.Region;

/**
 * <p>Represents a non-player character in the in-game world.</p>
 * @author Graham Edgecombe
 *
 */
public class NPC extends Entity {
	
	/**
	 * The id of the NPC.
	 */
	private int id;
	
	/**
	 * The health of the NPC.
	 */
	private int health;
	
	/**
	 * The definition.
	 */
	private final NPCDefinition definition;
	
	/**
	 * The ticks for a respawn.
	 */
	private int ticksUntilSpawn;
	
	/**
	 * Creates the NPC with the specified definition.
	 * @param definition The definition.
	 */
	public NPC(NPCDefinition definition) {
		super();
		this.definition = definition;
		this.ticksUntilSpawn = definition.getRespawn();
	}
	
	/**
	 * Creates an NPC with the specified id.
	 * @param id The id of the NPC.
	 */
	public NPC(int id) {
		super();
		this.id = id;
		this.definition = NPCDefinition.forId(id);
	}
	
	/**
	 * Gets the NPC definition.
	 * @return The NPC definition.
	 */
	public NPCDefinition getDefinition() {
		return definition;
	}
	
	/**
	 * Gets the ticks until spawn.
	 * @return The ticks.
	 */
	public int getTicksUntilSpawn() {
		return ticksUntilSpawn;
	}
	
	/**
	 * Sets the ticks until spawn.
	 */
	public void setTicksUntilSpawn(int ticks) {
		this.ticksUntilSpawn = ticks;
	}

	@Override
	public void addToRegion(Region region) {
		region.addNpc(this);
	}

	@Override
	public void removeFromRegion(Region region) {
		region.removeNpc(this);
	}

	@Override
	public int getClientIndex() {
		return this.getIndex();
	}

	@Override
	public void inflictDamage(int damage, HitType type) {
		// TODO Auto-generated method stub
		
	}

}
