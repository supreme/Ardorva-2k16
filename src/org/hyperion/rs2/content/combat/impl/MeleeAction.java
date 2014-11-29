package org.hyperion.rs2.content.combat.impl;

import java.text.DecimalFormat;
import java.util.Random;

import org.hyperion.rs2.content.combat.CombatAction;
import org.hyperion.rs2.content.combat.logic.CombatFormulas;
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
		Hit damage = calculateDamage(aggressor, victim);
		aggressor.playAnimation(Animation.create(aggressor.getCombatUtility().getAttackAnimation()));
		inflictDamage(victim, damage);
		aggressor.getEntityCooldowns().flag(CooldownFlags.MELEE_SWING, aggressor.getCombatUtility().getAttackSpeed(), aggressor);
	}

	@Override
	public boolean canAttack(Entity aggressor, Entity victim) {
		if (aggressor.isDead() || victim.isDead()) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public Hit calculateDamage(Entity aggressor, Entity victim) {
		if (aggressor instanceof NPC) {
			//TODO - Stephen
			int hit = (int) (Math.random() * aggressor.getCombatUtility().getMaxHit());
			return new Hit(hit, HitType.NORMAL_DAMAGE);
		}
		int maxHit = aggressor.getCombatUtility().getMaxHit();
		double aggressorAccuracy = CombatFormulas.calculateOffensiveAccuracy(getPlayer(), AttackType.MELEE);
		double victimDefense = CombatFormulas.calculateDefensiveBonus(victim);
		double hitChance = Math.round((1 - (victimDefense / aggressorAccuracy)) * 100);
		
		getPlayer().getActionSender().sendMessage("Max hit: " + maxHit + " | Hit chance: " + hitChance);
		if (hitChance >= (Math.random()  * 100)) { //TODO: Math.random has 0.0 inclusive - Stephen
			int hit = (int) (maxHit * Math.random());
			return new Hit(hit, HitType.NORMAL_DAMAGE);
		}
		
		//TODO: Calculate effectiveness against rangers - Stephen
		
		return new Hit(0, HitType.NO_DAMAGE);
	}

	@Override
	public void execute() {
		if (!aggressor.getEntityCooldowns().get(CooldownFlags.MELEE_SWING)) {
			if (!canAttack(aggressor, victim)) {
				this.stop();
				return;
			}
			
			executeAttack(aggressor, victim);
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
