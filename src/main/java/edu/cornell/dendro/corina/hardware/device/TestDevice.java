package edu.cornell.dendro.corina.hardware.device;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import gnu.io.SerialPortEvent;

public class TestDevice extends AbstractSerialMeasuringDevice {

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean isBaudEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isParityEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isDatabitsEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isStopbitsEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isUnitsEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isLineFeedEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isFlowControlEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCurrentValueCapable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void doInitialize()
	{
		
	}

}
