package corina.util;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

// if you just want to copy/paste plain text on the clipboard, the
// standard interface is too complex.
public class TextClipboard {

    // String -> clipboard
    public static void copy(String s) {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection t = new StringSelection(s);
        c.setContents(t, t);
    }

    // my paste() is really messed up.  what was i thinking?
    // clipboard -> String
    //    public static String paste() {
    // WRITEME
    //    }
}
