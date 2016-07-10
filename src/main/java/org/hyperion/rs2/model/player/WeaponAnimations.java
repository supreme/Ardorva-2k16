package org.hyperion.rs2.model.player;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.container.Equipment;

/**
 * Handles the weapon animations for standing, walking, etc.
 * @author Stephen Andrews
 */
public class WeaponAnimations {
	
	/**
	 * The default stand animation.
	 */
	private static final int STAND = 0x328;
	
	/**
	 * The default stand turn animation.
	 */
	private static final int STAND_TURN = 0x337;
	
	/**
	 * The default walk animation.
	 */
	private static final int WALK = 0x333;
	
	/**
	 * The default turn 180 animation.
	 */
	private static final int TURN_180 = 0x334;
	
	/**
	 * The default turn 90 CW animation.
	 */
	private static final int TURN_90CW = 0x335;
	
	/**
	 * The default turn 90 CCW animation.
	 */
	private static final int TURN_90CCW = 0x336;
	
	/**
	 * The default run animation.
	 */
	private static final int RUN = 0x338;
	
	/**
	 * Gets the stand animation for the equipped weapon.
	 * @return The stand animation.
	 */
	public static int getStandAnim(Player player) {
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weapon != null && weapon.getDefinition().getWeaponDefinition() != null) {
			return weapon.getDefinition().getWeaponDefinition().getStandAnimation();
		} else {
			return STAND;
		}
	}
	
	/**
	 * Gets the walk animation for the equipped weapon.
	 * @return The walk animation.
	 */
	public static int getWalkAnim(Player player) {
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weapon != null && weapon.getDefinition().getWeaponDefinition() != null) {
			return weapon.getDefinition().getWeaponDefinition().getWalkAnimation();
		} else {
			return WALK;
		}
	}
	
	/**
	 * Gets the run animation for the equipped weapon.
	 * @return The run animation.
	 */
	public static int getRunAnim(Player player) {
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weapon != null && weapon.getDefinition().getWeaponDefinition() != null) {
			return weapon.getDefinition().getWeaponDefinition().getRunAnimation();
		} else {
			return RUN;
		}
	}
}
