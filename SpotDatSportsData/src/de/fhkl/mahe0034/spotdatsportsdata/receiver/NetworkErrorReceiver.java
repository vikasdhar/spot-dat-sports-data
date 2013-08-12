/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.receiver;

import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBError;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBOperation;

/**
 * @author Markus Henn
 *
 */
public interface NetworkErrorReceiver {
	public void setNetworkError(OpenLigaDBOperation failedOperation, OpenLigaDBError error);
}
