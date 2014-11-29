package org.hyperion.rs2.content.combat.impl;

import java.text.DecimalFormat;
import java.util.Random;

import org.hyperion.rs2.content.combat.CombatAction;
import org.hyperion.rs2.content.combat.logic.CombatFormulas;
import org.hyperion.rs2.content.combat.util.AttackSpeeds;
import org.hyperion.rs2.content.combat.util.CombatAnimations;
import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.EntityCooldowns.CooldownFlags;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * Represents an ongoing combat session belonging to an entity of
 * the combat type melee.
 * @author Stephen Andrews
 */
public class MeleeAction extends CombatAction {

	/**
	 * Constructs the melee action.
	 * @param aggressor The entity performing the attack.
	 * @param victim The entity receiving the attack.
	 * @param delay The delay between attacks.
	 */
	public MeleeAction(Entity aggressor, Entity victim) {
		super(aggressor, victim);
		aggressor.setInteractingEntity(victim);
		victim.setInteractingEntity(aggressor);
	}

	@Override
	public void executeAttack(Entity aggressor, Entity victim) {
		int hit; 
		if (aggressor instanceof Player) {
			hit = (int) (CombatFormulas.calculateMeleeRangeMaxHit((Player) aggressor, AttackType.MELEE) * Math.random());
		} else {
			NPC npc = (NPC) aggressor;
			Random random = new Random();
			hit = random.nextInt(npc.getDefinition().getMaxHit());
		}
		
		Hit damage = new Hit(hit, HitType.NORMAL_DAMAGE);
		aggressor.playAnimation(Animation.create(CombatAnimations.getAttackingAnimation(aggressor)));
		victim.playAnimation(Animation.create(CombatAnimations.getDefensiveAnimation(victim)));
		inflictDamage(victim, damage);
		aggressor.getEntityCooldowns().flag(CooldownFlags.MELEE_SWING, AttackSpeeds.getAttackSpeed(aggressor), aggressor);
	}

	@Override
	public boolean canAttack(Entity aggressor, Entity victim) {
		if (aggressor.isDead() || victim.isDead()) {
			return false;
		}
		
		return true;
	}

	@Override
	public void execute() {
		if (!aggressor.getEntityCooldowns().get(CooldownFlags.MELEE_SWING)) {
			if (!canAttack(aggressor, victim)) {
				this.stop();
				return;
			}
			
			executeAttack(aggressor, victim);
			double accuracy = CombatFormulas.calculateOffensiveAccuracy(getPlayer(), AttackType.MELEE);
			double defense = CombatFormulas.calculateDefensiveBonus(getPlayer());
			double chance = (1 - (defense / accuracy)) * 100;
			DecimalFormat df = new DecimalFormat("#.00");
			//chance = Math.round(chance * 1000) / 1000;
			((Player) aggressor).getActionSender().sendMessage("Hit chance: " + df.format(chance) + "%");
			//100 - (Divide defensive bonus by offensive accuracy) to get hit percent change
			//Some numbers I got with my setup were a 28% chance to hit based on - http://gyazo.com/4975ece55d97fa44bc903db9f57e8533
			//And all 99 stats, seems pretty good to me :P
		}
	}

	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.NEVER;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.FOLLOW;
	}
}
