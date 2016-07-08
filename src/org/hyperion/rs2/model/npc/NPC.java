package org.hyperion.rs2.model.npc;

import org.hyperion.rs2.event.impl.DeathEvent;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.World;
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
	 * The forced chat message.
	 */
	private String forcedChat;
	
	/**
	 * Creates the NPC from the specified definition.
	 * @param definition The definition.
	 */
	public NPC(NPCDefinition definition) {
		super();
		this.id = definition.getId();
		this.definition = definition;
		this.health = definition.getHitpoints();
		this.ticksUntilSpawn = definition.getRespawn();
		setAutoRetaliating(true);
	}
	
	/**
	 * Creates an NPC from the specified id.
	 * @param id The id of the NPC.
	 */
	public NPC(int id) {
		super();
		this.id = id;
		this.definition = NPCDefinition.forId(id);
		this.health = definition.getHitpoints();
		this.ticksUntilSpawn = definition.getRespawn();
		setAutoRetaliating(true);
	}
	
	/**
	 * Gets the NPC's id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the NPC's current health.
	 * @return The NPC's current health.
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Sets the NPC's health to the specified amount.
	 * @param health The new health.
	 */
	public void setHealth(int health) {
		this.health = health;
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
	 * @param ticks The amount of ticks until spawn.
	 */
	public void setTicksUntilSpawn(int ticks) {
		this.ticksUntilSpawn = ticks;
	}
	
	/**
	 * Creates the force chat mask.
	 * 
	 * @param message
	 */
	public void forceChat(String message) {
		forcedChat = message;
		getUpdateFlags().flag(UpdateFlag.FORCED_CHAT);
	}

	/**
	 * Creates the force chat mask.
	 * 
	 * @param message
	 */
	public void setForceChat(String message) {
		forcedChat = message;
	}

	/**
	 * Gets the message to display with the force chat mask.
	 * 
	 * @return The message to display with the force chat mask.
	 */
	public String getForcedChatMessage() {
		return forcedChat;
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
	public void inflictDamage(Entity source, Hit hit) {
		if (!getUpdateFlags().get(UpdateFlag.HIT)) {
			System.out.println("Setting primary hit: " + hit.getType().getId());
			setPrimaryHit(hit);
			getUpdateFlags().flag(UpdateFlag.HIT);
		} else {
			if (!getUpdateFlags().get(UpdateFlag.HIT_2)) {
				setSecondaryHit(hit);
				getUpdateFlags().flag(UpdateFlag.HIT_2);
			}
		}
		
		health -= hit.getDamage();
		/*if((source instanceof Entity) && (source != null)) {
			this.getCombatUtility().setInCombat(true);
			this.setInteractingEntity(source);
			if (!this.isAnimimation()) {
				this.playAnimation(Animation.create(getCombatUtility().getBlockAnimation()));
			}
			if(this.isAutoRetaliating()) {
				this.face(source.getLocation());
				this.getActionQueue().addAction(new MeleeAction(this, source));
			}
		}*/
		
		if(this.isAutoRetaliating()) {
			this.face(source.getLocation());
			//this.getActionQueue().addAction(new MeleeAction(this, source));
		}
		
		if (health <= 0) {
			health = 0; //Health bar goes back to green if value is negative
			if (!isDead()) {
				playAnimation(Animation.create(definition.getDeathAnimation(), 2));
				World.getWorld().submit(new DeathEvent(this));
			}
			setDead(true);
		}
	}
}
