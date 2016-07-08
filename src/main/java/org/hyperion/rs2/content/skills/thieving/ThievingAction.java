package org.hyperion.rs2.content.skills.thieving;

import java.util.Random;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.skills.thieving.ThievingData.Mob;
import org.hyperion.rs2.content.skills.thieving.ThievingData.Stall;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * The actions taken when performing the thieving skill.
 * @author Stephen
 */
public class ThievingAction {

	/**
	 * The id of the pickpocket animation.
	 */
	private static final int STEALING_ANIM = 881;
	
	/**
	 * The id of the stunned graphic.
	 */
	private static final int STUNNED_GRAPHIC = 80;
	
	/**
	 * The time it takes to complete the action in ms.
	 */
	private static final int ACTION_TIME = 2000;
	
	/**
	 * The time a stun lasts on a failed attempt in ms.
	 */
	private static final int STUN_TIME = 7000;
	
	/**
	 * The action of stealing from an NPC.
	 * @param player The player attempting to steal.
	 * @param npc The NPC being stolen from.
	 */
	public static void stealFromNPC(final Player player, final NPC npc) {
		if (Mob.forId((int) npc.getId()) == null) return;
		
		final Mob mob = Mob.forId((int) npc.getId());
		if (!canSteal(player, mob)) return;
		
		player.face(npc.getLocation());
		player.playAnimation(Animation.create(STEALING_ANIM));
		World.getWorld().submit(new Event(ACTION_TIME, false) {

			@Override
			public void execute() {
				double skill = getSuccessRate(player, mob);
				double toughness = getToughness(mob);
				
				if (skill >= toughness) {
					player.getInventory().add(mob.getRewards()[0]);
					//player.getSkills().addExperience(Skills.THIEVING, mob.getXp() * Constants.SKILL_EXPERIENCE);
				} else {
					/*npc.setForcedText("Hey! What do you think you're doing?");
					player.inflictDamage(new Hit(mob.getHit(), HitType.NORMAL_DAMAGE), (Entity) npc); 
					player.root(STUN_TIME, player);
					player.playGraphics(Graphic.create(STUNNED_GRAPHIC, 0, 100));*/
				}
				
				this.stop();
			}
			
		});
	}
	
	public static void stealFromStall(final Player player/*WorldObject obj*/) {
		/*if (Stall.forId(obj.objectId) == null) return;
		
		final Stall stall = Stall.forId(obj.objectId);
		
		player.face(Location.create(obj.objectX, obj.objectY, player.getLocation().getZ()));
		player.playAnimation(Animation.create(STEALING_ANIM));
		World.getWorld().submit(new Event(ACTION_TIME, false) {

			@Override
			public void execute() {
				player.getInventory().add(stall.getRewards()[0]);
				//player.getSkills().addExperience(Skills.THIEVING, stall.getXp() * Constants.SKILL_EXPERIENCE);
				this.stop();
			}
			
		});*/
	}
	
	private static boolean canSteal(Player player, Mob mob) {
		//Check if player has been stunned.
		/*if (player.isRooted()) {
			player.getActionSender().sendMessage("You are still shaken up from the blow.");
			return false;
		}*/
		
		//Check player's thieving level.
		/*if (player.getSkills().getLevel(Skills.THIEVING) < mob.getLevel()) {
			player.getActionSender().sendMessage("You need a thieving level of " + mob.getLevel() 
					+ " to steal from the " + MobDefinition.forId(mob.getNpc()).getName() + ".");
			return false;
		}*/
		
		return true;
	}
	
	/**
	 * The formula for whether or not the player was successful in their
	 * thieving attempt.
	 * @param player The player attempting to steal.
	 * @param mob The mob being stolen from.
	 * @return The success rate.
	 */
	private static double getSuccessRate(Player player, Mob mob) {
		double successRate = 0;
		int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
		int toughness = mob.getLevel();
		
		successRate = (30 + thievingLevel) - toughness;
		successRate = successRate/100;
		
		if (Constants.DEV_MODE) {
			player.getActionSender().sendMessage("Theiving success rate: " + successRate + "%.");
		}
		
		return successRate;
	}
	
	/**
	 * Calculates the difficulty level of a mob.
	 * @param mob The mob being stolen from.
	 * @return The difficulty level.
	 */
	private static double getToughness(Mob mob) {
		Random random = new Random();
		double toughness = 0;
		toughness = (double) mob.getLevel()/100;
		
		if (toughness < .20) {
			toughness = .20 + ((double) random.nextInt(30)/100);
		}

		return toughness;
	}
}
