package org.hyperion.rs2.content.magic;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the teleporting actions.
 * @author Stephen Andrews
 */
public class Teleport {

	/**
	 * A representation of the available teleports.
	 * @author Stephen Andrews
	 */
	enum TP {
		
		/**
		 * The home teleport.
		 */
		EDGEVILLE(1, Entity.DEFAULT_LOCATION, 1, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Varrock teleport.
		 */
		VARROCK(16, Location.create(3210, 3424, 0), 25, new int[][] { {554, 1}, {556, 3}, {563, 1} }, false),
		
		/**
		 * Lumbridge teleport.
		 */
		LUMBRIDGE(19, Location.create(3222, 3218, 0), 31, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Falador teleport.
		 */
		FALADOR(22, Location.create(2964, 3378, 0), 37, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * House teleport.
		 */
		HOUSE(24, Location.create(1883, 5115, 0), 40, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Camelot teleport.
		 */
		CAMELOT(27, Location.create(2757, 3477, 0), 45, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Ardougne teleport.
		 */
		ARDOUGNE(33, Location.create(2529, 3307, 0), 51, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Watchtower teleport.
		 */
		WATCHTOWER(38, Location.create(2529, 3307, 0), 58, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Trollheim teleport.
		 */
		TROLLHEIM(45, Location.create(2529, 3307, 0), 61, new int[][] { /*EMPTY*/ }, false),
		
		/**
		 * Ape Atoll teleport.
		 */
		APE_ATOLL(48, Location.create(2529, 3307, 0), 64, new int[][] { /*EMPTY*/ }, false);
		
		/**
		 * The action button id.
		 */
		private int id;
		
		/**
		 * The location of the teleport.
		 */
		private Location location;
		
		/**
		 * The required magic level to cast.
		 */
		private int requiredLevel;
		
		/**
		 * The required runes to cast.
		 */
		private int[][] requiredRunes;
		
		/**
		 * Whether or not the teleport is available to premium members only.
		 */
		private boolean premium;
		
		/**
		 * Constructs a teleport.
		 * @param id The action button.
		 * @param location The location.
		 * @param requiredLevel The required level.
		 * @param requiredRunes The required runes.
		 * @param premium Whether or not the teleport is available to premium members only.
		 */
		TP(int id, Location location, int requiredLevel, int[][] requiredRunes, boolean premium) {
			this.id = id;
			this.location = location;
			this.requiredLevel = requiredLevel;
			this.requiredRunes = requiredRunes;
			this.premium = premium;
		}
		
		/**
		 * A map of all the teleports.
		 */
		private static Map<Integer, TP> teleports = new HashMap<Integer, TP>();
		
		/**
		 * Populate our map of teleports.
		 */
		static {
			for (TP teleport : TP.values()) {
				teleports.put(teleport.getId(), teleport);
			}
		}
		
		/**
		 * Gets a teleport based on it's action button id.
		 * @param id The id of the action button.
		 * @return The teleport for the specified id.
		 */
		public static TP forId(int id) {
			return teleports.get(id);
		}
		
		/**
		 * Gets the action button id of the teleport.
		 * @return The action button.
		 */
		public int getId() {
			return id;
		}
		
		/**
		 * Gets the location of the teleport.
		 * @return The location.
		 */
		public Location getLocation() {
			return location;
		}
		
		/**
		 * Gets the required level to cast the teleport.
		 * @return The required level.
		 */
		public int getRequiredLevel() {
			return requiredLevel;
		}
		
		/**
		 * Gets the required runes to cast the teleport.
		 * @return The required runes.
		 */
		public int[][] getRequiredRunes() {
			return requiredRunes;
		}
		
		/**
		 * Determines if the teleport is premium only.
		 * @return true If the teleport is available to only premium members.
		 */
		public boolean isPremium() {
			return premium;
		}
	}
	
	/**
	 * The teleport graphic.
	 */
	private static final int GRAPHIC = 308;
	
	/**
	 * The teleport animation.
	 */
	private static final int ANIM = 714;
	
	/**
	 * Creates a teleporting action.
	 * @param player The player to be teleported.
	 * @param actionButton The action button of the teleport.
	 */
	public static void create(final Player player, int actionButton) {
		final TP teleport = TP.forId(actionButton);
		
		if (!hasRunes(player, teleport)) {
			player.getActionSender().sendMessage("You do not have the required runes to cast this spell.");
			return;	
		}
		
		//Initiate the teleport
		consumeRunes(player, teleport);
		player.playAnimation(Animation.create(ANIM));
		player.setTeleporting(true);
		
		//Delay the graphic
		World.getWorld().submit(new Event(1200, false) {

			@Override
			public void execute() {
				player.playGraphics(Graphic.create(GRAPHIC, 100 << 16));
				stop();
			}
			
		});
		
		//Begin the actual teleporting action
		World.getWorld().submit(new Event(1800, false) {

			@Override
			public void execute() {
				player.setTeleportTarget(teleport.getLocation());
				player.setTeleporting(false);
				player.playAnimation(Animation.create(-1));
				stop();
			}
			
		});
	}
	
	/**
	 * Checks if the player has the required runes to cast the teleport.
	 * @return true If they do.
	 */
	private static boolean hasRunes(Player player, TP teleport) {
		//Teleports that require no runes
		if (teleport.getRequiredRunes().length == 0) return true;

		for (int[] i : teleport.getRequiredRunes()) {
			Item runes = new Item(i[0], i[1]);
			if (player.getInventory().hasItem(runes)) {
				return true;
			}
		}
		
		return !Constants.REQUIRE_RUNES ? true : false;
	}
	
	/**
	 * Removes the required runes from the player's inventory.
	 * @param player The player to remove the runes from.
	 * @param teleport The teleport being cast.
	 */
	private static void consumeRunes(Player player, TP teleport) {
		if (!Constants.REQUIRE_RUNES) return;
		
		for (int[] i : teleport.getRequiredRunes()) {
			Item runes = new Item(i[0], i[1]);
			player.getInventory().remove(runes);
		}
	}
	
}
