/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;
import java.util.Calendar;

/**
 * A Match between two opponents. Part of a Matchday.
 * @author Markus Henn
 */
public class Match implements Serializable {
	private static final long serialVersionUID = 8719866960705033601L;
	
	private int id;
	/** first opponent; not null */
	private Team team1;
	/** second opponent; not null */
	private Team team2;
	/** matchday, the match is part of; not null */
	private Matchday matchday;
	/** time, when the match starts; not null */
	private Calendar startTime;
	/** current number of goals of first team */
	private short team1goals;
	/** current number of goals of second team */
	private short team2goals;
	/** status of the match; not null */
	private Status status;
	/** venue, where the match takes place; not null */
	private Venue venue;
	
	/**
	 * 
	 * @param id
	 * @param matchday matchday, the match is part of; must not be null
	 * @param team1 first opponent; must not be null
	 * @param team2 second opponent; must not be null
	 * @param startTime time, when the match starts; must not be null
	 * @param team1goals current number of goals of first team
	 * @param team2goals current number of goals of second team
	 * @param status status of the match; must not be null
	 * @param venue venue, where the match takes place; must not be null
	 * @throws IllegalArgumentException if an invalid parameter value was passed
	 */
	public Match(int id, Matchday matchday, Team team1, Team team2, Calendar startTime,
			short team1goals, short team2goals, Status status, Venue venue) throws IllegalArgumentException {
		if (matchday == null) {
			throw new IllegalArgumentException("matchday must not be null");
		}
		if (team1 == null) {
			throw new IllegalArgumentException("team1 must not be null");
		}
		if (team2 == null) {
			throw new IllegalArgumentException("team2 must not be null");
		}
		if (startTime == null) {
			throw new IllegalArgumentException("startTime must not be null");
		}
		if (status == null) {
			throw new IllegalArgumentException("status must not be null");
		}
		if (venue == null) {
			throw new IllegalArgumentException("venue must not be null");
		}
		
		this.id = id;
		this.matchday = matchday;
		this.team1 = team1;
		this.team2 = team2;
		this.startTime = startTime;
		this.team1goals = team1goals;
		this.team2goals = team2goals;
		this.venue = venue;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * @return first opponent; can't be null
	 */
	public Team getTeam1() {
		return team1;
	}
	
	/**
	 * @return second opponent; can't be null
	 */
	public Team getTeam2() {
		return team2;
	}
	
	/**
	 * @return current number of goals of first team
	 */
	public short getTeam1goals() {
		return team1goals;
	}
	
	/**
	 * @param numberOfGoals current number of goals of first team
	 */
	public void setTeam1goals(short numberOfGoals) {
		this.team1goals = numberOfGoals;
	}
	
	/**
	 * @return current number of goals of second team
	 */
	public short getTeam2goals() {
		return team2goals;
	}
	
	/**
	 * @param numberOfGoals current number of goals of second team
	 */
	public void setTeam2goals(short numberOfGoals) {
		this.team2goals = numberOfGoals;
	}
	
	/**
	 * @return matchday, the match is part of; can't be null
	 */
	public Matchday getMatchday() {
		return matchday;
	}
	
	/**
	 * @return time, when the match starts; can't be null
	 */
	public Calendar getStartTime() {
		return startTime;
	}
	
	/**
	 * @return true, if a temporary score or result is available
	 */
	public boolean isScoreAvailable() {
		return this.status == Status.RUNNING || this.status == Status.FINISHED;
	}
	
	/**
	 * @return true, if match is finished
	 */
	public boolean isFinished() {
		return this.status == Status.FINISHED;
	}
	
	/**
	 * @param status status of the match; must not be null
	 * @throws IllegalArgumentException if status is null
	 */
	public void setStatus(Status status) throws IllegalArgumentException {
		if (status == null) {
			throw new IllegalArgumentException("status must not be null");
		}
		this.status = status;
	}
	
	/**
	 * @return venue, where the match takes place; can't be null
	 */
	public Venue getVenue() {
		return venue;
	}

	/**
	 * @return &lt;team1name&gt; &lt;team1goals&gt;:&lt;team2goals&gt; &lt;team2name&gt;<br />
	 * or if no score is available:<br />
	 * &lt;team1name&gt; -:- &lt;team2name&gt;
	 */
	@Override
	public String toString() {
		return team1 + " " + (isScoreAvailable() ? team1goals + ":" + team2goals : "-:-") + " " + team2;
	}
	
	/**
	 * Status of a match
	 * @author Markus Henn
	 */
	public static enum Status {
		NOT_STARTED,
		RUNNING,
		FINISHED
	}
}
