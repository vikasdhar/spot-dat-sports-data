/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.receiver;

import java.util.Collection;

import de.fhkl.mahe0034.spotdatsportsdata.objects.Match;

/**
 * @author Markus Henn
 *
 */
public interface MatchesReceiver extends NetworkErrorReceiver {
	public void setMatches(Collection<Match> matches);
}
