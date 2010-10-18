/**
 * 
 */
package edu.cornell.dendro.corina.manip;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;


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
		lastMeasurement.setVisible(false);
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
	


}
