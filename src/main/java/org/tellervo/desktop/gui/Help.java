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

package org.tellervo.desktop.gui;

import java.awt.event.ActionEvent;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.jpedal.examples.simpleviewer.Commands;
import org.jpedal.examples.simpleviewer.SimpleViewer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Alert;



public class Help {


	/**
	 * The base url of the help pages
	 */
	public static String baseURI = "http://dendro.cornell.edu/corina-manual/";
	
	/**
	 * Open the help page index
	 */
	@SuppressWarnings("deprecation")
	public static void showHelpIndex() 
	{
		//Help.showHelpPage("UserGuideContents");
		//Help.showExternalPage("http://dendro.cornell.edu/corina/documentation/corina-manual.pdf");
		
		@SuppressWarnings("unused")
		String[] locations = new String[] {
				"http://dendro.cornell.edu/corina/documentation/corina-manual.pdf" ,
			};

		SimpleViewer pdf = new SimpleViewer();
		pdf.setupViewer();
		
		pdf.openDefaultFile("http://dendro.cornell.edu/corina/documentation/corina-manual.pdf");
		


		//reads tree and populates lookup table
		String bookmark = "Installation";
		String bookmarkPage=pdf.getSwingGUI().getBookmark(bookmark);

		if(bookmarkPage==null)
		{
			Alert.error("Bookmark not found", "");
		}
			//throw new PdfException("Unknown bookmark "+bookmark);

		int page=Integer.parseInt((String)bookmarkPage);
		Object[] input = new Object[]{page}; 
		pdf.executeCommand(Commands.GOTO, input);
		
		
		
		
		//pdf.openDefaultFileAtPage(defaultFile, page)
		
		/*JDialog dialog = new JDialog();
		dialog.setContentPane(pdfHelpPanel);
		dialog.setIconImage(Builder.getApplicationIcon());
		dialog.setTitle("Help");
		dialog.pack();
		dialog.setVisible(true);*/
		
		
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
	public static void openPage(URI uri)
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
	public static boolean exists(URI uri){
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
