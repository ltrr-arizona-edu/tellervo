/**
 * 
 */
package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataDirection;
import org.tellervo.desktop.hardware.MeasurePanel;
import org.tellervo.desktop.hardware.MeasurementReceiver;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;


/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	protected AbstractEditor editor;
	private Integer cachedValue= null;
	private TimeoutTask task;
	
	@SuppressWarnings("serial")
	public EditorMeasurePanel(final AbstractEditor myeditor, final AbstractMeasuringDevice device) {
		super(device, myeditor);
		editor = myeditor;
		
		setMessageText("");
	
		btnQuit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				myeditor.getSeriesDataMatrix().stopMeasuring();
			}
		});	
	}

	
	public void receiverNewMeasurement(Integer value) {
		
		
		if(!checkNewValueIsValid(value))
		{
			return;
		}
				
		Year y = null;
		if(editor.getSample().containsSubAnnualData())
		{
			// Doing early/late wood measurements.  
			// Data are sent in pairs.  First value is 
			// cached locally.
			
			if (cachedValue==null)
			{
				cachedValue = value.intValue();
				setMessageText(I18n.getText("editor.measuring.latewood"));
				setLastMeasurementIndicators(value);
				return;
			}
			else
			{
				y = editor.getSeriesDataMatrix().measured(cachedValue, value.intValue());
				cachedValue = null;
				setMessageText(I18n.getText("editor.measuring.earlywood"));
			}
		}
		else
		{
			y = editor.getSeriesDataMatrix().measured(value.intValue());
		}
		
		if(y.column() == 0) 
		{
			SoundUtil.playSystemSound(SystemSound.MEASURE_DECADE);
		} else 
		{
			SoundUtil.playSystemSound(SystemSound.MEASURE_RING);
		}
		
		setLastMeasurementIndicators(value);
	}
	
	private void setLastMeasurementIndicators(int value)
	{
		setLastValue(value);
		
		if(super.dev.isMeasureCumulativelyConfigurable())
		{
			setLastPosition(super.dev.getPreviousPosition());
		}
	}


	@Override
	public void receiverRawData(DataDirection dir, String value) {
		
		startCountdown();
		
	}

	private void closeMeasuringPanel()
	{
		this.editor.stopMeasuring();		
	}

	
	private void startCountdown()
	{
		if(task!=null) task.cancel();
		
		java.util.Timer timer = new java.util.Timer();
		task = new TimeoutTask(this, App.prefs.getIntPref(PrefKey.SERIAL_PORT_TIMEOUT_LENGTH, 300));
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	 class TimeoutTask extends java.util.TimerTask
		{
			private Integer countdown;
			private boolean overrideshutdown = false;
			MeasurePanel parent;
			
			TimeoutTask(MeasurePanel parent, Integer timeoutlength)
			{
				this.countdown = timeoutlength;
				this.parent = parent;
			}
			
			@Override
			public boolean cancel(){
				overrideshutdown = true;
				return super.cancel();				
			}
			
			@Override
			public void run() {
				if(countdown==1)
				{
					if(overrideshutdown==false) closeMeasuringPanel();
					super.cancel();	
					return;
				}
				
				countdown--;
				
				if(overrideshutdown==true)
				{
					super.cancel();	
					return; 
				}
				
				if(countdown<=60)
				{
					setMessageText("Measuring platform is idle.  Closing measuring panel in "+countdown+ " seconds");
				}
				else
				{
					if(getMessageText().startsWith("Measuring platform is idle"))
					{
						setMessageText("");
					}
				}
			}
			
		}
	


}
