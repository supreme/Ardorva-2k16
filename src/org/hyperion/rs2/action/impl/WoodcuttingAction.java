package org.hyperion.rs2.action.impl;

import java.util.Random;

import org.hyperion.rs2.content.skills.Woodcutting.Axe;
import org.hyperion.rs2.content.skills.Woodcutting.Tree;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;

/**
 * The action taken when woodcutting.
 * @author Graham Edgecomb
 * @author Stephen
 */
public class WoodcuttingAction extends HarvestingAction {
	/**
	 * The delay.
	 */
	private static final int DELAY = 3000;
	
	/**
	 * The factor.
	 */
	private static final double FACTOR = 0.5;
	
	/**
	 * Whether or not this action requires an object replacement.
	 */
	private static final boolean REPLACEMENT = true;
	
	/**
	 * Whether or not this action grants periodic rewards.
	 */
	private static final boolean PERIODIC = true;
	
	/**
	 * The axe type.
	 */
	private Axe axe;
	
	/**
	 * The tree type.
	 */
	private Tree tree;

	/**
	 * Creates the <code>WoodcuttingAction</code>.
	 * @param player The player performing the action.
	 * @param location The Location of the tree.
	 * @param tree The tree.
	 */
	public WoodcuttingAction(Player player, Location location, Tree tree) {
		super(player, location);
		this.tree = tree;
	}
	
	@Override
	public long getHarvestDelay() {
		return DELAY;
	}
	
	@Override
	public boolean requiresReplacementObject() {
		return REPLACEMENT;
	}
	
	@Override
	public boolean getPeriodicRewards() {
		return PERIODIC;
	}
		
	@Override
	public void init() {
		final Player player = getPlayer();
		final int wc = player.getSkills().getLevel(Skills.WOODCUTTING);
		for(Axe axe : Axe.values()) {
			if((player.getEquipment().contains(axe.getId()) || player.getInventory().contains(axe.getId())) && wc >= axe.getRequiredLevel()) {
				this.axe = axe;
				break;
			}
		}
		if(axe == null) {
			player.getActionSender().sendMessage("You do not have an axe that you can use.");
			stop();
			return;
		}
		if(wc < tree.getRequiredLevel()) {
			player.getActionSender().sendMessage("You do not have the required level to cut down that tree.");
			stop();
			return;
		}
		player.getActionSender().sendMessage("You swing your axe at the tree...");
	}

	@Override
	public int getCycles() {
		if(tree == Tree.NORMAL) {
			return 1;
		} else {
			return new Random().nextInt(5) + 5; //Not working properly - got 2 logs
		}
	}

	@Override
	public double getFactor() {
		return FACTOR;
	}
	
	@Override
	public int getReplacementObject() {
		return tree.getReplacementObject();
	}
	
	@Override
	public Item getHarvestedItem() {
		return new Item(tree.getLogId(), 1);
	}

	@Override
	public double getExperience() {
		return tree.getExperience();
	}

	@Override
	public Animation getAnimation() {
		return Animation.create(879);//Animation.create(axe.getAnimation());
	}

	@Override
	public int getSkill() {
		return Skills.WOODCUTTING;
	}
	
	@Override
	public int getObjectId() {
		return tree.getTreeId();
	}
	
	@Override
	public int getRespawnTime() {
		return tree.getStumpTime();
	}
}
