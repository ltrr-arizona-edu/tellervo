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

public class SerialSampleIO 

/*{
	// for disabling this whole thing outright...
	public SerialSampleIO(String s) throws IOException {}
	public static boolean hasSerialCapability() { return false; }
	public void close() {}
	public void initialize() {}
	public static Vector enumeratePorts() { return new Vector(); }
	public void addSerialSampleIOListener(Object o) {}
	*/

implements SerialPortEventListener {
	
	// not used yet, since this is borked
	private static final int EVE_ENQ = 5;
	private static final int EVE_ACK = 6;
	private static final int EVE_NAK = 7;
	
	// the actual serial port
	private SerialPort dataPort;
	
	// the state...
	private static final int SERIALSTATE_WAITINGFORACK = 2;
	private static final int SERIALSTATE_NORMAL = 1;
	private static final int SERIALSTATE_POSTINIT = 4;
	private static final int SERIALSTATE_DIE = 3;
	
	private int serialState;
	
	// serial NUMBER of the last data point...
	private int lastSerial = -1;
	
	// the listeners that need to be notified when a SerialSampleIOEvent is triggered...
	private Set listeners = new HashSet();

	// tie us to the port...
	public SerialSampleIO(String portName) throws IOException {
		
		System.out.println("Opening port: " + portName);
		
		dataPort = openPort(portName);
		serialState = SERIALSTATE_NORMAL;
	}
	
	// on shutdown, make sure we closed the port.
	protected void finalize() throws Throwable {
		super.finalize();
		
		serialState = SERIALSTATE_DIE;
		
		if(dataPort != null) {
			System.out.println("Closing port (finalize): " + dataPort.getName());
			dataPort.close();
			dataPort = null;
		}
	}

	// clean up!
	public void close() {
		System.out.println("Closing port (manual): " + dataPort.getName());
		
		serialState = SERIALSTATE_DIE;
		if(initThread != null) {
			try {
				initThread.join();
			} catch (InterruptedException e) {} 
			initThread = null;
		}
		
		dataPort.close();
		dataPort = null;
	}
	
	Thread initThread;
	Object sync = new Object();
	
	public void initialize() throws IOException {
		serialState = SERIALSTATE_WAITINGFORACK;
		
		initThread = new Thread(new Runnable() {
			public void run() {
				boolean waiting_for_init = true;
				int tryCount = 0;
				
				while(waiting_for_init) {
					synchronized(sync) {
						if(serialState == SERIALSTATE_WAITINGFORACK) {
							
							if(tryCount++ == 25) {
								System.out.println("init tries exhausted; giving up.");
								break;
							}
							
							try {
								System.out.println("Initializing reader, try " + tryCount + "...");
								dataPort.getOutputStream().write(SerialSampleIO.EVE_ENQ);
							}
							catch (IOException e) {	}
						} else {
							waiting_for_init = false;
							continue;
						}
						
						// no response yet.. wait.
						try {
							sync.wait(300);
						} catch (InterruptedException e) {}						
					}					
				}
			}
		});
		
		//dataPort.getOutputStream().write(SerialSampleIO.EVE_ENQ);
		// keep sending this until we get a response, or 15 times.
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
				if(serialState == SERIALSTATE_WAITINGFORACK) {
					int val = input.read();
					
					if(val == EVE_ACK) {
						System.out.println("Received ACK from device, leaving initialize mode");
						
						// notify our initializing thread...
						serialState = SERIALSTATE_POSTINIT;
						sync.notify();
						
						try {
							initThread.join();
						} catch (InterruptedException ieeee) {} 
						initThread = null;
						
						serialState = SERIALSTATE_NORMAL;
						return;
					}
					System.out.println("Received " + val + "while waiting for ACK");
				}
				else if(serialState == SERIALSTATE_NORMAL) {
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
			}
			catch (IOException ioe) {
				System.out.println("Error reading from serial port: " + ioe);
			}
			
		}
	}
	
	// obnoxiously stupid, but on a 'link error' Java seems to 
	// mess around for a few seconds, perhaps searching other libraries?
	// Let's only do this once.
	private static boolean hscChecked = false;
	private static boolean hscResult = false;
	// returns TRUE if serial package is capable on this platform...
	public static boolean hasSerialCapability() {		
		// stupid.
		if(hscChecked)
			return hscResult;

		// set the checked flag... check it, if it succeeds, change the result.
		hscChecked = true;
		try {
			// this loads the DLL...
			Class.forName("gnu.io.RXTXCommDriver");
			hscResult = true;
		}
		catch (Exception e) {
			// driver not installed...
			System.err.println(e.toString());
		}
		catch (Error e) {
			// native interface not installed...
			System.err.println(e.toString());
		}
		return hscResult;
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
			
			//dataOutStream = new BufferedOutputStream((((SerialPort)port).getOutputStream()));
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
