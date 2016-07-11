package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.Skillcape;
import org.hyperion.rs2.content.magic.Teleport;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.util.Logger;
import org.hyperion.util.Logger.Level;


/**
 * Handles clicking on most buttons in the interface.
 * @author Graham Edgecombe
 *
 */
public class ActionButtonPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		Logger.log(Level.DEBUG, "Incoming packet " + packet.getOpcode());

		switch (packet.getOpcode()) {
			case 113: //Game
			case 153:
				handleActionButton(player, packet);
				break;
			case 240: //Chatbox
				int interfaceShit = packet.getInt();
				int interfaceId = interfaceShit >> 16;
				int id = interfaceShit & 0xffff;

				/** LevelUpMessage interfaces */
				if (interfaceId >= 157 && interfaceId <= 177) {
					player.getActionSender().removeChatboxInterface();
					return;
				}
				break;
		}
	}

	//TODO: Figure out what this corresponds to ~ Stephen
	private void handleActionButton(Player player, Packet packet) {
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
					case 55: //Run button
						if (!player.getWalkingQueue().isRunning()) {
							player.getWalkingQueue().setRunningToggled(true);
						} else {
							player.getWalkingQueue().setRunningToggled(false);
						}
						break;
					default:
						System.out.println("Settings: " + buttonId);
						break;
				}
				break;
			case 218: //Magic tab
				switch(buttonId) {
					case 1: //Home teleport
					case 16: //Varrock
					case 19: //Lumby
					case 22: //Fally
					case 24: //House
					case 27: //Cammy
					case 33: //Ardougne
					case 38: //Watchtower
					case 45: //Trollheim
					case 48: //Ape atoll
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
				if (Constants.DEV_MODE ) {
					player.getActionSender().sendMessage("Unhandled action button | Interface: " + interfaceId + " Button: " + buttonId);
				}
				break;
		}
	}

}
