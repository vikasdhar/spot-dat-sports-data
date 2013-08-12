/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.soap;

import org.ksoap2.serialization.SoapObject;

/**
 * Receiver of a {@link SoapTask} result
 * @author Markus Henn
 */
public interface SoapTaskResultReceiver<T> {
	/**
	 * @param taskIdentifier some Objekt which identifies the given task
	 * @param soapResponseObject the resulting soap object
	 */
	public void setSoapTaskResult(T taskIdentifier, SoapObject soapResponseObject);
}
