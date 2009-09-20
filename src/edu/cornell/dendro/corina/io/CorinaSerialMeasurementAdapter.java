package edu.cornell.dendro.corina.io;

import java.io.IOException;
import java.lang.reflect.Constructor;

import edu.cornell.dendro.corina.core.App;

public class CorinaSerialMeasurementAdapter implements SerialSampleIOListener {
	private AbstractSerialMeasuringDevice device;
	private MeasurementReceiver receiver;
	
	/**
	 * Opens a device for passing to a measuring device object!
	 * @return
	 * @throws IOException
	 */
	public static <T extends AbstractSerialMeasuringDevice> T create(Class<T> deviceClass) throws IOException {
		T device;
		
		// TODO: Make this remember ports per type
		// add a better interface
		// etc, etc
		
		
		String serialPortName = App.prefs.getPref("corina.serialsampleio.port", "COM1");		
				
		Constructor<T> constructor;
		try {
			constructor = deviceClass.getConstructor(String.class);
			device = constructor.newInstance(serialPortName);
		} catch (Exception ex) {
			throw new IOException(ex);
		}
		
		device.initialize();
		
		return device;
	}
	
	public CorinaSerialMeasurementAdapter(AbstractSerialMeasuringDevice device, MeasurementReceiver receiver) {
		this.device = device;
		this.receiver = receiver;
		
		device.addSerialSampleIOListener(this);
	}
	
	public void close() {
		device.removeSerialSampleIOListener(this);
		device.close();
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
