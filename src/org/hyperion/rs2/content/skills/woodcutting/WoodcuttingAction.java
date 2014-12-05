package org.hyperion.rs2.content.skills.woodcutting;

import java.util.Random;

import org.hyperion.Server;
import org.hyperion.rs2.action.impl.HarvestingAction;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;

/**
 * The action taken when woodcutting.
 * @author Graham Edgecomb
 * @author Stephen Andrews
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
	 * The object the woodcutting action is being invoked on.
	 */
	private GameObject object;
	
	/**
	 * The tree type.
	 */
	private Tree tree;

	/**
	 * Creates the <code>WoodcuttingAction</code>.
	 * @param player The player performing the action.
	 * @param object The tree object being cut.
	 * @param tree The tree.
	 */
	public WoodcuttingAction(Player player, GameObject object, Tree tree) {
		super(player, object.getLocation());
		this.object = object;
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
		boolean foundAxe = false;
		
		/* If we don't separate these loops, it takes the axe in the 
		 * inventory instead of prioritizing the player's equipped axe.
		 */
		for(Axe axe : Axe.values()) {
			if(player.getEquipment().contains(axe.getId()) && wc >= axe.getRequiredLevel()) {
				this.axe = axe;
				foundAxe = true;
				break;
			}
		}
		if (!foundAxe) {
			for (Axe axe: Axe.values()) {
				if (player.getInventory().contains(axe.getId()) && wc >= axe.getRequiredLevel()) {
					this.axe = axe;
					break;
				}
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
	public Item getHarvestedItem() {
		return new Item(tree.getLogId(), 1);
	}

	@Override
	public double getExperience() {
		return tree.getExperience();
	}

	@Override
	public Animation getAnimation() {
		return Animation.create(axe.getAnimation());
	}

	@Override
	public int getSkill() {
		return Skills.WOODCUTTING;
	}
	
	@Override
	public int getRespawnTime() {
		return tree.getStumpTime() * Server.CYCLE_TIME;
	}

	@Override
	public GameObject getObject() {
		return object;
	}

	@Override
	public GameObject getReplacementObject() {
		return new GameObject(tree.getStump(), object.getLocation(), object.getFace(), object.getType());
	}
}
