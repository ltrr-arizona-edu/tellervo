package org.tellervo.desktop.versioning;

import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.WSIServerDetails;


public class UpdateChecker {
	private static String latestVersionURL = "http://www.tellervo.org/updatechecker/mostrecentdesktopversion";
	private static String serverRequiredForLatestDesktop = "http://www.tellervo.org/updatechecker/serverrequiredformostrecentdesktopversion";
	private static String downloadPage = "http://www.tellervo.org/download/";
	  private final static Logger log = LoggerFactory.getLogger(UpdateChecker.class);

	  public static void doUpdateCheck(Boolean showConfirmation)
	  {
		  UpdateChecker.doUpdateCheck(showConfirmation, App.mainWindow);
	  }
	  
	public static void doUpdateCheck(Boolean showConfirmation, Component parent)
	{
		// Check server for current available version
		String availableDesktopVersion = VersionUtil.getAvailableVersion(latestVersionURL);
		String serverVersionRequiredForMostRecent = VersionUtil.getAvailableVersion(serverRequiredForLatestDesktop);
		if(availableDesktopVersion==null)
		{
			if(showConfirmation)
			{
				JOptionPane.showMessageDialog(parent, I18n.getText("view.popup.updateServerIOE"));
			}
			return;
		}
		
		// Parse string version numbers into Integers
		Integer available = 0;
		Integer current = 0;
		String currentVersion;
		try{

			currentVersion = MessageFormat.format(I18n.getText("about.revision"), new Object[] { Build.REVISION_NUMBER });
			available = Integer.parseInt(availableDesktopVersion);
			current = Integer.parseInt(currentVersion);
		} catch (NumberFormatException e)
		{
			if(showConfirmation)
			{	
				JOptionPane.showMessageDialog(parent, I18n.getText("view.popup.updateVersionParseError"));
			}
			return;
		}

		// Compare available and current build numbers
		if(available.compareTo(current)>0)
		{
			// Update required 
			String message = I18n.getText("view.popup.updateVersionAvailable");
			
			// Now check that this most recent version supports the server we are using
			try{
				WSIServerDetails serverDetails = new WSIServerDetails();
				Boolean currentServerOkWithUpdate = serverDetails.isServerValid(serverVersionRequiredForMostRecent);			
				if(!currentServerOkWithUpdate)
				{
					message = I18n.getText("view.popup.updateDesktopAndServer");
				}
			} catch (Exception ex)
			{
				log.error("Unable to check whether the update on offer will work with the user's current server");
			}
			 
			
			// Ask user what to do
			if(Desktop.isDesktopSupported())	
			{
			
				Object[] options = {"Yes",
				                    "Maybe later",
									"No, don't ask again"};
				
				int n = JOptionPane.showOptionDialog(parent, 
						message,
						"Update available",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]);

				if(n==JOptionPane.CANCEL_OPTION){
					App.prefs.setBooleanPref(PrefKey.CHECK_FOR_UPDATES, false);					
					JOptionPane.showMessageDialog(parent, "Tellervo will no longer automatically check for updates\nhowever you may still check manually using the option\nin the help menu.");					
					return;
				}
				
				if(n==JOptionPane.NO_OPTION) return;
				
				// Try to open browser
				Desktop desktop = Desktop.getDesktop();
				try {
					URI updateURL = new URI(downloadPage);
					desktop.browse(updateURL);
					return;
				} catch (URISyntaxException e) {
				} catch (IOException e) {
				}
				
				
				// Problems opening browser so get user to download manually
				JOptionPane.showMessageDialog(parent, I18n.getText("view.popup.updatePleaseDownload") +
						"\n"+downloadPage);
			}
		}
		else
		{
			// Update not required
			if(showConfirmation)
			{
				JOptionPane.showMessageDialog(parent, I18n.getText("view.popup.upToDate"));
			}
			return;
		}
	}
}
