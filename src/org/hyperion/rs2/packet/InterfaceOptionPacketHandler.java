package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Handles player clicking on interfaces.
 * @author Stephen Andrews
 */
public class InterfaceOptionPacketHandler implements PacketHandler {

	private static final int ENTER_AMOUNT = 78;
	private static final int ENTER_TEXT = 244;
	private static final int CLICK_1 = 177;
	private static final int CLICK_2 = 88;
	private static final int CLICK_3 = 159;
	private static final int CLICK_4 = 86;
	private static final int CLICK_5 = 220;
	private static final int CLICK_6 = 168;
	private static final int CLICK_7 = 166;
	private static final int CLICK_8 = 64; 
	private static final int CLICK_9 = 53;
	
	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
		case CLICK_1:
			handleClick1(player, packet);
			break;
		}
	} 
	
	/**
	 * Handles click 1 of the interface option.
	 * @param player The player invoking the click.
	 * @param packet The packet we're dealing with.
	 */
	private void handleClick1(Player player, Packet packet) {
		int itemId = packet.getShort();
		int interfaceSet = packet.getInt1();
		int slot = packet.getShort();
		int interfaceId = interfaceSet >> 16;
		int child = interfaceSet & 0xffff;
		
		switch(interfaceId) {
			case 12://Banking - withdraw 1
				if (slot < 0 || slot > 400) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.withdraw(player, slot, itemId, 1);
				}
				break;
			case 15://Banking - inventory deposit 1
				if (slot < 0 || slot > 27) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.deposit(player, slot, itemId, 1);
				}
				break;
			case 387://Unequip inv
			case 465://Equip interface - unequip
				if (slot < 0 || slot > 13) {
					break;
				}
				//Equipment.unequipItem(player, itemId, slot, interfaceId == 465);
				break;
			default:
				player.getActionSender().sendMessage("Unhandled interface ID | Interface Option 1, interface ID: " + interfaceId);
				break;
		}
	}
	
	
	
}
