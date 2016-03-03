/*******************************************************************************
 * Copyright (C) 2016 Peter Brewer
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.hardware.MeasuringSampleIOEvent;

public class MitutoyoDMX1 extends GenericASCIIDevice {
	protected final static Logger log = LoggerFactory.getLogger(MitutoyoDMX1.class);

	@Override
	public String toString() {
		return "Mitutoyo DMX-1";
	}

	@Override
	public void setDefaultPortParams(){
		
		//MeasureJ2X defaults to using 2 stop bits but Tellervo/Java/something bombs if you 
		//try to write to the port with 2 stop bits set.  So lets stick with 1 stop bit for now!
		
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CR;
		unitMultiplier = UnitMultiplier.TIMES_1000;
		measureCumulatively = true;
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
	protected void sendRequest(String strCommand)
	{
		OutputStream output;


    	try {
    		
	    output = getSerialPort().getOutputStream();
	    OutputStream outToPort=new DataOutputStream(output); 
	    
	    byte[] command = (strCommand+lineFeed.toCommandString()).getBytes();
	    outToPort.write(command);
        fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.RAW_DATA, strCommand, DataDirection.SENT);

	    
    	}
    	catch (Exception ioe) {
			fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Error sending command to serial port");

    	}	
	}
	
	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}
	
	@Override
	public Boolean isReverseMeasureCapable() {
		return true;
	}
	
	@Override
	public void zeroMeasurement()
	{
		// Not supported
		log.debug("Requesting device to zero is not supported");
		//String cmd = "#3";
		//sendRequest(cmd);
	}
	
	@Override
	public void requestMeasurement() {
		String cmd = "1";
		sendRequest(cmd);
		
	}
	
	@Override
	public void serialEvent(SerialPortEvent e) {
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			//try {
			 String strReadBuffer = null;
				try {
					input = getSerialPort().getInputStream();
			
	    
			    StringBuffer readBuffer = new StringBuffer();
			    int intReadFromPort;
			    	//Read from port into buffer while not line feed
			    	while ((intReadFromPort=input.read()) != this.lineFeed.toInt()){
			    		//If a timeout then show bad sample
						if(intReadFromPort == -1) {
							fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "Port timeout or line feed problems.  Check baud and line feed settings.");
							return;

						}

						//Ignore CR (13) and 'a'
			    		if(intReadFromPort!=13 && intReadFromPort!=97)  {
			    			readBuffer.append((char) intReadFromPort);
			    		}
			    		

			    	}

                 strReadBuffer = readBuffer.toString();
                fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.RAW_DATA, strReadBuffer, DataDirection.RECEIVED);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                log.debug("Raw string from device ="+strReadBuffer);
                
                if(strReadBuffer.trim().length()==0)
                {
                	log.debug("Empty line - ignored");
                	return;
                }

		    	// Raw data looks like: 01A+00036.26
                if(strReadBuffer.length()!=12)
                {
                	log.debug("Data length = "+strReadBuffer.length());
                	log.error("Expecting 12 characters");
                	return;
                }
                
                String sign = strReadBuffer.substring(3, 4);
                String value = strReadBuffer.substring(4, 12);

                if(sign.equals("+") || sign.equals("-"))
                {
                	
                }
                else
                {
                	log.error("Unexpected sign character");
                }
                
                
             	// Round up to micron integers
		    	Float fltValue = new Float(value) * unitMultiplier.toFloat();
		    	Integer intValue = Math.round(fltValue);
		    	
		    	
		    	log.debug("Measurement value from device = "+value);
		    	log.debug("Float value = "+fltValue);
		    	log.debug("Int value = "+intValue);
				
		    	// Handle any correction factor
		    	intValue = getCorrectedValue(intValue);
		    	
		    	log.debug("Corrected int value = "+intValue);
		    	
		    	// Do calculation if working in cumulative mode
		    	if(this.measureCumulatively)
		    	{
		    		Integer cumValue = intValue;
		    		intValue = intValue - getPreviousPosition();
		    		
		    		log.debug("Cummulative measurement : "+cumValue);
		    		log.debug("Previous position : "+getPreviousPosition());
		    		log.debug("Atomic measurement : "+intValue);
		    		
		    		setPreviousPosition(cumValue);
		    	}
		    			    	
		    	if(intValue>0)
		    	{	    	
			    	// Fire event
			    	fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    	}
		    	else
		    	{
		    		// Fire bad event as value is a negative number
		    		fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "Negative value = "+intValue);
		    	}
		}
	}
}
