package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.TextUtils;

/**
 * Handles public chat messages.
 * @author Graham Edgecombe
 *
 */
public class ChatPacketHandler implements PacketHandler {

	private static final int CHAT_QUEUE_SIZE = 4;
	
	@Override
	public void handle(Player player, Packet packet) {
		int effects = packet.getByte() & 0xFF;
		int colour = packet.getByte() & 0xFF;
		int size = packet.get() & 0xFF;
		if(player.getChatMessageQueue().size() >= CHAT_QUEUE_SIZE) {
			return;
		}
		String unpacked = TextUtils.decryptPlayerChat(packet, size);
		unpacked = TextUtils.filterText(unpacked);
		unpacked = TextUtils.optimizeText(unpacked);
		player.getChatMessageQueue().add(new ChatMessage(effects, size, unpacked, colour, null));
	}

}
