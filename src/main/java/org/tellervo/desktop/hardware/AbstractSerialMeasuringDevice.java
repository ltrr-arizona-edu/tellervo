package org.tellervo.desktop.hardware;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.CommPortOwnershipListener;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;

/**
 * Abstract base class for a serial port measuring device
 * Defaults to 9600,8,N,1, no flow control
 */
public abstract class AbstractSerialMeasuringDevice extends
		AbstractMeasuringDevice implements 
		SerialPortEventListener, CommPortOwnershipListener{
	
	protected final static Logger log = LoggerFactory.getLogger(AbstractSerialMeasuringDevice.class);


	/** The actual serial port we're operating on */
	private SerialPort port;
	
	/**
	 * Create a new serial measuring device, but do not open. Typically for 
	 * informational uses only. Port state is set to 'DIE' as if it had 
	 * been closed.
	 */
	public AbstractSerialMeasuringDevice() {
		super(DeviceProtocol.SERIAL);
		setDefaultPortParams();
		addMeasuringSampleIOListener(this);
		state = PortState.DIE;
	}
	
	/**
	 * Create a new serial measuring device on 'portName'
	 * 
	 * @param portName the port name ("COM1" on windows, etc)
	 * @throws IOException
	 */
	public AbstractSerialMeasuringDevice(String portName) throws IOException {
		super(DeviceProtocol.SERIAL);
		setDefaultPortParams();
		port = openPort(portName);		
		addMeasuringSampleIOListener(this);
	}
	
	protected void sendRequest(String strCommand)
	{
		return;
	}
	
	/**
	 * Get a vector of all the ports identified on this computer
	 * 
	 * @return
	 */

	public static Vector enumerateSerialPorts() {
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
	
	private static boolean hscChecked = false;
	private static boolean hscResult = false;
	
	/**
	 * Returns true if there is a USB measuring device attached or
	 * if all serial libraries are present and port set so that it 
	 * has the *potential* to talk to serial devices.  As we can't 
	 * poll serial devices there is no way to be sure if they are 
	 * present or not.
	 * 
	 * @return
	 */
	public static boolean hasMeasuringDeviceCapability()
	{
		if(hasSerialCapability() && App.prefs.getPref(PrefKey.SERIAL_PORT, null) != null)
		{
			return true;
		}
		else if (hasUSBDevicesPresent())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
    public void ownershipChange(int type) {
        switch (type) {
            case CommPortOwnershipListener.PORT_OWNED:
                log.debug("Tellervo has successfully taken ownership of the serial port");
                break;
            case CommPortOwnershipListener.PORT_UNOWNED:
            	log.debug("Tellervo has just lost ownership of the serial port");
            	close();
                break;
            case CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED:
            	log.debug("Someone is asking for ownership of the serial port");
            	close();
                break;
        }
    }
	
	/**
	 * Returns true if a USB measuring device known to Tellervo
	 * is present
	 * 
	 * @return
	 */
	public static boolean hasUSBDevicesPresent()
	{
		for(String devname : MeasuringDeviceSelector.getAvailableDevicesNames())
		{
			
		}
		
		
		return false;
	}
	
	/**
	 * Check whether this OS has serial capabilities.  
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
			// Check what libs are loaded
			/*final String[] libraries = ClassScope.getLoadedLibraries(ClassLoader.getSystemClassLoader()); 
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
			App.prefs.setPref(PrefKey.SERIAL_LIBRARY_PRESENT, "Driver not installed");
			return false;
		}
		catch (Error e) {
			// native interface not installed...
			log.error(e.toString());
			App.prefs.setPref(PrefKey.SERIAL_LIBRARY_PRESENT, "Java native interface not installed");
			return false;
		}
		App.prefs.setPref(PrefKey.SERIAL_LIBRARY_PRESENT, "true");
		return hscResult;
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
		unitMultiplier = UnitMultiplier.TIMES_1;
		
	}
	
	/**
	 * On shutdown, make sure we closed the port.
	 */
	@Override
	protected void finalize() throws Throwable {
	
		state = PortState.DIE;
		finishInitialize();
		
		if(port != null) {
			log.debug("Closing port (finalize): " + ((SerialPort) port).getName());
			((SerialPort) port).close();
			port = null;
		}
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
		addMeasuringSampleIOListener(this);
	}
	
	/**
	 * Open the port with the previously set name.
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public Object openPort() throws IOException{
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
	 * Close the port
	 */
	public void close() {
		if(port == null) {
			log.debug("dataport already closed; ignoring close call?");
			return;
		}
		
		log.debug("Closing port (manual): " + ((SerialPort) port).getName());
		
		state = PortState.DIE;
		finishInitialize();
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		port = null;
	}
	
	public SerialPort getSerialPort()
	{
		if(port==null)
		{
			try {
				openPort(this.portName);
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		}
		
		
		return (SerialPort) port;
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
	 * By default nothing happens during initialization.  If a sub-class
	 * needs to do something, then this should be overridden.
	 * @throws IOException 
	 */
	@Override
	protected void doInitialize() throws IOException {
		openPort();
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
	 * Open the port with the specified name.
	 * 
	 * @param portName
	 * @return
	 * @throws IOException
	 */
	public SerialPort openPort(String portName) throws IOException {
		
		// Make sure the port is closed
		try	{((SerialPort) port).close();}catch( Exception e){};
		
		this.portName = portName;
		portId = null;
		
		try {
			// get the port by name.
			portId = CommPortIdentifier.getPortIdentifier(portName);
			
			portId.addPortOwnershipListener(this);
			
			// take ownership...
			CommPort basePort = portId.open("Tellervo", 1000);
			
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
			
			state = PortState.NORMAL;
			return port;
		}
		catch (NoSuchPortException e) {
			throw new IOException(I18n.getText("preferences.hardware.portdoesntexist"));
		}
		catch (PortInUseException e) {
			try{				
				throw new IOException(I18n.getText("preferences.hardware.portinuse", portId.getCurrentOwner()));
			} catch (Exception e2)
			{
				throw new IOException(I18n.getText("preferences.hardware.portinuse", "(most likely) another Tellervo screen"));
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

	}
	


}
