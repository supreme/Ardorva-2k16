package org.hyperion.rs2.content.combat.util;

import org.hyperion.rs2.content.combat.logic.CombatFormulas;
import org.hyperion.rs2.content.combat.util.CombatData.AttackType;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.WeaponAnimations;

/**
 * Manages an entity's combat by updating max hits, handling attack animations, etc.
 * @author Stephen Andrews
 */
public class CombatUtility {

	/**
	 * The entity the <code>CombatUtility</code> belongs to.
	 */
	private Entity entity;
	
	/**
	 * The entity's max hit.
	 */
	private int maxHit;
	
	/**
	 * The entity's walk animation.
	 */
	private int walkAnimation;
	
	/**
	 * The entity's run animation.
	 */
	private int runAnimation;
	
	/**
	 * The entity's attack animation.
	 */
	private int attackAnimation;
	
	/**
	 * The entity's block animation.
	 */
	private int blockAnimation;
	
	/**
	 * The entity's attack speed.
	 */
	private int attackSpeed;
	
	/**
	 * Creates a <code>CombatUtility</code> to be assigned to an entity.
	 * @param entity The entity the <code>CombatUtility</code> belongs to.
	 */
	public CombatUtility(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * Refreshes all the data for the entity's <code>CombatUtility</code>.
	 * Usually invoked on an equipment change.
	 */
	public void refresh() {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			sendWeaponTab(player);
			maxHit = CombatFormulas.calculateMeleeRangeMaxHit(player, AttackType.MELEE);
			walkAnimation = WeaponAnimations.getWalkAnim(player);
			runAnimation = WeaponAnimations.getRunAnim(player);
			attackAnimation = CombatAnimations.getAttackingAnimation(player);
			blockAnimation = CombatAnimations.getDefensiveAnimation(player);
			attackSpeed = AttackSpeeds.getAttackSpeed(player);
		} else { //TODO: Call this somewhere lol - Stephen (on register for world mebe)
			NPC npc = (NPC) entity;
			maxHit = npc.getDefinition().getMaxHit();
			attackAnimation = CombatAnimations.getAttackingAnimation(npc);
			blockAnimation = CombatAnimations.getDefensiveAnimation(npc);
			attackSpeed = AttackSpeeds.getAttackSpeed(npc);
		}
	}
	
	/**
	 * Sends the correct weapon tab interface.
	 * @param player The player to send the interface to.
	 */
	private void sendWeaponTab(Player player) {
		/* Send the correct weapon interface */
		Item playerWeapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.getDefinition().getWeaponDefinition() != null) {
			int interfaceId = playerWeapon.getDefinition().getWeaponDefinition().getInterfaceId();
			player.getActionSender().sendTab(86, interfaceId);
			player.getActionSender().sendString(interfaceId, 0, playerWeapon.getDefinition().getName());
			player.getPlayerConfiguration().setWeaponTabInterface(interfaceId);
		} else {
			player.getActionSender().sendTab(86, 92);
			player.getActionSender().sendString(92, 0, "Unarmed");
			player.getPlayerConfiguration().setWeaponTabInterface(92);
		}
	}
	
	/**
	 * Gets the entity's max hit.
	 * @return The entity's max hit.
	 */
	public int getMaxHit() {
		return maxHit;
	}
	
	/**
	 * Sets the entity's max hit.
	 * @param maxHit The new max hit.
	 */
	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}
	
	/**
	 * Gets the entity's walk animation.
	 * @return The entity's walk animation.
	 */
	public int getWalkAnimation() {
		return walkAnimation;
	}
	
	/**
	 * Sets the entity's walk animation.
	 * @param walkAnimation The new walk animation.
	 */
	public void setWalkAnimation(int walkAnimation) {
		this.walkAnimation = walkAnimation;
	}
	
	/**
	 * Gets the entity's run animation.
	 * @return The entity's run animation.
	 */
	public int getRunAnimation() {
		return runAnimation;
	}
	
	/**
	 * Sets the entity's run animation.
	 * @param runAnimation The new run animation.
	 */
	public void setRunAnimation(int runAnimation) {
		this.runAnimation = runAnimation;
	}
	
	/**
	 * Gets the entity's attack animation.
	 * @return The entity's attack animation.
	 */
	public int getAttackAnimation() {
		return attackAnimation;
	}
	
	/**
	 * Sets the entity's attack animation.
	 * @param attackAnimation The new attack animation.
	 */
	public void setAttackAnimation(int attackAnimation) {
		this.attackAnimation = attackAnimation;
	}
	
	/**
	 * Gets the entity's block animation.
	 * @return The entity's block animation.
	 */
	public int getBlockAnimation() {
		return blockAnimation;
	}
	
	/**
	 * Sets the entity's block animation.
	 * @param blockAnimation The new block animation.
	 */
	public void setBlockAnimation(int blockAnimation) {
		this.blockAnimation = blockAnimation;
	}
	
	/**
	 * Gets the entity's attack speed.
	 * @return The entity's attack speed.
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	/**
	 * Sets the entity's attack speed.
	 * @param attackSpeed The new attack speed.
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}
}
