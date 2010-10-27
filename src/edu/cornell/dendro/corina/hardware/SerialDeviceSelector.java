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
import edu.cornell.dendro.corina.hardware.device.EveIODevice;
import edu.cornell.dendro.corina.hardware.device.LintabDevice;
import edu.cornell.dendro.corina.hardware.device.MetronicsGenericDevice;
import edu.cornell.dendro.corina.hardware.device.QC10Device;
import edu.cornell.dendro.corina.prefs.Prefs;


public class SerialDeviceSelector {
		
	/** Device map */
	private static final HashMap<String, Class<?extends AbstractSerialMeasuringDevice>> deviceMap = 
		new HashMap<String, Class<?extends AbstractSerialMeasuringDevice>>();

	private static final String none = "[none]";
	
	/** Register measuring devices */
	static {
		registerDevice(EveIODevice.class);
		registerDevice(LintabDevice.class);
		registerDevice(QC10Device.class);
		registerDevice(MetronicsGenericDevice.class);
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
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static AbstractSerialMeasuringDevice getSelectedDevice(Boolean doInitialize) throws IOException, InstantiationException, IllegalAccessException
	{
		String selectedDevice = App.prefs.getPref(Prefs.SERIAL_DEVICE, null);
		String portName = App.prefs.getPref("corina.serialsampleio.port", "COM1");
		AbstractSerialMeasuringDevice device = null;

		if(selectedDevice==null || selectedDevice.equals(SerialDeviceSelector.none))
		{
			throw new IOException("You haven't set up your measuring platform yet!");
		}
		
		if(!deviceMap.containsKey(selectedDevice))
		{
			throw new IOException("Unknown platform set in preferences");
		}
		
		// Try to grab a basic instantiation of the device

		device = deviceMap.get(selectedDevice).newInstance();
		System.out.println("Successfully instantiated device: "+selectedDevice);
		
		if(doInitialize)
		{
			// Device should be initialized
			
			if(device.arePortSettingsEditable())
			{
				// Device has settable port parameters so set them
				BaudRate baudRate = null;
				if(App.prefs.getPref("corina.port.baudrate", null)!=null &&
					device.isBaudEditable())
				{
					 try{
						 baudRate = BaudRate.valueOf(App.prefs.getPref("corina.port.baudrate", null));
					 } catch (Exception e) {}
				}
				
				PortParity parity = null;
				if (App.prefs.getPref("corina.port.parity", null)!=null &&
					device.isParityEditable())
				{
					try{
						parity = PortParity.valueOf(App.prefs.getPref("corina.port.parity", null));
					} catch (Exception e) {}
				}

				UnitMultiplier units = null;
				if(App.prefs.getPref("corina.port.unitmultiplier", null)!=null &&
					device.isUnitsEditable())
				{
					try{
						units = UnitMultiplier.valueOf(App.prefs.getPref("corina.port.unitmultiplier", null));
					} catch (Exception e) {}
				}
				
				FlowControl fc = null;
				if(App.prefs.getPref("corina.port.flowcontrol", null)!=null &&
					device.isFlowControlEditable())
				{
					try{
						fc = FlowControl.valueOf(App.prefs.getPref("corina.port.flowcontrol", null));
					} catch (Exception e){}
				}
				
				DataBits db = null;
				if(App.prefs.getPref("corina.port.databits", null)!=null &&
					device.isDatabitsEditable())
				{
					try{
						db = DataBits.valueOf(App.prefs.getPref("corina.port.databits", null));
					} catch (Exception e){}
				}
				
				StopBits sb = null;
				if(App.prefs.getPref("corina.port.stopbits", null)!=null &&
					device.isStopbitsEditable())
				{
					try{
						sb = StopBits.valueOf(App.prefs.getPref("corina.port.stopbits", null));
					} catch (Exception e){}
				}
				
				LineFeed lf = null;
				if(App.prefs.getPref("corina.port.linefeed", null)!=null &&
					device.isLineFeedEditable())
				{
					try{
						lf = LineFeed.valueOf(App.prefs.getPref("corina.port.linefeed", null));
					} catch (Exception e){}
				}
				
				device.setPortParams(portName, baudRate, parity, db, sb, lf, fc);
				System.out.println("Overriding default port parameters");
			}
			else
			{
				// Device has no settable parameters so just open the named port
				device.openPort(portName);
				System.out.println("Opened port");

			}
		}

		// Return the device
		return device;
	}

}