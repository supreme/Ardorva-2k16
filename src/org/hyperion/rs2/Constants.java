package org.hyperion.rs2;

/**
 * Holds global server constants.
 * @author Graham Edgecombe
 *
 */
public class Constants {
	
	/**
	 * The name of the server.
	 */
	public static final String SERVER_NAME = "Derithium";
	
	/**
	 * Whether or not the server is in dev mode.
	 */
	public static boolean DEV_MODE = true;
	
	/**
	 * Whether or not runes are required to cast magic spells.
	 */
	public static boolean REQUIRE_RUNES = false;
	
	/**
	 * The directory for the engine scripts.
	 */
	public static final String SCRIPTS_DIRECTORY = "./data/scripts/";
	
	/**
	 * Difference in X coordinates for directions array.
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	
	/**
	 * Difference in Y coordinates for directions array.
	 */
	public static final byte[] DIRECTION_DELTA_Y = new byte[] {1, 1, 1, 0, 0, -1, -1, -1};
	
	/**
	 * Incoming packet sizes array.
	 */
	public static int[] PACKET_SIZES = new int[] { -3, -3, -3, -3, -3, -3, -3, 1, -3, -3, // 0
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 10
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 20
			-3, -3, -3, -3, -3, -3, -1, 6, -3, -3, // 30
			-3, -3, -3, -1, 6, -3, -3, -3, -3, 13, // 40
			-1, 2, -3, -3, -3, -3, -3, -3, -3, -3, // 50
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 60
			-3, -3, -3, -3, -3, -3, -3, -3, 4, -3, // 70
			-3, -3, -3, -3, 2, -3, 8, -3, -3, -3, // 80
			-3, -3, -3, -3, -3, -3, -3, -3, -3, 4, // 90
			-3, 8, -3, -3, -3, -3, -3, -3, -3, -3, // 100
			-3, -3, -3, 6, -3, -1, -3, -3, -3, 6, // 110
			6, 9, 8, 8, -3, -3, -3, -3, -3, 2, // 120
			9, -3, -3, -3, 6, -3, -3, 6, -3, -3, // 130
			6, -3, -3, -1, -3, -3, -3, -3, -3, -3, // 140
			-3, -3, -3, 4, -3, -3, -3, -3, -3, -3, // 150
			-3, -3, -3, 14, -3, -1, 16, -3, -3, -3, // 160
			-3, -3, -3, -3, 5, -3, 2, 8, -3, -3, // 170
			-3, -3, -3, 2, -3, 1, -3, 10, -3, -3, // 180
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 190
			-3, -3, 0, -3, -3, 2, -3, -3, -3, -3, // 200
			-3, -3, 8, -3, -3, 8, -3, -3, -3, -3, // 210
			-3, -3, -3, -3, 8, -3, -3, -3, -3, -3, // 220
			0, -3, -3, -3, -3, -3, -3, -3, -1, -3, // 230
			4, -3, -3, -3, -3, -3, -3, 8, -3, -3, // 240
			-3, -3, -3, -3, -3, -3 // 250
	};

	/**
	 * The player cap.
	 */
	public static final int MAX_PLAYERS = 2000;
	
	/**
	 * The NPC cap.
	 */
	public static final int MAX_NPCS = 32000;
	
	/**
	 * An array of valid characters in a long username.
	 */
	public static final char VALID_CHARS[] = { '_', 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
		'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&',
		'*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"',
		'[', ']', '|', '?', '/', '`' };
	
	/**
	 * Packed text translate table.
	 */
	public static final char XLATE_TABLE[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n',
		's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b',
		'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
		'&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
		'[', ']' };
	
	/**
	 * The maximum amount of items in a stack.
	 */
	public static final int MAX_ITEMS = Integer.MAX_VALUE;

}
