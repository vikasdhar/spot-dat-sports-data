/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.receiver;

import java.util.List;

import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchday;

/**
 * @author Markus Henn
 *
 */
public interface MatchdaysReceiver extends NetworkErrorReceiver {
	public void setMatchdays(List<Matchday> matchdays);
}
