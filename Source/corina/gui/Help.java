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

package corina.gui;

import java.net.URL;
import java.net.MalformedURLException;

import javax.help.HelpSet;
import javax.help.HelpBroker;

import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
   A class for adding <a href="http://java.sun.com/products/javahelp/">JavaHelp</a>
   to components.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Help {

    /*
        disclaimer: i only barely figured out how to make javahelp work.
        it's not very well documented, and its error-handling/reporting
        is miserable.  (i plan to abandon javahelp in the future for
        these and other reasons.)  so no, i don't really understand how
        this code works.  i copied it straight from the javahelp examples.
    */

    private Help() {
        // don't instantiate me
    }

    private static HelpSet hs=null;
    private static HelpBroker hb=null;

    private static void createHelpSet() {
	try {
	    // Corina help, in the corina/manual/ folder of Corina.jar
	    ClassLoader loader = corina.gui.Help.class.getClassLoader();
            // URL url = loader.getResource("javahelp/jhelpset.hs");
            URL url = loader.getResource("corina/manual/jhelpset.hs");
	    hs = new HelpSet(null, url);
	} catch (Exception e) {
	    // (im)possible exceptions:
	    // MalformedURLException, ClassNotFoundException, HelpSetException
	    Bug.bug(e);
	}
    }

    /**
       Shows the help window.  It opens to the page the user was last
       looking at, or the table of contents, if the user hasn't used
       the help browser yet.
    */
    public static void showHelp() {
	/*
	if (hs == null) {
	    createHelpSet();
	    hb = hs.createHelpBroker();
	}

	hb.setDisplayed(true);
	*/

	// pure java way
	HelpBrowser.showHelpBrowser();
    }

    /**
       Attaches a listener to the specified button, so that clicking
       on the button shows the help window.  Instead of simply opening
       a help window, it jumps to a specified page of the manual, as
       specified by the <code>id</code> argument.  This corresponds to
       the "id" attribute of the DocBook source, so a chapter
       specified by

       <pre>
         &lt;chapter id="crossdating"&gt;
       </pre>

       will get opened by a button defined by

       <pre>
         Help.addToButton(helpButton, "crossdating");
       </pre>

       <p>Use this method whenever possible, instead of simply adding an
       action to call <code>showHelp()</code>, because it's much more
       helpful to show help that's most relevant to what the user is
       doing right now.</p>
       
       <p>Note: if you see a line in the error log that says simply
       "trouble in HelpActionListener", that's straight out of JavaHelp.
       JavaHelp printed that message (instead of throwing an exception),
       not Corina.  It usually means it was asked to go to a section of
       the manual by an ID that doesn't exist.</p>

       @param button the button to add help to; it's almost always
       called "Help" (or some localization of that)
       @param id the id string in the manual that clicking this button
       should jump to
    */
    public static void addToButton(JButton button, String id) {
	/*
	// redundant?
	if (hs == null) {
	    createHelpSet();
	    hb = hs.createHelpBroker();
	}

	hb.enableHelpOnButton(button, id, hs);
	*/

	// pure-java method:
	button.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    showHelp();
		}
	    });
    }

    public static void main(String args[]) {
	showHelp();
    }
}
