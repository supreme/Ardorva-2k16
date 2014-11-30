package org.hyperion.rs2.packet;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.content.skills.woodcutting.Tree;
import org.hyperion.rs2.content.skills.woodcutting.WoodcuttingAction;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Object option packet handler.
 * @author Graham Edgecombe
 * @author Brendan Dodd
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
		int id = packet.getLEShort() & 0xFFFF;
		int x = packet.getShort() & 0xFFFF;
		int y = packet.getLEShort() & 0xFFFF;
		Location loc = Location.create(x, y, player.getLocation().getZ());
		System.out.println("First click object ID: "+id+", x: "+x+", y: "+y);
		
		/* Create the interaction action */
		Action objectInteract1 = new Action(player, 0, true) {

			@Override
			public QueuePolicy getQueuePolicy() {
				return QueuePolicy.NEVER;
			}

			@Override
			public WalkablePolicy getWalkablePolicy() {
				return WalkablePolicy.WALKABLE;
			}

			@Override
			public void execute() {
				switch(id) {
				case 26972:
					Bank.open(player);
					break;
				case 1276:
						Tree tree = Tree.forId(id);
						WoodcuttingAction wc = new WoodcuttingAction(player, loc, tree);
						wc.init();
						wc.execute();
					break;
				default:
					player.getActionSender().sendMessage("Unhandled object first click | Id: " + id);
					break;
			}	
			
			this.stop();
		}};
		
		player.addInteractAction(loc, objectInteract1);
	}
	
    /**
     * Handles the option 2 packet.
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption2(Player player, Packet packet) {        
		int id = packet.getLEShort() & 0xFFFF;
		int x = packet.getShort() & 0xFFFF;
		int y = packet.getLEShort() & 0xFFFF;
        Location loc = Location.create(x, y, player.getLocation().getZ());
        /*Node node = Node.forId(id);
        if(node != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new ProspectingAction(player, loc, node));
            return;
        }*/
    }


}
