package org.hyperion.rs2.packet;

import org.hyperion.application.ConsoleMessage;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.shops.ShopLoader;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.definitions.NPCDefinition;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Handles the interaction between a player and an NPC.
 * @author Stephen Andrews
 */
public class NPCInteractPacketHandler implements PacketHandler {

	/**
	 * The attack option.
	 */
	private static final int ATTACK = 129;
	
	/**
	 * The talk-to option.
	 */
	private static final int TALK_TO = 156;
	
	/**
	 * The trade option.
	 */
	private static final int TRADE = 19;
	
	/**
	 * The examine option.
	 */
	private static final int EXAMINE = 72;

	@Override
	public void handle(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case ATTACK:
			break;
		case TALK_TO:
			break;
		case TRADE:
			handleTradeOption(player, packet);
			break;
		case EXAMINE:
			handleExamineOption(player, packet);
			break;
		default:
			ConsoleMessage.info("Unhandled NPC interaction, opcode=" + packet.getOpcode());
			break;
		}
	}
	
	/**
	 * Sends the examine info of an NPC to the player examining it.
	 * @param player The player performing the examination.
	 * @param packet The packet.
	 */
	private void handleExamineOption(Player player, Packet packet) {
		int id = packet.getShort();
		
		String examine = NPCDefinition.forId(id).getExamine();
		player.getActionSender().sendMessage(examine);
	}
	
	/**
	 * Opens the shop of an NPC.
	 * @param player The player performing the trade.
	 * @param packet The pack.
	 */
	private void handleTradeOption(Player player, Packet packet) {
		int index = packet.getLEShortA();
		
		if (index < 0 || index > Constants.MAX_NPCS) {
			return;
		}
		
		NPC npc = (NPC) World.getWorld().getNPCs().get(index);
		ShopLoader.loadShop(player, npc.getDefinition().getName().toLowerCase());
	}
	
}