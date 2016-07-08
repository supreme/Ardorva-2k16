package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.net.Packet;

/**
 * A packet which handles walking requests.
 * @author Graham Edgecombe
 *
 */
public class WalkingPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		int size = packet.getLength();
		if(packet.getOpcode() == 143) {
		    size -= 14;
		}
			
		player.getWalkingQueue().reset();
		player.getActionQueue().clearNonWalkableActions();
		player.resetInteractingEntity();

		final int steps = (size - 5) / 2;
		final int[][] path = new int[steps][2];

		final boolean runSteps = packet.getByteS() == 1;
		final int firstY = packet.getLEShort();
		final int firstX = packet.getShortA();
		for (int i = 0; i < steps; i++) {
		    path[i][0] = packet.getByteS();
		    path[i][1] = packet.getByteS();
		}
		
		player.getWalkingQueue().setRunningQueue(runSteps);
		player.getWalkingQueue().addStep(firstX, firstY );
		
		for (int i = 0; i < steps; i++) {
		    path[i][0] += firstX;
		    path[i][1] += firstY;
		    player.getWalkingQueue().addStep(path[i][0], path[i][1]);
		}
		player.getWalkingQueue().finish();
	}

}
