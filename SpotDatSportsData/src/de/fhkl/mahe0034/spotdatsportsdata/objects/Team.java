/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;


/**
 * 
 * @author Markus Henn
 */
public class Team implements Serializable {
	private static final long serialVersionUID = -8003998322962612227L;
	
	private int id;
	/** name of the team; not null */
	private String name;
	/** URL to a graphic of the team emblem; not null, but may be invalid URL; empty String means no URL available */
	private String emblemUrl;
	
	/**
	 * 
	 * @param id
	 * @param name name of the team; not null
	 * @param emblemUrl URL to a graphic of the team emblem; not null; use empty String, if there's no URL available
	 * @throws IllegalArgumentException if an invalid parameter value was passed
	 */
	public Team(int id, String name, String emblemUrl) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("name must be null");
		}
		if (emblemUrl == null) {
			throw new IllegalArgumentException("emblemUrl must not be null");
		}
		this.id = id;
		this.name = name;
		this.emblemUrl = emblemUrl;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * @return name of the team; can't be null
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return URL to a graphic of the team emblem; can't be null, but may be invalid URL; empty String means no URL available
	 */
	public String getEmblemUrl() {
		return emblemUrl;
	}
	
	/**
	 * @return &lt;name&gt;
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
