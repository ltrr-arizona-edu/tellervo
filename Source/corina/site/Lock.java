package corina.site;

import corina.gui.ButtonLayout;
// and others

import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
// and others
import javax.swing.*;
import java.awt.*;

public class Lock {
    /*
     what i do:
     -- let this instance of corina acquire a lock on a file (sitedb)
     -- release the lock
     -- try to acquire the lock, try again, and then present the user with a dialog explaining the situation
     -- (that all?)
     */

    // low-level locking
    private final static String SUFFIX = " - locked";
    public static boolean acquire(String filename) {
        try {
            File f = new File(filename + SUFFIX);
            return f.createNewFile();
        } catch (IOException ioe) {
            return false;
        }
    }
    // there's no explicit steal() method -- to do that, just do whatever you were going to do
    // (unless i want to store something useful in the lockfile, like the IP/username/PID/timestamp -- then i'd need one)
    public static void release(String filename) {
        File f = new File(filename + SUFFIX);
        f.delete(); // don't really care if it fails, do i?
    }

    public static void acquireButWait(String filename) {
        // strategy:
        // -- try to acquire
        boolean test = acquire(filename);
        if (test)
            return;
        // -- if no success, wait 1s, and try again
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            // ignore
        }
        test = acquire(filename);
        if (test)
            return;
        // -- (repeat?)
        // ???
        // -- show dialog
        JLabel summary = new JLabel("The Corina site data appears to be locked.");
        Font oldFont = summary.getFont();
        summary.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()));
        JLabel description = new JLabel("The Corina site data..."); // i really need a multi-line jlabel -- WRITEME
        // ...
        // TODO: put label+label+[steal/null/cancel/wait] in a vboxlayout?
        // ---- steal: return true
        // ---- cancel: return false
        // ---- wait: wait 1s, and try again, etc.
    }

    // semi-related...
    public static boolean canWrite(String filename) {
        File f = new File(filename);
        if (f.canWrite())
            return true;
        // -- show dialog
        // ---- cancel
        // ---- try again
        return false;
    }

    // debugging
    public static void main(String args[]) {
        String filename = args[0];
        System.out.println("acquiring lock...");
        boolean lock = acquire(filename);
        if (!lock) {
            System.out.println("not available!");
            System.exit(0);
        }
        System.out.println("got it!");
        System.out.println("acquiring lock...");
        boolean lock2 = acquire(filename);
        if (!lock2) {
            System.out.println("not available!");
        } else {
            System.out.println("got it!");
        }
        System.out.println("releasing lock...");
        release(filename);
        System.out.println("gone");
        System.out.println("acquiring lock...");
        boolean lock3 = acquire(filename);
        if (!lock3) {
            System.out.println("not available!");
        } else {
            System.out.println("got it!");
        }
        System.out.println("releasing lock...");
        release(filename);
        System.out.println("gone");
    }
}
