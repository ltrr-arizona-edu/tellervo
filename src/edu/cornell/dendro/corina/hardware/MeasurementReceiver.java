package edu.cornell.dendro.corina.hardware;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataDirection;

public interface MeasurementReceiver {
	public void receiverUpdateStatus(String status); 
	public void receiverNewMeasurement(Integer value); // a new value
	public void receiverUpdateCurrentValue(Integer value);
	public void receiverRawData(DataDirection dir, String value);
}
