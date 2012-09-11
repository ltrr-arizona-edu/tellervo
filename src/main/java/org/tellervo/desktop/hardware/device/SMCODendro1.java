package org.tellervo.desktop.hardware.device;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Build;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.MeasuringSampleIOEvent;
import org.tellervo.desktop.ui.I18n;
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
		return true;
	}

	
	@Override
	protected void doInitialize() throws IOException {
		
		log.debug("Initalising SMCO Dendro 1 device");
		openPort();
		fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.INITIALIZED_EVENT, null);
		log.debug("Starting live poller");
		
	}
	
	@Override
	public boolean doesInitialize() {
		return true;
	}
	
	@Override
	protected void finalize() throws Throwable 
	{
		sendMessage(I18n.getText("preferences.hardware.disconnected"));
		super.finalize();
	}
	
	@Override
	public SerialPort openPort(String portName) throws IOException {
		
		SerialPort port = super.openPort(portName);
		String version = Build.getVersion();
		version = version.replace("\u03B2", "b");
		sendMessage("Tellervo v"+version);
		

		
		return port;
	}
	
	/**
	 * Send a message to be displayed on the device screen.  Value is 
	 * truncated to 16 chars, otherwise it is centered.
	 * 
	 * @param message
	 */
	private void sendMessage(String message)
	{		
		if(message.length()>16)
		{
			log.debug("Message length = "+message.length()+" so truncating");
			message = message.substring(0, 15);
		}
		else if(message.length()<16)
		{
			Integer spacesRequired = 16-message.length();
			message = StringUtils.leftPad(message, 16-(spacesRequired/2));
			message = StringUtils.rightPad(message,16);
		}
		
		sendRequest("<e"+message+">");
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

	@Override
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
		    	
		    	// Handle any correction factor
		    	intValue = getCorrectedValue(intValue);
		    	
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
		    	
		    	//log.debug("Processed int value = "+intValue);

		    	if(intValue>0)
		    	{	    	
			    	// Fire event
			    	fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.NEW_SAMPLE_EVENT, intValue);
		    	}
		    	else if (intValue==0)
		    	{
		    		// Fire bad event as value is a negative number
		    		fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "Value was zero so it has been ignored");
		    	}
		    	else
		    	{
		    		// Fire bad event as value is a negative number
		    		fireMeasuringSampleEvent(this, MeasuringSampleIOEvent.BAD_SAMPLE_EVENT, "Value was negative so it has been ignored");
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

