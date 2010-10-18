package edu.cornell.dendro.corina.hardware;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;

/**
 * Abstract base class for a serial port measuring device
 * Defaults to 9600,8,N,1, no flow control
 * 
 * @author Lucas Madar
 */

public abstract class AbstractSerialMeasuringDevice
	implements 
		SerialPortEventListener, SerialSampleIOListener
{
	/** A list of states our port can be in */
	protected enum PortState {
		WAITING_FOR_ACK,
		NORMAL,
		POST_INIT,
		DIE
	}
	
	public enum BaudRate{
		B_9600(9600),
		B_4800(4800),
		B_2400(2400),
		B_1200(1200);		

		private int br;
		
		private BaudRate(int n)
		{
			br = n; 
		}
		public int getBaud()
		{
			return br;
		}
		
		public String toString()
		{
			return String.valueOf(br);
		}
		
		
	}

	/** The actual serial port we're operating on */
	private SerialPort port;
	
	/** The state our serial port is in */
	private PortState state;	
	
	/** A thread used to do initialization, if necessary */
	private Thread initializeThread;
	
	/** A set of listeners */
	private Set<SerialSampleIOListener> listeners = new HashSet<SerialSampleIOListener>();
	
	/**
	 * The following are standard serial connection settings.
	 * They default to 9600 baud N81.  They can be overridden using the setter methods.
	 */
	private BaudRate baudRate = BaudRate.B_9600;

	private int dataBits = SerialPort.DATABITS_8;
	
	private int stopBits = SerialPort.STOPBITS_1;
	
	private int parity = SerialPort.PARITY_NONE;
	
	private int flowControl = SerialPort.FLOWCONTROL_NONE;
	
	private MeasurementReceiver receiver;
	/**
	 * Create a new serial measuring device
	 * 
	 * @param portName the port name ("COM1" on windows, etc)
	 * @throws IOException
	 */
	public AbstractSerialMeasuringDevice(String portName) throws IOException {
		port = openPort(portName);
		state = PortState.NORMAL;
		
		addSerialSampleIOListener(this);
	}
	
	public AbstractSerialMeasuringDevice(String portName, BaudRate baud) throws IOException {
		baudRate = baud;
		port = openPort(portName);
		state = PortState.NORMAL;
		
		addSerialSampleIOListener(this);
	}
	
	
	/**
	 * Create a new serial measuring device, but for informational uses only.
	 * (starts out 'dead,' as if it had been closed)
	 */
	public AbstractSerialMeasuringDevice() {
		state = PortState.DIE;
	}
	
	/**
	 * If doesInitialization() returns true, starts a new thread and calls doInitialize
	 */
	public void initialize() throws IOException {		
		if(state != PortState.NORMAL)
			throw new IOException("Initializing in an invalid state!");
		
		if(doesInitialization()) {
			initializeThread = new Thread( new Runnable() {
				public void run() {
					doInitialize();
				}
			} );
			initializeThread.start();
		}
		
	}

	protected final SerialPort getPort() {
		return port;
	}
	
	protected final PortState getState() {
		return state;
	}
	
	protected final void setState(PortState state) {
		this.state = state;
	}
		
	private SerialPort openPort(String portName) throws IOException {
		SerialPort port;
		CommPortIdentifier portId;
		
		try {
			// get the port by name.
			portId = CommPortIdentifier.getPortIdentifier(portName);
			
			// take ownership...
			CommPort basePort = portId.open("Corina", 1000);
			
			// it's a serial port. If it's not, something's fubar.
			if(!(basePort instanceof SerialPort)) {
				throw new IOException("Unable to open port: Port type is unsupported.");
			}
			
			port = (SerialPort) basePort;
			
			// defaults to 9600 8N1, no flow control...
			port.setSerialPortParams(getBaud(),
								     getDataBits(),
								     getStopBits(),
								     getParity());
			
			port.setFlowControlMode(getFlowControl());

			// set up our event listener
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);
			
			// time out after 500ms when reading...
			port.enableReceiveTimeout(500);
			
			//dataOutStream = new BufferedOutputStream((port.getOutputStream()));
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
				
		return port;
	}
	
	/**
	 * on shutdown, make sure we closed the port.
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	
		state = PortState.DIE;
		finishInitialize();
		
		if(port != null) {
			System.out.println("Closing port (finalize): " + port.getName());
			port.close();
			port = null;
		}
	}

	// clean up!
	public void close() {
		if(port == null) {
			System.out.println("dataport already closed; ignoring close call?");
			return;
		}
		
		System.out.println("Closing port (manual): " + port.getName());
		
		state = PortState.DIE;
		finishInitialize();
		
		port.close();
		port = null;
	}
	
	////
	//// Initialization process
	////
	/**
	 * returns true if a thread should be started to run
	 * doInitialize()
	 */
	protected abstract boolean doesInitialization();
	
	protected void doInitialize() {
		// do nothing, by default
		// override me for actual initialization, runs in its own thread
	}
	
	protected void finishInitialize() {
		if(initializeThread != null) {
			try {
				initializeThread.join();
			} catch (InterruptedException e) {} 
			initializeThread = null;
		}		
	}
	
	////
	//// Listeners
	////
	
	public void addSerialSampleIOListener(SerialSampleIOListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}

	public void removeSerialSampleIOListener(SerialSampleIOListener l) {
		listeners.remove(l);
	}
	
	protected synchronized void fireSerialSampleEvent(int type, Object value) {
		// alert all listeners
		SerialSampleIOListener[] l;
		synchronized (listeners) {
			l = (SerialSampleIOListener[]) listeners.toArray(
					new SerialSampleIOListener[listeners.size()]);
		}

		int size = l.length;

		if (size == 0)
			return;

		SerialSampleIOEvent e = new SerialSampleIOEvent(LegacySerialSampleIO.class,
				type, value);

		for (int i = 0; i < size; i++) {
			l[i].SerialSampleIONotify(e);
		}
	}
	
	////
	//// Informational overrides
    ////
	protected int getBaud() {
		return baudRate.getBaud();
	}
	
	protected BaudRate getBaudRate(){
		return baudRate;
	}
	
	protected int getDataBits() {
		return dataBits;
	}
	
	protected int getStopBits() {
		return stopBits;
	}
	
	protected int getParity() {
		return parity;
	}
	
	protected int getFlowControl() {
		return flowControl;
	}
	
	protected void setBaudRate(BaudRate baudRate) {
		close();
		this.baudRate = baudRate;
		try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void setDataBits(int dataBits) {
		close();
		this.dataBits = dataBits;
		try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void setStopBits(int stopBits) {
		close();
		this.stopBits = stopBits;
		try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void setParity(int parity) {
		close();
		this.parity = parity;
		try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void setFlowControl(int flowControl) {
		close();
		this.flowControl = flowControl;
		try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract String getMeasuringDeviceName();
	
	/**
	 * Ask platforms for a measurement
	 */
	public abstract void requestMeasurement();
		
	/**
	 * Ask platform to zero its measurements
	 */
	public abstract void zeroMeasurement();
	
	/**
	 * Can this platform accept requests for data?
	 * 
	 * @return
	 */
	public abstract Boolean isRequestDataCapable();
	
	/**
	 * Does the platform provide current measurement values?
	 * 
	 * @return
	 */
	public abstract Boolean isCurrentValueCapable();
	
	
	// mess around for a few seconds, perhaps searching other libraries?
	// Let's only do this once.
	private static boolean hscChecked = false;
	private static boolean hscResult = false;
	
	/**
	 * @return TRUE if serial package is capable on this platform...
	 */
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
	
	public void SerialSampleIONotify(SerialSampleIOEvent sse) {
		if(sse.getType() == SerialSampleIOEvent.BAD_SAMPLE_EVENT) {
			receiver.receiverUpdateStatus("Error reading the previous sample!");
		}
		if(sse.getType() == SerialSampleIOEvent.ERROR) {
			receiver.receiverUpdateStatus((String) sse.getValue());
		}
		else if(sse.getType() == SerialSampleIOEvent.INITIALIZING_EVENT) {
			receiver.receiverUpdateStatus("Initializing reader (try "+ sse.getValue() +")");
		}
		else if(sse.getType() == SerialSampleIOEvent.INITIALIZED_EVENT) {
			receiver.receiverUpdateStatus("Initialized; ready to read measurements.");
		}
		else if(sse.getType() == SerialSampleIOEvent.NEW_SAMPLE_EVENT) {
 			Integer value = (Integer) sse.getValue();
			receiver.receiverNewMeasurement(value);
		}
		else if(sse.getType() == SerialSampleIOEvent.UPDATED_CURRENT_VALUE_EVENT) {
			Integer value = (Integer) sse.getValue();
			receiver.receiverUpdateCurrentValue(value);
		}
		
	}
	
	public void setMeasurementReceiver(MeasurementReceiver receiver){
		this.receiver = receiver;
	}
}
