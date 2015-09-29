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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.BugReport;

/**
 * A dialog for telling the user "You've found a bug!".
 * 
 * <p>
 * This dialog tells the user that a bug in Tellervo was encountered, and gives
 * the option of showing detailed information (the stack trace, and some info
 * about the OS and JVM).
 * </p>
 * 
 * <p>
 * In the future, it should also lets the user copy, mail, print, or submit to
 * SF's tracker the bug report. It can also allow the user to easily save all
 * data, or save all data to a special place (either with their default savers,
 * or using serialization.
 * </p>
 * 
 * <p>
 * Use this for "can't happen" blocks, when you <b>know</b> something can't
 * happen, but have to catch the exception for the compiler to be happy. For
 * example,
 * </p>
 * 
 * <pre>
 * try {
 * 	StringWriter s = new StringWriter();
 * 	s.write(&quot;hello, world&quot;);
 * } catch (IOException ioe) {
 * 	// there's no way a StringWriter can throw an IOE --
 * 	// it's just appending to a StringBuffer --
 * 	// but java requires us to catch it, since
 * 	// Writer declares it.  but we don't want to ignore
 * 	// it, either.
 * 	new Bug(ioe);
 * }
 * </pre>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i
 *         style="color: gray">dot</i> edu&gt;
 * @version $Id$
 */
public class BugDialog extends JDialog {

	private static final long serialVersionUID = -2316971119366897657L;
	private BugReport report;


	/**
	 * Make a new bug dialog.
	 * 
	 * @param t
	 *            a Throwable, usually an Exception, that is a bug
	 * @wbp.parser.constructor
	 */

	public BugDialog(Throwable t) {
		this(new BugReport(t));
	}

	/**
	 * Make a new bug dialog.
	 * 
	 * @param report
	 */
	public BugDialog(BugReport report) {
		super();
		this.setReport(report);
		completeAndShow();
	}


	public void completeAndShow() {
		
		
		
		setTitle("Bug Report");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setIconImage(Builder.getApplicationIcon());

		String bugText = "Exception:\n" + report.getStackTrace() + "\n\n"
				+ BugReport.getSystemInfo() + "\n\n" + BugReport.getUserInfo();

		JTextArea textArea = new JTextArea(bugText, 10, 50);
		textArea.setEditable(false);
		stackTrace = new JScrollPane(textArea);

		String msgText = "<html>We apologize but an internal error has occurred within Tellervo.";
			
		JPanel panelMessage = new JPanel();
		panelMessage.setLayout(new MigLayout("hidemode 2", "[][456px,grow]", "[15px,center][26.00,grow][52.00,top]"));
		
		JLabel lblBug = new JLabel("");
		lblBug.setIcon(Builder.getIcon("bug.png", 64));
		panelMessage.add(lblBug, "cell 0 0,aligny center");
		JLabel label = new JLabel(msgText);
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		panelMessage.add(label, "cell 1 0,alignx left,aligny center");

		JButton btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -4206533770425096704L;

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton btnSubmitReport = new JButton("Submit bug report");
		btnSubmitReport.addActionListener(new AbstractAction() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 4617777199430388184L;

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new BugReportDialog(report);
			}
		});

		btnShowDetails = new JButton("Show Details");
		btnShowDetails.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = -7572001036505366181L;

			public void actionPerformed(ActionEvent e) {
				// toggle visibility of stackTrace component
				if (stackTrace.isVisible()) {
					// hide it
					stackTrace.setVisible(false);
					pack();
					setLocationRelativeTo(App.mainWindow);

					// change text: next op will be "show"
					btnShowDetails.setText("Show Details");
				} else {
					// show it
					stackTrace.setVisible(true);
					setSize(new Dimension(700,400));
					setLocationRelativeTo(App.mainWindow);

					// change text: next op will be "hide"
					btnShowDetails.setText("Hide Details");
				}
				visible = !visible;
			}
		});
		

		panelMessage.add(stackTrace, "cell 0 1 2 1,grow");
		stackTrace.setVisible(false);

		JPanel buttons = new JPanel();
		buttons.setLayout(new MigLayout("", "[127px][163px][98px]", "[25px]"));
		
		buttons.add(btnShowDetails, "cell 0 0,alignx left,aligny top");
		buttons.add(btnSubmitReport, "cell 1 0,alignx left,aligny top");
		buttons.add(btnContinue, "cell 2 0,alignx left,aligny top");
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		panelMessage.add(buttons, "cell 0 2 2 1,alignx right,growy");


		
		if(report.getThrowable() instanceof UserCancelledException) return;
		
		
		this.getContentPane().add(panelMessage);
		pack();
		
		this.setLocationRelativeTo(App.mainWindow);
		this.setVisible(true);
	}

	private JComponent stackTrace;
	private JButton btnShowDetails;
	private boolean visible = false;


	private void setReport(BugReport report) {
		this.report = report;
	}

	public BugReport getReport() {
		return report;
	}
}
