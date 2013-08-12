/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;

import java.io.Serializable;

/**
 * An event which occurred during a match
 * @author Markus Henn
 */
public abstract class Matchevent implements Serializable {
	private static final long serialVersionUID = 7808170979360688256L;
	
	/** match, the event occurred in; not null */
	private Match match;
	/** minute of the match, when the event occurred; can be null */
	private Short minute;
	/** true, if event occurred in injury time; can be null */
	private Boolean isInjuryTime;
	/** some comment; can be null */
	private String comment;

	/**
	 * @param match match, the event occurred in; must not be null
	 * @param minute minute of the match, when the event occurred; use null, if unknown
	 * @param isInjuryTime true, if event occurred in injury time; use null, if unknown
	 * @param comment use null, if you don't want to add a comment
	 * @throws IllegalArgumentException if an invalid parameter was passed
	 */
	public Matchevent(Match match, Short minute, Boolean isInjuryTime, String comment) throws IllegalArgumentException {
		if (match == null) {
			throw new IllegalArgumentException("match must not be null");
		}
		this.minute = minute;
		this.isInjuryTime = isInjuryTime;
		this.comment = comment;
	}
	
	/**
	 * @return match, the event occurred in; not null
	 */
	public Match getMatch() {
		return match;
	}
	
	/**
	 * @return minute of the match, when the event occurred; can be null
	 */
	public Short getMinute() {
		return minute;
	}
	
	/**
	 * @return String which represents the minute. If minute wasn't specified, "??" is used instead.
	 * If event occurred in injury time, a plus sign (+) is added as suffix, e.g.: "45+"
	 */
	public String getMinuteString() {
		String minuteString = (this.minute != null ? Short.toString(minute.shortValue()) : "??");
		if (this.isInjuryTime != null && this.isInjuryTime.booleanValue() == true) {
			minuteString += "+";
		}
		return minuteString;
	}
	
	/**
	 * @return true, if event occurred in injury time; can be null
	 */
	public Boolean getIsInjuryTime() {
		return isInjuryTime;
	}
	
	/**
	 * @return comment; can be null
	 */
	public String getComment() {
		return comment;
	}

}
