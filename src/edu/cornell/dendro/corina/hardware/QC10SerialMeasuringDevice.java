package edu.cornell.dendro.corina.hardware;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

public class QC10SerialMeasuringDevice extends AbstractSerialMeasuringDevice{

	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	//private static final int EVE_NAK = 7;
	
	/** serial NUMBER of the last data point... */
	private int lastSerial = -1;
	
	public QC10SerialMeasuringDevice(String portName) throws IOException {
		super(portName);
		//MeasureJ2X defaults to using 2 stop bits but Corina/Java/something bombs if you 
		//try to write to the port with 2 stop bits set.  So lets stick with 1 stop bit for now!
		//setStopBits(SerialPort.STOPBITS_2);
		//setFlowControl(SerialPort.FLOWCONTROL_RTSCTS_OUT);
	}

	public QC10SerialMeasuringDevice() {
	}

	@Override
	protected boolean doesInitialization() {
		return true;
	}

	@Override
	public String getMeasuringDeviceName() {
		return "Quadra-Chek QC-10 IO device";
	}

	@Override
	protected void doInitialize() {
		boolean waiting_for_init = true;
		int tryCount = 0;
		
		while(waiting_for_init) {
			synchronized(this) {
				if(getState() == PortState.WAITING_FOR_ACK) {
					
					if(tryCount++ == 25) {
						fireSerialSampleEvent(SerialSampleIOEvent.ERROR, "Failed to initialize reader device.");
						System.out.println("init tries exhausted; giving up.");
						break;
					}
					
					try {
						System.out.println("Initializing reader, try " + tryCount + "...");
						fireSerialSampleEvent(SerialSampleIOEvent.INITIALIZING_EVENT, new Integer(tryCount));
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
			OutputStream output;
			
			try {
				input = getPort().getInputStream();
	    
			    StringBuffer readBuffer = new StringBuffer();
			    int intReadFromPort;
			    	//Read from port into buffer while not LF (10)
			    	while ((intReadFromPort=input.read()) != 10){
			    		//If a timeout then show bad sample
						if(intReadFromPort == -1) {
							fireSerialSampleEvent(SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
							return;

						}

						//Ignore CR (13)
			    		if(intReadFromPort!=13)  {
			    			readBuffer.append((char) intReadFromPort);
			    		}
			    	}

                String strReadBuffer = readBuffer.toString();
 	
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
					System.out.println("invalid value received from device");
				}
                
                
             	// Round up to micron integers
		    	Float fltValue = new Float(strReadBuffer)*1000;
		    	Integer intValue = Math.round(fltValue);
		    	
		    	if(intValue>0)
		    	{	    	
			    	// Fire event
			    	fireSerialSampleEvent(SerialSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    	}
		    	else
		    	{
		    		// Fire bad event as value is a negative number
		    		fireSerialSampleEvent(SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
		    	}
			    							

			}
			catch (Exception ioe) {
					System.out.println("Error reading from serial port: " + ioe.toString());
			}   	
			    	
			zeroMeasurement();
	
		}
	}
	
	/**
	 * Send zero command to Quadra-check QC10
	 */
	@Override
	public void zeroMeasurement()
	{
		String strZeroDataCommand = "@3\r\n";
		sendRequest(strZeroDataCommand);
	}

	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}

	@Override
	public void requestMeasurement() {
		String strZeroDataCommand = "@33\r\n";
		sendRequest(strZeroDataCommand);
		
	}
	
	private void sendRequest(String strCommand)
	{
		OutputStream output;


    	try {
    		
	    output = getPort().getOutputStream();
	    OutputStream outToPort=new DataOutputStream(output); 
	    
	    byte[] command = strCommand.getBytes();
	    outToPort.write(command);
	    
    	}
    	catch (Exception ioe) {
			System.out.println("Error sending zero command to serial port: " + ioe.toString());
    	}	
	}

	@Override
	public Boolean isCurrentValueCapable() {
		return false;
	}


}