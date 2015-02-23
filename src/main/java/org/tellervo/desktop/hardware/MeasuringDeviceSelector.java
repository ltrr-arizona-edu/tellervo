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
package org.tellervo.desktop.hardware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.BaudRate;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataBits;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.FlowControl;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.LineFeed;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.PortParity;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.StopBits;
import org.tellervo.desktop.hardware.device.EveIODevice;
import org.tellervo.desktop.hardware.device.GenericASCIIDevice;
import org.tellervo.desktop.hardware.device.HeidenhainND287;
import org.tellervo.desktop.hardware.device.LintabDevice;
import org.tellervo.desktop.hardware.device.Microcode2;
import org.tellervo.desktop.hardware.device.QC1000;
import org.tellervo.desktop.hardware.device.QC10Device;
import org.tellervo.desktop.hardware.device.QC1100;
import org.tellervo.desktop.hardware.device.SMCODendro1;
import org.tellervo.desktop.hardware.device.UParSerDevice;
import org.tellervo.desktop.hardware.device.VRODevice;
import org.tellervo.desktop.prefs.Prefs.PrefKey;



public class MeasuringDeviceSelector {
		
	/** Device map */
	private static final HashMap<String, Class<?extends AbstractMeasuringDevice>> deviceMap = 
		new HashMap<String, Class<?extends AbstractMeasuringDevice>>();

	private static final String none = "[none]";
	
	/** Register measuring devices */
	static {
		registerDevice(EveIODevice.class);
		registerDevice(LintabDevice.class);
		registerDevice(QC10Device.class);
		registerDevice(GenericASCIIDevice.class);
		registerDevice(HeidenhainND287.class);
		registerDevice(QC1000.class);
		registerDevice(QC1100.class);
		registerDevice(Microcode2.class);
		registerDevice(UParSerDevice.class);
		registerDevice(SMCODendro1.class);
		registerDevice(VRODevice.class);
	}
	
	/**
	 * Register a device 
	 * @param measuringDevice
	 */
	public synchronized static void registerDevice(
			Class<? extends AbstractMeasuringDevice> measuringDevice) {
		
		AbstractMeasuringDevice dev = null;
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
	public static AbstractMeasuringDevice getSelectedDevice(Boolean doInitialize) throws IOException, InstantiationException, IllegalAccessException
	{
		String selectedDevice = App.prefs.getPref(PrefKey.SERIAL_DEVICE, null);
		String portName = App.prefs.getPref(PrefKey.SERIAL_PORT, "COM1");
		AbstractMeasuringDevice device = null;

		if(selectedDevice==null || selectedDevice.equals(MeasuringDeviceSelector.none))
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
				if(App.prefs.getPref(PrefKey.SERIAL_BAUD, null)!=null &&
					device.isBaudEditable())
				{
					 try{
						 baudRate = BaudRate.valueOf(App.prefs.getPref(PrefKey.SERIAL_BAUD, null));
					 } catch (Exception e) {}
				}
				
				PortParity parity = null;
				if (App.prefs.getPref(PrefKey.SERIAL_PARITY, null)!=null &&
					device.isParityEditable())
				{
					try{
						parity = PortParity.valueOf(App.prefs.getPref(PrefKey.SERIAL_PARITY, null));
					} catch (Exception e) {}
				}

				/*UnitMultiplier units = null;
				if(App.prefs.getPref("tellervo.port.unitmultiplier", null)!=null &&
					device.isUnitsEditable())
				{
					try{
						units = UnitMultiplier.valueOf(App.prefs.getPref("tellervo.port.unitmultiplier", null));
					} catch (Exception e) {}
				}*/
				
				FlowControl fc = null;
				if(App.prefs.getPref(PrefKey.SERIAL_FLOWCONTROL, null)!=null &&
					device.isFlowControlEditable())
				{
					try{
						fc = FlowControl.valueOf(App.prefs.getPref(PrefKey.SERIAL_FLOWCONTROL, null));
					} catch (Exception e){}
				}
				
				DataBits db = null;
				if(App.prefs.getPref(PrefKey.SERIAL_DATABITS, null)!=null &&
					device.isDatabitsEditable())
				{
					try{
						db = DataBits.valueOf(App.prefs.getPref(PrefKey.SERIAL_DATABITS, null));
					} catch (Exception e){}
				}
				
				StopBits sb = null;
				if(App.prefs.getPref(PrefKey.SERIAL_STOPBITS, null)!=null &&
					device.isStopbitsEditable())
				{
					try{
						sb = StopBits.valueOf(App.prefs.getPref(PrefKey.SERIAL_STOPBITS, null));
					} catch (Exception e){}
				}
				
				LineFeed lf = null;
				if(App.prefs.getPref(PrefKey.SERIAL_LINEFEED, null)!=null &&
					device.isLineFeedEditable())
				{
					try{
						lf = LineFeed.valueOf(App.prefs.getPref(PrefKey.SERIAL_LINEFEED, null));
					} catch (Exception e){}
				}
				
				Double mf = null;
				if(App.prefs.getPref(PrefKey.SERIAL_MULTIPLIER, null)!=null &&
					device.isCorrectionFactorEditable())
				{
					
					mf = App.prefs.getDoublePref(PrefKey.SERIAL_MULTIPLIER, null);
				}
				
				Boolean reverse = null;
				if(App.prefs.getPref(PrefKey.SERIAL_MEASURE_IN_REVERSE, null)!=null &&
					device.isReverseMeasureCapable())
				{
					reverse = App.prefs.getBooleanPref(PrefKey.SERIAL_MEASURE_IN_REVERSE, null);
				}
				
				device.setPortParams(portName, baudRate, parity, db, sb, lf, fc, mf, reverse);
				System.out.println("Overriding default port parameters");
			}
			else
			{
				// Device has no settable parameters so just open the named port
				device.openPort(portName);
				System.out.println("Opened port");

			}
			
			device.doInitialize();
		}

		// Return the device
		return device;
	}

}
