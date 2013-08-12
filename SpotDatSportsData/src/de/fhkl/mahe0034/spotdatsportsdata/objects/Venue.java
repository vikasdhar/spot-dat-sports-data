/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;

/**
 * A location where a match can take place.
 * @author Markus Henn
 */
public class Venue implements Serializable {
	private static final long serialVersionUID = -8197447082025371355L;
	
	private int id;
	/** name of the venue; not null */
	private String name;
	/** name of the city; not null */
	private String city;
	
	/**
	 * @param id
	 * @param name must not be null
	 * @param city name of the city, must not be null
	 * @throws IllegalArgumentException if an invalid parameter value was passed
	 */
	public Venue(int id, String name, String city) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}
		if (city == null) {
			throw new IllegalArgumentException("city must not be null");
		}
		this.id = id;
		this.name = name;
		this.city = city;
	}
	
	public int getId() {
		return id;
	}
	
	/** not null */
	public String getName() {
		return name;
	}
	
	/** name of the city, not null */
	public String getCity() {
		return city;
	}

	/**
	 * @return &lt;name&gt; (&lt;city&gt;)<br />
	 * or if city is not specified:<br />
	 * &lt;name&gt;
	 */
	@Override
	public String toString() {
		if (!city.isEmpty()) {
			return name + " (" + city + ")";
		}
		return name;
	}
}
