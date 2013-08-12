/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.openligadb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Competition;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Goal;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Match;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Match.Status;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchday;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Player;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Team;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Venue;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.CompetitionsReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.CurrentMatchdayReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatchdaysReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatchesReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatcheventsReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.soap.SoapTask;
import de.fhkl.mahe0034.spotdatsportsdata.soap.SoapTaskResultReceiver;

/**
 * TODO: prevent starting a new task while another is processed (e.g. because of temp variables)
 * @author Markus Henn
 */
public class OpenLigaDBDataProvider implements SoapTaskResultReceiver<OpenLigaDBOperation> {
	private static final String NAMESPACE = "http://msiggi.de/Sportsdata/Webservices";
	private static final String URL = "http://www.openligadb.de/Webservices/Sportsdata.asmx";
	
	private Context context;
	
	private CompetitionsReceiver competitionsResponseReceiver;
	private MatchdaysReceiver matchdaysResponseReceiver;
	private CurrentMatchdayReceiver currentMatchdayResponseReceiver;
	private MatchesReceiver matchesResponseReceiver;
	private MatcheventsReceiver matcheventsReceiver;
	
	private Competition matchdaysCompetition;
	private Competition currentMatchdayCompetition;
	private Matchday matchesMatchday;
	private Match goalsMatch;
	
	/**
	 * @param context
	 * @throws IllegalArgumentException if context is null
	 */
	public static OpenLigaDBDataProvider getInstance(Context context) throws IllegalArgumentException {
		return new OpenLigaDBDataProvider(context);
	}
	
	/**
	 * @param context
	 * @throws IllegalArgumentException if context is null
	 */
	private OpenLigaDBDataProvider(Context context) throws IllegalArgumentException {
		if (context == null) {
			throw new IllegalArgumentException("context must not be null");
		}
		this.context = context;
	}
	
	/**
	 * requests the competitions
	 * @param responseReceiver
	 * @throws IllegalArgumentException if responseReceiver is null
	 * @throws NetworkErrorException if network is not available
	 */
	public void requestAvailableCompetitions(CompetitionsReceiver responseReceiver) throws IllegalArgumentException, NetworkErrorException {
		if (responseReceiver == null) {
			throw new IllegalArgumentException("responseReceiver must not be null");
		}
		checkNetworkAvailability();
		
		this.competitionsResponseReceiver = responseReceiver;
		
		OpenLigaDBOperation operation = OpenLigaDBOperation.GET_AVAILABLE_LEAGUES;
		SoapObject requestObject = new SoapObject(NAMESPACE, operation.getSoapOperationName());
		SoapTask<OpenLigaDBOperation> soapTask = new SoapTask<OpenLigaDBOperation>(this, operation, URL);
		
		soapTask.execute(requestObject);
	}
	
	/**
	 * requests matchdays of the given competition
	 * @param responseReceiver
	 * @param competition
	 * @throws IllegalArgumentException if responseReceiver is null
	 * @throws NetworkErrorException if network is not available
	 */
	public void requestAvailableMatchdays(MatchdaysReceiver responseReceiver, Competition competition) throws IllegalArgumentException, NetworkErrorException {
		if (responseReceiver == null) {
			throw new IllegalArgumentException("responseReceiver must not be null");
		}
		if (competition == null) {
			throw new IllegalArgumentException("competition must not be null");
		}
		checkNetworkAvailability();
		
		this.matchdaysResponseReceiver = responseReceiver;
		this.matchdaysCompetition = competition;
		
		OpenLigaDBOperation operation = OpenLigaDBOperation.GET_AVAILABLE_GROUPS;
		
		SoapObject requestObject = new SoapObject(NAMESPACE, operation.getSoapOperationName());
		setRequestObjectParameter(requestObject, "leagueShortcut", competition.getSeasonIndependentId());
		setRequestObjectParameter(requestObject, "leagueSaison", competition.getSeason());
		SoapTask<OpenLigaDBOperation> soapTask = new SoapTask<OpenLigaDBOperation>(this, operation, URL);
		soapTask.execute(requestObject);
	}
	
	/**
	 * requests the matches of the given matchday
	 * @param responseReceiver
	 * @param matchday
	 * @throws IllegalArgumentException if responseReceiver is null
	 * @throws NetworkErrorException if network is not available
	 */
	public void requestMatches(MatchesReceiver responseReceiver, Matchday matchday) throws IllegalArgumentException, NetworkErrorException {
		if (responseReceiver == null) {
			throw new IllegalArgumentException("responseReceiver must not be null");
		}
		if (matchday == null) {
			throw new IllegalArgumentException("matchday must not be null");
		}
		checkNetworkAvailability();
		
		this.matchesResponseReceiver = responseReceiver;
		this.matchesMatchday = matchday;
		
		OpenLigaDBOperation operation = OpenLigaDBOperation.GET_MATCHDATA_BY_GROUP_LEAGUE_SAISON;
		
		SoapObject requestObject = new SoapObject(NAMESPACE, operation.getSoapOperationName());
		setRequestObjectParameter(requestObject, "groupOrderID", Integer.valueOf(matchday.getOrderId()));
		Competition competition = matchday.getCompetition();
		setRequestObjectParameter(requestObject, "leagueShortcut", competition.getSeasonIndependentId());
		setRequestObjectParameter(requestObject, "leagueSaison", competition.getSeason());
		SoapTask<OpenLigaDBOperation> soapTask = new SoapTask<OpenLigaDBOperation>(this, operation, URL);
		soapTask.execute(requestObject);
	}
	
	/**
	 * requests the current matchday of the given competition
	 * @param responseReceiver
	 * @param competition
	 * @throws IllegalArgumentException if responseReceiver is null
	 * @throws NetworkErrorException if network is not available
	 */
	public void requestCurrentMatchday(CurrentMatchdayReceiver responseReceiver, Competition competition) throws IllegalArgumentException, NetworkErrorException {
		if (responseReceiver == null) {
			throw new IllegalArgumentException("responseReceiver must not be null");
		}
		if (competition == null) {
			throw new IllegalArgumentException("competition must not be null");
		}
		checkNetworkAvailability();
		
		this.currentMatchdayResponseReceiver = responseReceiver;
		this.currentMatchdayCompetition = competition;
		
		OpenLigaDBOperation operation = OpenLigaDBOperation.GET_CURRENT_GROUP;
		
		SoapObject requestObject = new SoapObject(NAMESPACE, operation.getSoapOperationName());
		setRequestObjectParameter(requestObject, "leagueShortcut", competition.getSeasonIndependentId());
		SoapTask<OpenLigaDBOperation> soapTask = new SoapTask<OpenLigaDBOperation>(this, operation, URL);
		soapTask.execute(requestObject);
	}
	
	/**
	 * requests the goals scored in the given match
	 * @param responseReceiver
	 * @param match
	 * @throws IllegalArgumentException if responseReceiver is null
	 * @throws NetworkErrorException if network is not available
	 */
	public void requestGoals(MatcheventsReceiver responseReceiver, Match match) throws NetworkErrorException {
		if (responseReceiver == null) {
			throw new IllegalArgumentException("responseReceiver must not be null");
		}
		if (match == null) {
			throw new IllegalArgumentException("match must not be null");
		}
		checkNetworkAvailability();
		
		this.matcheventsReceiver = responseReceiver;
		this.goalsMatch = match;
		
		OpenLigaDBOperation operation = OpenLigaDBOperation.GET_GOALS_BY_MATCH;
		
		SoapObject requestObject = new SoapObject(NAMESPACE, operation.getSoapOperationName());
		setRequestObjectParameter(requestObject, "MatchID", Integer.valueOf(match.getId()));
		SoapTask<OpenLigaDBOperation> soapTask = new SoapTask<OpenLigaDBOperation>(this, operation, URL);
		soapTask.execute(requestObject);
	}
	

	@Override
	public void setSoapTaskResult(OpenLigaDBOperation operation, SoapObject soapResponseObject) {
		switch (operation) {
		
		case GET_AVAILABLE_LEAGUES_BY_SPORT:
		case GET_AVAILABLE_LEAGUES:
			handleCompetitionsResponse(soapResponseObject);
			break;
			
		case GET_AVAILABLE_GROUPS:
			handleMatchdaysResponse(soapResponseObject);
			break;
			
		case GET_MATCHDATA_BY_GROUP_LEAGUE_SAISON:
			handleMatchesResponse(soapResponseObject);
			break;
		
		case GET_CURRENT_GROUP:
			handleCurrentMatchdayResponse(soapResponseObject);
			break;
		
		case GET_GOALS_BY_MATCH:
			handleGoalsResponse(soapResponseObject);
			break;

		default:
			break;
		}
	}
	
	/**
	 * Sets a property (parameter) of the object with namespace {@value #NAMESPACE}. This is essentially a sub element.
	 * @param requestObject
	 * @param name the name of the property
	 * @param value the value of the property
	 */
	private static void setRequestObjectParameter(SoapObject requestObject, String name, Object value) {
		PropertyInfo propertyInfo = new PropertyInfo();
		propertyInfo.setNamespace(NAMESPACE);
		propertyInfo.setName(name);
		propertyInfo.setValue(value);
		requestObject.addProperty(propertyInfo);
	}
	
	/**
	 * transfers the competitions from the soapResponseObject to the {@link #competitionsResponseReceiver}
	 * @param soapResponseObject
	 */
	private void handleCompetitionsResponse(SoapObject soapResponseObject) {
		Collection<Competition> competitions = new ArrayList<Competition>();

		for (int i = 0; i < soapResponseObject.getPropertyCount(); i++) {
			competitions.add(convertToCompetition((SoapObject) soapResponseObject.getProperty(i)));
		}
		competitionsResponseReceiver.setCompetitions(competitions);
	}
	
	/**
	 * transfers the matchdays from the soapResponseObject to the {@link #matchdaysResponseReceiver}
	 * @param soapResponseObject
	 */
	private void handleMatchdaysResponse(SoapObject soapResponseObject) {
		List<Matchday> matchdays = new ArrayList<Matchday>();
		
		for (int i = 0; i < soapResponseObject.getPropertyCount(); i++) {
			matchdays.add(convertToMatchday(this.matchdaysCompetition, (SoapObject) soapResponseObject.getProperty(i)));
		}
		// TODO: sort matchdays by orderId? They already seem to be ordered by orderId
		
		this.matchdaysResponseReceiver.setMatchdays(matchdays);
		
		// reset temporarily used variables
		//this.matchdaysCompetition = null;
	}
	
	/**
	 * transfers the matches from the soapResponseObject to the {@link #matchesResponseReceiver}
	 * @param soapResponseObject
	 */
	private void handleMatchesResponse(SoapObject soapResponseObject) {
		Collection<Match> matches = new ArrayList<Match>();
		
		for(int i = 0; i < soapResponseObject.getPropertyCount(); i++) {
			matches.add(convertToMatch(this.matchesMatchday, (SoapObject) soapResponseObject.getProperty(i)));
		}
		this.matchesResponseReceiver.setMatches(matches);

		// reset temporarily used variables
		//this.matchesMatchday = null;
	}
	
	/**
	 * transfers the current matchday from the soapResponseObject to the {@link #currentMatchdayResponseReceiver}
	 * @param soapResponseObject
	 */
	private void handleCurrentMatchdayResponse(SoapObject soapResponseObject) {
		Matchday matchday = convertToMatchday(this.currentMatchdayCompetition, soapResponseObject);
		this.currentMatchdayResponseReceiver.setCurrentMatchday(matchday);
	}
	
	private void handleGoalsResponse(SoapObject soapResponseObject) {
		Collection<Goal> goals = new ArrayList<Goal>();
		
		for(int i = 0; i < soapResponseObject.getPropertyCount(); i++) {
			goals.add(convertToGoal(this.goalsMatch, (SoapObject) soapResponseObject.getProperty(i)));
		}
		
		this.matcheventsReceiver.setMatchevents(goals);
		
		// reset temporarily used variables
		//this.goalsMatch = null;
	}
	
	private static Competition convertToCompetition(SoapObject leagueSoapObject) {
		String idString = leagueSoapObject.getPropertyAsString("leagueID");
		String sportIdString = leagueSoapObject.getPropertyAsString("leagueSportID");
		String name =  leagueSoapObject.getPropertySafelyAsString("leagueName");
		String shortcut = leagueSoapObject.getPropertySafelyAsString("leagueShortcut");
		String season = leagueSoapObject.getPropertySafelyAsString("leagueSaison");
		
		// convert int values
		int id = Integer.parseInt(idString);
		int sportId = Integer.parseInt(sportIdString);
		
		// create competition
		return new Competition(id, sportId, name, shortcut, season, null);
	}
	
	private static Matchday convertToMatchday(Competition competition, SoapObject groupSoapObject) {
		String idString = groupSoapObject.getPropertyAsString("groupID");
		String orderIdString = groupSoapObject.getPropertyAsString("groupOrderID");
		String name = groupSoapObject.getPropertyAsString("groupName");
		
		// convert int values
		int id = Integer.parseInt(idString);
		int orderId = Integer.parseInt(orderIdString);
		
		// create matchday
		return new Matchday(id, competition, orderId, name);
	}
	
	private static Match convertToMatch(Matchday matchday, SoapObject matchSoapObject) {
		String idString = matchSoapObject.getPropertyAsString("matchID");
		String team1idString = matchSoapObject.getPropertyAsString("idTeam1");
		String team2idString = matchSoapObject.getPropertyAsString("idTeam2");
		String team1name = matchSoapObject.getPropertyAsString("nameTeam1");
		String team2name = matchSoapObject.getPropertyAsString("nameTeam2");
		String team1iconUrl = matchSoapObject.getPropertyAsString("iconUrlTeam1");
		String team2iconUrl = matchSoapObject.getPropertyAsString("iconUrlTeam2");
		String isFinishedString = matchSoapObject.getPropertyAsString("matchIsFinished");
		String matchDateTimeUtcString = matchSoapObject.getPropertyAsString("matchDateTimeUTC");
		String team1goalsString = matchSoapObject.getPropertyAsString("pointsTeam1");
		String team2goalsString = matchSoapObject.getPropertyAsString("pointsTeam2");
		SoapObject locationSoapObject = (SoapObject) matchSoapObject.getProperty("location");
		
		// convert values
		int id = Integer.parseInt(idString);
		int team1id = Integer.parseInt(team1idString);
		int team2id = Integer.parseInt(team2idString);
		short team1goals = Short.parseShort(team1goalsString);
		short team2goals = Short.parseShort(team2goalsString);
		boolean isFinished = Boolean.parseBoolean(isFinishedString);
		Status status = convertToStatus(team1goals, team2goals, isFinished);
		Venue venue = convertToVenue(locationSoapObject);
		Calendar startTime = null;
		try {
			startTime = convertIso8601UtcToCalendar(matchDateTimeUtcString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: cache Teams?
		Team team1 = new Team(team1id, team1name, team1iconUrl);
		Team team2 = new Team(team2id, team2name, team2iconUrl);
		
		return new Match(id, matchday, team1, team2, startTime, team1goals, team2goals, status, venue);
	}
	
	private Goal convertToGoal(Match match, SoapObject goalSoapObject) {
		String idString = goalSoapObject.getPropertyAsString("goalID");
		String team1scoreString = goalSoapObject.getPropertySafelyAsString("goalScoreTeam1");
		String team2scoreString = goalSoapObject.getPropertySafelyAsString("goalScoreTeam2");
		String minuteString = goalSoapObject.getPropertySafelyAsString("goalMatchMinute");
		String scorerIdString = goalSoapObject.getPropertyAsString("goalGetterID");
		String scorerName = goalSoapObject.getPropertySafelyAsString("goalGetterName");
		String isPenaltyString = goalSoapObject.getPropertySafelyAsString("goalPenalty");
		String isOwnGoalString = goalSoapObject.getPropertySafelyAsString("goalOwnGoal");
		String isInjuryTimeString = goalSoapObject.getPropertySafelyAsString("goalOvertime");
		String comment = goalSoapObject.getPropertySafelyAsString("goalComment");
		
		// convert values
		int id = Integer.parseInt(idString);
		Short team1score = null;
		if (!team1scoreString.isEmpty()) {
			team1score = Short.valueOf(team1scoreString);
		}
		Short team2score = null;
		if (!team2scoreString.isEmpty()) {
			team2score = Short.valueOf(team2scoreString);
		}
		Player scorer = convertToPlayer(scorerIdString, scorerName);
		Short minute = null;
		if (!minuteString.isEmpty()) {
			minute = Short.valueOf(minuteString);
		}
		Boolean isPenalty = null;
		if (!isPenaltyString.isEmpty()) {
			isPenalty = Boolean.valueOf(isPenaltyString);
		}
		Boolean isOwnGoal = null;
		if (!isOwnGoalString.isEmpty()) {
			isOwnGoal = Boolean.valueOf(isOwnGoalString);
		}
		Boolean isInjuryTime = null;
		if (!isInjuryTimeString.isEmpty()) {
			isInjuryTime = Boolean.valueOf(isInjuryTimeString);
		}
		
		return new Goal(id, this.goalsMatch, team1score, team2score, scorer, minute, isPenalty, isOwnGoal, isInjuryTime, comment);
	}
	
	private static Venue convertToVenue(SoapObject locationSoapObject) {
		String idString = locationSoapObject.getPropertyAsString("locationID");
		String name = locationSoapObject.getPropertySafelyAsString("locationStadium");
		String city = locationSoapObject.getPropertySafelyAsString("locationCity");
		
		// convert values
		int id = Integer.parseInt(idString);
		
		return new Venue(id, name, city);
	}
	
	private static Calendar convertIso8601UtcToCalendar(String iso8601UtcDateTime) throws ParseException {
		// TODO: cache formatter?
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(formatter.parse(iso8601UtcDateTime));
		return calendar;
	}
	
	private static Status convertToStatus(short team1goals, short team2goals, boolean isFinished) {
		if (isFinished) {
			return Status.FINISHED;
		} else if (team1goals != -1 && team2goals != -1) {
			return Status.RUNNING;
		}
		return Status.NOT_STARTED;
	}
	
	private static Player convertToPlayer(String idString, String name) {
		int id = Integer.parseInt(idString);
		return new Player(id, name);
	}
	
	/**
	 * Throws {@link NetworkErrorException} if network is not available, does nothing otherwise.
	 */
	private void checkNetworkAvailability() throws NetworkErrorException {
		if (!isNetworkAvailable()) {
			throw new NetworkErrorException("Network not available");
		}
	}
	
	/**
	 * @return <code>true</code>, if network is available, <code>false</code> otherwise.
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
}
