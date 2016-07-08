package org.hyperion.rs2.content.skills.mining;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents types of nodes.
 * @author Graham Edgecombe
 */
public enum Node {

	/**
	 * Empty ore.
	 */
	EMPTY(0, 0, 1, 0, new short[] { 451 }),
	
	/**
	 * Copper ore.
	 */
	COPPER(436, 50, 1, 17.5, new short[] { 2090, 2091 }),
	
	/**
	 * Tin ore.
	 */
	TIN(438, 5, 1, 17.5, new short[] { 2094, 2095 }),
	
	/**
	 * Blurite ore.
	 */
	BLURITE(668, 5, 10, 17.5, new short[] { 2110 }),
	
	/**
	 * Iron ore.
	 */
	IRON(440, 5, 15, 35, new short[] { 2092, 2093 }),
	
	/**
	 * Silver ore.
	 */
	SILVER(442, 5, 20, 40, new short[] { 2100, 2101 }),
	
	/**
	 * Gold ore.
	 */
	GOLD(444, 5, 40, 65, new short[] { 2098, 2099 }),
	
	/**
	 * Coal ore.
	 */
	COAL(453, 5, 30, 50, new short[] { 2096, 2097 }),
	
	/**
	 * Mithril ore.
	 */
	MITHRIL(447, 5, 55, 80, new short[] { 2102, 2103 }),
	
	/**
	 * Adamantite ore.
	 */
	ADAMANTITE(449, 5, 70, 95, new short[] { 2104, 2105 }),
	
	/**
	 * Rune ore.
	 */
	RUNE(451, 5, 85, 125, new short[] { 2106, 2107}),
	
	/**
	 * Clay ore.
	 */
	CLAY(434, 5, 1, 5, new short[] { 2108, 2109 });
	
	/**
	 * A map of object ids to nodes.
	 */
	private static Map<Short, Node> nodes = new HashMap<Short, Node>();
	
	/**
	 * Gets a node by an object id.
	 * @param object The object id.
	 * @return The node, or <code>null</code> if the object is not a node.
	 */
	public static Node forId(int object) {
		return nodes.get((short)object);
	}
	
	/**
	 * Populates the node map.
	 */
	static {
		for(Node node : Node.values()) {
			for(short object : node.objects) {
				nodes.put(object, node);
			}
		}
	}
	
	/**
	 * The object ids of this node.
	 */
	private short[] objects;
	
	/**
	 * The minimum level to mine this node.
	 */
	private byte level;
	
	/**
	 * The id of the node.
	 */
	private int nodeId;
	
	/**
	 * The ore this node contains.
	 */
	private short ore;
	
	/**
	 * The replacement object of the ore.
	 */
	private short oreReplacement;
	
	/**
	 * The amount of time the replacement ore appears after the ore is mined.
	 */
	private short oreReplacementTime;
	
	/**
	 * The experience.
	 */
	private double experience;
	
	/**
	 * Creates the node.
	 * @param ore The ore id.
	 * @param oreReplacementTime The time the replacement ore is visible.
	 * @param level The required level.
	 * @param experience The experience per ore.
	 * @param objects The object ids.
	 */
	private Node(int ore, int oreReplacementTime, int level, double experience, short[] objects) {
		this.objects = objects;
		this.level = (byte) level;
		this.experience = experience;
		this.ore = (short) ore;
		this.oreReplacementTime = (short) oreReplacementTime;
		oreReplacement = (short) 451;
	}
	
	/**
	 * Gets the ore id.
	 * @return The ore id.
	 */
	public int getOreId() {
		return ore;
	}
	
	/**
	 * Gets the replacement ore id.
	 * @return The replacement ore id.
	 */
	public int getReplacementObject() {
		return oreReplacement;
	}
	
	/**
	 * Gets the time a stump will appear.
	 * @return The stump time.
	 */
	public int getOreReplacementTime() {
		return oreReplacementTime;
	}
	
	/**
	 * Gets the object ids.
	 * @return The object ids.
	 */
	public short[] getObjectIds() {
		return objects;
	}
	
	/**
	 * Gets the required level.
	 * @return The required level.
	 */
	public int getRequiredLevel() {
		return level;
	}
	
	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}
	
	/**
	 * Gets the node id.
	 * @return The node id.
	 */
	public int getNodeId() {
		return nodeId;
	}
	
	/**
	 * Sets the id of the node being mined.
	 */
	public void setNodeId(int id) {
		nodeId = id;
	}
}
