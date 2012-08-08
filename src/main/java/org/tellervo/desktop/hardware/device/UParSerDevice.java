package org.tellervo.desktop.hardware.device;

import java.io.IOException;

import org.tellervo.desktop.hardware.AbstractHIDMeasuringDevice;

public class UParSerDevice extends AbstractHIDMeasuringDevice {

	@Override
	public String toString() {
		return "SCIEM uParSer";
	}
	
	@Override
	public boolean doesInitialize() {
		return false;
	}

	@Override
	public void requestMeasurement() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zeroMeasurement() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean isRequestDataCapable() {
		return true;
	}

	@Override
	public Boolean isMeasureCumulativelyConfigurable() {
		return false;
	}

	@Override
	public Boolean isReverseMeasureCapable() {
		return false;
	}

	@Override
	public Boolean isCurrentValueCapable() {
		return true;
	}

	@Override
	public Object openPort(String portName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object openPort() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
