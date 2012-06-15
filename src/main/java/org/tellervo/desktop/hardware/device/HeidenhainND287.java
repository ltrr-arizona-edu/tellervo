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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.hardware.MeasuringSampleIOEvent;




public class HeidenhainND287 extends AbstractSerialMeasuringDevice {
	private final static Logger log = LoggerFactory.getLogger(HeidenhainND287.class);
	private static final int EVE_ENQ = 5;

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
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.EVEN;
		flowControl = FlowControl.RTSCTS_IN;
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
		return false;
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
		
		//log.debug("Received serial port event (type "+e.getEventType()+")");
		
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			try {
				input = getSerialPort().getInputStream();
	    
			    StringBuffer readBuffer = new StringBuffer();
			    int intReadFromPort;
			    int charcount = 0;
			    int errcount = 0;
			    int exitcounter = 0;
		    	//Read from port into buffer while not LF or CR
		    	while (exitcounter==0)
		    	{
		    		
		    		/*if(errcount>20)
		    		{
						fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "Port timed out");
						return;
		    		}*/
		    		
		    		charcount++;
		    		intReadFromPort=input.read();
		    		//log.debug("Raw int value (#"+charcount+") from port: "+intReadFromPort+ " = " + (char) intReadFromPort);

					//Ignore CR (13), LF (10), SP (32) and STX (2)
		    		if(intReadFromPort==10)  
		    		{
		    			//log.debug("Reached LF - breaking");
		    			break;
		    		}
		    		else if (intReadFromPort==21)
		    		{
		    			//log.debug("NAK received - breaking");
						fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "NAK");
						return;		    			
		    		
		    		}
		    		else if (intReadFromPort==13)
		    		{
		    			//log.debug("Reached CR - breaking");
		    			exitcounter++;
		    		}
		    		else if (intReadFromPort==32)
		    		{
		    			//log.debug("Ignoring SP");
		    		}
		    		else if (intReadFromPort==2)
		    		{
		    			log.debug("Ignoring STX");
		    		}
		    		else if (intReadFromPort==-1)
		    		{
		    			errcount++;
		    			exitcounter++;
		    			//log.debug("Port timeout - "+errcount);
		    		}
		    		else if (intReadFromPort<43 && intReadFromPort>57)
		    		{
		    			//log.debug("Ignoring non-digit value ("+intReadFromPort+")= " + (char) intReadFromPort);
		    		}
		    		else
		    		{
		    			readBuffer.append((char) intReadFromPort);
		    		}
		    	}

			    
			    	
                String strReadBuffer = readBuffer.toString().trim();
                
                // Skip if line is empty
                if(strReadBuffer==null || !(strReadBuffer.length()>0))
                {
                	return;
                }
                
                
                //log.debug("Port read complete. Value = "+strReadBuffer);
                
                fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.RAW_DATA, strReadBuffer, DataDirection.RECEIVED);
 	

             	// Round up to micron integers
		    	Float fltValue = new Float(strReadBuffer) * unitMultiplier.toFloat();
		    	//log.debug("Float value = "+fltValue);
		    	Integer intValue = Math.round(fltValue);
		    	//log.debug("Integer value = "+intValue);
		    	
		    	// Do calculation if working in cumulative mode
		    	if(this.measureCumulatively)
		    	{
		    		//log.debug("Measuring cumulatively so doing calcs");
		    		Integer cumValue = intValue;
		    		intValue = intValue - getPreviousPosition();
		    		//log.debug("New integer value = "+intValue);
		    		setPreviousPosition(cumValue);
		    	}
		    	
    	
		    	// Fire event
	    		//log.debug("Firing SerialSampleIOEvent with value = "+intValue);
		    	fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    
	
			    							

			}
			catch (Exception e2) {
				log.error("Exception caught during serialEvent");
				log.error("Message: "+e2.getMessage());
				
				//log.debug("Stacktrace", e2.getStackTrace());
				
				fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Error reading from serial port");

			}  
			 
			// Only zero the measurement if we're not measuring cumulatively
			if(!measureCumulatively)
			{
				zeroMeasurement();
			}
	
		}
	}
	
	protected void doInitialize() throws IOException {
		
		log.debug("Initalising Heidenhain");
		
		
		openPort();
		boolean waiting_for_init = true;
		int tryCount = 0;
		
		while(waiting_for_init) {
			synchronized(this) {
				if(getState() == PortState.WAITING_FOR_ACK) {
					
					if(tryCount++ == 25) {
						log.debug("Platform init tries exhausted; giving up.");
						fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Failed to initialize reader device.");
						break;
					}
					
					try {
						log.debug("Initializing reader, try " + tryCount + "...");
						fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.INITIALIZING_EVENT, new Integer(tryCount));
						getSerialPort().getOutputStream().write(EVE_ENQ);
					}
					catch (IOException e) {	}
				} else {
					log.debug("Platform init complete.");
					waiting_for_init = false;
					continue;
				}
				
				// no response yet.. wait.
				try {
					log.debug("No response yet from device.  Waiting...");
					this.wait(300);
				} catch (InterruptedException e) {}						
			}					
		}				
	}
	

	protected void sendRequest(String strCommand)
	{
		//log.debug("Sending command to Heidenhain device: "+strCommand);		
		
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
	public void zeroMeasurement()
	{
		//log.debug("Sending request to zero device");
		//sendRequest("F0001");
		sendRequest("\u001b"+"T0000"+"\r"); // 0
		try {
			synchronized(this)
			{
				this.wait(500);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendRequest("\u001b"+"T0104"+"\r"); // <enter>
		
		// <ESC>S0000<CR>  - reset position display
		setPreviousPosition(0);
	}
	
	public void reboot()
	{
		byte[] barr = new byte[] {27, 83, 48, 48, 48, 48, 13};
		String value = new String(barr);
		sendRequest(value);
	}
	
	@Override
	public void requestMeasurement() {
		//log.debug("Sending request for measurement");
	     
		sendRequest("\u001b"+"F0002"+"\r"); 
		try {
			synchronized(this)
			{
				this.wait(500);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}

	@Override
	public Boolean isCorrectionFactorEditable() {
		return false;
	}

	@Override
	public Boolean isReverseMeasureCapable() {
		
		return false;
	}


	@Override
	public Boolean isCurrentValueCapable() {
		
		return false;
	}
}
