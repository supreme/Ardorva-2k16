package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Object option packet handler.
 * @author Graham Edgecombe
 *
 */
public class ObjectOptionPacketHandler implements PacketHandler {
	
	/**
	 * Option 1 opcode.
	 */
	private static final int OPTION_1 = 44, OPTION_2 = 252;

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
		case OPTION_1:
			handleOption1(player, packet);
			break;
		case OPTION_2:
			handleOption2(player, packet);
			break;
		}
	}

	/**
	 * Handles the option 1 packet.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleOption1(Player player, Packet packet) {
		int x = packet.getLEShortA() & 0xFFFF;
		int id = packet.getShort() & 0xFFFF;
		int y = packet.getShortA() & 0xFFFF;
		Location loc = Location.create(x, y, player.getLocation().getZ());
		
		switch(id) {
			case 3095:
				Bank.open(player);
				break;
			default:
				player.getActionSender().sendMessage("Unhandled object first click | Id: " + id);
				break;
		}
	}
	
    /**
     * Handles the option 2 packet.
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption2(Player player, Packet packet) {        
        int id = packet.getLEShortA() & 0xFFFF;
        int y = packet.getLEShort() & 0xFFFF;
        int x = packet.getShortA() & 0xFFFF;
        Location loc = Location.create(x, y, player.getLocation().getZ());
        /*Node node = Node.forId(id);
        if(node != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new ProspectingAction(player, loc, node));
            return;
        }*/
    }


}
