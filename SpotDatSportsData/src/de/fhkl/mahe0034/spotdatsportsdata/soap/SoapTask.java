package de.fhkl.mahe0034.spotdatsportsdata.soap;
import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

/**
 * A Task which will use a SOAP interface. Task will be executed asynchronously.
 * @author Markus Henn
 *
 * @param <T> objects of this class are used to match the result of the task to the request
 */
public class SoapTask<T> extends AsyncTask<SoapObject, Integer, SoapObject> {
	private static final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
	private SoapTaskResultReceiver<T> resultReceiver;
	private T taskIdentifier;
	private HttpTransportSE httpTransport;
	
	public SoapTask(SoapTaskResultReceiver<T> resultReceiver, T taskIdentifier, String serviceUrl) {
		this.resultReceiver = resultReceiver;
		this.taskIdentifier = taskIdentifier;
		this.httpTransport = new HttpTransportSE(serviceUrl);
		/*
		this.httpTransport.debug = true;
		SoapTask.soapEnvelope.implicitTypes = true;
		*/
		SoapTask.soapEnvelope.setAddAdornments(false);
	}
	
	@Override
	protected SoapObject doInBackground(SoapObject... soapObjects) {
		SoapObject result = null;
		for (SoapObject soapObject : soapObjects) {
			// TODO: collect results xor make sure that only one task is coming in
			result = runSoapAction(soapObject);
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(SoapObject result) {
		if (resultReceiver != null) {
			resultReceiver.setSoapTaskResult(taskIdentifier, result);
		}
	}
	
	private SoapObject runSoapAction(SoapObject soapObject) {
		soapEnvelope.setOutputSoapObject(soapObject);
		try {
			httpTransport.call(getSoapAction(soapObject), soapEnvelope);
			// response is SoapObject
			return (SoapObject) soapEnvelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getSoapAction(SoapObject soapObject) {
		return soapObject.getNamespace() + "/" + soapObject.getName();
	}

}
