package org.hyperion.rs2.model.definitions;

import org.hyperion.rs2.LivingClasses;

/**
 * Provides a structure for JSON serialization of item definitions.
 * @author Stephen Andrews
 */
public class ItemDefinition {

	/**
	 * The name of the item.
	 */
	private String itemName;
	
	/**
	 * The id of the item.
	 */
	private int itemId;
	
	/**
	 * The value of the item.
	 */
	private int itemValue;
	
	/**
	 * Whether or not the item is tradeable.
	 */
	private boolean isTradeable;
	
	/**
	 * Whether or not the item stacks on itself.
	 */
	private boolean isStackable;
	
	/**
	 * Whether or not the item is a noted version.
	 */
	private boolean isNote;
	
	/**
	 * The noted id of the item.
	 */
	private int notedId;
	
	/**
	 * The unnoted id of the item.
	 */
	private int unNotedId;
	
	/**
	 * The equipment definition of the item.
	 */
	private EquipmentDefinition equipmentDefinition;
	
	/**
	 * The weapon definition of the item.
	 */
	private WeaponDefinition weaponDefinition;
	
	/**
	 * Gets an item definition for the specified item id.
	 * @return The item definition.
	 */
	public static ItemDefinition forId(int id) {
		return LivingClasses.definitionLoader.getItemDefinitions()[id];
	}
	
	/**
	 * Gets an item definition for the specified item name.
	 * @return The item definition.
	 */
	public static ItemDefinition forName(String name) {
		name = name.toLowerCase();
		ItemDefinition requested = null;
		String currentName = "";
		for (ItemDefinition def : LivingClasses.definitionLoader.getItemDefinitions()) {
			if (def != null && def.getName() != null) 
				currentName = def.getName().toLowerCase();
			if (currentName.equals(name)) {
				requested = def;
			}
		}
		return requested;
	}
	
	/**
	 * Gets the bonuses of the item.
	 * @param itemId The item id to get bonuses for.
	 * @return An array of bonuses.
	 */
	public static final int[] getBonuses(int itemId) {
		return LivingClasses.definitionLoader.getBonuses().get(itemId);
	}
	
	/**
	 * Gets the item's name.
	 * @return The item name.
	 */
	public String getName() {
		return itemName;
	}
	
	/**
	 * Gets the item's id.
	 * @return The item id;
	 */
	public int getId() {
		return itemId;
	}
	
	/**
	 * Gets the item's value.
	 * @return The item value.
	 */
	public int getValue() {
		return itemValue;
	}
	
	/**
	 * Whether or not the item is tradeable.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isTradeable() {
		return isTradeable;
	}
	
	/**
	 * Whether or not the item is stackable.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isStackable() {
		return isStackable;
	}
	
	/**
	 * Whether or not the item is noted.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isNoted() {
		return isNote;
	}
	
	/**
	 * Gets the item's noted id.
	 * @return The noted id.
	 */
	public int getNotedId() {
		return notedId;
	}
	
	/**
	 * Gets the item's unnoted id.
	 * @return The unnoted id.
	 */
	public int getUnNotedId() {
		return unNotedId;
	}
	
	/**
	 * Gets the item's equipment definition.
	 * @return The equipment definition.
	 */
	public EquipmentDefinition getEquipmentDefinition() {
		return equipmentDefinition;
	}
	
	/**
	 * Gets the item's weapon definition.
	 * @return The weapon definition.
	 */
	public WeaponDefinition getWeaponDefinition() {
		return weaponDefinition;
	}
	
	@Override
	public String toString() {
		return "ItemDef: [" + getName() + "] [" + getId() + "] [" + getValue() + "] [" + isTradeable() + "] "
				+ "[" + isStackable() + "] [" + isNoted() + "] [" + getNotedId() + "] [" + getUnNotedId() + "] "
				+ "[" + getEquipmentDefinition() + "] [" + getWeaponDefinition() + "].";
	}
	
	/**
	 * Structure of the nested object for equipment definitions.
	 * @author Stephen Andrews
	 */
	public class EquipmentDefinition {
		
		/**
		 * The equipment slot of the item.
		 */
		private int equipmentSlot;
		
		/**
		 * The requirements of the equipment.
		 */
		private int[] requirements;
		
		/**
		 * Gets the equipment slot.
		 * @return The equipment slot.
		 */
		public int getSlot() {
			return equipmentSlot;
		}
		
		/**
		 * Gets the requirements.
		 * @return The requirements.
		 */
		public int[] getRequirements() {
			return requirements;
		}
	}
	
	/**
	 * Structure of the nested object for weapon definitions.
	 * @author Stephen Andrews
	 */
	public class WeaponDefinition {
		
		/**
		 * Whether or not the weapon is a two hander.
		 */
		private boolean isTwoHanded;
		
		/**
		 * The combat style of the weapon.
		 */
		private String combatType;
		
		/**
		 * The attack styles of the weapon.
		 */
		private AttackStyles attackStyles;
		
		/**
		 * The ranged weapon definition.
		 */
		private RangedWeaponDefinition rangedWeaponDefinition;
		
		/**
		 * The block animation.
		 */
		private int blockAnimation;
		
		/**
		 * The stand animation.
		 */
		private int standAnimation;
		
		/**
		 * The walk animation.
		 */
		private int walkAnimation;
		
		/**
		 * The run animation.
		 */
		private int runAnimation;
		
		/**
		 * The interface id.
		 */
		private int interfaceId;
		
		/**
		 * The child id.
		 */
		private int child;
		
		/**
		 * Whether or not the weapon has a special attack.
		 */
		private boolean hasSpecialAttack;
		
		/**
		 * The special attack data.
		 */
		private SpecialAttackData specialAttackData;
		
		/**
		 * The sound id.
		 */
		private int soundId;
		
		/**
		 * Whether or not the weapon is a two hander.
		 * @return <code>true</code> if so, <code>false</code> if not.
		 */
		public boolean isTwoHanded() {
			return isTwoHanded;
		}
		
		/**
		 * Gets the combat type.
		 * @return The combat type.
		 */
		public String getCombatType() {
			return combatType;
		}
		
		/**
		 * Gets the attack styles.
		 * @return The attack styles.
		 */
		public AttackStyles getAttackStyles() {
			return attackStyles;
		}
		
		/**
		 * Gets the ranged weapon definition.
		 * @return The ranged weapon definitoin.
		 */
		public RangedWeaponDefinition getRangedWeaponDefinition() {
			return rangedWeaponDefinition;
		}
		
		/**
		 * Gets the block animation.
		 * @return The block animation.
		 */
		public int getBlockAnimation() {
			return blockAnimation;
		}
		
		/**
		 * Gets the stand animation.
		 * @return The stand animation.
		 */
		public int getStandAnimation() {
			return standAnimation;
		}
		
		/**
		 * Gets the walk animation.
		 * @return The walk animation.
		 */
		public int getWalkAnimation() {
			return walkAnimation;
		}
		
		/**
		 * Gets the run animation.
		 * @return The run animation.
		 */
		public int getRunAnimation() {
			return runAnimation;
		}
		
		/**
		 * Gets the interface id.
		 * @return The interface id.
		 */
		public int getInterfaceId() {
			return interfaceId;
		}
		
		/**
		 * Gets the child.
		 * @return The child.
		 */
		public int getChild() {
			return child;
		}
		
		/**
		 * Whether or not the item has a special attack.
		 * @return <code>true</code> if so, <code>false</code> if not.
		 */
		public boolean hasSpecialAttack() {
			return hasSpecialAttack;
		}
		
		/**
		 * Gets the special attack data.
		 * @return The special attack data.
		 */
		public SpecialAttackData getSpecialAttackData() {
			return specialAttackData;
		}
		
		/**
		 * Gets the sound id.
		 * @return The sound id.
		 */
		public int getSoundId() {
			return soundId;
		}
	}
	
	/**
	 * Structure of the nested object for attack styles.
	 * @author Stephen Andrews
	 */
	public class AttackStyles {
		
		/**
		 * The accurate animation.
		 */
		private int accurateAnimation;
		
		/**
		 * The accurate speed.
		 */
		private int accurateSpeed;
		
		/**
		 * The aggressive animation.
		 */
		private int aggressiveAnimation;
		
		/**
		 * The aggressive speed.
		 */
		private int aggressiveSpeed;
		
		/**
		 * The controlled animation.
		 */
		private int controlledAnimation;
		
		/**
		 * The controlled speed.
		 */
		private int controlledSpeed;
		
		/**
		 * The defensive animation;
		 */
		private int defensiveAnimation;
		
		/**
		 * The defensive speed.
		 */
		private int defensiveSpeed;
		
		/**
		 * Gets the accurate animation.
		 * @return The acurate animation.
		 */
		public int getAccurateAnimation() {
			return accurateAnimation;
		}
		
		/**
		 * Gets the accurate speed.
		 * @return The accurate speed.
		 */
		public int getAccurateSpeed() {
			return accurateSpeed;
		}
		
		/**
		 * Gets the aggressive animation.
		 * @return The aggressive animation.
		 */
		public int getAggressiveAnimation() {
			return aggressiveAnimation;
		}
		
		/**
		 * Gets the aggressive speed.
		 * @return The aggressive speed.
		 */
		public int getAggressiveSpeed() {
			return aggressiveSpeed;
		}
		
		/**
		 * Gets the controlled animation.
		 * @return The controlled animation.
		 */
		public int getControlledAnimation() {
			return controlledAnimation;
		}
		
		/**
		 * Gets the controlled speed.
		 * @return The controlled speed.
		 */
		public int getControlledSpeed() {
			return controlledSpeed;
		}
		
		/**
		 * Gets the defensive animation.
		 * @return The defensive animation.
		 */
		public int getDefensiveAnimation() {
			return defensiveAnimation;
		}
		
		/**
		 * Gets the defensive speed.
		 * @return The defensive speed.
		 */
		public int getDefensiveSpeed() {
			return defensiveSpeed;
		}
	}
	
	/**
	 * Structure of the nested object for ranged weapon definitions.
	 * @author Stephen Andrews
	 */
	public class RangedWeaponDefinition {
		
		/**
		 * The arrows compatible with the weapon.
		 */
		private int[] arrowsAllowed;
		
		/**
		 * Gets the arrows combatible with the weapon.
		 * @return The arrows allowed.
		 */
		public int[] getArrowsAllowed() {
			return arrowsAllowed;
		}
	} 
	
	/**
	 * Structure of the nested object for special attack data.
	 * @author Stephen Andrews
	 */
	public class SpecialAttackData {
		
		/**
		 * The animation id.
		 */
		private int animationId;
		
		/**
		 * The animation delay.
		 */
		private int animationDelay;
		
		/**
		 * The graphic id.
		 */
		private int graphicId;
		
		/**
		 * The graphic delay.
		 */
		private int graphicDelay;
		
		/**
		 * Whether or not the graphic is a high graphic.
		 */
		private boolean isHighGraphic;
		
		/**
		 * The special bar data.
		 */
		private int[] specialBarData;
		
		/**
		 * The special amount.
		 */
		private String specialAmount;
		
		/**
		 * The special bar button id.
		 */
		private int specialBarButtonId;
		
		/**
		 * Gets the animation id.
		 * @return The animation id.
		 */
		public int getAnimationId() {
			return animationId;
		}
		
		/**
		 * Gets the animation delay.
		 * @return The animation delay.
		 */
		public int getAnimationDelay() {
			return animationDelay;
		}
		
		/**
		 * Gets the graphic id.
		 * @return The graphic id.
		 */
		public int getGraphicId() {
			return graphicId;
		}
		
		/**
		 * Gets the graphic delay.
		 * @return The graphic delay.
		 */
		public int getGraphicDelay() {
			return graphicDelay;
		}
		
		/**
		 * Whether or not the graphic is high.
		 * @return <code>true</code> if so, <code>false</code> if not.
		 */
		public boolean isHighGraphic() {
			return isHighGraphic;
		}
		
		/**
		 * Gets the special bar data.
		 * @return The special bar data.
		 */
		public int[] getSpecialBarData() {
			return specialBarData;
		}
		
		/**
		 * Gets the special amount.
		 * @return The special amount.
		 */
		public String getSpecialAmount() {
			return specialAmount;
		}
		
		/**
		 * Gets the special bar button id.
		 * @return The special bar button id.
		 */
		public int getSpecialBarButtonId() {
			return specialBarButtonId;
		}
	}
}
