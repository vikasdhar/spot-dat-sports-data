/**
 * 
 */
package de.fhkl.mahe0034.spotdatsportsdata.openligadb;

/**
 * @author Markus Henn
 *
 */
public enum OpenLigaDBError {
	NETWORK_NOT_AVAILABLE,
	NETWORK_CONNECTION_ABORTED,
	RESPONSE_TIMED_OUT,
	/**
	 * HTTP 500 Internal Server Error
	 * <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
   <soap:Body>
      <soap:Fault>
         <soap:Code>
            <soap:Value>soap:Receiver</soap:Value>
         </soap:Code>
         <soap:Reason>
            <soap:Text xml:lang="en">Server was unable to process request. ---> Object reference not set to an instance of an object.</soap:Text>
         </soap:Reason>
         <soap:Detail/>
      </soap:Fault>
   </soap:Body>
</soap:Envelope>
	 */
	ILLEGAL_PARAMETER,
	/**
	 * HTTP 400 Bad Request
	 */
	UNKNOWN_COMMAND
}
