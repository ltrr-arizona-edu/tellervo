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

import gnu.io.SerialPortEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.hardware.SerialSampleIOEvent;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice.DataDirection;




public class HeidenhainND287 extends GenericASCIIDevice {
	private final static Logger log = LoggerFactory.getLogger(HeidenhainND287.class);

	@Override
	public String toString() {
		return "Heidenhain ND 287";
	}

	@Override
	public void setDefaultPortParams(){
		
		//MeasureJ2X defaults to using 2 stop bits but Corina/Java/something bombs if you 
		//try to write to the port with 2 stop bits set.  So lets stick with 1 stop bit for now!
		log.debug("Setting Heidenhain default port parameters");
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_7;
		stopBits = StopBits.STOPBITS_2;
		parity = PortParity.EVEN;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CRLF;
		unitMultiplier = UnitMultiplier.TIMES_1000;
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
		return false;
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
		return false;
	}

	@Override
	public Boolean isMeasureCumulativelyConfigurable() {
		return true;
	}
	

	
	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}
	
	@Override
	public void serialEvent(SerialPortEvent e) {
		
		log.debug("Received serial port event (type "+e.getEventType()+")");
		
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			try {
				input = getPort().getInputStream();
	    
			    StringBuffer readBuffer = new StringBuffer();
			    int intReadFromPort;
			    int charcount = 0;
			    
		    	//Read from port into buffer while not LF or CR
		    	while (1==1)
		    	{
		    		charcount++;
		    		intReadFromPort=input.read();
		    		log.debug("Raw int value (#"+charcount+") from port: "+intReadFromPort+ " = " + (char) intReadFromPort);

					//Ignore CR (13), LF (10), SP (40) and STX (2)
		    		if(intReadFromPort==10)  
		    		{
		    			log.debug("Reached LF - breaking");
		    			break;
		    		}
		    		else if (intReadFromPort==13)
		    		{
		    			log.debug("Reached CR - breaking");
		    			break;
		    		}
		    		else if (intReadFromPort==40)
		    		{
		    			log.debug("Ignoring SP");
		    		}
		    		else if (intReadFromPort==2)
		    		{
		    			log.debug("Ignoring STX");
		    		}
		    		else if (intReadFromPort==-1)
		    		{
		    			log.debug("Port timeout -1");
						//fireSerialSampleEvent(this, SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
						//return;
		    		}
		    		else if (intReadFromPort<43 && intReadFromPort>57)
		    		{
		    			log.debug("Ignoring non-digit value ("+intReadFromPort+")= " + (char) intReadFromPort);
		    		}
		    		else
		    		{
		    			readBuffer.append((char) intReadFromPort);
		    		}
		    	}

			    
			    	
                String strReadBuffer = readBuffer.toString().trim();
                
                log.debug("Port read complete. Value = "+strReadBuffer);
                
                fireSerialSampleEvent(this, SerialSampleIOEvent.RAW_DATA, strReadBuffer, DataDirection.RECEIVED);
 	

             	// Round up to micron integers
		    	Float fltValue = new Float(strReadBuffer) * unitMultiplier.toFloat();
		    	log.debug("Float value = "+fltValue);
		    	Integer intValue = Math.round(fltValue);
		    	log.debug("Integer value = "+intValue);
		    	
		    	// Do calculation if working in cumulative mode
		    	if(this.measureCumulatively)
		    	{
		    		log.debug("Measuring cumulatively so doing calcs");
		    		Integer cumValue = intValue;
		    		intValue = intValue - getPreviousPosition();
		    		log.debug("New integer value = "+intValue);
		    		setPreviousPosition(cumValue);
		    	}
		    	
		    	if(intValue>0)
		    	{	    	
			    	// Fire event
		    		log.debug("Firing SerialSampleIOEvent with value = "+intValue);
			    	fireSerialSampleEvent(this, SerialSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    	}
		    	else
		    	{
		    		// Fire bad event as value is a negative number
		    		log.debug("Firing bad SerialSampleIOEvent because value = "+intValue);
		    		fireSerialSampleEvent(this, SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
		    	}
			    							

			}
			catch (Exception e2) {
				log.error("Exception caught during serialEvent");
				log.error("Message: "+e2.getMessage());
				log.error("Source: "+e.getSource().toString());
				//log.debug("Stacktrace", e2.getStackTrace());
				
				fireSerialSampleEvent(this, SerialSampleIOEvent.ERROR, "Error reading from serial port");

			}  
			 
			// Only zero the measurement if we're not measuring cumulatively
			if(!measureCumulatively)
			{
				zeroMeasurement();
			}
	
		}
	}
	
	@Override
	protected void doInitialize() throws IOException {
		//super.doInitialize();
		log.debug("Initalising Heidenhain ND 286 device");
		openPort();
		/*sendRequest("\u001b"+"A0000"); //Output of device identification
		sendRequest("\u001b"+"A0800"); //Output of status bar
		sendRequest("\u001b"+"A0900"); //Output of status indicators*/
	}
	
	@Override
	protected void sendRequest(String strCommand)
	{
		log.debug("Sending command to Heidenhain device: "+strCommand);		
		
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
	public void zeroMeasurement()
	{
		log.debug("Sending request to zero device");
		//sendRequest("F0001");
		//sendRequest("\u001b"+"T0000"); // 0
		//sendRequest("\u001b"+"T0104"); // <enter>
	}
	
	@Override
	public void requestMeasurement() {
		log.debug("Sending request for measurement");
		sendRequest("\u0002"); //Output of current position
		
	}
}
