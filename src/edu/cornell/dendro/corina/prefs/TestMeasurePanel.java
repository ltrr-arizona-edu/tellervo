/**
 * 
 */
package edu.cornell.dendro.corina.prefs;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.MeasurePanel;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;


/**
 * @author Lucas Madar
 *
 */
public class TestMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private JTextPane txt;
	private TimeoutTask task;
	public TestMeasurePanel(JTextPane txtComCheckLog, final AbstractSerialMeasuringDevice device) 
	{
		super(device);
		this.txt = txtComCheckLog;
			
		// Hide extra widgets
		btnQuit.setVisible(false);
		
		startCountdown();
		
	}

	private void startCountdown()
	{
		java.util.Timer timer = new java.util.Timer();
		task = new TimeoutTask(this.lastMeasurement, this);
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	public void cancelCountdown()
	{
		if(task!=null)
		{
			task.cancel();
		}
		
		lastMeasurement.setText("");
	}
	

	public void receiverUpdateStatus(String status) {
		 txt.setText("["+I18n.getText("preferences.hardware.status")+"] : "+status+"\n");
	}
	
	public void receiverNewMeasurement(Integer value) {
		
		cancelCountdown();
		
		if(!checkNewValueIsValid(value))
		{
			return;
		}
				
		txt.setText(txt.getText()+"["+I18n.getText("preferences.hardware.datareceived")+"] : "+value+"\n");
	}
		
	class TimeoutTask extends java.util.TimerTask
	{
		private JLabel label;
		private Integer countdown =11; 
		TestMeasurePanel parent;
		
		TimeoutTask(JLabel label, TestMeasurePanel parent)
		{
			this.label = label;
			this.parent = parent;
		}
		
		@Override
		public void run() {
			if(countdown==1)
			{
				lastMeasurement.setText("");
				Alert.message(I18n.getText("error"), I18n.getText("preferences.hardware.nodatareceived"));
				this.cancel();
				parent.setVisible(false);
				return;
			}
			
			countdown--;
			
			label.setText(I18n.getText("preferences.hardware.timeremaining")+": "+countdown);
			
			
		}
		
	}


}
