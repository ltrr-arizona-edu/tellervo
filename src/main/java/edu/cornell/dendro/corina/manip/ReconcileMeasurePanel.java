/**
 * 
 */
package edu.cornell.dendro.corina.manip;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.MeasurePanel;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataDirection;


/**
 * @author Lucas Madar
 *
 */
public class ReconcileMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private ReconcileMeasureDialog rmd;

	public ReconcileMeasurePanel(ReconcileMeasureDialog rmd, final AbstractSerialMeasuringDevice device) 
	{
		super(device);
		this.rmd = rmd;
			
		// Hide extra widgets
		btnQuit.setVisible(false);
		txtLastValue.setVisible(false);
	}

	
	public void receiverNewMeasurement(Integer value) {
		
		if(!checkNewValueIsValid(value))
		{
			return;
		}
				
		
		// make a new measurement!
		AMeasurement nm = new AMeasurement(true, value.intValue(), "meas");
		rmd.addMeasurementValue(nm);	
	}


	@Override
	public void receiverRawData(DataDirection dir, String value) {
		//IGNORE
	}




}
