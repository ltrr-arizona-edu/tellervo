package corina.util;

import java.text.DateFormat;
import java.util.Date;

import corina.core.App;
import corina.ui.Alert;
import corina.logging.CorinaLog;

import javax.swing.ListModel;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class EmailBugReport {
	// don't instantiate me!
	private EmailBugReport() {}
	
	public static void submitBugReport() {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(App.prefs.getPref("corina.mail.mailhost"));
			email.setFrom("corina-auto-report@dendro.cornell.edu", "Corina Automated Bug Report");
			email.addTo("lucas@dendro.cornell.edu");
			
			String userName = System.getProperty("user.name", "(unknown user)");
			ListModel logEntries = CorinaLog.getLogListModel();
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
			
			for(int i = 0; i < logEntries.getSize(); i++) {
				errors.append(logEntries.getElementAt(i));
				errors.append("\r\n");
			}

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
}
