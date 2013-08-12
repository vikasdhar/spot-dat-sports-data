/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;

/**
 * @author Markus Henn
 *
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -8610111175261507638L;
	
	private int id;
	/** name of the player; not null */
	private String name;
	
	/**
	 * @param id
	 * @param name name of the player; must not be null
	 * @throws IllegalArgumentException if name is null
	 */
	public Player(int id, String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * @return name of the player; can't be null
	 */
	public String getName() {
		return name;
	}
}
