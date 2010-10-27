package edu.cornell.dendro.corina.hardware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.BaudRate;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.FlowControl;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.LineFeed;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortParity;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.StopBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.UnitMultiplier;
import edu.cornell.dendro.corina.hardware.device.EVESerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.device.LINTABSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.device.MetronicsMeasuringDevice;
import edu.cornell.dendro.corina.hardware.device.QC10MeasuringDevice;
import edu.cornell.dendro.corina.prefs.Prefs;


public class SerialDeviceSelector {
		
	/** Device map */
	private static final HashMap<String, Class<?extends AbstractSerialMeasuringDevice>> deviceMap = 
		new HashMap<String, Class<?extends AbstractSerialMeasuringDevice>>();

	/** Register measuring devices */
	static {
		registerDevice(EVESerialMeasuringDevice.class);
		registerDevice(LINTABSerialMeasuringDevice.class);
		registerDevice(QC10MeasuringDevice.class);
		registerDevice(MetronicsMeasuringDevice.class);
	}
	
	/**
	 * Register a device 
	 * @param measuringDevice
	 */
	public synchronized static void registerDevice(
			Class<? extends AbstractSerialMeasuringDevice> measuringDevice) {
		
		AbstractSerialMeasuringDevice dev = null;
		try{
			dev = measuringDevice.newInstance();			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		
		if(dev!=null)
		{
			deviceMap.put(dev.toString(), measuringDevice);
		}
		else
		{
			System.out.println("Error registering measuring device "+measuringDevice.toString());
		}
	}
	
	/**
	 * Get an string array of all the registered measuring devices
	 * 
	 * @return
	 */
	public synchronized static String[] getAvailableDevicesNames()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("[none]");
		for (String name : deviceMap.keySet()) {
			list.add(name);
		}	
		Collections.sort(list);
		return list.toArray(new String[0]);
	}
	
	
	/**
	 * Get the device specified in the preferences - optionally initialize
	 * the device as well.
	 * 
	 * @param doInitialize
	 * @return
	 */
	public static AbstractSerialMeasuringDevice getSelectedDevice(Boolean doInitialize)
	{
		String selectedDevice = App.prefs.getPref(Prefs.SERIAL_DEVICE, null);
		String portName = App.prefs.getPref("corina.serialsampleio.port", "COM1");
		AbstractSerialMeasuringDevice device = null;

		if(!deviceMap.containsKey(selectedDevice))
		{
			return null;
		}
		
		// Try to grab a basic instantiation of the device
		try {
			device = deviceMap.get(selectedDevice).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(doInitialize)
		{
			// Device should be initialized
			
			if(device.arePortSettingsEditable())
			{
				// Device has settable port parameters so use complex constructor
				BaudRate baudRate = BaudRate.valueOf(App.prefs.getPref("corina.port.baudrate", null));
				PortParity parity = PortParity.valueOf(App.prefs.getPref("corina.port.parity", null));
				UnitMultiplier units = UnitMultiplier.valueOf(App.prefs.getPref("corina.port.unitmultiplier", null));
				FlowControl fc = FlowControl.valueOf(App.prefs.getPref("corina.port.flowcontrol", null));
				DataBits db = DataBits.valueOf(App.prefs.getPref("corina.port.databits", null));
				StopBits sb = StopBits.valueOf(App.prefs.getPref("corina.port.stopbits", null));
				LineFeed lf = LineFeed.valueOf(App.prefs.getPref("corina.port.linefeed", null));
				try {
					device = new MetronicsMeasuringDevice(portName, baudRate, parity, db, sb, lf, fc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			else
			{
				// Device has no settable parameters so use simple portName constructor
				try {
					device = new MetronicsMeasuringDevice(portName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}

		// Return the device
		return device;
	}

}