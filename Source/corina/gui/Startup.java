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
// import corina.prefs.Migrate; -- OBSOLETE?
import corina.util.Platform;
import corina.util.Macintosh;
import corina.util.Netware;

import java.io.*;
import java.io.IOException;

import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
   Bootstrap for Corina.  It all starts here...

   <h2>Left to do</h2>
   <ul>
     <li>extract Bootstrap, which will make testing much easier
         (Startup = Bootstrap + new XCorina())
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Startup {
    /**
       The <code>main()</code> method that sets all of Corina in
       motion.  Loads system and user preferences, and instantiates an
       XCorina object.

       @param args command-line arguments; ignored
    */
    public static void main(String args[]) {
	/*
	Font f = new Font("courier", java.awt.Font.PLAIN, 24);
	UIManager.put("Menu.font", f);
	UIManager.put("MenuItem.font", f);
	*/

        try {
            // if the user hasn't specified a parser with
            // -Dorg.xml.sax.driver=..., use crimson.
            if (System.getProperty("org.xml.sax.driver") == null)
		System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
            // xerces: "org.apache.xerces.parsers.SAXParser"
	    // gnu/jaxp: "gnu.xml.aelfred2.SAXDriver"

            // try to get the native L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // on a mac, always use the mac menubar -- see TN2031
            // (http://developer.apple.com/technotes/tn/tn2031.html)
	    // REFACTOR: move this to Platform?
            if (Platform.isMac) {
                // REFACTOR: make a Platform.JVMVersion field?
                if (System.getProperty("java.version").startsWith("1.4"))
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                else
                    System.setProperty("com.apple.macos.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");
                // System.setProperty("com.apple.mrj.application.live-resize", "true");

		// also, treat apps as files, not folders (duh -- why's this not default, steve?)
                System.setProperty("com.apple.macos.use-file-dialog-packages", "false"); // for AWT
                UIManager.put("JFileChooser.packageIsTraversable", "never"); // for swing
            }

            // this sets the "about..." name only -- not "hide", "quit", or in the dock.
            // have to use -X args for those, anyway, so this is useless.
            // System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");

	    // migrate old prefs (!!!)
                // WAS: Migrate.migrate();

            // load properties -- messagedialog here is UGLY!
            try {
                Prefs.load();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null,
                                              "While trying to load preferences:\n" + ioe.getMessage(),
                                              "Corina: Error Loading Preferences",
                                              JOptionPane.ERROR_MESSAGE);
            }

	    // using windows with netware, netware doesn't tell windows the real username
	    // and home directory.  here's an ugly workaround to set user.* properties,
	    // if they're there.  (old way: always call with "java -Duser.home=...",
	    // and have the user type in her name -- ugh.)  by doing this after the prefs
	    // loading, i override anything the user set in the prefs (unless they
	    // set it again -- hence it should be removed).
	    // try {
	    Netware.workaround();
	    // } catch (IOException ioe) {
	    // Bug.bug(ioe);
	    // }

	    // can't install a new default exception handler, but i can log them
	    ErrorLog.logErrors();
	    // (i COULD make this log call bug.bug() ...)

            // set up mac menubar
            Macintosh.configureMenus();
            
            // let's go...
	    XCorina.showCorinaWindow();

        } catch (Exception e) {
            Bug.bug(e);
        }
    }
}
