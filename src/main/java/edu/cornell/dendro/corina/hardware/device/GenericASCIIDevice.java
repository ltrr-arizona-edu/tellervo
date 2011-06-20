package edu.cornell.dendro.corina.hardware.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.SerialSampleIOEvent;
import gnu.io.SerialPortEvent;

public class GenericASCIIDevice extends AbstractSerialMeasuringDevice{

	private static final int EVE_ENQ = 5;
		
	@Override
	public void setDefaultPortParams(){
		
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CRLF;
		unitMultiplier = UnitMultiplier.TIMES_1000;
	}
	
	@Override
	public String toString() {
		return "Generic ASCII Platform";
	}

	@Override
	protected void doInitialize() throws IOException {
		openPort();
		boolean waiting_for_init = true;
		int tryCount = 0;
		
		while(waiting_for_init) {
			synchronized(this) {
				if(getState() == PortState.WAITING_FOR_ACK) {
					
					if(tryCount++ == 25) {
						fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Failed to initialize reader device.");
						System.out.println("init tries exhausted; giving up.");
						break;
					}
					
					try {
						System.out.println("Initializing reader, try " + tryCount + "...");
						fireSerialSampleEvent(this, SerialSampleIOEvent.INITIALIZING_EVENT, new Integer(tryCount));
						getPort().getOutputStream().write(EVE_ENQ);
					}
					catch (IOException e) {	}
				} else {
					waiting_for_init = false;
					continue;
				}
				
				// no response yet.. wait.
				try {
					this.wait(300);
				} catch (InterruptedException e) {}						
			}					
		}				
	}
	
	public void serialEvent(SerialPortEvent e) {
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			try {
				input = getPort().getInputStream();
	    
			    StringBuffer readBuffer = new StringBuffer();
			    int intReadFromPort;
			    	//Read from port into buffer while not line feed
			    	while ((intReadFromPort=input.read()) != 10){
			    		//If a timeout then show bad sample
						if(intReadFromPort == -1) {
							fireSerialSampleEvent(this, SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
							return;

						}

						//Ignore CR (13)
			    		if(intReadFromPort!=13)  {
			    			readBuffer.append((char) intReadFromPort);
			    		}
			    	}

                String strReadBuffer = readBuffer.toString();
                fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, strReadBuffer, DataDirection.RECEIVED);
 	
		    	// Raw data is in mm like "2.575"
                // Strip label or units if present
				String regex = "[0-9.]+";
				Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(strReadBuffer);
				if (m.find()) {
					strReadBuffer = m.group();
				}
				else
				{
					fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Invalid value from device");

				}
                
                
             	// Round up to micron integers
		    	Float fltValue = new Float(strReadBuffer) * unitMultiplier.toFloat();
		    	Integer intValue = Math.round(fltValue);
		    	
		    	if(intValue>0)
		    	{	    	
			    	// Fire event
			    	fireSerialSampleEvent(this, SerialSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    	}
		    	else
		    	{
		    		// Fire bad event as value is a negative number
		    		fireSerialSampleEvent(this, SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
		    	}
			    							

			}
			catch (Exception ioe) {
				fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Error reading from serial port");

			}   	
			    	
			zeroMeasurement();
	
		}
	}
	
	@Override
	public void zeroMeasurement()
	{
		return;
	}

	@Override
	public Boolean isRequestDataCapable() {
		return false;
	}

	@Override
	public void requestMeasurement() {
		return;
		
	}
	
	protected void sendRequest(String strCommand)
	{
		return;
	}

	@Override
	public Boolean isCurrentValueCapable() {
		return false;
	}

	@Override
	public Boolean isBaudEditable() {
		return true;
	}

	@Override
	public Boolean isDatabitsEditable() {
		return true;
	}

	@Override
	public Boolean isLineFeedEditable() {
		return true;
	}

	@Override
	public Boolean isParityEditable() {
		return true;
	}

	@Override
	public Boolean isStopbitsEditable() {
		return true;
	}
	
	@Override
	public Boolean isFlowControlEditable(){
		return true;
	}

	@Override
	public Boolean isUnitsEditable() {
		return true;
	}

}