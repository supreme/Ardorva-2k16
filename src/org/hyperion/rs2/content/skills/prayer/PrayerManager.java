package org.hyperion.rs2.content.skills.prayer;

import java.util.ArrayList;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.player.Player;

/**
 * Manages all aspects of the prayer skill.
 * Activation, draining, etc.
 * @author Stephen Andrews
 */
public class PrayerManager {

	/**
	 * Used for a prayer spell that doesn't modify a skill.
	 */
	public static final int SKILLS_NONE = -1;
	
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
	 * The index of the headicon active, if any.
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
			player.getActionSender().sendConfig(configId, Constants.CONFIG_OFF);
			activeSpells.remove(spell);
		} else {
			/* Remove any conflicting spells */
			long start = System.currentTimeMillis();
			for (int i = 0; i < activeSpells.size(); i++)  {
				PrayerSpell activated = activeSpells.get(i);
				for (int modifier : spell.getModifiedSkills()) {
					for (int activeModifier : activated.getModifiedSkills()) {
						if (modifier == activeModifier) {
							toggleSpell(activated);
						}
					}
				}
			}
			long elapsed = System.currentTimeMillis() - start;
			player.getActionSender().sendMessage("Took " + elapsed + "ms to toggle prayer spell.");
			
			player.getActionSender().sendConfig(configId, Constants.CONFIG_ON);
			activeSpells.add(spell);
		}
	}
}
