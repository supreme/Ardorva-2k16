package org.hyperion.rs2.content.skills.prayer;

import java.util.ArrayList;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.player.Player;

/**
 * Manages all aspects of the prayer skill.
 * Activation, draining, etc.
 * @author Stephen Andrews
 */
public class PrayerManager {

	/**
	 * Used for a prayer spell that doesn't modify a skill,
	 * but has a unique property.
	 */
	public static final int UNIQUE_SPELL = 1337;
	
	/**
	 * Used for protection spells.
	 */
	public static final int PROTECTION_SPELL = -1;
	
	/**
	 * The prayer configs to send to the client.
	 */
	private final int[] PRAYER_CONFIG = {
		83, 84, 85, 862, 863, 86, 87, 88, 89, 90, 91, 864,
		865, 92, 93, 94, 95, 96, 97, 866, 867, 98, 99, 100,
		1052, 1053
	};
	
	/**
	 * The player the <code>PrayerManager</code> belongs to.
	 */
	private Player player;
	
	/**
	 * The index of the head icon active, if any.
	 */
	private int headIcon;
	
	/**
	 * The active prayer spells.
	 */
	private ArrayList<PrayerSpell> activeSpells;
	
	/**
	 * Constructs a <code>PrayerManager</code> for the specified player.
	 * @param player The player the <code>PrayerManager</code> belongs to.
	 */
	public PrayerManager(Player player) {
		this.player = player;
		this.headIcon = -1;
		this.activeSpells = new ArrayList<PrayerSpell>();
	}
	
	/**
	 * Activates a prayer spell.
	 * @param actionButton The action button of the spell.
	 */
	public void activateSpell(int actionButton) {
		PrayerSpell spell = PrayerSpell.forId(actionButton);
		
		/* If for some reason the prayer spell cannot be found */
		if (spell == PrayerSpell.NON_EXISITING_SPELL) {
			player.getActionSender().sendMessage("Critical error in activating prayer spell");
			return;
		}
		
		toggleSpell(spell);
	}
	
	/**
	 * Toggles a prayer spell on/off depending on it's state.
	 * @param spell The prayer spell being toggled.
	 */
	private void toggleSpell(PrayerSpell spell) {
		int configId = PRAYER_CONFIG[spell.ordinal()];
		
		if (activeSpells.contains(spell)) {
			handleHeadIcons(spell, false);
			player.getActionSender().sendConfig(configId, Constants.CONFIG_OFF);
			activeSpells.remove(spell);
		} else {
			/* Remove any conflicting spells */
			int iterations = 0;
			long start = System.currentTimeMillis();
			for (int i = 0; i < activeSpells.size(); i++)  {
				PrayerSpell activated = activeSpells.get(i);
				iterations++;
				for (int modifier : spell.getModifiedSkills()) {
					iterations++;
					for (int activeModifier : activated.getModifiedSkills()) {
						iterations++;
						if (modifier == activeModifier) {
							toggleSpell(activated);
						}
					}
				}
			}
			long elapsed = System.currentTimeMillis() - start;
			player.getActionSender().sendMessage("Took " + elapsed + "ms to toggle prayer spell with " + iterations + " iterations.");
			
			handleHeadIcons(spell, true);
			player.getActionSender().sendConfig(configId, Constants.CONFIG_ON);
			activeSpells.add(spell);
		}
	}
	
	/**
	 * Updates the head icon if the prayer spell requires it.
	 * @param spell The prayer spell being toggled.
	 * @param activation Whether or not the toggle action is activation of the spell.
	 */
	private void handleHeadIcons(PrayerSpell spell, boolean activation) {
		if (activation) {
			switch(spell) {
			case PROTECT_FROM_MAGIC:
				setHeadIcon(2);
				break;
			case PROTECT_FROM_MISSILES:
				setHeadIcon(1);
				break;
			case PROTECT_FROM_MELEE:
				setHeadIcon(0);
				break;
			case RETRIBUTION:
				setHeadIcon(3);
				break;
			case REDEMPTION:
				setHeadIcon(5);
				break;
			case SMITE:
				setHeadIcon(4);
				break;
			default:
				break;
			}
		} else {
			switch(spell) {
			case PROTECT_FROM_MAGIC:
			case PROTECT_FROM_MISSILES:
			case PROTECT_FROM_MELEE:
			case RETRIBUTION:
			case REDEMPTION:
			case SMITE:
				setHeadIcon(-1);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Gets the active head icon.
	 * @return The head icon, or -1 if none.
	 */
	public int getHeadIcon() {
		return headIcon;
	}
	
	/**
	 * Sets the player's head icon and flags the appearance update 
	 * @param headIcon The head icon to set.
	 */
	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}
}
