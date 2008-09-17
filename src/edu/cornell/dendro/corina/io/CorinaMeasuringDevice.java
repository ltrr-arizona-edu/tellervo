package edu.cornell.dendro.corina.io;

import java.io.IOException;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;

public class CorinaMeasuringDevice implements SerialSampleIOListener {
	private SerialSampleIO port;
	private CorinaMeasuringReceiver receiver;
	
	/**
	 * Opens a dataport for passing to a measuring device object!
	 * @return
	 * @throws IOException
	 */
	public static SerialSampleIO initialize() throws IOException {
		SerialSampleIO dataPort;
		
		dataPort = new SerialSampleIO(App.prefs.getPref("corina.serialsampleio.port"));
		dataPort.initialize();
		
		return dataPort;
	}
	
	public CorinaMeasuringDevice(SerialSampleIO port, CorinaMeasuringReceiver receiver) {
		this.port = port;
		this.receiver = receiver;
		
		port.addSerialSampleIOListener(this);
	}
	
	public void close() {
		port.removeSerialSampleIOListener(this);
		port.close();
	}
	
	public void SerialSampleIONotify(SerialSampleIOEvent sse) {
		if(sse.getType() == SerialSampleIOEvent.BAD_SAMPLE_EVENT) {
			receiver.receiverUpdateStatus("Error reading the previous sample!");
		}
		if(sse.getType() == SerialSampleIOEvent.ERROR) {
			receiver.receiverUpdateStatus((String) sse.getValue());
		}
		else if(sse.getType() == SerialSampleIOEvent.INITIALIZING_EVENT) {
			receiver.receiverUpdateStatus("Initializing reader (try "+ sse.getValue() +")");
		}
		else if(sse.getType() == SerialSampleIOEvent.INITIALIZED_EVENT) {
			receiver.receiverUpdateStatus("Initialized; ready to read measurements.");
		}
		else if(sse.getType() == SerialSampleIOEvent.NEW_SAMPLE_EVENT) {
			Integer value = (Integer) sse.getValue();
			
			receiver.receiverNewMeasurement(value);
		}
	}
}
