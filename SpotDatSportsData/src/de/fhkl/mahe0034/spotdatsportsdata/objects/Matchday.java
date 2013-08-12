/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;



/**
 * Matchday of a {@link Competition}
 * @author Markus Henn
 */
public class Matchday implements Serializable {
	private static final long serialVersionUID = -1556191159185769761L;
	
	private int id;
	/** competition, the matchday is part of; not null */
	private Competition competition;
	/** starting with 1 for the first matchday of a competition */
	private int orderId;
	/** name of the competition; not null */
	private String name;
	
	/**
	 * @param id
	 * @param competition competition, the matchday is part of; must not be null
	 * @param orderId e.g. 1 for the first matchday of a competition
	 * @param name name of the competition; must not be null
	 * @throws IllegalArgumentException if an invalid parameter value was passed
	 */
	public Matchday(int id, Competition competition, int orderId, String name) throws IllegalArgumentException {
		if (competition == null) {
			throw new IllegalArgumentException("competition must not be null");
		}
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}
		this.id = id;
		this.competition = competition;
		this.orderId = orderId;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	/**
	 * @return competition, the matchday is part of; can't be null
	 */
	public Competition getCompetition() {
		return competition;
	}

	
	/**
	 * @return e.g. 1 for the first matchday of a competition
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 * @return name of the competition; can't be null
	 */
	public String getName() {
		return name;
	}	
	
	/**
	 * @return &lt;name&gt;
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
