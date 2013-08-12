/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.objects;



/**
 * {@link Matchevent} Goal
 * @author Markus Henn
 */
public class Goal extends Matchevent {
	private static final long serialVersionUID = -8011655742450227829L;
	
	private int id;
	/** score of first team after this goal; null, if unknown */
	private Short team1score;
	/** score of second team after this goal; null, if unknown */
	private Short team2score;
	/** scorer of the goal; not null */
	private Player scorer;
	/** true, if goal was scored by penalty; null, if unknown */
	private Boolean isPenalty;
	/** true, if goal was an own goal; null, if unknown */
	private Boolean isOwnGoal;
	
	/**
	 * @param id
	 * @param match match, the event occurred in; must not be null
	 * @param team1score score of first team after this goal; use null, if unknown
	 * @param team2score score of second team after this goal; use null, if unknown
	 * @param scorer scorer of the goal; must not be null
	 * @param minute minute of the match, when the event occurred; use null, if unknown
	 * @param isPenalty true, if goal was scored by penalty; use null, if unknown
	 * @param isOwnGoal true, if goal was an own goal; use null, if unknown
	 * @param isInjuryTime true, if goal occurred in injury time; use null, if unknown
	 * @param comment use null, if you don't want to add a comment
	 * @throws IllegalArgumentException if an invalid parameter value was passed
	 */
	public Goal(int id, Match match, Short team1score, Short team2score, Player scorer, Short minute, Boolean isPenalty, Boolean isOwnGoal, Boolean isInjuryTime, String comment) throws IllegalArgumentException {
		super(match, minute, isInjuryTime, comment);
		if (scorer == null) {
			throw new IllegalArgumentException("scorer must not be null");
		}
		this.id = id;
		this.team1score = team1score;
		this.team2score = team2score;
		this.scorer = scorer;
		this.isPenalty = isPenalty;
		this.isOwnGoal = isOwnGoal;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * @return score of first team after this goal; null, if unknown
	 */
	public Short getTeam1score() {
		return team1score;
	}
	
	/**
	 * @return score of second team after this goal; null, if unknown
	 */
	public Short getTeam2score() {
		return team2score;
	}
	
	/**
	 * @return scorer of the goal; can't be null
	 */
	public Player getScorer() {
		return scorer;
	}
	
	/**
	 * @return true, if goal was scored by penalty; null, if unknown
	 */
	public Boolean getIsPenalty() {
		return isPenalty;
	}
	
	/**
	 * @return true, if goal was an own goal; null, if unknown
	 */
	public Boolean getIsOwnGoal() {
		return isOwnGoal;
	}
	
	/**
	 * @return &lt;minute_string&gt;. &lt;team1score&gt;:&lt;team2score&gt; &lt;scorer_name&gt;
	 * @see #getMinuteString()
	 */
	@Override
	public String toString() {
		String result = getMinuteString() + ".";
		if (this.team1score != null && this.team2score != null) {
			result += " " + this.team1score + ":" + this.team2score;
		}
		result += " " + this.scorer.getName();
		if (this.isOwnGoal != null && this.isOwnGoal.booleanValue() == true) {
			// TODO: don't hardcode String
			result += " (own goal)";
		} else if (this.isPenalty != null && this.isPenalty.booleanValue() == true) {
			// TODO: don't hardcode String
			result += " (by penalty)";
		}
		return result;
	}
}
