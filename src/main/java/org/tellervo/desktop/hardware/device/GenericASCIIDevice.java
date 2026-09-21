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

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.hardware.MeasuringSampleIOEvent;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.LineFeed;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

public class GenericASCIIDevice extends AbstractSerialMeasuringDevice{
	
	private final static Logger log = LoggerFactory.getLogger(GenericASCIIDevice.class);
	private static final int EVE_ENQ = 5;
	private static final int MAX_SERIAL_READ = 1024;
	private static final int MAX_SERIAL_LINE_LENGTH = 256;
	private final StringBuffer pendingSerialData = new StringBuffer();
		
	@Override
	public void setDefaultPortParams(){
		
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CRLF;
		unitMultiplier = UnitMultiplier.TIMES_1000;
		measureCumulatively = false;
	}
	
	@Override
	public String toString() {
		return "Generic ASCII Platform";
	}

	@Override
	protected void doInitialize() throws IOException {
		
		log.debug("Initalising Generic ASCII device");
		
		
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
	
	public void serialEvent(SerialPortEvent e) {
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			boolean processedLine = false;
			try {
				if(getState() == PortState.DIE) {
					return;
				}

				SerialPort serialPort = getSerialPort();
				if(serialPort == null) {
					return;
				}

				InputStream input = serialPort.getInputStream();
				int availableBytes = input.available();
				if(availableBytes <= 0) {
					return;
				}

				byte[] buffer = new byte[Math.min(availableBytes, MAX_SERIAL_READ)];
				int bytesRead = input.read(buffer, 0, buffer.length);
				if(bytesRead <= 0) {
					return;
				}

				for(int i = 0; i < bytesRead; i++) {
					int intReadFromPort = buffer[i] & 0xff;

					if(intReadFromPort == this.lineFeed.toInt()) {
						processSerialLine(pendingSerialData.toString());
						pendingSerialData.setLength(0);
						processedLine = true;
					}
					else if(intReadFromPort != 13) {
						pendingSerialData.append((char) intReadFromPort);

						if(pendingSerialData.length() > MAX_SERIAL_LINE_LENGTH) {
							pendingSerialData.setLength(0);
							fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "Serial data line too long");
						}
					}
				}
			}
			catch (Exception ioe) {
				log.error("Error reading from serial port", ioe);
				fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Error reading from serial port: " + ioe.getMessage());

			}
			finally {
				// Only zero the measurement if we're not measuring cumulatively
				if(processedLine && !measureCumulatively)
				{
					zeroMeasurement();
				}
			}
	
		}
	}

	private void processSerialLine(String strReadBuffer) {
		fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.RAW_DATA, strReadBuffer, DataDirection.RECEIVED);

		// Check units
		if(strReadBuffer.endsWith("in") || strReadBuffer.endsWith("inch") )
		{
			fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Device is transmitting values in inches.  Only millimetre units are supported in Tellervo.");
			return;
		}
		else if(strReadBuffer.endsWith("deg") || strReadBuffer.endsWith("dms"))
		{
			fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Device is transmitting values in degrees.  Only millimetre units are supported in Tellervo.");
			return;
		}
		else if (strReadBuffer.endsWith("ct"))
		{
			fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Device is transmitting values in raw counts.  See your device's manual for directions.");
			return;
		}

		// Raw data is in mm like "2.575"
		// Strip label and/or units if present
		String regex = "[\\d\\.]+";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(strReadBuffer);
		if (m.find()) {
			strReadBuffer = m.group();
		}
		else
		{
			fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Invalid value from device");
			return;
		}

		// Round up to micron integers
		Float fltValue = new Float(strReadBuffer) * unitMultiplier.toFloat();
		Integer intValue = Math.round(fltValue);

		// Handle any correction factor
		intValue = getCorrectedValue(intValue);

		// Do calculation if working in cumulative mode
		if(this.measureCumulatively)
		{
			Integer cumValue = intValue;
			intValue = intValue - getPreviousPosition();
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
	
	@Override
	protected void setLineFeed(LineFeed lf){
		
		if(lf.equals(LineFeed.NONE)) 
		{
			log.error("ASCII devices can't have line feed set to 'none'");
			return;
		}
		
		
		this.lineFeed = lf;

		
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

	@Override
	public Boolean isMeasureCumulativelyConfigurable() {
		return true;
	}

	@Override
	public Boolean isReverseMeasureCapable() {
		return false;
	}
	
	@Override
	public Boolean isCorrectionFactorEditable() {
		return false;
	}

	@Override
	public boolean doesInitialize() {
		return false;
	}

}
