package org.hyperion.rs2.content.skills.mining;

import java.util.Random;

import org.hyperion.rs2.action.impl.HarvestingAction;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.object.GameObject;
import org.hyperion.rs2.model.player.Player;

/**
 * An action for mining.
 * @author Graham Edgecombe
 * @author Stephen
 */
public class MiningAction extends HarvestingAction {
	/**
	 * The delay.
	 */
	private int delay = 3000;
	
	/**
	 * The factor.
	 */
	private double factor = 0.5;
	
	/**
	 * The multiplyer.
	 */
	private double multiplyer = 1;
	
	/**
	 * Whether or not this action requires an object replacement.
	 */
	private static final boolean REPLACEMENT = true;
	
	/**
	 * Whether or not this action grants periodic rewards.
	 */
	private static final boolean PERIODIC = true;
	
	/**
	 * The pickaxe type.
	 */
	private Pickaxe pickaxe;
	
	/**
	 * The cycle count.
	 */
	private int cycleCount = 0;
	
	/**
	 * The node type.
	 */
	private Node node;

	/**
	 * Creates the <code>MiningAction</code>.
	 * @param player The player performing the action.
	 * @param node The Node.
	 */
	public MiningAction(Player player, Location location, Node node) {
		super(player, location);
		this.node = node;
	}
	
	@Override
	public long getHarvestDelay() {
		return delay;
	}
	
	@Override
	public boolean getPeriodicRewards() {
		return PERIODIC;
	}
		
	@Override
	public void init() {
		final Player player = getPlayer();
		final int mining = player.getSkills().getLevel(Skills.MINING);
		for(Pickaxe pickaxe : Pickaxe.values()) {
			if((player.getEquipment().contains(pickaxe.getId()) || player.getInventory().contains(pickaxe.getId())) && mining >= pickaxe.getRequiredLevel()) {
				this.pickaxe = pickaxe;
				break;
			}
		}
		if(pickaxe == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe for which you have the level to use.");
			stop();
			return;
		}
		if(mining < node.getRequiredLevel()) {
			player.getActionSender().sendMessage("You do not have the required level to mine this rock.");
			stop();
			return;
		}
		player.getActionSender().sendMessage("Clicked ore: " + node.getNodeId() + " Exhausted ore: " + getReplacementObject());
		/*if(node.getNodeId() == getReplacementObject()) {
			player.getActionSender().sendMessage("This rock's resources have been exhausted.");
			stop();
			return;
		}*/
		player.getActionSender().sendMessage("You swing your pick at the rock...");
		cycleCount = calculateCycles(player, node, pickaxe);
		factor = factor * factorBoost(player, pickaxe);
		
	}


	/**
	 * Attempts to calculate the number of cycles to mine the ore based on mining level, ore level and axe speed modifier.
	 * Needs heavy work. It's only an approximation.
	 * Scrapped system for now.
	 */
	public int calculateCycles(Player player, Node node, Pickaxe pickaxe) {
		final int mining = player.getSkills().getLevel(Skills.MINING);
		final int difficulty = node.getRequiredLevel();
		final int modifier = pickaxe.getRequiredLevel();
		final int random = new Random().nextInt(3);
		double cycleCount = 1;
		cycleCount = Math.ceil((difficulty * 60 - mining * 20) / modifier * 0.25 - random * 4);
		//player.getActionSender().sendMessage("Cycle count: " + ((difficulty * 60 - mining * 20) / modifier * 0.25 - random * 4));
		if(cycleCount < 1) {
			cycleCount = 1;
		}
		//player.getActionSender().sendMessage("You must wait " + cycleCount + " cycles to mine this ore.");
		return new Random().nextInt(5) + 7;
	}
	
	/**
	 * Attempts to calculate the mining success rate based on mining level and the player's pickaxe.
	 * @param player The player performing the action.
	 * @param pickaxe The pickaxe the player is using.
	 */
	public double factorBoost(Player player, Pickaxe pickaxe) {
		final int miningLevel = player.getSkills().getLevel(Skills.MINING)/1000;
		if(pickaxe == pickaxe.BRONZE)
			multiplyer = 1;
		if (pickaxe == pickaxe.IRON)
			multiplyer =  .95;
		if(pickaxe == pickaxe.STEEL)
			multiplyer = .90;
		if (pickaxe == pickaxe.MITHRIL)
			multiplyer = .80;
		if (pickaxe == pickaxe.ADAMANT)
			multiplyer = .70;
		if (pickaxe == pickaxe.RUNE)
			multiplyer = .60;
		return multiplyer - miningLevel;
	}
	
	
	@Override
	public int getCycles() {
		return cycleCount;
	}

	@Override
	public double getFactor() {
		return factor;
	}

	@Override
	public Item getHarvestedItem() {
		return new Item(node.getOreId(), 1);
	}

	@Override
	public double getExperience() {
		return node.getExperience();
	}

	@Override
	public Animation getAnimation() {
		return Animation.create(pickaxe.getAnimation());
	}

	@Override
	public int getSkill() {
		return Skills.MINING;
	}

	@Override
	public boolean requiresReplacementObject() {
		return REPLACEMENT;
	}
	
	@Override
	public int getRespawnTime() {
		return node.getOreReplacementTime();
	}

	@Override
	public GameObject getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameObject getReplacementObject() {
		// TODO Auto-generated method stub
		return null;
	}
}
