package org.hyperion.rs2.content;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.definitions.ItemDefinition;
import org.hyperion.rs2.model.player.Player;

/**
 * Contains data pertaining to skillcapes.
 * @author Stephen Andrews
 * @author Jason (used the ids he provided).
 */
public class Skillcape {

	enum Cape {
		
		ATTACK(Skills.ATTACK, 9747, 9748, 9749, 4959, 823, 4),
		
		MAGIC(Skills.MAGIC, 9762, 9763, 9764, 4939, 813, 3); //gfx 872 ??
		
		/**
		 * The skill number.
		 */
		private int skillNumber;
		
		/**
		 * The id of an untrimmed cape.
		 */
		private int untrimmed;
		
		/**
		 * The id of a trimmed cape.
		 */
		private int trimmed;
		
		/**
		 * The id of the cape's hood.
		 */
		private int hood;
		
		/**
		 * The id of the animation.
		 */
		private int anim;
		
		/**
		 * The id of the graphic.
		 */
		private int gfx;
		
		/**
		 * The duration of the animation.
		 */
		private int duration;
		
		/**
		 * Contructs a cape.
		 * @param untrimmed The id of an untrimmed cape.
		 * @param trimmed The id of a trimmed cape.
		 * @param hood The id of the cape's hood.
		 * @param anim The id of the animation.
		 * @param gfx The id of the graphic.
		 * @param duration The duration of the animation.
		 */
		Cape(int skillNumber, int untrimmed, int trimmed, int hood, int anim, int gfx, int duration) {
			this.skillNumber = skillNumber;
			this.untrimmed = untrimmed;
			this.trimmed = trimmed;
			this.hood = hood;
			this.anim = anim;
			this.gfx = gfx;
			this.duration = duration;
		}
		
		/**
		 * Map of capes.
		 */
		private static Map<Integer, Cape> capes = new HashMap<Integer, Cape>();
		
		static {
			for (Cape c : Cape.values()) {
				capes.put(c.getSkill(), c);
			}
		}
		
		/**
		 * Gets the skill the cape belongs to.
		 * @return The skill number of the cape.
		 */
		public int getSkill() {
			return skillNumber;
		}
		
		/**
		 * Gets the item id of the untrimmed cape.
		 * @return The id of the untrimmed cape.
		 */
		public int getUntrimmed() {
			return untrimmed;
		}
		
		/**
		 * Gets the item id of the trimmed cape.
		 * @return The id of the trimmed cape.
		 */
		public int getTrimmed() {
			return trimmed;
		}
		
		/**
		 * Gets the item id of the hood.
		 * @return The id of the hood.
		 */
		public int getHood() {
			return hood;
		}
		
		/**
		 * Gets the animation id.
		 * @return The animation id.
		 */
		public int getAnim() {
			return anim;
		}
		
		/**
		 * Gets the graphic id.
		 * @return The graphic id.
		 */
		public int getGfx() {
			return gfx;
		}
		
		/**
		 * Gets the duration of the animation.
		 * @return The duration of the animation.
		 */
		public int getDuration() {
			return duration;
		}
		
		/**
		 * Gets the capes map.
		 * @return The capes map.
		 */
		public static Map<Integer, Cape> getCapes() {
			return capes;
		}
	}
	
	/**
	 * Awards a skill cape for a certain skill.
	 * @param player The player to be awarded.
	 * @param skillNumber The skill number of the skill the cape pertains to.
	 */
	public static void awardSkillcape(Player player, int skillNumber) {
		Skills skills = new Skills(player);
		Cape cape = Cape.getCapes().get(skillNumber);
		
		if (skills.getMasteredSkills() < 2) {
			if (!player.getInventory().add(new Item(cape.getUntrimmed()))) {
				//FloorItemEvent.addFloorItem(new FloorItem(cape.getUntrimmed(), 1, player.getLocation(), player, player, false));
			}
		} else {
			if (!player.getInventory().add(new Item(cape.getTrimmed()))) {
				//FloorItemEvent.addFloorItem(new FloorItem(cape.getTrimmed(), 1, player.getLocation(), player, player, false));
			}
		}
		
		player.getActionSender().sendMessage("Congratulations " + player.getName() + "! You have mastered the " + Skills.SKILL_NAME[skillNumber] + " skill.");
	}
	
	/**
	 * Plays a skillcape animation.
	 */
	public static void doAnimation(Player player) {
		if (player.getEquipment().get(Container.CAPE) != null) {
			int skillNumber = getSkill(player.getEquipment().get(Container.CAPE));
			if (skillNumber == -1) return;
			
			Cape cape = Cape.getCapes().get(skillNumber);
			player.playAnimation(Animation.create(cape.getAnim()));
			player.playGraphics(Graphic.create(cape.getGfx()));
		}
	}
	
	/**
	 * Gets the skill that a skillcape belongs to.
	 * @param item The skillcape.
	 * @return The skill number the cape belongs to.
	 */
	private static int getSkill(Item item) {
		String itemName = ItemDefinition.forId(item.getId()).getName().toLowerCase();
		if (itemName.contains("attack")) {
			return Skills.ATTACK;
		} else if (itemName.contains("defence")) {
			return Skills.DEFENCE;
		} else if (itemName.contains("strength")) {
			return Skills.STRENGTH;
		} else if (itemName.contains("hitpoints")) {
			return Skills.HITPOINTS;
		} else if(itemName.contains("range")) {
			return Skills.RANGE;
		} else if(itemName.contains("prayer")) {
			return Skills.PRAYER;
		} else if(itemName.contains("magic")) {
			return Skills.MAGIC;
		} else if(itemName.contains("cooking")) {
			return Skills.COOKING;
		} else if(itemName.contains("woodcutting")) {
			return Skills.WOODCUTTING;
		} else if(itemName.contains("fletching")) {
			return Skills.FLETCHING;
		} else if(itemName.contains("fishing")) {
			return Skills.FISHING;
		} else if(itemName.contains("firemaking")) {
			return Skills.FIREMAKING;
		} else if(itemName.contains("crafting")) {
			return Skills.CRAFTING;
		} else if(itemName.contains("smithing")) {
			return Skills.SMITHING;
		} else if(itemName.contains("mining")) {
			return Skills.MINING;
		} else if(itemName.contains("herblore")) {
			return Skills.HERBLORE;
		} else if(itemName.contains("agility")) {
			return Skills.AGILITY;
		} else if(itemName.contains("thieving")) {
			return Skills.THIEVING;
		} else if(itemName.contains("slayer")) {
			return Skills.SLAYER;
		} else if(itemName.contains("farming")) {
			return Skills.FARMING;
		} else if(itemName.contains("runecrafting")) {
			return Skills.RUNECRAFTING;
		}
		
		return -1;
	}
}
