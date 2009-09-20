package edu.cornell.dendro.corina.io;

import gnu.io.SerialPortEvent;

import java.io.IOException;
import java.io.InputStream;

public class EVESerialMeasuringDevice extends AbstractSerialMeasuringDevice {

	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	//private static final int EVE_NAK = 7;
	
	/** serial NUMBER of the last data point... */
	private int lastSerial = -1;
	
	public EVESerialMeasuringDevice(String portName) throws IOException {
		super(portName);
	}

	public EVESerialMeasuringDevice() {
	}

	@Override
	protected boolean doesInitialization() {
		return true;
	}

	@Override
	public String getMeasuringDeviceName() {
		return "EVE IO device";
	}

	@Override
	protected void doInitialize() {
		boolean waiting_for_init = true;
		int tryCount = 0;
		
		while(waiting_for_init) {
			synchronized(this) {
				if(getState() == PortState.WAITING_FOR_ACK) {
					
					if(tryCount++ == 25) {
						fireSerialSampleEvent(SerialSampleIOEvent.ERROR, "Failed to initialize reader device.");
						System.out.println("init tries exhausted; giving up.");
						break;
					}
					
					try {
						System.out.println("Initializing reader, try " + tryCount + "...");
						fireSerialSampleEvent(SerialSampleIOEvent.INITIALIZING_EVENT, new Integer(tryCount));
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
	
	public void serialEvent(SerialPortEvent e) {
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
				switch(getState())
				{
				case WAITING_FOR_ACK: {
					int val = input.read();
					
					if(val == EVE_ACK) {
						System.out.println("Received ACK from device, leaving initialize mode");
						
						// update our status...
						synchronized(this) {
							setState(PortState.POST_INIT);
							
							// tell our other thread
							this.notify();							
						}
												
						// wait for it to die..
						finishInitialize();
						
						// dump any input we have...
						while(input.read() != -1);
						
						fireSerialSampleEvent(SerialSampleIOEvent.INITIALIZED_EVENT, null);
					}
					else {
						System.out.println("Received " + val + "while waiting for ACK");
					}
				}
				break;
					
				case NORMAL: {
					int counter, valuehi, valuelo, value;
					
					// if any of these are -1, we timed out. 
					// something was most likely invalid in the send/serial link...
					// don't worry, we still have to ACK everything.
					if(((counter = input.read()) == -1) ||
							((valuehi = input.read()) == -1) ||
							((valuelo = input.read()) == -1)) {
						fireSerialSampleEvent(SerialSampleIOEvent.BAD_SAMPLE_EVENT, null);
						return;
					}
					
					// this is a duplicate packet. ignore it!
					if(counter == lastSerial)
						return;
					
					lastSerial = counter;
					value = (256 * valuehi) + valuelo;
					
					fireSerialSampleEvent(SerialSampleIOEvent.NEW_SAMPLE_EVENT, new Integer(value));					
				}
				break;
				
				default:
					break;					
				}
			}
			catch (IOException ioe) {
				System.out.println("Error reading from serial port: " + ioe);
			}
			
		}		
	}
}
