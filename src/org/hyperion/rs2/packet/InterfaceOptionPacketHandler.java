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
			handleClickOne(player, packet);
			break;
		case CLICK_2:
			handleClickTwo(player, packet);
			break;
		case CLICK_3:
			handleClickThree(player, packet);
			break;
		case CLICK_4:
			handleClickFour(player, packet);
			break;
		}
	} 
	
	/**
	 * Handles click one of the interface option.
	 * @param player The player invoking the click.
	 * @param packet The packet we're dealing with.
	 */
	private void handleClickOne(Player player, Packet packet) {
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
	
	/**
	 * Handles click two of the interface option.
	 * @param player The player invoking the click.
	 * @param packet The packet we're dealing with.
	 */
	private void handleClickTwo(Player player, Packet packet) {
		int interfaceSet = packet.getLEInt();
		int interfaceId = interfaceSet >> 16;
		int child = interfaceSet & 0xffff;
		int itemId = packet.getLEShort() & 0xffff;
		int slot = packet.getLEShortA() & 0xffff;
		
		switch(interfaceId) {
			case 12://Banking - withdraw 5
				if (slot < 0 || slot > 400) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.withdraw(player, slot, itemId, 5);
				}
				break;
			case 15://Banking - inventory deposit 5
				if (slot < 0 || slot > 27) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.deposit(player, slot, itemId, 5);
				}
				break;
			default:
				player.getActionSender().sendMessage("Unhandled interface ID | Interface Option 2, interface ID: " + interfaceId);
				break;
		}
	}
	
	/**
	 * Handles click three of the interface option.
	 * @param player The player invoking the click.
	 * @param packet The packet we're dealing with.
	 */
	private void handleClickThree(Player player, Packet packet) {
		int interfaceSet = packet.getLEInt();
		int interfaceId = interfaceSet >> 16;
		int child = interfaceSet & 0xffff;
		int slot = packet.getLEShort() & 0xffff;
		int itemId = packet.getLEShort() & 0xffff;
		
		switch(interfaceId) {
			case 12://Banking - withdraw 10
				if (slot < 0 || slot > 400) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.withdraw(player, slot, itemId, 10);
				}
				break;
			case 15://Banking - inventory deposit 10
				if (slot < 0 || slot > 27) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.deposit(player, slot, itemId, 10);
				}
				break;
			default:
				player.getActionSender().sendMessage("Unhandled interface ID | Interface Option 3, interface ID: " + interfaceId);
				break;
		}
	}
	
	/**
	 * Handles click four of the interface option.
	 * @param player The player invoking the click.
	 * @param packet The packet we're dealing with.
	 */
	private void handleClickFour(Player player, Packet packet) {
		int slot = packet.getLEShort() & 0xFFFF;
		int itemId = packet.getShort() & 0xFFFF;
		int interfaceSet = packet.getInt2();
		int interfaceId = interfaceSet >> 16;
		int child = interfaceSet & 0xffff;
		
		switch(interfaceId) {
			case 12://Banking - withdraw all
				if (slot < 0 || slot > 400) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.withdraw(player, slot, itemId, player.getBank().getAmountInSlot(slot));
				}
				break;
			case 15://Banking - inventory deposit all
				if (slot < 0 || slot > 27) {
					break;
				}
				if (player.getPlayerVariables().isBanking()) {
					Bank.deposit(player, slot, itemId, player.getInventory().getCount(itemId));
				}
				break;
			default:
				player.getActionSender().sendMessage("Unhandled interface ID | Interface Option 4, interface ID: " + interfaceId);
				break;
		}
	}
	
	
	
}
