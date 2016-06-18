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
import javax.swing.JPanel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.BugReport;
import org.tellervo.desktop.util.BugReport.DocumentHolder;
import org.tellervo.desktop.util.Center;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import java.awt.Font;



@SuppressWarnings("serial")
public class BugReportDialog implements ActionListener {
	private final BugReport report;
	private BugSubmissionPanel submissionPanel;
	private JDialog dialog;
	private final Window parent;

	/**
	 * @wbp.parser.constructor
	 */
	public BugReportDialog(Throwable error)
	{
		this.parent = App.mainWindow;
		
		report = new BugReport(error);
		init();
	}
		
	public BugReportDialog(Window parent, Throwable error)
	{
		this.parent = parent;
		report = new BugReport(error);
		init();
	}
	
	public BugReportDialog(Window parent, BugReport report) {
		
		this.parent = parent;
		this.report = report;

		init();
	
	}
	
	public BugReportDialog(BugReport report) {
		
		this.parent = App.mainWindow;
		this.report = report;

		init();
	
	}
	
	private void init()
	{		
		try{
					
			DocumentHolder[] docs = report.getDocuments().toArray(new DocumentHolder[0]);

			// JDialog constructor fails when parent is a null dialog (!@#)
			if(parent instanceof Dialog)
				dialog = new JDialog((Dialog) parent, "Submitting bug report...", true);
			else
				dialog = new JDialog((Frame) parent, "Submitting bug report...", true);
			
			
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new MigLayout("", "[739px,grow]", "[grow]"));
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setContentPane(mainPanel);
	        submissionPanel = new BugSubmissionPanel();
	        mainPanel.add(submissionPanel, "cell 0 0,growy");
	        
	        			
	        			new TextComponentWrapper(submissionPanel.txtEmailAddress, PrefKey.PERSONAL_DETAILS_EMAIL, "");
	        			submissionPanel.lstAttachments.setListData(docs);
	        			
	        						submissionPanel.btnSubmit.addActionListener(this);
	        						
	        						submissionPanel.pbProgress.setMaximum(100);
	        						submissionPanel.pbProgress.setValue(50);
	        						submissionPanel.txtProgressLabel.setText("");
	        						
	        						
	        						
	        submissionPanel.txtComments.setLineWrap(true);
	        submissionPanel.txtComments.setWrapStyleWord(true);
	        submissionPanel.txtComments.requestFocus();
		} catch (Exception e)
		{
			Alert.error("Bug bug!", "How embarrassing! There was a bug creating the bug report!");
		}
		
		dialog.pack();
        dialog.setIconImage(Builder.getApplicationIcon());
        dialog.setLocationRelativeTo(App.mainWindow);
		dialog.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String email = submissionPanel.txtEmailAddress.getText();
		String comments = submissionPanel.txtComments.getText();
		
		
		
		
		// attach stuff to the bug report...
		if(email.length() > 0) {
			App.prefs.setPref(PrefKey.PERSONAL_DETAILS_EMAIL, submissionPanel.txtEmailAddress.getText());
			
			report.setFromEmail(email);
		}
		else
		{
			JOptionPane.showMessageDialog(dialog, 
					"We need your email address as we may need more information to understand\n" +
				    "what has gone wrong.");
			
			return;
		}
		
		if(comments != null && comments.length() > 0)
			report.setComments(comments);
		
		submissionPanel.pbProgress.setValue(75);
		submissionPanel.txtProgressLabel.setText("Submitting report...");
		submissionPanel.btnSubmit.setEnabled(false);

		
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
