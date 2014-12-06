package org.hyperion.rs2.packet;

import java.util.logging.Logger;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.Skillcape;
import org.hyperion.rs2.content.magic.Teleport;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Handles clicking on most buttons in the interface.
 * @author Graham Edgecombe
 *
 */
public class ActionButtonPacketHandler implements PacketHandler {

	/**
	 * The logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ActionButtonPacketHandler.class.getName());
	
	@Override
	public void handle(Player player, Packet packet) {
		int interfaceId = packet.getShort() & 0xFFFF;
		int buttonId = packet.getShort() & 0xFFFF;
		int buttonId2 = 0;
		if(packet.getLength() >= 6) {
			buttonId2 = packet.getShort() & 0xFFFF;
		}
		if(buttonId2 == 65535) {
			buttonId2 = 0;
		}
		switch(interfaceId) {
		case 271: //Prayer tab
			player.getPrayerManager().activateSpell(buttonId);
			break;
		case 12: //Bank
			switch(buttonId) {
			case 10: //Noted items
				player.getSettings().setWithdrawAsNotes(true);
				break;
			case 11: //Regular items
				player.getSettings().setWithdrawAsNotes(false);
				break;
			}
			break;
		case 182: //Logout tab
			switch(buttonId) {
			case 6: //Logout button
				player.getActionSender().sendLogout();
				break;
			}
			break;
		case 261: //Settings tab
			switch(buttonId) {
			case 0: //Run button
				if (!player.getWalkingQueue().isRunning()) {
					player.getWalkingQueue().setRunningToggled(true);
				} else {
					player.getWalkingQueue().setRunningToggled(false);
				}
				break;
			}
			break;
		case 192: //Magic tab
			switch(buttonId) {
			case 0: //Home teleport
			case 15: //Varrock
			case 18: //Lumby
			case 21: //Fally
			case 23: //House
			case 26: //Cammy
			case 32: //Ardougne
			case 37: //Watchtower
			case 44: //Trollheim
			case 47: //Ape atoll
				Teleport.create(player, buttonId);
				break;
			}
			break;
		case 464:
			if (buttonId == 37) {
				//Handle skillcape
				Skillcape.doAnimation(player);
			} else {
				player.playAnimation(Animation.create(Animation.ANIMS[buttonId - 1]));
			}
			break;
		case 387: //Equipment tab
			switch(buttonId) {
				case 51: //Show equipment
					Equipment.displayEquipmentScreen(player);
					break;
			}
			break;
		default:
			if (Constants.DEV_MODE && interfaceId != 548) {
				player.getActionSender().sendMessage("Unhandled action button | Interface: " + interfaceId + " Button: " + buttonId);
			}			
			break;
		}
	}

}
