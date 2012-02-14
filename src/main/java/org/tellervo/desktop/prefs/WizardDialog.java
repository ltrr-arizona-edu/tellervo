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
package org.tellervo.desktop.prefs;

import javax.swing.JOptionPane;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;



public class WizardDialog{
		
	   	   
	   public WizardDialog(PreferencesDialog prefsDialog){
		   int response = JOptionPane.showConfirmDialog(null, "Welcome to Corina.\nThis appears to be the first time you are running Corina. Would you like to configure a web service?", "Corina First-Use Setup Wizard", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		   if (response == JOptionPane.YES_OPTION)
		   {
			   String url = JOptionPane.showInputDialog(null, "Please enter the web service url.", "Corina First-Use Setup Wizard", JOptionPane.PLAIN_MESSAGE);
			   App.prefs.setPref(PrefKey.WEBSERVICE_URL, url);
		   }
		   response = JOptionPane.showConfirmDialog(null, "Would you like to set up a measuring platform?", "Corina First-Use Setup Wizard", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		   if (response == JOptionPane.YES_OPTION)
		   {
			   prefsDialog.showHardPrefsPanel();
			   prefsDialog.setVisible(true);
		   }
	   }
}
