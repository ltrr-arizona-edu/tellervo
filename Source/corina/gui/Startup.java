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
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.gui;

import corina.prefs.Prefs;
import corina.util.Platform;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
   <p>Bootstrap for Corina.  It all starts here...</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Startup {

    /** <p>The <code>main()</code> method that sets all of Corina in
       motion.  Loads system and user preferences, and instantiates an
       XCorina object.</p>
       @param args command-line arguments; ignored */
    public static void main(String args[]) throws Exception {
	// if the user hasn't specified a parser with
	// -Dorg.xml.sax.driver=..., use gnujaxp.
	if (System.getProperty("org.xml.sax.driver") == null)
	    System.setProperty("org.xml.sax.driver", "gnu.xml.aelfred2.SAXDriver");
	// xerces is "org.apache.xerces.parsers.SAXParser"

        // on a mac, always use the mac menubar (yay!)
        if (Platform.isMac) {
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");
            System.setProperty("com.apple.mrj.application.live-resize", "true");
        }

	// try to get the native L&F
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

	// first time, show the about box, which contains some
	// copyright information; don't display it again.
	if (Prefs.firstRun())
	    new AboutBox();

	// load properties -- messagedialog here is UGLY!
	try {
	    Prefs.load();
	} catch (IOException ioe) {
	    JOptionPane.showMessageDialog(null,
					  "While trying to load preferences:\n" + ioe.getMessage(),
					  "Corina: Error Loading Preferences",
					  JOptionPane.ERROR_MESSAGE);
	}

	// let's go...
	new XCorina();
    }
}
