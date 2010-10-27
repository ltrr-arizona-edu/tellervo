package edu.cornell.dendro.corina.hardware.device;

import java.io.IOException;
import java.io.InputStream;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.SerialSampleIOEvent;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.BaudRate;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.FlowControl;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.LineFeed;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortParity;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortState;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.StopBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.UnitMultiplier;

import gnu.io.SerialPortEvent;

/**
 * This is the implementation of the Cornell EveIO measuring device.  It is an open source device
 * the circuit diagram for which can be obtained from the Cornell lab.  
 * 
 * EveIO is a simple device with no user definable parameters.  It does not listen for
 * requests for data or 'zero' requests.  Data can only be sent via a hardware button attached
 * to the device.
 * 
 * @author peterbrewer
 *
 */
public class EVESerialMeasuringDevice extends AbstractSerialMeasuringDevice {

	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	//private static final int EVE_NAK = 7;
	
	/** serial NUMBER of the last data point... */
	private int lastSerial = -1;
	
	@Override
	public void setDefaultPortParams(){
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.NONE;
		unitMultiplier = UnitMultiplier.TIMES_10;
		
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
				fireSerialSampleEvent(SerialSampleIOEvent.ERROR, "Error getting serial port input stream");

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
					
					// Get value 
					value = ((256 * valuehi) + valuelo);
					
					// Convert from 1/100th mm to our default units = microns
					value = value * 10;
					
					fireSerialSampleEvent(SerialSampleIOEvent.NEW_SAMPLE_EVENT, new Integer(value));					
				}
				break;
				
				default:
					break;					
				}
			}
			catch (IOException ioe) {
				fireSerialSampleEvent(SerialSampleIOEvent.ERROR, "Error reading from serial port");
			}
			
		}		
	}
	
	@Override
	public void requestMeasurement() {
		System.out.println(toString()+ " does not listen for measurement requests.");
		
	}

	@Override
	public void zeroMeasurement() {
		System.out.println(toString()+ " does not listen for zero requests.");
		
	}
	
	/**
	 *   INFORMATIONAL METHODS
	 */
	
	@Override
	public String toString() {
		return "EVE IO";
	}

	@Override
	public Boolean isRequestDataCapable() {
		return false;
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
	public Boolean isUnitsEditable() {
		return false;
	}

	@Override
	public Boolean isFlowControlEditable() {
		return false;
	}
}
