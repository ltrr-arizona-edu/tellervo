/**
 * 
 */
package edu.cornell.dendro.corina.hardware;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import org.tridas.io.util.StringUtils;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.MeasurePanel;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataDirection;
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

	public void startCountdown()
	{
		java.util.Timer timer = new java.util.Timer();
		task = new TimeoutTask(infoLabel, this.lastMeasurement, this);
		timer.scheduleAtFixedRate(task, 0, 1000);
		
		log.setText(log.getText()+"********************************\n");
		log.setText(log.getText()+"Starting measuring platform test\n");
		log.setText(log.getText()+"********************************\n");
		log.setText(log.getText()+"Platform type: "+super.dev.toString()+"\n");
		log.setText(log.getText()+"Port         : "+super.dev.getPortName()+"\n");
		log.setText(log.getText()+"Baud rate    : "+super.dev.getBaud()+"\n");
		log.setText(log.getText()+"Data bits    : "+super.dev.getDataBits()+"\n");
		log.setText(log.getText()+"Stop bits    : "+super.dev.getStopBits()+"\n");
		log.setText(log.getText()+"Parity       : "+super.dev.getParity()+"\n");
		log.setText(log.getText()+"Flow control : "+super.dev.getFlowControl()+"\n");
		log.setText(log.getText()+"Line feed    : "+super.dev.getLineFeed()+"\n");
		log.setText(log.getText()+"********************************\n");
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
				
		txt.setText(txt.getText()+value+"μm\n");
		infoLabel.setText("Success!  Data recieved from platform");
	}
		
	@Override
	public void receiverRawData(DataDirection dir, String value) {
		
		if(value==null) return;
		if(log==null) return;
		
		log.setText(log.getText()+StringUtils.leftPad("["+dir.toString()+"] ", 11)+value+"\n");
		try{ log.setCaretPosition(log.getText().length()-1); } catch (Exception e){ }
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
