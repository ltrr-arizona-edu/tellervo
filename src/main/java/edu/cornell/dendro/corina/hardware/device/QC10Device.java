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
package edu.cornell.dendro.corina.hardware.device;

import java.io.DataOutputStream;
import java.io.OutputStream;

import edu.cornell.dendro.corina.hardware.SerialSampleIOEvent;



public class QC10Device extends GenericASCIIDevice {

	@Override
	public String toString() {
		return "Metronics Quadra-Chek 10";
	}

	@Override
	public void setDefaultPortParams(){
		
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_2;
		parity = PortParity.NONE;
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
