/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.hardware.device;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.model.GroupsWithPermissionsTableModel;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.hardware.SerialSampleIOEvent;

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
	private Boolean fireOnNextValue = false;
	private String previousFireState = "0";
	private Boolean resetting = false;
	int resetCounter = 0;
	private final static Logger log = LoggerFactory.getLogger(LintabDevice.class);

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
		this.correctionMultiplier = 1.0;
	}
	
	@Override
	public String toString() {
		return "LINTAB with ASCII adapter";
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
			    		
			    		//System.out.println(intReadFromPort);
			    		
			    		//If a timeout then show bad sample
						if(intReadFromPort == -1) {
							fireSerialSampleEvent(this, SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
							return;
						}

			    		readBuffer.append((char) intReadFromPort);

			    	}

                String strReadBuffer = readBuffer.toString();
                fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, String.valueOf(strReadBuffer), DataDirection.RECEIVED);
               
                // Ignore "0;10" data as this is a side-effect of Lintab's hardware button
                if (strReadBuffer.equals("0;10")) return;
                
                // Ignore repeated 'fire' requests
                String thisFireState = strReadBuffer.substring(strReadBuffer.indexOf(";")+1, strReadBuffer.indexOf(";")+2);                         
                if (previousFireState.equals("1") && thisFireState.equals("1")) return;
                
                // Keep track of the state of the 'fire' button
                previousFireState = thisFireState;
                
            	//Chop the three characters off the right side of the string to leave the number.
            	String strReadPosition = strReadBuffer.substring(0,(strReadBuffer.length())-3);
                
            	// Check that Lintab has actually reset when we asked.  Sometimes if a hardware
            	// switch is used quickly, it doesn't hear the reset request
                if(resetting)
                {
                	if(!strReadPosition.equals("0"))
                	{
                		log.debug("Platform reset request ignored... retrying (attempt "+resetCounter+")");
                		zeroMeasurement();
                		return;
                	}
                	else if (resetCounter>10)
                	{
                		log.error("Lintab appears to be continually ignoring reset requests!");
                	}

                	resetRequestTrack(false);
                	
                }
                
            	// Round up to integer of 1/1000th mm
            	Float fltValue = new Float(strReadPosition);
            	Integer intValue = Math.round(fltValue);
            	
            	// Inverse if reverse measuring mode is on
            	if(this.getReverseMeasuring())
            	{
            		intValue = 0 - intValue;
            	}
            	
            	// Apply correction factor
            	intValue = (int) (intValue * this.correctionMultiplier);
            	                
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
	 * Lintab boxes sometimes ignore reset requests, so we need to ask several
	 * times to make sure it is accepted.  This function keeps track of requests, 
	 * to ensure we don't enter an infinite loop. 
	 * 
	 * @param reset
	 */
	private void resetRequestTrack(Boolean reset)
	{
		if (reset == true)
		{
			resetting = true;
			resetCounter++;
		}
		else
		{
			resetting = false;
			resetCounter = 0;
		}
	}
	
	/**
	 * Send zero command to LINTAB 6
	 */
	@Override
	public void zeroMeasurement()
	{

		String strCommand = "RESET";
		resetRequestTrack(true);
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
	    fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, strCommand, DataDirection.SENT);
	    
    	}
    	catch (IOException ioe) {
			fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Error writing to serial port", DataDirection.SENT);
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

	@Override
	public Boolean isMeasureCumulativelyConfigurable() {
		return false;
	}

	@Override
	public Boolean isReverseMeasureCapable() {
		return true;
	}
	
	@Override
	public Boolean isCorrectionFactorEditable() {
		return true;
	}
}
