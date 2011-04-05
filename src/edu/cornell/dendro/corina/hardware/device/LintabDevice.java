package edu.cornell.dendro.corina.hardware.device;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.SerialSampleIOEvent;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.BaudRate;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.FlowControl;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.LineFeed;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortParity;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortState;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.StopBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.UnitMultiplier;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

/**
 * The LINTAB platform is made by RINNTECH.  The original platform uses a protocol
 * that RINNTECH claim is proprietary.  An agreement was made whereby RINNTECH 
 * would produce and supply new boxes that would attach to LINTAB platforms that 
 * communicate with a non-proprietary ASCII-based protocol. Users must purchase
 * such an adapter to use LINTAB platforms with Corina (or any other software 
 * other than TSAP-Win). 
 * 
 * These new boxes include a serial and USB connection.  The USB connection is 
 * provided by an internal USB-to-serial adapter (driver from www.ftdichip.com).
 * 
 * Both serial and virtual-serial connections use the following parameters:
 * - Baud: 1200
 * - Data bits : 8
 * - Stop bits : 1
 * - Parity : none
 * - Flow control : none
 * 
 * There is no way for the user to alter these settings.
 * 
 * Data is transmitted by LINTAB whenever the platform is moved.  The data is
 * as follows:
 * [integer position in 1/1000mm];[add button state �0� or �1�][reset button state �0� or �1�][LF]
 * 
 * LINTAB also accepts commands. To force a manual data record output the ASCII-command 
 * GETDATA should be sent to LINTAB. A reset of the counter is done by sending the 
 * ASCII-command RESET to LINTAB. After a command a linefeed (0x0A) or carriage return 
 * (0x0D) must sent to execute the command.
 *  
 * @author peterbrewer
 *
 */
public class LintabDevice extends AbstractSerialMeasuringDevice{

	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;	
	private Boolean fireOnNextValue = false;
	
	/** serial NUMBER of the last data point... */
	private int lastSerial = -1;
	
	@Override
	public void setDefaultPortParams()
	{
		baudRate = BaudRate.B_1200;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.NONE;
		unitMultiplier = UnitMultiplier.ZERO;
	}
	
	@Override
	public String toString() {
		return "LINTAB 6 with ASCII adapter";
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
	
	@Override
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
			    		fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, String.valueOf(intReadFromPort));
			    		System.out.println(intReadFromPort);
			    		
			    		//If a timeout then show bad sample
						if(intReadFromPort == -1) {
							fireSerialSampleEvent(this, SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
							return;
						}

			    		readBuffer.append((char) intReadFromPort);

			    	}

                String strReadBuffer = readBuffer.toString();
            	//Chop the three characters off the right side of the string to leave the number.
            	String strReadPosition = strReadBuffer.substring(0,(strReadBuffer.length())-3);
            	// Round up to integer of 1/1000th mm
            	Float fltValue = new Float(strReadPosition);
            	Integer intValue = Math.round(fltValue);
                
		    	//Only process the data if the add button is set and the reset button is not set.
                if( strReadBuffer.endsWith(";10") || fireOnNextValue) 
                {
                	fireOnNextValue = false;
                	fireSerialSampleEvent(this, SerialSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
                	zeroMeasurement();
                }
                else if( strReadBuffer.endsWith(";01") || strReadBuffer.endsWith(";11")) 
                {
                	zeroMeasurement();
                }
                else
                {
                	// Not recording this value just updating current value counter
                	fireSerialSampleEvent(this, SerialSampleIOEvent.UPDATED_CURRENT_VALUE_EVENT, intValue);
                }
							
			}
			catch (IOException ioe) {
				fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Error reading from serial port");

			}   	
		}
	}
	
	/**
	 * Send zero command to LINTAB 6
	 */
	@Override
	public void zeroMeasurement()
	{

		String strCommand = "RESET";
		this.sendData(strCommand);
	}
	
	/**
	 * Send request for data to LINTAB 6
	 */
	@Override
	public void requestMeasurement()
	{
		fireOnNextValue=true;
		String strCommand = "GETDATA";
		this.sendData(strCommand);
	}
	
	/**
	 * Send a command to the LINTAB 6 platform. 
	 * 
	 * @param strCommand
	 */
	private void sendData(String strCommand)
	{
		 //After a command a linefeed (0x0A) or carriage return (0x0D) must be sent to execute the command.	
		strCommand = strCommand+"\r";
		OutputStream output;

    	try {
    		
	    output = getPort().getOutputStream();
	    OutputStream outToPort=new DataOutputStream(output);
	    byte[] command = strCommand.getBytes();
	    outToPort.write(command);
	    fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, command.toString());
	    
    	}
    	catch (IOException ioe) {
			fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Error writing to serial port");
    	}	
	}

	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}

	@Override
	public Boolean isCurrentValueCapable() {
		return true;
	}

	@Override
	public Boolean isBaudEditable() {
		return false;
	}

	@Override
	public Boolean isDatabitsEditable() {
		return false;
	}

	@Override
	public Boolean isLineFeedEditable() {
		return false;
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
}