package edu.cornell.dendro.corina.io;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.SerialPortEvent;

public abstract class StringBasedMeasuringDevice 
	extends AbstractSerialMeasuringDevice {

	/** The current value, in a string... */
	private StringBuffer currentValue = new StringBuffer();
	

	public StringBasedMeasuringDevice() {
		super();
	}

	public StringBasedMeasuringDevice(String portName) throws IOException {
		super(portName);
	}

	/**
	 * @return a string that terminates a value
	 */
	protected String getValueTerminator() {
		return "\r\n";
	}
	
	@Override
	protected boolean doesInitialization() {
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.io.AbstractSerialMeasuringDevice#getMeasuringDeviceName()
	 */
	@Override
	public String getMeasuringDeviceName() {
		return "Abstract text thingy";
	}

	/* (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 */
	public void serialEvent(SerialPortEvent e) {
		if(getState() != PortState.NORMAL)
			return;
		
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			try {
				input = getPort().getInputStream();
			}
			catch (IOException ioe) {
				// uh.. ?
				System.out.println("Error getting serial port input stream: " + ioe);
				return;
			}

			try {
				int ch;
				String valueTerminator = getValueTerminator();

				// Sanity check, so we don't eat memory
				if(currentValue.length() > 1024) {
					System.out.println("Invalid values in value: " + currentValue.toString());
					currentValue.setLength(0);
				}
				
				while((ch = input.read()) != -1) {
					currentValue.append((char) ch);
					
					if(currentValue.toString().endsWith(valueTerminator)) {
						// truncate off the value terminator
						currentValue.setLength(currentValue.length() - valueTerminator.length());
						
						System.out.println("Got a value: " + currentValue);
						fireSerialSampleEvent(SerialSampleIOEvent.NEW_SAMPLE_EVENT, new Integer(currentValue.toString()));					
						
						// clear the string!
						currentValue.setLength(0);
					}
				}
			}
			catch (IOException ioe) {
				System.out.println("Error reading from serial port: " + ioe);
			}
		}
	}
	
}
