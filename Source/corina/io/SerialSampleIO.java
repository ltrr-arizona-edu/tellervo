package corina.io;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

import corina.prefs.Prefs;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import gnu.io.*;

/*
 * Provides an abstract interface to the "EveIO" Corina external ring counter thingy.
 * 
 */

public class SerialSampleIO implements SerialPortEventListener {
	
	// not used yet, since this is borked
	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	
	// the actual serial port
	private SerialPort dataPort;
	// serial NUMBER of the last data point...
	private int lastSerial = -1;
	
	// the listeners that need to be notified when a SerialSampleIOEvent is triggered...
	private Set listeners = new HashSet();

	// tie us to the port...
	public SerialSampleIO(String portName) throws IOException {
		dataPort = openPort(portName);
	}
	
	// on shutdown, make sure we closed the port.
	protected void finalize() throws Throwable {
		super.finalize();
		if(dataPort != null)
			dataPort.close();
	}

	// clean up!
	public void close() {
		dataPort.close();
		dataPort = null;
	}
	
	public void initialize() throws IOException {
		OutputStream output = dataPort.getOutputStream();

		// uh.. nothing yet. this is broken?
	}
	
	public void serialEvent(SerialPortEvent e) {
		if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			InputStream input;
			
			try {
				input = dataPort.getInputStream();
			}
			catch (IOException ioe) {
				// uh.. ?
				System.out.println("Error getting serial port input stream: " + ioe);
				return;
			}
			
			try {
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
			catch (IOException ioe) {
				System.out.println("Error reading from serial port: " + ioe);
			}
			
		}
	}
		
	// returns TRUE if serial package is capable on this platform...
	public static boolean hasSerialCapability() {
		try {
			Class.forName("gnu.io.RXTXCommDriver");
			return true;
		}
		catch (ClassNotFoundException e) {
			// driver not installed...
			System.err.println(e.toString());
			return false;
		}
		catch (java.lang.UnsatisfiedLinkError e) {
			// native interface not installed...
			System.err.println(e.toString());
			return false;
		}
	}
	
	// return an array of Strings that contains all serial port identifiers...
	public static Vector enumeratePorts() {
		Enumeration ports = CommPortIdentifier.getPortIdentifiers();
		Vector portStrings = new Vector();
				
		while(ports.hasMoreElements()) {
			CommPortIdentifier currentPort = (CommPortIdentifier)ports.nextElement();
			
			if(currentPort.getPortType() != CommPortIdentifier.PORT_SERIAL)
				continue;
			
			portStrings.add(new String(currentPort.getName()));
		}
		
		return portStrings;
	}
	
	private SerialPort openPort(String portName) throws IOException {
		CommPort port;
		CommPortIdentifier portId;
		
		try {
			// get the port by name.
			portId = CommPortIdentifier.getPortIdentifier(portName);
			
			// take ownership...
			port = portId.open("Corina", 1000);
			
			// it's a serial port. If it's not, something's fubar.
			if(!(port instanceof SerialPort)) {
				throw new IOException("Unable to open port: Port type is unsupported.");
			}
			
			// 9600 8N1, no flow control...
			((SerialPort)port).setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			((SerialPort)port).setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			// set up our event listener
			((SerialPort)port).addEventListener(this);
			((SerialPort)port).notifyOnDataAvailable(true);
			
			// time out after 500ms when reading...
			((SerialPort)port).enableReceiveTimeout(500);
		}
		catch (NoSuchPortException e) {
			throw new IOException("Unable to open port: it does not exist!");
		}
		catch (PortInUseException e) {
			throw new IOException("Unable to open port: it is in use by another application.");
		}
		catch (UnsupportedCommOperationException e) {
			// something is broken??
			throw new IOException("Unable to open port: unknown error 1. help me.");
		}
		catch (TooManyListenersException e) {
			// uh... we just made it. and set the listener.  something is broken.
			throw new IOException("Unable to open port: unknown error 2. help me.");
		}
				
		return (SerialPort)port;
	}
	
	public void addSerialSampleIOListener(SerialSampleIOListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}

	public void removeSerialSampleIOListener(SerialSampleIOListener l) {
		listeners.remove(l);
	}
	
	private void fireSerialSampleEvent(int type, Object value) {
		// alert all listeners
		SerialSampleIOListener[] l;
		synchronized (listeners) {
			l = (SerialSampleIOListener[]) listeners.toArray(
					new SerialSampleIOListener[listeners.size()]);
		}

		int size = l.length;

		if (size == 0)
			return;

		SerialSampleIOEvent e = new SerialSampleIOEvent(SerialSampleIO.class,
				type, value);

		for (int i = 0; i < size; i++) {
			l[i].SerialSampleIONotify(e);
		}
	}

	/*
	public static void main(String[] args) {
		SerialSampleIO eio;
		
		//SerialSampleIO.enumPorts();
		
		try {
			eio = new SerialSampleIO("COM4");
			eio.initialize();
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
		
	}
	*/

}
