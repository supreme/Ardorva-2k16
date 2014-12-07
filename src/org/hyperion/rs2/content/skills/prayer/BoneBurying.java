package org.hyperion.rs2.content.skills.prayer;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.player.Player;

/**
 * This class implements code related to all buryable bones. 
 * 
 * All of the data is an accurate representation of Runescape itself.
 * Data has been collected from <url>http://runescape.wikia.com/wiki/Calculator:Prayer/Bones</url>
 * @author Stephen Andrews
 */

public class BoneBurying {

	/**
	 * The bone burying animation.
	 */
	private final static Animation BURY_ANIMATION = Animation.create(827);
	
	/**
	 * The time it takes to bury a bone.
	 */
	private final static int BURY_TIME = 1200;
	
	/**
	 * Represents all the buryable bones.
	 * @author Stephen Andrews
	 */
	enum Bone {
			
			/**
			 * Regular bones.
			 */
			REGULAR(526, 4.5),
			
			/**
			 * Regular bones.
			 */
			WOLF(2859, 4.5),
			
			/**
			 * Regular bones.
			 */
			BURNT(528, 4.5),
			
			/**
			 * Regular bones.
			 */
			MONKEY(3179, 5),
			
			/**
			 * Regular bones.
			 */
			BAT(530, 5.3),
			
			/**
			 * Big bones.
			 */
			BIG(532, 15),
			
			/**
			 * Jogre bones.
			 */
			JOGRE(3125, 15),
			
			/**
			 * Zogre bones.
			 */
			Zogre(4812, 22.5),
			
			/**
			 * Shaikahan bones.
			 */
			SHAIKAHAN(3123, 25),
			
			/**
			 * Baby dragon bones.
			 */
			BABY_DRAGON(534, 30),
			
			/**
			 * Wyvern bones.
			 */
			WYVERN(6812, 50),
			
			/**
			 * Dragon bones.
			 */
			DRAGON(536, 72),
			
			/**
			 * Fayrg bones.
			 */
			FAYRG(4830, 84),
			
			/**
			 * Raurg bones.
			 */
			RAURG(4832, 96),
			
			/**
			 * Dagannoth bones.
			 */
			DAGANNOTH(6729, 125),
			
			/**
			 * Airut bones.
			 */
			AIRUT(526, 132.5),
			
			/**
			 * Ourg bones.
			 */
			OURG(4834, 140),
			
			/**
			 * Frost dragon bones (#614).
			 */
			FROST_DRAGON(18830, 180);
			
			
			/**
			 * The id.
			 */
			short id;
			
			/**
			 * The experience.
			 */
			short experience;
			
			/**
			 * Constructs a bone object.
			 * @param id The id of the bone.
			 * @param experience The experience given.
			 */
			Bone(int id, double experience) {
				this.id = (short) id;
				this.experience = (short) experience;
			}
			
			/**
			 * Map of bone info.
			 */
			private static Map<Short, Bone> bones = new HashMap<Short, Bone>();
			
			static {
				for(Bone b : Bone.values())
					bones.put(b.id, b);
			}
			
			/**
			 * Gets bone data.
			 * @param id The boneId.
			 * @return The boneData.
			 */
			public static Bone forId(int id) {
				return bones.get((short)id);
			}
			
			/**
			 * Gets the animation id.
			 * @return The animation id.
			 */
			public double getExperience() {
				return experience;
			}	
		}
	
	/**
	 * Buries the bone from the clicked slot.
	 * @param player The player burying the bones.
	 * @param slot The inventory slot the bones are in.
	 */
	public static void buryBone(Player player, int slot) {
		Item clicked = player.getInventory().get(slot);
		Bone bone = Bone.forId(clicked.getId());
		
		/* If the bone is null, immediately return */
		if (bone == null) {
			player.getActionSender().sendMessage("Critical error in attempting to bury bones.");
			return;
		}
		
		/* Start the animation */
		player.playAnimation(BURY_ANIMATION);
		
		/* Execute the burying action */
		player.getActionQueue().addAction(new Action(player, BURY_TIME, false) {

			@Override
			public QueuePolicy getQueuePolicy() {
				return QueuePolicy.NEVER;
			}

			@Override
			public WalkablePolicy getWalkablePolicy() {
				return WalkablePolicy.NON_WALKABLE;
			}

			@Override
			public void execute() {
				player.getInventory().set(slot, null);
				player.getSkills().addExperience(Skills.PRAYER, bone.getExperience());
				player.getActionSender().sendMessage("You bury the " + clicked.getDefinition().getName() + ".");
				stop();				
			}
			
		});
	}
}
