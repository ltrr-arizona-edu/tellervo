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
	private final JTextPane txt;
	private final JTextPane log;
	private TimeoutTask task;
	private final JLabel infoLabel;
	
	
	public TestMeasurePanel(JLabel infoLabel, JTextPane txtLog, JTextPane txtComCheckLog, final AbstractSerialMeasuringDevice device) 
	{
		super(device);
		
		this.txt = txtComCheckLog;
		this.log = txtLog;
		this.infoLabel = infoLabel;
		
		// Hide extra widgets
		btnQuit.setVisible(false);
		
		startCountdown();
		
	}

	private void startCountdown()
	{
		java.util.Timer timer = new java.util.Timer();
		task = new TimeoutTask(infoLabel, this.lastMeasurement, this);
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
		infoLabel.setText("Success!  Data recieved from platform");
	}
		
	@Override
	public void receiverRawData(String value) {
		
		if(value==null) return;
		if(log==null) return;
		
		log.setText(log.getText()+value+"\n");
	}
	
	

	
	
	class TimeoutTask extends java.util.TimerTask
	{
		private JLabel label;
		private Integer countdown =11;
		private final JLabel infoLabel;
		TestMeasurePanel parent;
		
		TimeoutTask(JLabel infoLabel, JLabel label, TestMeasurePanel parent)
		{
			this.label = label;
			this.parent = parent;
			this.infoLabel = infoLabel;
		}
		
		@Override
		public void run() {
			if(countdown==1)
			{
				lastMeasurement.setText("");
				infoLabel.setText(I18n.getText("preferences.hardware.nodatareceived"));
				this.cancel();
				//parent.setVisible(false);
				return;
			}
			
			countdown--;
			
			label.setText(I18n.getText("preferences.hardware.timeremaining")+": "+countdown);
			
			
		}
		
	}



}
