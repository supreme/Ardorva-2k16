package org.hyperion.rs2.model;

import java.util.HashMap;
import java.util.Map;

/**
 * An attribute system that each entity holds. Attributes are
 * defined by string and accompanied by their respective object.
 * To keep a standard naming system, attributes requiring spaces
 * should use naming similar to python with underscores, '_', in
 * the place of a space.
 * @author Stephen Andrews
 */
public class EntityAttributes {

	/**
	 * The attributes map.
	 */
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * Adds and sets an attribute.
	 * @param name The name of the attribute.
	 * @param data The attributes data.
	 */
	public void set(String name, Object data) {
		attributes.put(name, data);
	}
	
	/**
	 * Gets an attribute based on the specified name.
	 * @param name The name of the attribute to get.
	 * @return The attribute's data.
	 */
	public Object get(String name) {
		return attributes.get(name);
	}
	
	/**
	 * Determines whether and attribute exists.
	 * @param name The name of the attribute to check.
	 */
	public boolean isSet(String name) {
		if (attributes.get(name) == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Removes an active attribute.
	 * @param name The attribute to remove.
	 */
	public void remove(String name) {
		attributes.remove(name);
	}
}
