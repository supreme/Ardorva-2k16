package org.hyperion.rs2.model.container;

/**
 * A container interface is specific to revisions higher than #317. By assigning a
 * container interface to a container, we are able to dynamically update items in the
 * interface since we know the interface id and child id.
 * @author Stephen Andrews
 */
public class ContainerInterface {

	/**
	 * The id of the interface.
	 */
	private int interfaceId;
	
	/**
	 * The child id.
	 */
	private int childId;
	
	/**
	 * The type id.
	 * TODO: Figure out what the different types stand for.
	 */
	private int type;
	
	/**
	 * Contstructs a container interface.
	 * @param interfaceId The id of the interface.
	 * @param childId The child id.
	 * @param type The type id.
	 */
	public ContainerInterface(int interfaceId, int childId, int type) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.type = type;
	}
	
	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}
	
	/**
	 * Sets the interface id.
	 * @param interfaceId the interface id to set.
	 */
	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	/**
	 * Gets the child id.
	 * @return The child id.
	 */
	public int getChildId() {
		return childId;
	}
	
	/**
	 * Sets the child id.
	 * @param childId The child id to set.
	 */
	public void setChildId(int childId) {
		this.childId = childId;
	}
	
	/**
	 * Gets the type id.
	 * @return The type id.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Sets the type id.
	 * @param type The type id.
	 */
	public void setType(int type) {
		this.type = type;
	}
}
