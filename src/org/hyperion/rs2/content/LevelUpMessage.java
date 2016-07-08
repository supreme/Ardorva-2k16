package org.hyperion.rs2.content;

import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.player.Player;

/**
 * Sends the appropriate level up message to the player after successfully
 * gaining a level in a skill.
 *
 * NOTE: Data collected from Paragon server. Credits to their development team.
 * @author Stephen Andrews
 */
public class LevelUpMessage {

    /**
     * The level up graphic.
     */
    private static final Graphic LEVEL_UP_GRAPHIC = Graphic.create(199, 2);

    /**
     * Represents the different interfaces displayed on level up.
     * Order matters since the ordinal is used to determine the skill name.
     */
    enum LevelUpInterface {

        ATTACK(158),
        DEFENCE(161),
        STRENGTH(175),
        HITPOINTS(167),
        RANGE(171),
        PRAYER(170),
        MAGIC(168),
        COOKING(159),
        WOODCUTTING(177),
        FLETCHING(165),
        FISHING(164),
        FIREMAKING(163),
        CRAFTING(160),
        SMITHING(174),
        MINING(169),
        HERBLORE(166),
        AGILITY(157),
        THIEVING(176),
        SLAYER(173),
        FARMING(162),
        RUNECRAFTING(172);

        /**
         * Represents the interface id of the level up message.
         */
        private int interfaceId;

        /**
         * Constructs a level up message.
         * @param interfaceId The interface id of the level up message.
         */
        LevelUpInterface(int interfaceId) {
            this.interfaceId = interfaceId;
        }

        /**
         * Gets the interface id of the level up message.
         * @return The interface id of the level up message.
         */
        public int getInterfaceId() {
            return interfaceId;
        }
    }

    /**
     * Sends a level up message to a player for a specified skill.
     * @param player The player to send the message to.
     * @param skill The skill the player has leveled up in.
     */
    public static void display(Player player, int skill) {
        LevelUpInterface inter = LevelUpInterface.values()[skill];

        /** Unable to match skill with an interface */
        if (inter == null) {
            return;
        }

        String firstLine = "<col=00008B>" + "Congratulations, you have just advanced a " + Skills.SKILL_NAME[skill] + " level!";
        String secondLine = "You have now reached level " + player.getSkills().getLevelForExperience(skill) + ".";
        player.playGraphics(LEVEL_UP_GRAPHIC);
        player.getActionSender().sendString(inter.getInterfaceId(), 0, firstLine);
        player.getActionSender().sendString(inter.getInterfaceId(), 1, secondLine);
        player.getActionSender().sendChatBoxInterface(inter.getInterfaceId());
    }

}
