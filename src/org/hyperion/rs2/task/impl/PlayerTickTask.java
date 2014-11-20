package org.hyperion.rs2.task.impl;

import java.util.Queue;

import org.hyperion.rs2.engine.task.listener.OnFireActionListener;
import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.player.Player;

/**
 * A task which is executed before an <code>UpdateTask</code>. It is similar to
 * the call to <code>process()</code> but you should use <code>Event</code>s
 * instead of putting timers in this class.
 * @author Graham Edgecombe
 *
 */
public class PlayerTickTask extends OnFireActionListener {

	private Player player;
	
	public PlayerTickTask(Player player) {
		this.player = player;
	}

	@Override
	public boolean cancelWhen() {
		return !player.getSession().isConnected();
	}

	@Override
	public void run() {
		Queue<ChatMessage> messages = player.getChatMessageQueue();
		if(messages.size() > 0) {
			player.getUpdateFlags().flag(UpdateFlag.CHAT);
			ChatMessage message = player.getChatMessageQueue().poll();
			player.setCurrentChatMessage(message);
		} else {
			player.setCurrentChatMessage(null);
		}
		player.getWalkingQueue().processNextMovement();		
	}

}
