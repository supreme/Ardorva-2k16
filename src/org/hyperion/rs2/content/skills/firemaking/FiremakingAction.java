package org.hyperion.rs2.content.skills.firemaking;

import org.hyperion.rs2.LivingClasses;
import org.hyperion.rs2.action.impl.DestructionAction;
import org.hyperion.rs2.content.skills.firemaking.LogData.Log;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.player.Player;

/**
 * The actions taken when lighting a fire.
 * @author Stephen
 */
public class FiremakingAction extends DestructionAction {

	/**
	 * An instance of the firemaking class.
	 */
	private static Firemaking firemaking = /*LivingClasses.firemaking*/ null;
	
	/**
	 * The location the player is attempting to light a fire at.
	 */
	private Location lightingLocation;
	
	/**
	 * The log we are attempting to light.
	 */
	private Log log;
	
	/**
	 * The player attemping to light a fire.
	 */
	private Player player;
	
	public FiremakingAction(Player player, Log log) {
		super(player);
		lightingLocation = player.getLocation();
		this.log = log;
		this.player = player;
	}
	
	/**
	 * Determines whether or not a player can light a fire.
	 * @return true if they can.
	 */
	private boolean canLight() {
		//Make sure we recognize the log they are lighting.
		if (log == null) {
			return false;
		}
		
		//Check the player's firemaking level.
		if (player.getSkills().getLevel(Skills.FIREMAKING) < log.getLevel()) {
			player.getActionSender().sendMessage("You need a firemaking level of " 
					+ log.getLevel() + " to burn " + new Item(log.getId()).getDefinition().getName() + ".");
			return false;
		}
		
		/*if (LivingClasses.firemaking.getExistingFires().contains(lightingLocation)) {
			player.getActionSender().sendMessage("You cannot light a fire on an existing fire.");
			return false;
		}*/
		
		//TODO: Check to see if player is in a bank.
		return true;
	}
	
	/**
	 * The action of lighting a fire.
	 * @param player The player attempting to light the fire.
	 * @param logId The id of the log the player is attempting to light.
	 */
	public void lightFire() {		
		if (canLight()) {
			World.getWorld().submit(new Event(1000, false) {

				@Override
				public void execute() {
					//WorldObjectManager.addObject(new WorldObject(2732, lightingLocation.getX(), lightingLocation.getY()));
					//LivingClasses.firemaking.getExistingFires().add(lightingLocation);
					this.stop();
				}
				
			});
		}
	}

	@Override
	public void init() {
		lightFire();
	}

	@Override
	public Item getDestructionItem() {
		return new Item(log.getId());
	}

	@Override
	public long getDestructionDelay() {
		return 1000;
	}

	@Override
	public int getCycles() {
		return 1;
	}

	@Override
	public double getExperience() {
		return log.getXp();
	}

	@Override
	public int getSkill() {
		return Skills.FIREMAKING;
	}

	@Override
	public Animation getAnimation() {
		return firemaking.LIGHTING_ANIM;
	}

	@Override
	public int getItemId() {
		return log.getId();
	}
}
