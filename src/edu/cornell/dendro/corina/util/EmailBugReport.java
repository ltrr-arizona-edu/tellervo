package edu.cornell.dendro.corina.util;

import java.text.DateFormat;
import java.util.Date;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.logging.CorinaLog;

import javax.swing.ListModel;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class EmailBugReport {
	// don't instantiate me!
	private EmailBugReport() {}
	
	public static void submitBugReportText(String errorText) {
		try {
			SimpleEmail email = new SimpleEmail();
			String mailHost = App.prefs.getPref("corina.mail.mailhost", null);
			
			if(mailHost == null) {
				Alert.error("No mail server set",
						"Cannot submit bug report because no e-mail server was set.\r\n" +
						"This may be due to a bug occuring before the system that stores preferences\r\n" +
						"was initialized. Please copy and paste this bug to report it manually.");
				return;
			}
			
			email.setHostName(mailHost);
			email.setFrom("corina-auto-report@dendro.cornell.edu", "Corina Automated Bug Report");
			email.addTo("lucas@dendro.cornell.edu");
			
			String userName = System.getProperty("user.name", "(unknown user)");
			StringBuffer errors = new StringBuffer();
			
	        Date date = new Date();
	        errors.append("Corina error log submission\r\n");
	        errors.append("Submitted on ");
	        errors.append(DateFormat.getDateInstance().format(date));
	        errors.append(" ");
	        errors.append(DateFormat.getTimeInstance().format(date));
	        errors.append(" by ");
	        errors.append(userName);
	        errors.append("\r\n\r\n");
			
	        errors.append(errorText);

			email.setSubject("CORINA Bug report from " + userName);
			email.setMsg(errors.toString());
			email.send();
		} catch (EmailException e) {
			Alert.error("Error report email", "Could not send error report e-mail. View the error log.");
			e.printStackTrace();
			return;
		}
		
		Alert.message("Error report email", "Error report e-mail successfully sent.");
		
	}
	
	public static void submitBugReport() {
		StringBuffer errors = new StringBuffer();	
		ListModel logEntries = CorinaLog.getLogListModel();

		for(int i = 0; i < logEntries.getSize(); i++) {
			errors.append(logEntries.getElementAt(i));
			errors.append("\r\n");
		}

		submitBugReportText(errors.toString());
	}
}
