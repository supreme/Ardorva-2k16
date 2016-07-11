package org.hyperion.rs2.model.player;

import org.hyperion.rs2.content.magic.MagicBook;

import java.io.*;

/**
 * Player configurations are saved on logout and loaded on login. This class
 * servers soley to maintain the player's previous play style before logout.
 * @author Stephen Andrews
 */
@SuppressWarnings("serial")
public class PlayerConfiguration implements Serializable {

	/**
	 * The directory of player configuration files.
	 */
	private static final String CONFIG_DIRECTORY = "./data/savedGames/config/";

	/**
	 * The magic book the player is using.
	 */
	private MagicBook magicBook;

	/**
	 * The name of the equipped weapon.
	 */
	private String weaponName;
	
	/**
	 * The interface displaying on the weapon tab.
	 */
	private int weaponTabInterface;
	
	/**
	 * Whether or not the player is auto retaliating.
	 */
	private boolean autoRetaliating;
	
	/**
	 * Whether or not the player has running selected.
	 */
	private boolean running;
	
	/**
	 * Constructs the default configuration for players.
	 */
	public PlayerConfiguration() {
		magicBook = MagicBook.MODERN;
		weaponName = "Unarmed";
		weaponTabInterface = 593;
		autoRetaliating = true;
		running = false;
	}
	
	/**
	 * Serializes a player's configuration file.
	 * @param player The player we are saving the configuration for.
	 * @param config The configuration to serialize.
	 */
	public static void serialize(Player player, PlayerConfiguration config) {
		if (config == null) { return; }
		
		try {
			File file = new File(CONFIG_DIRECTORY + player.getName() + ".dat");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));
			out.writeObject(config);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Deserializes a player's configuration file.
	 * @param player The player we are deserializing the configuration for.
	 */
	public static PlayerConfiguration deserialize(Player player) {
		try {
			File file = new File(CONFIG_DIRECTORY + player.getName() + ".dat");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			PlayerConfiguration config = (PlayerConfiguration) in.readObject();
			in.close();
			return config;
		} catch (IOException ex) {
			System.out.println("Non-existing player configuration file for " + player.getName() + "... Creating one.");
			return new PlayerConfiguration();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		/* Create a new configuration if a player doesn't already have one */
		return new PlayerConfiguration();
	}

	/**
	 * Gets the player's current magic book.
	 * @return The current magic book.
     */
	public MagicBook getMagicBook() {
		return magicBook;
	}

	/**
	 * Sets the player's magic book.
	 * @param book The magic book to set.
     */
	public void setMagicBook(MagicBook book) {
		this.magicBook = book;
	}

	/**
	 * Gets the name of the equipped weapon.
	 * @return The name of the equipped weapon.
	 */
	public String getWeaponName() {
		return weaponName;
	}
	
	/**
	 * Sets the name of the equipped weapon.
	 * @param weaponName The new weapon name.
	 */
	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}
	
	/**
	 * Gets the interface displaying on the weapon tab.
	 * @return The interface displaying on the weapon tab.
	 */
	public int getWeaponTabInterface() {
		return weaponTabInterface;
	}
	
	/**
	 * Sets the interface displaying on the weapon tab.
	 * @param weaponTabInterface The new id.
	 */
	public void setWeaponTabInterface(int weaponTabInterface) {
		this.weaponTabInterface = weaponTabInterface;
	}
	
	/**
	 * Whether or not the player is auto retaliating.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isAutoRetaliating() {
		return autoRetaliating;
	}
	
	/**
	 * Sets the auto retaliating flag.
	 * @param autoRetaliating The new flag.
	 */
	public void setAutoRetaliating(boolean autoRetaliating) {
		this.autoRetaliating = autoRetaliating;
	}
	
	/**
	 * Whether or not the player is running.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Sets the running flag.
	 * @param running The new flag.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}
