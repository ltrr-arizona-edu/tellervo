package org.tellervo.desktop.hardware.device;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.hardware.MeasuringSampleIOEvent;
import org.tridas.io.gui.App;
import org.tridas.io.util.StringUtils;

/**
 * The SMCO Dendro 1 device was designed by Paweł Piątek (ppi@agh.edu.pl).  It is a  
 * standard USB/Serial device (using the FTDI FT232C chip) which is supported by 
 * standard device drivers in Linux.  Windows drivers are available for download.
 * 
 * @author pwb48
 *
 */
public class SMCODendro1 extends GenericASCIIDevice {

	private final static Logger log = LoggerFactory.getLogger(SMCODendro1.class);
	private static final int EVE_ENQ = 5;
		
	@Override
	public void setDefaultPortParams(){
		
		baudRate = BaudRate.B_57600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.NONE;
		unitMultiplier = UnitMultiplier.TIMES_1;
		measureCumulatively = false;
	}

	@Override
	public String toString() {
		return "SMCO Dendro 1";
	}
	

	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}



	@Override
	public Boolean isCurrentValueCapable() {
		return false;
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
		return true;
	}

	@Override
	public Boolean isReverseMeasureCapable() {
		return true;
	}
	
	@Override
	public Boolean isCorrectionFactorEditable() {
		return false;
	}

	
	@Override
	protected void doInitialize() throws IOException {
		
		log.debug("Initalising SMCO Dendro 1 device");
		
		openPort();
				
	}
	
	@Override
	public boolean doesInitialize() {
		return true;
	}
	
	protected void finalize() throws Throwable 
	{
		log.debug("FINALIZING");
		String cmd = "<e  disconnected  >";
		sendRequest(cmd);
		super.finalize();
	}
	
	
	public SerialPort openPort(String portName) throws IOException {
		
		SerialPort port = super.openPort(portName);
		
		sendMessage("Tellervo v"+App.getBuildVersion());
		
		return port;
	}
	
	private void sendMessage(String message)
	{
		if(message.length()>16)
		{
			message.substring(0, 15);
		}
		else if(message.length()<16)
		{
			Integer spacesRequired = 16-message.length();
			
			if((spacesRequired % 2) == 0)
			{
				message = StringUtils.leftPad(message, spacesRequired/2);
				message = StringUtils.rightPad(message, spacesRequired/2);
			}
			else
			{
				message = StringUtils.leftPad(message, (spacesRequired+1/2));
				message = StringUtils.rightPad(message,(spacesRequired-1/2));
			}
		}
		
		String cmd ="<e"+message+">";
		sendRequest(cmd);
	}
	
	/**
	 * Send zero command 
	 */
	@Override
	public void zeroMeasurement()
	{
		String strZeroDataCommand = "<d>";
		sendRequest(strZeroDataCommand);
		setPreviousPosition(0);
	}

	@Override
	public void requestMeasurement() {
		String cmd = "<c>";
		sendRequest(cmd);
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

	public void serialEvent(SerialPortEvent e) {
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			try {
				input = getSerialPort().getInputStream();
	    
			    StringBuffer readBuffer = new StringBuffer();
			    int intReadFromPort;
			    	//Read from port into buffer while not ">" character
			    	while ((intReadFromPort=input.read()) != 62){
			    		//If a timeout then show bad sample
						/*if(intReadFromPort == -1) {
							fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, null);
							return;

						}*/

						//Ignore CR (13)
			    		if(intReadFromPort!=13 && intReadFromPort!=10)  {
			    			readBuffer.append((char) intReadFromPort);
			    		}
			    	}

                String strReadBuffer = readBuffer.toString();
                fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.RAW_DATA, strReadBuffer+">", DataDirection.RECEIVED);              

                if(strReadBuffer.startsWith("<c"))
                {
                	strReadBuffer = strReadBuffer.substring(2);
                }
                else
                {
                	// Ignore all other responses
                	return;
                }
                        
             	// Convert to micron integers
		    	Float fltValue = new Float(strReadBuffer) * unitMultiplier.toFloat();
		    	Integer intValue = Math.round(fltValue);
		    	
            	// Inverse if reverse measuring mode is on
            	if(getReverseMeasuring())
            	{
            		intValue = 0 - intValue;
            	}
		    	
		    	// Do calculation if working in cumulative mode
		    	if(this.measureCumulatively)
		    	{
		    		Integer cumValue = intValue;
		    		intValue = intValue - getPreviousPosition();
		    		setPreviousPosition(cumValue);
		    	}
		    	
		    	log.debug("Processed int value = "+intValue);

		    	if(intValue>0)
		    	{	    	
			    	// Fire event
			    	fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    	}
		    	else
		    	{
		    		// Fire bad event as value is a negative number
		    		fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, null);
		    	}
			    							

			}
			catch (Exception ioe) {
				fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.ERROR, "Error reading from serial port");

			}   	
			 
			// Only zero the measurement if we're not measuring cumulatively
			if(!measureCumulatively)
			{
				zeroMeasurement();
			}
	
		}
	}
	
	
}

