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

import corina.gui.Bug;

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
import java.util.StringTokenizer;

/**
   Static class for loading and saving preferences.

   Note that this doesn't store the preferences per se; that's done
   by System properties.  This class only provides methods for loading
   properties, saving properties, and making sense of them via
   Prefs.Option.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Prefs {

    /** Name of the properties directory, as a subdirectory of
        user.home: ".corina", except on
        Windows (where there are a lot of silly restrictions on what
        filenames can be) where it's "xcorina", and MacOS where
        it's the incredibly elegant "Library/Corina".  You're
        assumed to be on Windows if the Java property
        os.name starts with "Windows", and on MacOS if it starts with "Mac". */
    public final static String USER_PROPERTIES_DIR;
    /*
     this is BAD.  it should be called "Corina Preferences" (except on unix where it's .corina).
     it should be a file, not a folder.
     (open-recent needs to be merged into the prefs framework)
     so:
     unix = ~/.corina
     mac = ~/Library/Preferences/Corina Preferences
     win32 = ~/Corina Preferences
     (unless there's a better place to put win32 prefs, but i don't know one)
     */
    static {
        String home = System.getProperty("user.home") + File.separator;
        
	if (System.getProperty("os.name").startsWith("Windows"))
            // for lack of a  better place -- FIXME: MIGRATE TO "corina"!
	    USER_PROPERTIES_DIR = home + "xcorina";
	else if (System.getProperty("os.name").startsWith("Mac"))
            // this check belongs elsewhere, refactor!  (XMenubar.isMac() also exists)
	    USER_PROPERTIES_DIR = home + "Library/Corina";
	else // plain ol' unix
	    USER_PROPERTIES_DIR = home + ".corina";
    }

    /** Name of the system properties file: prefs.properties */
    public final static String SYSTEM_PROPERTIES_FILE = "prefs.properties";

    /** Name of the user properties file: prefs.properties */
    public final static String USER_PROPERTIES_FILE = USER_PROPERTIES_DIR + File.separator +
                                                      "prefs.properties";

    /** Load system and user properties (preferences).

	This runs 2 steps:

	<ol>

        <li>Load system properties, by loading <code>SYSTEM_PROPERTIES_FILE</code>
        from inside the jar itself

        <li>Load user's properties, by loading
        <code>USER_PROPERTIES_FILE</code> (in
        <code>USER_PROPERTIES_DIR</code>, in the user's home directory)

	</ol>

	Note that this uses a Properties object, gotten from
	System.getProperties(), to load the properties into.  This is
	because Unix and Win32 behavior differs: on one,
	System.setProperties(p) adds the properties in p to the
	existing properties, while the other replaces the existing
	properties with p.  (I don't remember offhand which is which.)
	As usual, the API docs don't really specify.  Grrr... */
    public static void load() throws IOException {
        // get existing properties
        Properties p = System.getProperties();

        // a place to record errors that may occur, because we don't
        // want to crap out before we've tried everything.
        String errors="";

        // load system properties
        try {
            ClassLoader cl = Class.forName("corina.prefs.Prefs").getClassLoader();
            p.load(cl.getResource(SYSTEM_PROPERTIES_FILE).openStream());
        } catch (IOException ioe) {
            errors += "Error loading Corina's default preferences (bug!).";
        } catch (ClassNotFoundException cnfe) {
            Bug.bug(cnfe);
        }

        // make sure the user has a .corina directory; silently create, if absent
        File dir = new File(USER_PROPERTIES_DIR);
        if (!dir.exists())
            dir.mkdir();

        // get user properties
        try {
            p.load(new FileInputStream(USER_PROPERTIES_FILE));
        } catch (FileNotFoundException fnfe) {
            // user doesn't have a properties file, so we'll give her
            // one!  the system properties were already loaded, so all
            // i need to do is call save() now.

            // (p->system properties, for save())
            System.setProperties(p);

            try {
                save();
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

    // BUG?  why's save() not synch?  (actually, save and load should both synch on the same ref.)
    
    /** <p>Save current properties to the user's system-writable
	properties (preferences) file,
	<code>USER_PROPERTIES_FILE</code>.</p> */
    public static void save() throws IOException {
	// copy corina properties to new property list
	Properties p = new Properties();
	List options = PrefsTemplate.getOptions();
	for (int i=0; i<options.size(); i++) {
	    PrefsTemplate.Option o = (PrefsTemplate.Option) options.get(i);
	    String v = System.getProperty(o.property);
	    if (v != null)
		p.setProperty(o.property, v);
	}

	// save -- (store() uses a bufferedoutputstream)
	p.store(new FileOutputStream(USER_PROPERTIES_FILE), "Corina user preferences");
    }

    // true iff there's no .corina (or similar) directory
    public static boolean firstRun() {
	File dir = new File(USER_PROPERTIES_DIR);
	return !dir.exists();
    }

    // from "1 2 3", extract int[] { 1, 2, 3 }
    // (seems as good a place as any for this, since it's prefs-related.)
    public static int[] extractInts(String s) {
        StringTokenizer tok = new StringTokenizer(s, " ");
        int n = tok.countTokens();
        int r[] = new int[n];
        for (int i=0; i<n; i++)
            r[i] = Integer.parseInt(tok.nextToken());
        return r;
    }
}
