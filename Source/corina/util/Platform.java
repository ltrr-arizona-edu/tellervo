package corina.util;

import corina.gui.Bug;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

// platform-specific crap that java can't take care of.
// (gee, thanks, james.)

public class Platform {
    // method from TN2042, http://developer.apple.com/technotes/tn/tn2042.html
    public static final boolean isMac = (System.getProperty("mrj.version") != null);
    public static final boolean isWindows = (System.getProperty("os.name").indexOf("Windows") != -1);
    public static final boolean isUnix = (!isMac && !isWindows); // assume it's one of mac, win32, unix
    // (make these private?)

    // open a folder in the system file browser
    public static void open(String folder) {
	if (isWindows) {
	    try {
		// if file, "explorer /select,FILENAME"
		// else, "explorer FILENAME"
		boolean isDir = new File(folder).isDirectory();
		Runtime.getRuntime().exec(new String[] {
					      "explorer",
					      (isDir ? folder : "/select," + folder) });

		// TODO: this is completely untested!

		// note: in my old SiteInfo.java, i used
		// ("c:\\winnt\\system32\\cmd.exe" "/c" "start" folder)
	    } catch (IOException ioe) {
		Bug.bug(ioe);
	    }
	    return;
	}

	// REFACTOR: combine these if-catch-statements!

	if (isMac) {
	    try {
		Runtime.getRuntime().exec(new String[] {
					      "open", "-a",
					      "/System/Library/CoreServices/Finder.app/",
					      folder });

		// REFACTOR: make into methods openFolder(folder, file), openFolder(folder)?

		// TODO: should i snarf up stdout/stderr?
		// i think mac is smart enough i don't have to.

		// (don't bother watching return value; it can't fail)

		// note: in my old SiteInfo.java, i used
		// ("/usr/bin/open" folder)
	    } catch (IOException ioe) {
		Bug.bug(ioe);
	    }
	    return;
	}

	// what to do on unix?  gmc, konqueror, xterm?
	new Bug(new IllegalArgumentException("Platform.open() not implemented on unix yet!"));
    }

    // get the name of the trash folder
    public static String getTrash() {
	if (isWindows)
	    return "C:\\recycled\\"; // do they still not have a per-user trash?  just use the trash on C:\.

	if (isMac)
	    return System.getProperty("user.home") + "/.Trash/";

	return null; // what to do on unix?
    }

    // mac-only -- windows prepends "* "
    public static void setModified(JFrame window, boolean mod) {
        if (isMac)
            window.getRootPane().putClientProperty("windowModified",
                                                   mod ? Boolean.TRUE : Boolean.TRUE); // no news is good news

        // REFACTOR: add this to xframe, or whatever my document window is
	// (is that really what i want?)
    }

    // for DnD, the key you press to copy, instead of move.
    public static String getCopyModifier() {
	if (isMac)
	    return "alt"; // option, really
	else
	    return "control";
	// i have no idea what it is on generic unix.  does motif specify?
    }
}
