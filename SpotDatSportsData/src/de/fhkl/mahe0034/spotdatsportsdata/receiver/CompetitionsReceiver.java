/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.receiver;

import java.util.Collection;

import de.fhkl.mahe0034.spotdatsportsdata.objects.Competition;

/**
 * @author Markus Henn
 *
 */
public interface CompetitionsReceiver extends NetworkErrorReceiver {
	public void setCompetitions(Collection<Competition> competitions);
}
