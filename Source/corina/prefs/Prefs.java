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

package corina.prefs;

import corina.util.Platform;
import corina.util.JDisclosureTriangle;
import corina.util.JLinedLabel;
import corina.gui.Bug;
import corina.ui.I18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
    Storage and access of user preferences.

    <h2>Left to do</h2>
    <ul>
        <li>switch from System.getProperty() to my own Properties object
            (save all, since i'll have that hash table to myself)
        <li>add getFont(), setColor(), etc. convenience methods
        <li>get rid of JDisclosureTriangle usage (and then delete it)
        <li>get rid of "system" preferences (and file Source/prefs.properties) -
            these should be inline, in each subsystem, with the prefs call
        <li>extract prefs file location to platform?
        <li>(goal: 300 lines for everything, maybe 350)
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class Prefs {
    /*
      OLD:
      ~/xcorina/prefs.properties [win32]
      ~/.corina/prefs.properties [unix]
      ~/Library/Corina/prefs.properties [mac]

      NEW:
      ~/Corina Preferences [win32]
      ~/.corina [unix]
      ~/Library/Preferences/Corina Preferences [mac]
    */

    private final static String FILENAME;
    static {
	String home = System.getProperty("user.home");
	if (!home.endsWith(File.separator))
	    home = home + File.separator;

	if (Platform.isWindows)
	    FILENAME = home + "Corina Preferences";
	else if (Platform.isMac)
	    FILENAME = home + "Library/Preferences/Corina Preferences"; // why in prefs?  isn't lib ok?
	else // plain ol' unix
	    FILENAME = home + ".corina";
    }

    /** Load system and user properties (preferences).  This runs 2 steps:

	<ol>

        <li>Load system properties, by loading ...

        <li>Load user's properties, by loading ...

	</ol>

	Note that this uses a Properties object, gotten from
	System.getProperties(), to load the properties into.  This is
	because Unix and Win32 behavior differs: on one,
	System.setProperties(p) adds the properties in p to the
	existing properties, while the other replaces the existing
	properties with p.  (I don't remember offhand which is which.)
	As usual, the API docs don't really specify.  Grrr... */
    public synchronized static void load() throws IOException {
        // get existing properties
        Properties p = System.getProperties();

        // a place to record errors that may occur, because we don't
        // want to crap out before we've tried everything.
        String errors="";

        // load system properties as a resource from this jar
        try {
          ClassLoader cl = Class.forName("corina.prefs.Prefs").getClassLoader();
          java.io.InputStream is = cl.getResourceAsStream("prefs.properties");
          if (is != null) {
            try {
              p.load(is);
            } finally {
              is.close();
            }
          }
	        // RENAME this?  ("Default Corina Preferences")
        } catch (IOException ioe) {
            errors += "Error loading Corina's default preferences (bug!).";
        } catch (ClassNotFoundException cnfe) {
            Bug.bug(cnfe);
        }

        // get user properties (with a hack to preserve user.name)
        try {
	    String n = p.getProperty("user.name"); // HACK!!!
            p.load(new FileInputStream(FILENAME));
	    p.setProperty("user.name", n);
        } catch (FileNotFoundException fnfe) {
            // user doesn't have a properties file, so we'll give her
            // one!  the system properties were already loaded, so all
            // i need to do is call save() now.

            // (p->system properties, for save())
            System.setProperties(p);

            try {
		// this is the guts of save(), but without the nice error handling.
		Properties pp = getCorinaProperties();
		pp.store(new FileOutputStream(FILENAME), "Corina user preferences");
            } catch (IOException ioe) {
                errors += "Error copying preferences file to your home directory: " +
                ioe.getMessage();
            }
        } catch (IOException ioe) {
            errors += "Error loading user preferences file: " + ioe.getMessage();
        }

        // set properties
        System.setProperties(p);

        // if there was an exception thrown-and-caught, re-throw it now
        if (errors.length() != 0)
            throw new IOException(errors);
    }

    // copy corina properties to new property list
    private static Properties getCorinaProperties() {
        // FUTURE: return (Properties) hashtable.clone();

	Properties p = new Properties();
	List options = PrefsTemplate.getOptions();
	for (int i=0; i<options.size(); i++) {
	    PrefsTemplate.Option o = (PrefsTemplate.Option) options.get(i);
	    String v = System.getProperty(o.property);
	    if (v != null)
		p.setProperty(o.property, v);
	}
	return p;
    }

    /** Save current properties to the user's system-writable
	properties (preferences) file,
	<code>FILENAME</code>. */
    public synchronized static void save() {
	// get corina prefs
	Properties p = getCorinaProperties();

	// try to save prefs, and on failure present "try again?" dialog.
	for (;;) {
	    try {
                // -- p.store() buffers internally, so if i used a buffered stream
                // here it would only hurt performance.
                // -- the second string passed to store() is a comment line which
                // is added to the top of the file.
		p.store(new FileOutputStream(FILENAME), "Corina user preferences");
		return;
	    } catch (IOException ioe) {
		if (dontWarn)
		    return;

		boolean tryAgain = cantSave(ioe);
		if (!tryAgain)
		    return;
	    }
	}
    }

    // if true, silently ignore if the prefs can't be saved.
    private static boolean dontWarn = false;

    // exception |e| thrown while saving; tell user.
    // return value: true => "try again".
    // TODO: use jlinedlabel to be more explicit about the problem
    // TODO: (need left-alignment option on that class, first)
    private static boolean cantSave(Exception e) {
	JPanel message = new JPanel(new BorderLayout(0, 8)); // (hgap,vgap)
	message.add(new JLabel(I18n.getText("prefs_cant_save")), BorderLayout.NORTH);

	// -- dialog with optionpane (warning?)
	JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
	JDialog dialog = optionPane.createDialog(null /* ? */, I18n.getText("prefs_cant_save_title"));

	// -- buttons: cancel, try again.
	optionPane.setOptions(new String[] { I18n.getText("try_again"), I18n.getText("cancel") });

	// -- disclosure triangle with scrollable text area: click for details... (stacktrace)
	JComponent stackTrace = new JScrollPane(new JTextArea(Bug.getStackTrace(e), 10, 60));
	JDisclosureTriangle v = new JDisclosureTriangle(I18n.getText("click_for_details"),
							stackTrace, false);
	message.add(v, BorderLayout.CENTER);

	// -- checkbox: don't warn me again
	JCheckBox dontWarnCheckbox = new JCheckBox(I18n.getText("dont_warn_again"), false);
	dontWarnCheckbox.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    dontWarn = !dontWarn;
		}
	    });

	// FIXME: consolidate |message| panel construction with Layout methods
	message.add(dontWarnCheckbox, BorderLayout.SOUTH);

	// show dialog
	dialog.pack();
	dialog.setResizable(false);
	dialog.show();

	// return true iff "try again" is clicked
	return optionPane.getValue().equals(I18n.getText("try_again"));
    }

    // --------------------------------------------------
    // new prefs api below here

    /*
      TODO:
      -- set/get any data type
      -- automatically save
      -- (but not right away, which would be slow)
      -- defaults -- required?
      -- convenience functions for non-atomic things like window geometries?
      -- also, lists of data
      -- prefs event
      -- prefs listener
      -- on prefs.set, fire events
      -- get rid of all refreshFromPrefs() methods, HasPreferences interface
      -- (register listener with type, like jrendezvous?)
    */

    // just wrappers, for now
    public static void setPref(String pref, String value) {
	System.setProperty(pref, value);
	firePrefChanged(pref);
	save();
    }
    public static String getPref(String pref) { // TODO: require default?
	return System.getProperty(pref);
    }

    // event model -- a standard event model, except that it's entirely static
    // (and "this" changed to "Prefs.class")
    private static Vector listeners = new Vector();

    // IDEA: addPrefsListener(l, String pref)?
    public static synchronized void addPrefsListener(PrefsListener l) {
	if (!listeners.contains(l))
	    listeners.add(l);
    }
    public static synchronized void removePrefsListener(PrefsListener l) {
	listeners.remove(l);
    }
    public static void firePrefChanged(String pref) {
	// alert all listeners
	Vector l;
	synchronized (Prefs.class) {
	    l = (Vector) listeners.clone();
	}

	int size = l.size();

	if (size == 0)
	    return;

	PrefsEvent e = new PrefsEvent(Prefs.class, pref);

	for (int i=0; i<size; i++) {
	    PrefsListener listener = (PrefsListener) l.elementAt(i);
	    listener.prefChanged(e);
	}
    }
}
