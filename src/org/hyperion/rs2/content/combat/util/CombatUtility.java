package org.hyperion.rs2.content.combat.util;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.player.Player;

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
	 * Creates a <code>CombatUtility</code> to be assigned to an entity.
	 * @param entity The entity the <code>CombatUtility</code> belongs to.
	 */
	public CombatUtility(Entity entity) {
		if (entity instanceof Player) {
			
		} else {
			
		}
	}
	
	/**
	 * Refreshes the data for the entity's <code>CombatUtility</code>.
	 * Usually invoked on an equipment change.
	 */
	public void refresh() {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			walkAnimation = player.getEquipment().get(Equipment.SLOT_WEAPON)
					.getDefinition().getWeaponDefinition().getWalkAnimation();
			runAnimation = player.getEquipment().get(Equipment.SLOT_WEAPON)
					.getDefinition().getWeaponDefinition().getRunAnimation();
			//attackAnimation = player.getEquipment().get(Equipment.SLOT_WEAPON)
					//.getDefinition().getWeaponDefinition().getAttackStyles().get
		}
	}
	
	/**
	 * Sends the correct weapon tab interface.
	 * @param player The player to send the interface to.
	 */
	public static void sendWeaponTab(Player player) {
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
}
