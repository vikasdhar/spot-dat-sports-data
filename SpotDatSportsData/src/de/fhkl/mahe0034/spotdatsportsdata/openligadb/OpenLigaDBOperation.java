/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.openligadb;

/**
 * @author Markus Henn
 *
 */
public enum OpenLigaDBOperation {
	GET_AVAILABLE_LEAGUES("GetAvailLeagues"),
	GET_AVAILABLE_LEAGUES_BY_SPORT("GetAvailLeaguesBySport"),
	GET_AVAILABLE_GROUPS("GetAvailGroups"),
	GET_MATCHDATA_BY_GROUP_LEAGUE_SAISON("GetMatchdataByGroupLeagueSaison"),
	GET_CURRENT_GROUP("GetCurrentGroup"),
	GET_GOALS_BY_MATCH("GetGoalsByMatch");
	
	private String soapOperationName;
	
	OpenLigaDBOperation(String soapOperationName) {
		this.soapOperationName = soapOperationName;
	}
	
	public String getSoapOperationName() {
		return soapOperationName;
	}
}
