package org.hyperion.rs2.content.combat.impl;

import org.hyperion.rs2.content.combat.CombatAction;
import org.hyperion.rs2.content.combat.logic.CombatFormulas;
import org.hyperion.rs2.content.combat.util.AttackSpeeds;
import org.hyperion.rs2.content.combat.util.CombatAnimations;
import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
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
		super(aggressor, victim, AttackSpeeds.getAttackSpeed(aggressor));
		aggressor.setInteractingEntity(victim);
		victim.setInteractingEntity(aggressor);
	}

	@Override
	public void executeAttack(Entity aggressor, Entity victim) {
		int hit = (int) (CombatFormulas.calculateMeleeRangeMaxHit((Player) aggressor, AttackType.MELEE) * Math.random());
		Hit damage = new Hit(hit, HitType.NORMAL_DAMAGE);
		aggressor.playAnimation(Animation.create(CombatAnimations.getAttackingAnimation(aggressor)));
		victim.playAnimation(Animation.create(CombatAnimations.getDefensiveAnimation(victim)));
		inflictDamage(victim, damage);
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
		if (!canAttack(aggressor, victim)) {
			stop();
		}
		
		executeAttack(aggressor, victim);
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
