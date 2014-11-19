package org.hyperion.rs2.model;

/**
 * Represents a single chat message.
 * @author Graham Edgecombe
 *
 */
public class ChatMessage {
	
	private int colour;
	private int numChars;
	private String chatText;
	private int effect;
	private byte[] packed;
	
	public ChatMessage(int colour, int numChars, String chatText, int effect, byte[] packed) {
		this.colour = colour;
		this.numChars = numChars;
		this.chatText = chatText;
		this.effect = effect;
		this.packed = packed;
	}
	
	public int getColour() {
		return colour;
	}
	
	public int getNumChars() {
		return numChars;
	}
	
	public String getChatText() {
		return chatText;
	}

	public int getEffect() {
		return effect;
	}
	
	public byte[] getPacked() {
		return packed;
	}

	public byte getPacked(int i) {
		return packed[i];
	}

}
