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
    public static void main(String args[]) {
        try {
            // if the user hasn't specified a parser with
            // -Dorg.xml.sax.driver=..., use crimson.
            if (System.getProperty("org.xml.sax.driver") == null)
		System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
            // xerces: "org.apache.xerces.parsers.SAXParser"
	    // gnu/jaxp: "gnu.xml.aelfred2.SAXDriver"

            // on a mac, always use the mac menubar -- see TN2031
            // (http://developer.apple.com/technotes/tn/tn2031.html)
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

            // on Mac, treat applications as files, not folders (duh -- why's this not default, steve?)
            if (Platform.isMac) {
                System.setProperty("com.apple.macos.use-file-dialog-packages", "false"); // for AWT
                UIManager.put("JFileChooser.packageIsTraversable", "never"); // for swing
            }

            // this sets the "about..." name only -- not "hide", "quit", or in the dock.
            // have to use -X args for those, anyway, so this is useless.
            // System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");
            
            // load properties -- messagedialog here is UGLY!
            try {
                Prefs.load();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null,
                                              "While trying to load preferences:\n" + ioe.getMessage(),
                                              "Corina: Error Loading Preferences",
                                              JOptionPane.ERROR_MESSAGE);
            }

	    /*
	    // install a default-uncaught-exception-handler
	    ThreadGroup tg = new ThreadGroup("safe") {
		    public void uncaughtException(Thread t, Throwable e) {
			Bug.bug(e);
		    }
		};
	    // Q: what's the problem?
	    // A: i can only override uncaughtException() on new threadgroups,
	    // and i can't move threads between groups, so AWT event handling
	    // can never be caught by an uncaughtException() method i write (right?)
	    Thread t = new Thread(tg, "countdown") {
		    public void run() {
			System.out.println("counting down from 10...");
			for (int i=9; i>0; i--) {
			    System.out.println(i);
			    try {
				Thread.sleep(1000);
			    } catch (InterruptedException ie) {
				// ignore
			    }
			}
			System.out.println("zero!");
			throw new IllegalArgumentException("die die die!");
		    }
		};
	    t.start();
	    */

	    // so let's do the next-best thing: dump all uncaught exceptions to a file.
	    // (then later, maybe on quit, i can offer to mail them to me, or post them, or some such)
	    // WRITE ME
	    // Q: delete it on quit if there's no data in it?
	    // Q: what filename to use?  "${username} - ${time}" would be good.
	    // Q: where to put it?  wd?  Logs/?  C:\winnt\logs?  $(TMP)?
	    // System.setErr(new PrintStream(new OutputStream


            // let's go...
            new XCorina();
        } catch (Exception e) {
            Bug.bug(e);
        }
    }
}
