package corina.util;

import javax.swing.JFrame;

// platform-specific crap that java can't take care of.
// (gee, thanks, james.)

public class Platform {
    // method from TN2042, http://developer.apple.com/technotes/tn/tn2042.html
    public static final boolean isMac = (System.getProperty("mrj.version") != null);
    public static final boolean isWindows = (System.getProperty("os.name").indexOf("Windows") != -1);

    // mac-only -- windows prepends "* "
    public static void setModified(JFrame window, boolean mod) {
        if (isMac)
            window.getRootPane().putClientProperty("windowModified",
                                                   mod ? Boolean.TRUE : Boolean.TRUE); // no news is good news

        // REFACTOR: add this to xframe, or whatever my document window is
    }
}
