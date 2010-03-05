package edu.cornell.dendro.corina.io;

import java.io.IOException;

import edu.cornell.dendro.corina.core.App;

public class LegacyCorinaMeasuringDevice implements SerialSampleIOListener {
	private LegacySerialSampleIO port;
	private MeasurementReceiver receiver;
	
	/**
	 * Opens a dataport for passing to a measuring device object!
	 * @return
	 * @throws IOException
	 */
	public static LegacySerialSampleIO initialize() throws IOException {
		LegacySerialSampleIO dataPort;
		
		dataPort = new LegacySerialSampleIO(App.prefs.getPref("corina.serialsampleio.port", "COM1"));
		dataPort.initialize();
		
		return dataPort;
	}
	
	public LegacyCorinaMeasuringDevice(LegacySerialSampleIO port, MeasurementReceiver receiver) {
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
