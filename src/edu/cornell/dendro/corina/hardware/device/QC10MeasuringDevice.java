package edu.cornell.dendro.corina.hardware.device;

import java.io.IOException;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.BaudRate;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.FlowControl;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.LineFeed;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortParity;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.StopBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.UnitMultiplier;


public class QC10MeasuringDevice extends MetronicsMeasuringDevice {

	public QC10MeasuringDevice(String portName) throws IOException {
		super(portName);
	}

	public QC10MeasuringDevice() {
		super();
	}
	
	public QC10MeasuringDevice(String portName, BaudRate baudRate, PortParity parity,
			DataBits dataBits, StopBits stopBits, LineFeed lineFeed, FlowControl flowControl)
			throws IOException
	{
		super(portName, baudRate, parity, dataBits, stopBits, lineFeed, flowControl);
	}
	
	@Override
	public String toString() {
		return "Metronics Quadra-Chek 10";
	}

	@Override
	public void setDefaultPortParams(){
		baudRate = BaudRate.B_9600;
		dataBits = DataBits.DATABITS_8;
		stopBits = StopBits.STOPBITS_1;
		parity = PortParity.NONE;
		flowControl = FlowControl.NONE;
		lineFeed = LineFeed.CRLF;
		unitMultiplier = UnitMultiplier.TIMES_1000;
	}
	

	@Override
	public Boolean isBaudEditable() {
		return true;
	}

	@Override
	public Boolean isDatabitsEditable() {
		return true;
	}

	@Override
	public Boolean isLineFeedEditable() {
		return true;
	}

	@Override
	public Boolean isParityEditable() {
		return true;
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

	
}
