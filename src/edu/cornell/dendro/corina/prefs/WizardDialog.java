package edu.cornell.dendro.corina.prefs;

import javax.swing.JOptionPane;

import edu.cornell.dendro.corina.core.App;


public class WizardDialog{
		
	   	   
	   public WizardDialog(PreferencesDialog prefsDialog){
		   int response = JOptionPane.showConfirmDialog(null, "Welcome to Corina.\nThis appears to be the first time you are running Corina. Would you like to configure a web service?", "Corina First-Use Setup Wizard", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		   if (response == JOptionPane.YES_OPTION)
		   {
			   String url = JOptionPane.showInputDialog(null, "Please enter the web service url.", "Corina First-Use Setup Wizard", JOptionPane.PLAIN_MESSAGE);
			   App.prefs.setPref("corina.webservice.url", url);
		   }
		   response = JOptionPane.showConfirmDialog(null, "Would you like to set up a measuring platform?", "Corina First-Use Setup Wizard", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		   if (response == JOptionPane.YES_OPTION)
		   {
			   prefsDialog.showHardPrefsPanel();
			   prefsDialog.setVisible(true);
		   }
	   }
}
