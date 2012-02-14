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
package edu.cornell.dendro.corina.hardware;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final static Logger log = LoggerFactory.getLogger(AbstractSerialMeasuringDevice.class);

	/** The actual serial port we're operating on */
	private SerialPort port;
	
	/** The state our serial port is in */
	private PortState state;	
		
	/** Port settings */
	protected String portName;
	protected BaudRate baudRate = BaudRate.B_9600;
	protected DataBits dataBits = DataBits.DATABITS_8;
	protected StopBits stopBits = StopBits.STOPBITS_1;
	protected PortParity parity = PortParity.NONE;
	protected LineFeed lineFeed = LineFeed.NONE;
	protected FlowControl flowControl = FlowControl.NONE;
	protected UnitMultiplier unitMultiplier = UnitMultiplier.ZERO;
	protected Boolean measureCumulatively = false;
	protected Boolean measureInReverse = true;
	protected Double correctionMultiplier = 1.0;
	
	
	/** The previous measurement position. Used in 
	 *  cumulative measurements */
	private Integer prevMeasurementPosition = 0;
	
	/** Class that receives the measurements */
	private MeasurementReceiver receiver;
	
	/** A set of listeners */
	private Set<SerialSampleIOListener> listeners = new HashSet<SerialSampleIOListener>();
	
	/** A thread used to do initialization, if necessary */
	private Thread initializeThread;
	
	/**
	 * Set the previous measurement value.  This is
	 * used when measuring in cumulatively mode
	 * 
	 * @param val
	 */
	public void setPreviousPosition(Integer val)
	{
		this.prevMeasurementPosition = val;
	}
	
	/**
	 * Get the previous measurement value 
	 * 
	 * @return
	 */
	public Integer getPreviousPosition()
	{
		return this.prevMeasurementPosition;
	}
	
	/**
	 * Set the factor by which the data value should be
	 * multiplied
	 * 
	 * @param dbl
	 */
	public void setCorrectionMultiplier(Double dbl)
	{
		this.correctionMultiplier = dbl;
	}
	
	/**
	 * Get the factor by which data values are multiplied
	 * 
	 * @return
	 */
	public Double getCorrectionMultipier()
	{
		return this.correctionMultiplier;
	}
	
	/**
	 * Create a new serial measuring device, but do not open. Typically for 
	 * informational uses only. Port state is set to 'DIE' as if it had 
	 * been closed.
	 */
	public AbstractSerialMeasuringDevice() {
		setDefaultPortParams();
		addSerialSampleIOListener(this);
		state = PortState.DIE;
	}
	
	/**
	 * Create a new serial measuring device on 'portName'
	 * 
	 * @param portName the port name ("COM1" on windows, etc)
	 * @throws IOException
	 */
	public AbstractSerialMeasuringDevice(String portName) throws IOException {
		setDefaultPortParams();
		port = openPort(portName);		
		addSerialSampleIOListener(this);
	}
	
	/**
	 * Helper function for setting port params in one go.
	 * 
	 * @param portName
	 * @param baudRate
	 * @param parity
	 * @param dataBits
	 * @param stopBits
	 * @param lineFeed
	 * @param flowControl
	 * @throws IOException
	 */
	public void setPortParams(String portName, BaudRate baudRate, PortParity parity,
			DataBits dataBits, StopBits stopBits, LineFeed lineFeed, FlowControl flowControl, 
			Double mf, Boolean reverse)
		throws IOException
	
	{
		if(baudRate!=null)    this.baudRate = baudRate;
		if(parity!=null)      this.parity = parity;
		if(dataBits!=null)    this.dataBits = dataBits;
		if(stopBits!=null)    this.stopBits = stopBits;
		if(lineFeed!=null)    this.lineFeed = lineFeed;
		if(flowControl!=null) this.flowControl = flowControl;
		if(mf!=null)          this.correctionMultiplier = mf;
		if(reverse!=null)     this.measureInReverse = reverse;
		port = openPort(portName);		
		addSerialSampleIOListener(this);
	}
	/**
	 * If doesInitialization() returns true, starts a new thread and calls doInitialize
	 */
	public void initialize() throws IOException {		
		if(state != PortState.NORMAL)
			throw new IOException("Initializing in an invalid state!");
		

		initializeThread = new Thread( new Runnable() {
			public void run() {
				try {
					doInitialize();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} );
		initializeThread.start();
	
		
	}

	/** Abstract methods used for identifying which port settings can be edited */
	public abstract Boolean isBaudEditable();
	public abstract Boolean isParityEditable();
	public abstract Boolean isDatabitsEditable();
	public abstract Boolean isStopbitsEditable();
	public abstract Boolean isUnitsEditable();
	public abstract Boolean isLineFeedEditable();	
	public abstract Boolean isFlowControlEditable();
	public abstract Boolean isCorrectionFactorEditable();
	
	/**
	 * Are one or more of the port settings editable?
	 * 
	 * @return
	 */
	public Boolean arePortSettingsEditable()
	{
		if (isBaudEditable() ||
			isParityEditable() ||
			isDatabitsEditable() ||
			isStopbitsEditable() ||
			isUnitsEditable() ||
			isLineFeedEditable()||
			isFlowControlEditable() ||
			isCorrectionFactorEditable() ||
			isReverseMeasureCapable()
			)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Default port settings.  This should be overridden by sub-classes if default
	 * settings are different.
	 */
	public void setDefaultPortParams(){
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.NONE;
		unitMultiplier = UnitMultiplier.ZERO;
		
	}
	
	/**
	 * Set the port parameters from the preferences, falling back to defaults for the device if 
	 * no preference is available
	 * @throws UnsupportedPortParameterException 
	 * @throws IOException 
	 */
	public void setPortParamsFromPrefs() throws UnsupportedPortParameterException, IOException
	{
		close();
		// Port
		
		
	    // Baud rate
		try {
			BaudRate br = BaudRate.fromInt(Integer.parseInt(App.prefs.getPref(PrefKey.SERIAL_BAUD, getBaudRate().toString())));
			setBaudRate(br);
		} catch (Exception e) {
			throw new UnsupportedPortParameterException("Baud", "?");
		} 
		
		// Parity		
		PortParity pp = PortParity.fromString(App.prefs.getPref(PrefKey.SERIAL_PARITY, getParity().toString()));
		setParity(pp);

		// Stop bits
		StopBits sb = StopBits.fromString(App.prefs.getPref(PrefKey.SERIAL_STOPBITS, getStopBits().toString()));
		setStopBits(sb);

		// Data bits
		DataBits db = DataBits.fromString(App.prefs.getPref(PrefKey.SERIAL_DATABITS, getDataBits().toString()));
		setDataBits(db);
		
		// Flow control
		FlowControl fc = FlowControl.fromString(App.prefs.getPref(PrefKey.SERIAL_FLOWCONTROL, getFlowControl().toString()));
		setFlowControl(fc);
		
		// Line feed
		LineFeed lf = LineFeed.fromString(App.prefs.getPref(PrefKey.SERIAL_LINEFEED, getLineFeed().toString()));
		setLineFeed(lf);		
		
		// Cumulative
		Boolean cum = App.prefs.getBooleanPref(PrefKey.SERIAL_MEASURE_CUMULATIVELY, this.getMeasureCumulatively());
		setMeasureCumulatively(cum);

		// Reverse
		Boolean rev = App.prefs.getBooleanPref(PrefKey.SERIAL_MEASURE_IN_REVERSE, this.getReverseMeasuring());
		setReverseMeasuring(rev);
		
		// Correction Multiplier
		Double multiplier = App.prefs.getDoublePref(PrefKey.SERIAL_MULTIPLIER, this.getCorrectionMultipier());
		setCorrectionMultiplier(multiplier);
		
		openPort();
		
	}
	
	/**
	 * Get the actual port
	 * 
	 * @return
	 */
	protected final SerialPort getPort() {
		if(port!=null)
		{
			return port;
		}
		else
		{
			log.error("Port is null!");
			return null;
		}
	}
	
	public String getPortName()
	{
		return portName;
	}
	
	/**
	 * Get the state of the port
	 * 
	 * @return
	 */
	protected final PortState getState() {
		return state;
	}
	
	
	/**
	 * Set the state of the port
	 * 
	 * @param state
	 */
	protected final void setState(PortState state) {
		this.state = state;
	}
	
	/**
	 * Open the port with the previously set name.
	 * 
	 * @return
	 * @throws IOException
	 */
	public SerialPort openPort() throws IOException{
		if(portName!=null)
		{
			return openPort(portName);
		}
		else
		{
			Alert.error("Error", "Port name not specified");
		}
		
		return null;
	}
	
	/**
	 * Open the port with the specified name.
	 * 
	 * @param portName
	 * @return
	 * @throws IOException
	 */
	protected SerialPort openPort(String portName) throws IOException {
		
		// Make sure the port is closed
		try	{port.close();}catch( Exception e){};
		
		this.portName = portName;
		CommPortIdentifier portId = null;
		
		try {
			// get the port by name.
			portId = CommPortIdentifier.getPortIdentifier(portName);
			
			// take ownership...
			CommPort basePort = portId.open("Corina", 1000);
			
			// it's a serial port. If it's not, something's fubar.
			if(!(basePort instanceof SerialPort)) {
				throw new IOException(I18n.getText("preferences.hardware.unsupportedporttype"));
			}
			
			port = (SerialPort) basePort;
			
			// defaults to 9600 8N1, no flow control...
			port.setSerialPortParams(getBaud(),
								     getDataBits().toInt(),
								     getStopBits().toInt(),
								     getParity().toInt());
			
			port.setFlowControlMode(getFlowControl().toInt());

			// set up our event listener
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);
			
			// time out after 500ms when reading...
			port.enableReceiveTimeout(500);
			
			//dataOutStream = new BufferedOutputStream((port.getOutputStream()));
		}
		catch (NoSuchPortException e) {
			throw new IOException(I18n.getText("preferences.hardware.portdoesntexist"));
		}
		catch (PortInUseException e) {
			try{
				throw new IOException(I18n.getText("preferences.hardware.portinuse", portId.getCurrentOwner()));
			} catch (Exception e2)
			{
				throw new IOException(I18n.getText("preferences.hardware.portinuse", "Unknown"));
			}
		}
		catch (UnsupportedCommOperationException e) {
			// something is broken??
			throw new IOException("Unable to open port: UnsupportedCommOperationException\n"+e.getLocalizedMessage());
		}
		catch (TooManyListenersException e) {
			// uh... we just made it. and set the listener.  something is broken.
			throw new IOException("Unable to open port: TooManyListenersException\n"+e.getLocalizedMessage());
		}
			
		state = PortState.NORMAL;
		
		return port;
	}
	
	/**
	 * On shutdown, make sure we closed the port.
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	
		state = PortState.DIE;
		finishInitialize();
		
		if(port != null) {
			log.debug("Closing port (finalize): " + port.getName());
			port.close();
			port = null;
		}
	}

	/**
	 * Close the port
	 */
	public void close() {
		if(port == null) {
			log.debug("dataport already closed; ignoring close call?");
			return;
		}
		
		log.debug("Closing port (manual): " + port.getName());
		
		state = PortState.DIE;
		finishInitialize();
		
		port.close();
		port = null;
	}
	
	/**
	 * By default nothing happens during initialization.  If a sub-class
	 * needs to do something, then this should be overridden.
	 * @throws IOException 
	 */
	protected void doInitialize() throws IOException {
		openPort();
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

	
	public enum DataDirection{
		SENT,
		RECEIVED;	
	}
	
	protected synchronized void fireSerialSampleEvent(Object source, int type, Object value)
	{
		fireSerialSampleEvent(source, type, value, DataDirection.RECEIVED);
	}
	
	protected synchronized void fireSerialSampleEvent(Object source, int type, Object value, DataDirection dir) {
		// alert all listeners
		SerialSampleIOListener[] l;
		synchronized (listeners) {
			l = (SerialSampleIOListener[]) listeners.toArray(
					new SerialSampleIOListener[listeners.size()]);
		}

		int size = l.length;

		if (size == 0)
			return;

		SerialSampleIOEvent e = new SerialSampleIOEvent(source, type, value, dir);

		for (int i = 0; i < size; i++) {
			l[i].SerialSampleIONotify(e);
		}
	}
	
	/**
	 * Get the baud rate
	 * @return
	 */
	public BaudRate getBaudRate(){
		return baudRate;
	}
	
	/**
	 * Get the Baud rate as an int
	 * 
	 * @return
	 */
	public int getBaud() {
		return baudRate.toInt();
	}
	
	/**
	 * Get the line feed
	 * @return
	 */
	public LineFeed getLineFeed(){
		return lineFeed;
	}
	
	/**
	 * Get the data bits
	 * @return
	 */
	public DataBits getDataBits() {
		return dataBits;
	}
	
	/**
	 * Get the stop bits
	 * @return
	 */
	public StopBits getStopBits() {
		return stopBits;
	}
	
	/**
	 * Get the parity
	 * @return
	 */
	public PortParity getParity() {
		return parity;
	}
	
	/**
	 * Get the flow control
	 * @return
	 */
	public FlowControl getFlowControl() {
		return flowControl;
	}
	
	/**
	 * Get unit multiplier
	 * @return
	 */
	public UnitMultiplier getUnitMultiplier(){
		return unitMultiplier;
	}
	
	/**
	 * Set the baud rate
	 * @param baudRate
	 */
	protected void setBaudRate(BaudRate baudRate) {
		close();
		this.baudRate = baudRate;
		try {
			openPort();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the data bits
	 * @param dataBits
	 */
	protected void setDataBits(DataBits dataBits) {
		//close();
		this.dataBits = dataBits;
		/*try {
			openPort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/**
	 * Set the stop bits
	 * @param stopBits
	 */
	protected void setStopBits(StopBits stopBits) {
		close();
		this.stopBits = stopBits;
		try {
			openPort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the parity
	 * @param parity
	 */
	protected void setParity(PortParity parity) {
		//close();
		this.parity = parity;
		/*try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	protected void setLineFeed(LineFeed lf){
		//close();
		this.lineFeed = lf;
		/*try{
			this.openPort(port.getName());
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	/**
	 * Set the flow control
	 * @param flowControl
	 */
	protected void setFlowControl(FlowControl flowControl) {
		//close();
		this.flowControl = flowControl;
		/*try {
			this.openPort(port.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Get the name of the measuring device
	 * @return
	 */
	public abstract String toString();
	
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
	 * Can this hardware be configured to measure cumulatively
	 * or not
	 * 
	 * @return
	 */
	public abstract Boolean isMeasureCumulativelyConfigurable();
	

	/**
	 * Set whether this hardware should read cumulatively or not
	 * 
	 * @param b
	 */
	public void setMeasureCumulatively(Boolean b)
	{
		this.measureCumulatively = b;
	}
	
	/**
	 * Get whether this platform is measuring cumulatively or not 
	 * @return
	 */
	public Boolean getMeasureCumulatively()
	{
		return measureCumulatively;
	}
	
	/**
	 * Whether the hardware can be configured to measure backwards
	 * 
	 * @return
	 */
	public abstract Boolean isReverseMeasureCapable();
	
	/**
	 * Set whether the hardware is measuring in reverse
	 * 
	 * @param b
	 */
	public void setReverseMeasuring(Boolean b)
	{
		this.measureInReverse = b;
	}
	
	/**
	 * Get whether the hardware is measuring in reverse
	 * 
	 * @return
	 */
	public Boolean getReverseMeasuring()
	{
		return this.measureInReverse;
	}
	
	
	/**
	 * Does the platform provide current measurement values?  
	 * If so then make sure that the SerialSampleIOEvent.UPDATED_CURRENT_VALUE_EVENT
	 * is dispatched.
	 * 
	 * @return
	 */
	public abstract Boolean isCurrentValueCapable();
	
	private static boolean hscChecked = false;
	private static boolean hscResult = false;
	
	/**
	 * Check whether this OS has serial capablities
	 * 
	 * @return 
	 */
	public static boolean hasSerialCapability() {		
		// stupid.
		if(hscChecked)
			return hscResult;

		// set the checked flag... check it, if it succeeds, change the result.
		hscChecked = true;
		
		try {
			
			/* Check what libs are loaded
			final String[] libraries = ClassScope.getLoadedLibraries(ClassLoader.getSystemClassLoader()); 
			//MyClassName.class.getClassLoader()
			for(String lib: libraries)
			{
				log.debug(lib+" is loaded");
			}*/
			
			
			Class.forName("gnu.io.RXTXCommDriver");
			hscResult = true;
		}
		catch (Exception e) {
			// driver not installed...
			log.error(e.toString());
		}
		catch (Error e) {
			// native interface not installed...
			log.error(e.toString());
		}
		return hscResult;
	}
	
	/**
	 * Get a vector of all the ports identified on this computer
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * Notify the receiver of an event
	 */
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
		else if(sse.getType() == SerialSampleIOEvent.RAW_DATA){
			String value = (String) sse.getValue();
			receiver.receiverRawData(sse.getDataDirection(), value);
		}
		
	}
	
	/**
	 * Set the class that will receive the measurement events
	 * 
	 * @param receiver
	 */
	public void setMeasurementReceiver(MeasurementReceiver receiver){
		this.receiver = receiver;
	}
	
	
	
	
	/** A list of states our port can be in */
	public enum PortState {
		WAITING_FOR_ACK,
		NORMAL,
		POST_INIT,
		DIE
	}
	
	/**
	 * Port parity enum
	 * 
	 * @author peterbrewer
	 *
	 */
	public enum PortParity{
		NONE(SerialPort.PARITY_NONE),
		EVEN(SerialPort.PARITY_EVEN),
		MARK(SerialPort.PARITY_MARK),
		ODD(SerialPort.PARITY_ODD),
		SPACE(SerialPort.PARITY_SPACE);
		
		private int br;
		
		private PortParity(int n)
		{
			br = n; 
		}
		public int toInt()
		{
			return br;
		}
		
		public String toString()
		{
			return StringUtils.capitalize(this.name().toLowerCase());
		}	
		
		public static String[] allValuesAsArray()
		{
			ArrayList<String> arr = new ArrayList<String>();
			for(PortParity val : PortParity.values())
			{
				arr.add(val.toString());
			}
			
			return arr.toArray(new String[0]);
		}
		
		public static PortParity fromString(String pp) throws UnsupportedPortParameterException
		{
			for(PortParity val : PortParity.values())
			{
				if(val.toString().equals(pp)) return val;
			}
			
			throw new UnsupportedPortParameterException("Parity", pp);
		}
	}
	
	/**
	 * Line feed enum
	 * @author peterbrewer
	 *
	 */
	public enum LineFeed{
		NONE(0),
		CR(1),
		CRLF(2);
		
		private int br;
		
		private LineFeed(int n)
		{
			br = n; 
		}
		
		public static LineFeed fromString(String pp) throws UnsupportedPortParameterException
		{
			for(LineFeed val : LineFeed.values())
			{
				if(val.toString().equals(pp)) return val;
			}
			
			throw new UnsupportedPortParameterException("LineFeed", pp);
		}
		
		public int toInt()
		{
			return br;
		}
		
		public String toCommandString()
		{
			if(this.equals(LineFeed.CR))
			{
				return "\r";
			}
			else if (this.equals(LineFeed.CRLF))
			{
				return "\r\n";			
			}
			else 
			{
				return "";
			}		
		}
		
		public static String[] allValuesAsArray()
		{
			ArrayList<String> arr = new ArrayList<String>();
			for(LineFeed val : LineFeed.values())
			{
				arr.add(val.toString());
			}
			
			return arr.toArray(new String[0]);
		}
	}
	
	/**
	 * Baud rate enum
	 * @author peterbrewer
	 *
	 */
	public enum BaudRate{
		B_115200(115200),
		B_57600(57600),
		B_38400(38400),
		B_19200(19200),
		B_9600(9600),
		B_4800(4800),
		B_2400(2400),
		B_1200(1200),
		B_600(600),
		B_300(300);

		private int br;
		
		BaudRate(int n)
		{
			br = n; 
		}
		public int toInt()
		{
			return br;
		}
		
		public String toString()
		{
			return String.valueOf(br);
		}	
		
		public static String[] allValuesAsArray()
		{
			ArrayList<String> arr = new ArrayList<String>();
			for(BaudRate val : BaudRate.values())
			{
				arr.add(val.toString());
			}
			
			return arr.toArray(new String[0]);
		}
		
		public static BaudRate fromInt(int i) throws Exception
		{
			
			for(BaudRate val : BaudRate.values())
			{
				if (i == val.br)
				{
					return val;
				}
			}
			throw new UnsupportedPortParameterException("Baud", i+"");
		}

	}

	/**
	 * Stop bits enum
	 * @author peterbrewer
	 *
	 */
	public enum StopBits{
		STOPBITS_1(SerialPort.STOPBITS_1),
		STOPBITS_2(SerialPort.STOPBITS_2);		

		private int br;
		
		private StopBits(int n)
		{
			br = n; 
		}
		public int toInt()
		{
			return br;
		}
		
		public String toString()
		{
			if(this.equals(StopBits.STOPBITS_1))
			{	
				return "1";
			}
			else
			{
				return "2";
			}			
		}	
		
		public static String[] allValuesAsArray()
		{
			ArrayList<String> arr = new ArrayList<String>();
			for(StopBits val : StopBits.values())
			{
				arr.add(val.toString());
			}
			
			return arr.toArray(new String[0]);
		}
		
		public static StopBits fromString(String st) throws UnsupportedPortParameterException
		{
			for(StopBits val : StopBits.values())
			{
				if(st.equals(val.toString())) return val;
			}
			
			throw new UnsupportedPortParameterException("Stopbits", st);
		}
	}
	
	public enum UnitMultiplier{
		ZERO(0.0f),
		TIMES_10(10.0f),
		TIMES_100(100.0f),
		TIMES_1000(1000.0f),
		DIVIDE_10(0.1f),
		DIVIDE_100(0.01f),
		DIVIDE_1000(0.001f);

		private float br;
		
		private UnitMultiplier(float n)
		{
			br = n; 
		}
		
		public float toFloat()
		{
			return br;
		}
		
		public String toString()
		{
			if(this.equals(UnitMultiplier.ZERO))
			{	
				return "\u00D70";
			}
			else if(this.equals(UnitMultiplier.TIMES_10))
			{
				return "\u00D710";
			}	
			else if(this.equals(UnitMultiplier.TIMES_100))
			{
				return "\u00D7100";
			}
			else if(this.equals(UnitMultiplier.TIMES_1000))
			{
				return "\u00D71000";
			}
			else if(this.equals(UnitMultiplier.DIVIDE_10))
			{
				return "\u00F710";
			}
			else if(this.equals(UnitMultiplier.DIVIDE_100))
			{
				return "\u00F7100";
			}
			else if(this.equals(UnitMultiplier.DIVIDE_1000))
			{
				return "\u00F71000";
			}
			return "";
		}	
	}
	
	/**
	 * Flow control enum
	 * @author peterbrewer
	 *
	 */
	public enum FlowControl{
		NONE(SerialPort.FLOWCONTROL_NONE),
		RTSCTS_IN(SerialPort.FLOWCONTROL_RTSCTS_IN),
		RTSCTS_OUT(SerialPort.FLOWCONTROL_RTSCTS_OUT),
		XONXOFF_IN(SerialPort.FLOWCONTROL_XONXOFF_IN),
		XONXOFF_OUT(SerialPort.FLOWCONTROL_XONXOFF_OUT);

		private int br;
		
		private FlowControl(int n)
		{
			br = n; 
		}
		
		public static FlowControl fromString(String pp) throws UnsupportedPortParameterException
		{
			for(FlowControl val : FlowControl.values())
			{
				if(val.toString().equals(pp)) return val;
			}
			
			throw new UnsupportedPortParameterException("FlowControl", pp);
		}
		
		public int toInt()
		{
			return br;
		}
		
		public static String[] allValuesAsArray()
		{
			ArrayList<String> arr = new ArrayList<String>();
			for(FlowControl val : FlowControl.values())
			{
				arr.add(val.toString());
			}
			
			return arr.toArray(new String[0]);
		}

	}
	
	/**
	 * Data bits enum
	 * 
	 * @author peterbrewer
	 *
	 */
	public enum DataBits{
		DATABITS_5(SerialPort.DATABITS_5),
		DATABITS_6(SerialPort.DATABITS_6),
		DATABITS_7(SerialPort.DATABITS_7),
		DATABITS_8(SerialPort.DATABITS_8);		

		private int br;
		
		private DataBits(int n)
		{
			br = n; 
		}
		
		public static DataBits fromString(String st) throws UnsupportedPortParameterException
		{
			
			for(DataBits val : DataBits.values())
			{
				if (st.equals(val.toString()))
				{
					return val;
				}
			}
			throw new UnsupportedPortParameterException("DataBits", st);
		}
		
		public int toInt()
		{
			return br;
		}
		
		public String toString()
		{
			return String.valueOf(br);
		}	
		
		public static String[] allValuesAsArray()
		{
			ArrayList<String> arr = new ArrayList<String>();
			for(DataBits val : DataBits.values())
			{
				arr.add(val.toString());
			}
			
			return arr.toArray(new String[0]);
		}
		
	}
	
	

	
}
