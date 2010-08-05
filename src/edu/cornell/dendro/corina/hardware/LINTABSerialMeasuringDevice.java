package edu.cornell.dendro.corina.hardware;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

public class LINTABSerialMeasuringDevice extends AbstractSerialMeasuringDevice{

	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	//private static final int EVE_NAK = 7;
	
	/** serial NUMBER of the last data point... */
	private int lastSerial = -1;
	
	public LINTABSerialMeasuringDevice(String portName) throws IOException {
		super(portName);

		setBaudRate(1200);
		//setDataBits(SerialPort.DATABITS_8);
		//setStopBits(SerialPort.STOPBITS_1);
		//setParity(SerialPort.PARITY_NONE);
		//setFlowControl(SerialPort.FLOWCONTROL_NONE);

	}

	public LINTABSerialMeasuringDevice() {
	}

	@Override
	protected boolean doesInitialization() {
		return true;
	}

	@Override
	public String getMeasuringDeviceName() {
		return "LINTAB 6 IO device";
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
			    	/*LINTAB data appears in the following format:
					 *[integer position in 1/1000mm];[add button state ‘0’ or ’1’][reset button state ‘0’ or ‘1’][LF]
					 *It should look like "140;10" or "46;10"with a LF. 
					 *With every change of the LINTAB table state (move table, button press, button release) a
					 *new data record is sent with a line feed (0x0A) at the end of the line.
					 *This means that a lot of the data needs to be ignored.
			    	 */
			    	//Read from port into buffer while not LF (10)
			    	while ((intReadFromPort=input.read()) != 10){
			    		//If a timeout then show bad sample
						if(intReadFromPort == -1) {
							fireSerialSampleEvent(SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
							return;
						}
//						//Ignore CR (13)
//			    		if(intReadFromPort!=13)  {
//			    			readBuffer.append((char) intReadFromPort);
//			    		}
			    	}

                String strReadBuffer = readBuffer.toString();
		    	//Only process the data if the add button is set and the reset button is not set.
                if( strReadBuffer.endsWith(";10")) {
                	// Raw data is in 1/1000th mm like "140" or "46" to the left of semicolon. 
                	/*TODO Investigate changing this to allow for 1/1000th resolution
                	 *Peter had mentioned that Corina can now handle 1/1000th
                	 *Maybe this could be dynamic here? and even read what the device is set too?
                	 */
                	
                	//Chop the three characters of ";10" off the right side of the string to leave the number.
                	String strReadPosition = strReadBuffer.substring(0,(strReadBuffer.length())-3);
                	// Round up to integer of 1/1000th mm
                	Float fltValue = new Float(strReadPosition)*1000;
                	Integer intValue = Math.round(fltValue);
		    	
                	// Fire event
                	fireSerialSampleEvent(SerialSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
                	}					
							
			}
			catch (IOException ioe) {
					System.out.println("Error reading from or writing to serial port: " + ioe);
			}   	
			    	
			zeroLINTAB();
	
		}
	}
	
	/**
	 * Send zero command to LINTAB 6
	 */
	private void zeroLINTAB()
	{
		OutputStream output;

    	/*zero the data
    	 *A reset of the counter is done by sending the ASCII-command RESET to the LINTAB 6.
		 *After a command a linefeed (0x0A) or carriage return (0x0D) must be sent to execute the command.
    	 */
    	try {
    		
	    output = getPort().getOutputStream();
	    OutputStream outToPort=new DataOutputStream(output);
	    // "RESET" followed by a carriage return
	    String strZeroDataCommand = "RESET\r";
	    byte[] command = strZeroDataCommand.getBytes();
	    outToPort.write(command);
	    
    	}
    	catch (IOException ioe) {
			System.out.println("Error writing to serial port: " + ioe);
    	}	
	}
}