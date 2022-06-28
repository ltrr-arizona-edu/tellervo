package org.tellervo.desktop.wsi.tellervo;

import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.tellervo.desktop.wsi.Resource;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;

import net.miginfocom.swing.MigLayout;

public class TellervoMultiResourceAccessPanel extends JPanel implements ResourceEventListener{

	
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JLabel lblInfo;
	private long startTimestamp ;
	private JButton btnCancel;
	private JLabel lblStatus;

	/**
	 * Create the panel.
	 */
	public TellervoMultiResourceAccessPanel(int max) {
		setLayout(new MigLayout("", "[grow,fill][]", "[25.00,fill][][]"));
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		
		add(progressBar, "cell 0 0 2 1");
		
		lblInfo = new JLabel("Please wait...");
		add(lblInfo, "cell 0 1 2 1");
		
		lblStatus = new JLabel("");
		add(lblStatus, "cell 0 2");
		
		btnCancel = new JButton("Cancel");
		add(btnCancel, "cell 1 2,alignx right");
		
		this.startTimestamp = System.currentTimeMillis();

	}
	
	public void addResource(Resource resource)
	{
		
		resource.addResourceEventListener(this);
		
	}


	
	public static void showMultiResourceDialog(int processcount)
	{
		JDialog dialog = new JDialog();
		TellervoMultiResourceAccessPanel panel = new TellervoMultiResourceAccessPanel(processcount);
		
		dialog.getContentPane().add(panel);
		dialog.pack();
		dialog.setVisible(true);
		
		
	}
	
	private void incrementProgress()
	{
		int max = progressBar.getMaximum();
		int curr = progressBar.getValue();
		
		if(curr<max)
		{
			progressBar.setValue(curr+1);
		}
	}

	@Override
	public void resourceChanged(ResourceEvent re) {
		int eventType = re.getEventType();
		switch(eventType) {
		case ResourceEvent.RESOURCE_QUERY_COMPLETE:
			incrementProgress();
			break;
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			incrementProgress();
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_IN:
			break;
			
		default:
			break;
		}
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JLabel getStatusLabel() {
		return lblInfo;
	}

	public void setEstimate() {
		
		long now = System.currentTimeMillis();
		
		long timesincestart = now - startTimestamp;		
		long prog = progressBar.getValue();
		
		if(prog==0) return;
		
		
		long total = progressBar.getMaximum();
		
		long msperitem = timesincestart / prog;
		long mstotal = msperitem * total;
		
		long msleft = mstotal;
		
		
		
		lblInfo.setText("Estimated time remaining: "+
		String.format("%02d min, %02d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(msleft),
			    TimeUnit.MILLISECONDS.toSeconds(msleft) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msleft))
			));
		
		
	}
	
	
	public void setStatusMessage(String message){
		
		lblStatus.setText(message);
		
	}
	
	
	public JButton getBtnCancel() {
		return btnCancel;
	}
}
