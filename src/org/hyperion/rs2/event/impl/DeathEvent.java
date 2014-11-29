package org.hyperion.rs2.event.impl;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * The death event handles player and npc deaths. Drops loot, does animation, teleportation, etc.
 * @author Graham
 * @author Stephen Andrews
 */
@SuppressWarnings("unused")
public class DeathEvent extends Event {
	
	/**
	 * The entity who just died.
	 */
	private Entity entity;
	
	/**
	 * The reset animation.
	 */
	private Animation RESET_ANIMATION = Animation.create(-1);

	/**
	 * Creates the death event for the specified entity.
	 * @param entity The player or npc whose death has just happened.
	 */
	public DeathEvent(Entity entity) {
		super(2400, false);
		this.entity = entity;
	}

	@Override
	public void execute() {
		if(entity instanceof Player) {
			Player p = (Player) entity;
			p.getSkills().setLevel(Skills.HITPOINTS, p.getSkills().getLevelForExperience(Skills.HITPOINTS));
			entity.setDead(false);
			entity.setTeleportTarget(Entity.DEFAULT_LOCATION);
			p.getActionSender().sendMessage("Oh dear, you are dead!");
			this.stop();
		} else {
			NPC npc = (NPC) entity;
			Location deathLocation = npc.getLocation();
			
			npc.setHealth(npc.getDefinition().getHitpoints());
			npc.setVisible(false);
			npc.playAnimation(RESET_ANIMATION);
			npc.setInteractingEntity(null);
			npc.getCombatUtility().setInCombat(false);
			World.getWorld().submit(new NPCRespawnEvent(npc, npc.getDefinition().getRespawn() * 600));
			this.stop();
		}
	}

}