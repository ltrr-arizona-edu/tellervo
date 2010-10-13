//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.gui;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.AbstractAction;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.ui.Alert;

import java.awt.event.ActionEvent;


public class Help {


	/**
	 * The base url of the help pages
	 */
	public static String baseURI = "http://dendro.cornell.edu/corina-manual/";
	
	/**
	 * Open the help page index
	 */
	public static void showHelpIndex() 
	{
		Help.showHelpPage("UserGuideContents");	
	}
	
	/**
	 * Open a specific help page 
	 * 
	 * @param pagename
	 */
	public static void showHelpPage(String pagename)
	{
		try {
			URI uri = new URI(baseURI+pagename);
			Help.openPage(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert.error("Error", "Help page cannot be found.");
		}
	}
	
	/**
	 * Open an external webpage
	 * 
	 * @param pagename
	 */
	public static void showExternalPage(String pagename)
	{
		try {
			URI uri = new URI(pagename);
			Help.openPage(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert.error("Error", "Help page cannot be found.");
		}
	}
	
	
	/**
	 * Actually open the page
	 * 
	 * @param uri
	 */
	private static void openPage(URI uri)
	{
		if(Help.exists(uri))
		{
			App.platform.openURL(uri);
		}
		else
		{
			Alert.error("Error", "Help page cannot be found.");
		}

	}

	/**
	 * Check whether the specified URI exists
	 * 
	 * @param uri
	 * @return
	 */
	private static boolean exists(URI uri){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con =
	    	  (HttpURLConnection) uri.toURL().openConnection();
	      con.setRequestMethod("HEAD");
	      	return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	}
    
	/**
	 * Wires up a button to open a specific help page
	 * 
	 * @param button
	 * @param page
	 */
	public static void assignHelpPageToButton(JButton button, final String page) 
	{
		assignHelpPageToButton(button, page, false);
    }
	
	/**
	 * Wires up a button to open a specific page.  If external is true, then
	 * then 'page' should be the name of the help page in the Corina wiki.  Otherwise
	 * 'page' should be a complete URL.
	 * 
	 * @param button
	 * @param page
	 * @param external
	 */
	public static void assignHelpPageToButton(JButton button, final String page, Boolean external) 
	{
		if(external)
		{
			button.addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
						Help.showExternalPage(page);
					}
			    });
		}
		else
		{
			button.addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
						Help.showHelpPage(page);
					}
			    });
		}
    }


}
