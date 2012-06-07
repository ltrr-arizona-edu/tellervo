package org.tellervo.desktop.hardware;

import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.tellervo.desktop.hardware.AbstractMeasuringDevice.BaudRate;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataBits;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.FlowControl;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.LineFeed;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.PortParity;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.StopBits;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.UnitMultiplier;

public abstract class AbstractHIDMeasuringDevice extends
		AbstractMeasuringDevice {

	protected AbstractHIDMeasuringDevice() {
		super(DeviceProtocol.HID_USB);
	}

	/**
	 * Default port settings.  This should be overridden by sub-classes if default
	 * settings are different.
	 */
	public void setDefaultPortParams(){	
	}
	
	/**
	 * Is the device actually present?
	 * @return
	 */
	public Boolean isDevicePresent()
	{
		if(port==null)
		{
			return false;
		}
		
		return true;
		
	}
	
	public Vector enumeratePorts() {
		Vector portStrings = new Vector();			
		return portStrings;
	}
	
	@Override
	public Boolean isBaudEditable() {
		return false;
	}

	@Override
	public Boolean isParityEditable() {
		return false;
	}

	@Override
	public Boolean isDatabitsEditable() {
		return false;
	}

	@Override
	public Boolean isStopbitsEditable() {
		return false;
	}

	@Override
	public Boolean isUnitsEditable() {
		return false;
	}

	@Override
	public Boolean isLineFeedEditable() {
		return false;
	}

	@Override
	public Boolean isFlowControlEditable() {
		return false;
	}

	@Override
	public Boolean isCorrectionFactorEditable() {
		return false;
	}

	@Override
	public void setPortParamsFromPrefs()
			throws UnsupportedPortParameterException, IOException {
		// No params required for HID

	}

	@Override
	protected void finalize() throws Throwable {
		// Not required for HID
	}

	@Override
	public void close() {
		// Not required for HID
	}


}
