package edu.cornell.dendro.corina.hardware;

import java.io.IOException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.PreferencesDialog;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.ui.Alert;

public class SerialDeviceSelector {

	private static AbstractSerialMeasuringDevice device = null;
	//private Prefs prefs = new Prefs();

	public SerialDeviceSelector(){

	}

	public AbstractSerialMeasuringDevice getDevice(){
		init(true);		
		return device;
	}
	
	
	private void init(Boolean init)
	{
		//prefs.
		String selectedDevice = App.prefs.getPref(Prefs.SERIAL_DEVICE, null);
		String portName = App.prefs.getPref("corina.serialsampleio.port", "COM1");

		try{
			if(selectedDevice.equals(SerialMeasuringDeviceConstants.NONE)){
				device = null;
				if(init)
				{
					Alert.error("Error", "You have not yet set up your measuring device.");
					PreferencesDialog.showPreferencesAtTabIndex(1);
				}
			}
			else if(selectedDevice.equals(SerialMeasuringDeviceConstants.EVE)){
				if(init)
				{	
					device = new EVESerialMeasuringDevice(portName);
				}
				else
				{
					device = new EVESerialMeasuringDevice();
				}
			}else if(selectedDevice.equals(SerialMeasuringDeviceConstants.VELMEX)){
				if(init)
				{	
					device = new QC10SerialMeasuringDevice(portName);
				}
				else
				{
					device = new QC10SerialMeasuringDevice();
				}
			}else if(selectedDevice.equals(SerialMeasuringDeviceConstants.LINTAB)){
				if(init)
				{
					device = new LINTABSerialMeasuringDevice(portName);
				}
				else
				{
					device = new LINTABSerialMeasuringDevice();
				}
			}
			else{
				Alert.error("Error", "Unknown device type!");
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
			Alert.error("Error", "Error connecting to platform.  Is it switched on and plugged in?\n"+ioe);
		}
	}
	
	
	public AbstractSerialMeasuringDevice getDeviceWithoutInit()
	{
		init(false);		
		return device;	
	}
}