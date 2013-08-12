/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.receiver;

import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchday;

/**
 * @author Markus Henn
 *
 */
public interface CurrentMatchdayReceiver extends NetworkErrorReceiver {
	public void setCurrentMatchday(Matchday matchday);
}
