package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.event.impl.ObjectReplacementEvent;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;

/**
 * <p>A harvesting action is a resource-gathering action, which includes, but
 * is not limited to, woodcutting and mining.</p>
 * 
 * <p>This class implements code related to all harvesting-type skills, such as
 * dealing with the action itself, looping, expiring the object (i.e. changing
 * rocks to the gray rock and trees to the stump), checking requirements and
 * giving out the harvested resources.</p>
 * 
 * <p>The individual woodcutting and mining classes implement things specific
 * to these individual skills such as random events.</p>
 * @author Graham Edgecombe
 *
 */
public abstract class HarvestingAction extends Action {
	
	/**
	 * The location.
	 */
	private Location location;
	
	/**
	 * Creates the harvesting action for the specified player.
	 * @param player The player to create the action for.
	 */
	public HarvestingAction(Player player, Location location) {
		super(player, 0, false); //Get rid of 0 and convert to new action system
		this.location = location;
	}
	
	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.ALWAYS;
	}
	
	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}
	
	/**
	 * Called when the action is initialized.
	 */
	public abstract void init();
	
	/**
	 * Gets the harvest delay.
	 * @return The delay between consecutive harvests.
	 */
	public abstract long getHarvestDelay();
	
	/**
	 * Gets the number of cycles.
	 * @return The number of cycles.
	 */
	public abstract int getCycles();
	
	/**
	 * Gets the success factor.
	 * @return The success factor.
	 */
	public abstract double getFactor();
	
	/**
	 * Gets the replacement object.
	 * @return The object.
	 */
	public abstract GameObject getReplacementObject();
	
	/**
	 * Gets the respawn time of the object.
	 * @return The object.
	 */
	public abstract int getRespawnTime();
	
	/**
	 * Gets the harvested item.
	 * @return The harvested item.
	 */
	public abstract Item getHarvestedItem();
	
	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public abstract double getExperience();
	
	/**
	 * Gets the skill.
	 * @return The skill.
	 */
	public abstract int getSkill();
	
	/**
	 * Gets the animation.
	 * @return The animation.
	 */
	public abstract Animation getAnimation();
	
	/**
	 * Gets the object the harvesting action is focusing on.
	 * @return The object.
	 */
	public abstract GameObject getObject();
	
	/**
	 * Gets replacement object.
	 * @return <code>true/false</code> Whether objects need to be replaced during the action.
	 */
	public abstract boolean requiresReplacementObject();
	
	/**
	 * Gets reward type.
	 * @return <code>true/false</code> Whether items are rewarded periodically during the action.
	 */
	public abstract boolean getPeriodicRewards();
	
	/**
	 * The total number of cycles.
	 */
	private int totalCycles;
	
	/**
	 * The number of remaining cycles.
	 */
	private int cycles;
	
	/**
	 * Grants the player his or her reward.
	 * @param player The player object.
	 * @param reward The item reward object.
	 */
	private void giveRewards(Player player, Item reward) {
		player.getInventory().add(reward);
		ItemDefinition def = reward.getDefinition();
		player.getActionSender().sendMessage("You get some " + def.getName() + ".");
		player.getSkills().addExperience(getSkill(), getExperience());
	}

	@Override
	public void execute() {
		final Player player = getPlayer();
		if(this.getDelay() == 0) {
			this.setDelay(getHarvestDelay());
			init();
			if(this.isRunning()) {
				player.playAnimation(getAnimation());
				player.face(location);
			}
			this.cycles = getCycles();
			this.totalCycles = cycles;
		} else {
			cycles--;
			Item item = getHarvestedItem();
			if(player.getInventory().hasRoomFor(item)) {
				if(totalCycles == 1 || Math.random() > getFactor()) {
					if(getPeriodicRewards() ) {
						giveRewards(player, item);
					}
				}
			} else {
				stop();
				player.getActionSender().sendMessage("There is not enough space in your inventory.");
				return;
			}
			if(cycles == 0) {
				if (requiresReplacementObject()) {
					World.getWorld().submit(new ObjectReplacementEvent(getObject(), getReplacementObject(), getRespawnTime()));
				}
				if(!getPeriodicRewards()) {
					giveRewards(player, item);
				}
				stop();
			} else {
				player.playAnimation(getAnimation());
				player.face(location);
			}
		}
	}
}
