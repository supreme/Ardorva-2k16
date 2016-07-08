package org.hyperion.rs2.content.skills.fishing;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.content.skills.fishing.FishingData.Fish;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.player.Player;

/**
 * Based off HarvestingAction, but modified because it uses a NPC instead of Object.
 * @author phil
 * @author Stepehen
 * @date 2/4/2011
 */
public class FishingAction extends Action {
	
	/**
	 * Constructs a fishing action.
	 * @param player The player fishing.
	 * @param location The location of the fishing spot.
	 * @param fish The fish attempting to be caught.
	 */
	public FishingAction(Player player, Location location, Fish fish) {
		super(player, 600, false);
		this.player = player;
		this.fishingSpot = location;
		this.fish = fish;
	}
	
	/**
	 * The player fishing.
	 */
	private Player player;
	
	/**
	 * The location of the fishing spot.
	 */
	private Location fishingSpot;
	
	/**
	 * The fish attempting to be caught.
	 */
	private Fish fish;
	
	/**
	 * Determines if the action is applicable for fishing.
	 * @return
	 */
	public boolean startUpCheck() {
		if(player.getSkills().getLevel(Skills.FISHING) < fish.getLevel()) {
			player.getActionSender().sendMessage("You need a fishing level of at least " + fish.getLevel() + ".");
			return false;
		}
		for(int item : fish.getMaterials()) {
			if(!player.getInventory().contains(item)) {
				player.getActionSender().sendMessage("You do not have a " + ItemDefinition.forId(item).getName() + ".");
				return false;
			}
		}
		return true;
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
	 * Gloves to increase xp and rate of catch.
	 * @return
	 */
	public boolean hasGloves() {
		return (fish == Fish.SHARK && getPlayer().getEquipment().contains(12861));
	}

	/**
	 * The action of fishing.
	 */
	@Override
	public void execute() {
		//if(!getPlayer().getLocation().withinRange(fishingSpot, 1)) return;
		
		if(!startUpCheck()) {
			this.stop();
			return;
		}
		
		if(!(player.getInventory().freeSlots() > 0)) {
			player.getActionSender().sendMessage("You have run out of inventory space.");
			player.playAnimation(Animation.create(-1));
			this.stop();
			return;
		}
		
		player.face(fishingSpot);
		if(player.getCurrentAnimation() == null) {
			player.playAnimation(Animation.create(fish.getEmote()));
		}
		
		/*if(CombatCheck.random(((fish.getLevel() * (hasGloves() ? 12 : 10)) * fish.getFactor())) <= player.getSkills().getLevel(Skills.FISHING) * (fish.getFactor() / 2)) {
			player.getInventory().add(new Item(fish.getFish()));
			if(fish.getMaterials().length > 1) {
				player.getInventory().remove(new Item(fish.getMaterials()[1]));
			}
			player.getActionSender().sendMessage("You catch a " + ItemDefinition.forId(fish.getFish()).getName().replace("Raw ", "") + ".");
			player.getSkills().addExperience(Skills.FISHING, hasGloves() ? 1.10 * fish.getXp() * Constants.SKILL_EXPERIENCE : fish.getXp() * Constants.SKILL_EXPERIENCE);
		}*/
	}

}
