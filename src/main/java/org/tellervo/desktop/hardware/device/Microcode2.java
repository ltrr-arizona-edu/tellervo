package org.tellervo.desktop.hardware.device;


public class Microcode2 extends GenericASCIIDevice {

	@Override
	public String toString() {
		return "Boeckeler Microcode II";
	}

	@Override
	public void setDefaultPortParams(){
		
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_7;
		stopBits = StopBits.STOPBITS_2;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CRLF;
		unitMultiplier = UnitMultiplier.TIMES_1000;
		measureCumulatively = true;
	}
	
	@Override
	public Boolean isBaudEditable() {
		return false;
	}

	@Override
	public Boolean isDatabitsEditable() {
		return false;
	}

	@Override
	public Boolean isLineFeedEditable() {
		return false;
	}

	@Override
	public Boolean isParityEditable() {
		return false;
	}

	@Override
	public Boolean isStopbitsEditable() {
		return false;
	}
	
	@Override
	public Boolean isFlowControlEditable(){
		return false;
	}

	@Override
	public Boolean isUnitsEditable() {
		return false;
	}

	
	@Override
	public Boolean isRequestDataCapable() {
		return false;
	}
	
	@Override
	public Boolean isMeasureCumulativelyConfigurable() {
		return false;
	}
}
