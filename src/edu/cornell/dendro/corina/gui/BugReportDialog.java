package edu.cornell.dendro.corina.gui;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.BugReport;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.BugReport.DocumentHolder;


public class BugReportDialog extends BugReportInfoPanel_UI implements ActionListener {
	private BugReport report;
	private JDialog dialog;
	
	public BugReportDialog(Window parent, BugReport report) {
		this.report = report;

		txtEmailAddress.setText(App.prefs.getPref("corina.bugreport.fromemail", ""));
		
		DocumentHolder[] docs = report.getDocuments().toArray(new DocumentHolder[0]);
		lstAttachments.setListData(docs);

		btnSubmit.addActionListener(this);
		
		pbProgress.setMaximum(100);
		pbProgress.setValue(50);
		txtProgressLabel.setText("Waiting for user input...");

		// JDialog constructor fails when parent is a null dialog (!@#)
		if(parent instanceof Dialog)
			dialog = new JDialog((Dialog) parent, "Submitting bug report...", true);
		else
			dialog = new JDialog((Frame) parent, "Submitting bug report...", true);
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setContentPane(this);
		dialog.pack();
		Center.center(dialog);
        dialog.setIconImage(Builder.getApplicationIcon());
		
		txtComments.setLineWrap(true);
		txtComments.setWrapStyleWord(true);
		txtComments.requestFocus();
		
		dialog.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String email = txtEmailAddress.getText();
		String comments = txtComments.getText();
		
		// attach stuff to the bug report...
		if(email.length() > 0) {
			App.prefs.setPref("corina.bugreport.fromemail", txtEmailAddress.getText());
			
			report.setFromEmail(email);
		}
		
		if(comments != null && comments.length() > 0)
			report.setComments(comments);
		
		pbProgress.setValue(75);
		txtProgressLabel.setText("Submitting report...");
		btnSubmit.setEnabled(false);

		
		// run this separately!
		EventQueue.invokeLater(new Thread() {
			public void run() {
				// actually submit the report
				report.submit();
				dialog.dispose();
			}
		});		
	}
}
