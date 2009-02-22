package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.util.BugReport;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.PureStringWriter;

// TODO: refactor!

// TODO: sizing: the dialog shouldn't change width -- set initial
// width based on textField.getPreferredSize()?  also, it might look
// weird for it to be centered when small, and then expand down.

// TODO: let user copy bug report
// TODO: let user print bug report (!!)

// TODO: need big bug icon!

/**
 * A dialog for telling the user "You've found a bug!".
 * 
 * <p>
 * This dialog tells the user that a bug in Corina was encountered, and gives
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
public class Bug extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2316971119366897657L;

	/**
	 * Make a new bug dialog.
	 * 
	 * @param t
	 *            a Throwable, usually an Exception, that is a bug
	 */

	public Bug(Throwable t) {
		this(t, new BugReport(t));
	}

	/**
	 * Make a new bug dialog.
	 * 
	 * @param t
	 *            a Throwable, usually an Exception, that is a bug
	 * @param typ
	 * 			  the type of bug (see BugType)
	 * 
	 * Shows the dialog automatically if BugType is BugType.NORMAL
	 */
	public Bug(Throwable t, BugReport report) {
		super();

		this.bug = t;
		this.report = report;
		completeAndShow();
	}

	private Throwable bug;
	private BugReport report;

	public void completeAndShow() {
		setTitle("Bug Report");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		this.setModal(true);

		String bugText = "Exception:\n" + BugReport.getStackTrace(bug) + "\n\n"
				+ BugReport.getSystemInfo() + "\n\n" + BugReport.getUserInfo();

		JTextArea textArea = new JTextArea(bugText, 10, 50);
		textArea.setEditable(false);
		stackTrace = new JScrollPane(textArea);

		String msgText = "<html>We apologize but an internal error has occurred within Corina and there "
						+ "<i>may</i> have been a <br>loss of data.<br><br>"
						+ "Please contact the Corina developers with details of what you were doing"
						+ " prior to getting<br> this message by submitting a report.<br>";
			
		JPanel message = Layout
				.flowLayoutL(msgText);
		
		JButton bummer = new JButton("Continue");
		bummer.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -4206533770425096704L;

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		final JDialog glue = this;
		JButton submitreport = new JButton("Submit bug report");
		submitreport.addActionListener(new AbstractAction() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 4617777199430388184L;

			public void actionPerformed(ActionEvent e) {
				BugReportDialog reportDlg = new BugReportDialog(glue, report);
				dispose();
			}
		});

		more = new JButton("Show Details");
		more.addActionListener(new AbstractAction() {
			/**
		 * 
		 */
			private static final long serialVersionUID = -7572001036505366181L;

			public void actionPerformed(ActionEvent e) {
				// toggle visibility of stackTrace component
				if (visible) {
					// hide it
					getContentPane().remove(stackTrace);
					pack();

					// change text: next op will be "show"
					more.setText("Show Details");
				} else {
					// show it
					getContentPane().add(stackTrace, BorderLayout.CENTER);
					pack();

					// change text: next op will be "hide"
					more.setText("Hide Details");
				}
				visible = !visible;
			}
		});

		JPanel buttons = Layout.buttonLayout(more, submitreport, bummer);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JPanel content = Layout
				.borderLayout(message, null, null, null, buttons);
		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		setContentPane(content);

		pack();
		OKCancel.addKeyboardDefaults(bummer);
		// setResizable(false);
		Center.center(this);
		this.setVisible(true);
	}

	private JComponent stackTrace;
	private JButton more;
	private boolean visible = false;

	/**
	 * Old interface! -- fix all occurrences of this, then remove.
	 * 
	 * @deprecated use new Bug(t)
	 */
	@Deprecated
	public static void bug(Throwable t) {
		new Bug(t);
	}
}