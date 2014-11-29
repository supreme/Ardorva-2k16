package org.hyperion.rs2.packet;

import java.util.logging.Logger;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.combat.impl.MeleeAction;
import org.hyperion.rs2.content.shops.ShopLoader;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.definitions.NPCDefinition;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.shops.Shop;
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
			handleAttackOption(player, packet);
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
			System.out.println("Unhandled NPC interaction, opcode=" + packet.getOpcode());
			break;
		}
	}
	
	/**
	 * Handles the attacking action on an NPC.
	 * @param player The player performing the attack.
	 * @param packet The packet.
	 */
	private void handleAttackOption(Player player, Packet packet) {
		int npcIndex = packet.getLEShort() & 0xFFFF;
		if (npcIndex < 0 || npcIndex > Constants.MAX_NPCS) {
			return;
		}
		final NPC npc = World.getWorld().getNPC(npcIndex);
		if (npc == null || npc.isDead()) {
			return;
		}
		
		Logger.getLogger(this.getClass().getName()).info(player.getName() + " attacked npc: " + npc.getDefinition().getId() + ", at location: " + npc.getLocation().toString());
		player.getActionQueue().addAction(new MeleeAction(player, npc));
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
		int npcIndex = packet.getLEShortA();
		
		if (npcIndex < 0 || npcIndex > Constants.MAX_NPCS) {
			return;
		}
		
		final NPC npc = World.getWorld().getNPC(npcIndex);
		player.setInteractingEntity(npc);
		
		Shop shop = ShopLoader.getShopForNpc(npc.getId());
		if(shop != null) {
			ShopLoader.displayShop(player, shop);
		}
	}
	
}
