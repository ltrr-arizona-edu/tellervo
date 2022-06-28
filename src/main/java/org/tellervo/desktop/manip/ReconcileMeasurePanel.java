/**
 * 
 */
package org.tellervo.desktop.manip;

import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.MeasurePanel;
import org.tellervo.desktop.hardware.MeasurementReceiver;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataDirection;


/**
 * @author Lucas Madar
 *
 */
public class ReconcileMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private ReconcileMeasureDialog rmd;

	public ReconcileMeasurePanel(ReconcileMeasureDialog rmd, final AbstractMeasuringDevice device) 
	{
		super(device, rmd);
		this.rmd = rmd;
			
		// Hide extra widgets
		btnQuit.setVisible(false);
		this.setLastValueGuiVisible(false);
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
