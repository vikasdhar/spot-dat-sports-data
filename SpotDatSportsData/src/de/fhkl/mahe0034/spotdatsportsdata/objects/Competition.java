/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;

/**
 * Execution of a competition
 * @author Markus Henn
 */
public class Competition implements Serializable {
	private static final long serialVersionUID = -321921057047172192L;
	
	private int id;
	private int sportId;
	/** name of the competition; not null */
	private String name;
	/** season independent identifier for competition; not null */
	private String seasonIndependentId;
	/** season, this competition is executed in; not null */
	private String season;
	/** competition, which this competition is part of;
	 * e.g. this competition: "World Cup Group A", parent competition: "World Cup;
	 * null, if this is the overall competition */
	private Competition parentCompetition;
	
	/**
	 * @param id
	 * @param sportId
	 * @param name name of the competition; must not be null
	 * @param seasonIndependentId season independent identifier for competition; must not be null
	 * @param season season, this competition is executed in; must not be null
	 * @param parentCompetition competition, which this competition is part of;
	 * e.g. this competition: "World Cup Group A", parent competition: "World Cup;
	 * use null, if this is the overall competition
	 * @throws IllegalArgumentException
	 */
	public Competition(int id, int sportId, String name, String seasonIndependentId, String season, Competition parentCompetition) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}
		if (seasonIndependentId == null) {
			throw new IllegalArgumentException("shortcut must not be null");
		}
		if (season == null) {
			throw new IllegalArgumentException("season must not be null");
		}
		this.id = id;
		this.sportId = sportId;
		this.name = name;
		this.seasonIndependentId = seasonIndependentId;
		this.season = season;
		this.parentCompetition = parentCompetition;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSportId() {
		return sportId;
	}
	
	/**
	 * @return name of the competition; can't be null
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return season independent identifier for competition; can't be null
	 */
	public String getSeasonIndependentId() {
		return seasonIndependentId;
	}
	
	/**
	 * @return season, this competition is executed in; can't be null
	 */
	public String getSeason() {
		return season;
	}
	
	/**
	 * @return competition, which this competition is part of;
	 * e.g. this competition: "World Cup Group A", parent competition: "World Cup;
	 * null, if this is the overall competition
	 */
	public Competition getParentCompetition() {
		return parentCompetition;
	}
	
	/**
	 * @return &lt;name&gt;
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
