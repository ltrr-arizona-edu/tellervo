/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.BugReport;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.BugReport.DocumentHolder;



@SuppressWarnings("serial")
public class BugReportDialog extends BugReportInfoPanel_UI implements ActionListener {
	private BugReport report;
	private JDialog dialog;
	
	public BugReportDialog(Window parent, BugReport report) {
		
		try{
		this.report = report;

		new TextComponentWrapper(txtEmailAddress, PrefKey.PERSONAL_DETAILS_EMAIL.getValue(), "");
				
		DocumentHolder[] docs = report.getDocuments().toArray(new DocumentHolder[0]);
		lstAttachments.setListData(docs);

		btnSubmit.addActionListener(this);
		
		pbProgress.setMaximum(100);
		pbProgress.setValue(50);
		txtProgressLabel.setText("");

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
		} catch (Exception e)
		{
			Alert.error("Bug bug!", "How embarrassing! There was a bug creating the bug report!");
		}
	}

	public void actionPerformed(ActionEvent e) {
		String email = txtEmailAddress.getText();
		String comments = txtComments.getText();
		
		
		
		
		// attach stuff to the bug report...
		if(email.length() > 0) {
			App.prefs.setPref(PrefKey.PERSONAL_DETAILS_EMAIL, txtEmailAddress.getText());
			
			report.setFromEmail(email);
		}
		else
		{
			int n = JOptionPane.showConfirmDialog(
				    null,
				    "If you don't provide your email address, we won't be able to help you\n" +
				    "with your problem.  We may also need more information to understand\n" +
				    "what has gone wrong.\n\n" +
				    "Do you still want to submit the bug report anonymously?",
				    "Submit anonymously?",
				    JOptionPane.YES_NO_CANCEL_OPTION);
			
			if(n != JOptionPane.YES_OPTION) return;
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
