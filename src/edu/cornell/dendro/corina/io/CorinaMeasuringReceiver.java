package edu.cornell.dendro.corina.io;

public interface CorinaMeasuringReceiver {
	public void receiverUpdateStatus(String status); 
	public void receiverNewMeasurement(Integer value); // a new value
}
