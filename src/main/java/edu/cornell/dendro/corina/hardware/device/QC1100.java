package edu.cornell.dendro.corina.hardware.device;

import java.io.DataOutputStream;
import java.io.OutputStream;

import edu.cornell.dendro.corina.hardware.SerialSampleIOEvent;



public class QC1100 extends GenericASCIIDevice {

	@Override
	public String toString() {
		return "Metronics Quick-Chek 1100";
	}

	@Override
	public void setDefaultPortParams(){
		
		//MeasureJ2X defaults to using 2 stop bits but Corina/Java/something bombs if you 
		//try to write to the port with 2 stop bits set.  So lets stick with 1 stop bit for now!
		
		baudRate = BaudRate.B_600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_2;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CR;
		unitMultiplier = UnitMultiplier.TIMES_1000;
	}
	

	@Override
	public Boolean isBaudEditable() {
		return true;
	}

	@Override
	public Boolean isDatabitsEditable() {
		return false;
	}

	@Override
	public Boolean isLineFeedEditable() {
		return true;
	}

	@Override
	public Boolean isParityEditable() {
		return false;
	}

	@Override
	public Boolean isStopbitsEditable() {
		return false;
	}
	
	@Override
	public Boolean isFlowControlEditable(){
		return false;
	}

	@Override
	public Boolean isUnitsEditable() {
		return false;
	}

	@Override
	protected void sendRequest(String strCommand)
	{
		OutputStream output;


    	try {
    		
	    output = getPort().getOutputStream();
	    OutputStream outToPort=new DataOutputStream(output); 
	    
	    byte[] command = (strCommand+lineFeed.toCommandString()).getBytes();
	    outToPort.write(command);
        fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, strCommand, DataDirection.SENT);

	    
    	}
    	catch (Exception ioe) {
			fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Error sending command to serial port");

    	}	
	}
	
	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}
	
	/**
	 * Send zero command to Quadra-check QC10
	 */
	@Override
	public void zeroMeasurement()
	{
		String strZeroDataCommand = "@3";
		sendRequest(strZeroDataCommand);
	}
	
	@Override
	public void requestMeasurement() {
		String strZeroDataCommand = "@16";
		sendRequest(strZeroDataCommand);
		
	}
}
