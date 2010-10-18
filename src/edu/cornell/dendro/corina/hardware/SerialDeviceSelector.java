package edu.cornell.dendro.corina.hardware;

import java.io.IOException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs;

public class SerialDeviceSelector {

	private static AbstractSerialMeasuringDevice device = null;
	//private Prefs prefs = new Prefs();

	public SerialDeviceSelector(){
		//prefs.
		String selectedDevice = App.prefs.getPref(Prefs.SERIAL_DEVICE, null);
		String portName = App.prefs.getPref("corina.serialsampleio.port", "COM1");

		try{
			if(selectedDevice.equals(SerialMeasuringDeviceConstants.NONE)){
				device = null;
			}
			if(selectedDevice.equals(SerialMeasuringDeviceConstants.EVE)){
				device = new EVESerialMeasuringDevice(portName);
			}else if(selectedDevice.equals(SerialMeasuringDeviceConstants.VELMEX)){
				device = new QC10SerialMeasuringDevice(portName);
			}else if(selectedDevice.equals(SerialMeasuringDeviceConstants.LINTAB)){
				device = new LINTABSerialMeasuringDevice(portName);
			}
			else{
				device = null;
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public AbstractSerialMeasuringDevice getDevice(){
		
		if(device==null)
		{
			edu.cornell.dendro.corina.ui.Alert.error("Error", "You have not yet set up your measuring device in the preferences dialog");
		}
		return device;
	}
}