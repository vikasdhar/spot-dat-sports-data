/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.receiver;

import java.util.Collection;

import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchevent;

/**
 * @author Markus Henn
 *
 */
public interface MatcheventsReceiver extends NetworkErrorReceiver {
	public void setMatchevents(Collection<? extends Matchevent> matchevents);
}
