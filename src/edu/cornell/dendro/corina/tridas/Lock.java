package edu.cornell.dendro.corina.tridas;

import java.io.File;
import java.io.IOException;

// MOVE ME: this class belongs in corina.util, not corina.site

/**
   A file locking mechanism, requiring nothing beyond what is provided
   by Java 1.1.  This uses the atomicity guaranteed by
   <code>File.createNewFile()</code>.  (The lockfile it creates for a
   file called "Data" would be called "Data - locked".  If Corina gets
   wedged, you can delete it by hand.)

   <p>It's appropriate for a relatively low-utilization fileserver
   where one filesystem is shared by multiple users.  For lock a
   single file in one JVM, use class methods and members, not the
   filesystem; for heavy-duty locking, use <a
   href="http://www.postgresql.org/">a real database</a> (which is
   more robust, and probably higher performance).</p>

   <p>Please do <em>not</em> write a native version of this.  Java's
   <code>createNewFile()</code> method guarantees atomicity, and it
   will work on any platform, so (for example) Mac and Windows
   workstations using a shared folder will all be able to do locking
   together correctly.  And creating a zero-length file will almost
   certainly be cached, so performance isn't really an issue.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Lock {
    /*
     what i do:
     -- let this instance of corina acquire a lock on a file (sitedb)
     -- release the lock
     -- try to acquire the lock, try again, and then present the user with a dialog explaining the situation
     -- (that all?)
     */

    // don't instantiate me
    private Lock() { }

    // add this suffix to a filename to create the lockfile
    private final static String SUFFIX = " - locked";

    /** Try to acquire a lock on a file.

        <p>There's nothing forcing anybody to respect this lock,
	especially other programs; you have to trust that they'll use
	this Lock class, as well.</p>

	@param filename the name of the file to lock
	@return true if the lock was acquired, else false */
    public static boolean acquire(String filename) {
        try {
            File f = new File(filename + SUFFIX);
            return f.createNewFile();
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
       Try to acquire a lock on a file.  If it's not available, keep
       trying every half second until it's available, up to a certain
       number of tries.  If it's still unavailable after that time, it
       returns false as normal.

       <p>There's nothing forcing anybody to respect this lock,
       especially other programs; you have to trust that they'll use
       this Lock class, as well.</p>

       @param filename the name of the file to lock
       @param keepTrying if locked, try again this many times, 0.5 seconds apart
       @return true if the lock was acquired, else false */
    public static boolean acquire(String filename, int keepTrying) {
        try {
            File f = new File(filename + SUFFIX);
	    for (int i=0; i<keepTrying; i++) {
		boolean x = f.createNewFile();

		// success
		if (x)
		    return x;

		// failure -- pause, and have another try
		if (i < keepTrying-1) {
		    try {
			Thread.sleep(500);
		    } catch (InterruptedException ie) {
			// ignore
		    }
		}
	    }

	    // WRITEME: this method doesn't have a unit test.

	    // no good.  oh well.
	    return false;
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
       Release the lock on a file.  If that file wasn't locked, does nothing.

       <p>There's no explicit <code>steal()</code> method, because Lock doesn't
       provide any assurance that this file is truly locked, aside from trusting
       that you will call <code>acquire()</code> to check first.  If you really
       need to steal a lock, just do whatever you were going to do - but bad
       things will happen.</p>

<!-- note: if i want to store useful information in the lockfile, like IP/username/PID/timestamp, then i'll need a steal() method -->

       @param filename the name of the file to release the lock on
    */
    public static void release(String filename) {
        File f = new File(filename + SUFFIX);
        f.delete(); // don't really care if it fails, do i?
    }

    /*
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
        summary.setFont(summary.getFont().deriveFont(Font.BOLD));
        JLabel description = new JLabel("The Corina site data..."); // i really need a multi-line jlabel -- WRITEME
        // ...
        // TODO: put label+label+[steal/null/cancel/wait] in a vboxlayout?
        // ---- steal: return true
        // ---- cancel: return false
        // ---- wait: wait 1s, and try again, etc.
    }
    */

    // semi-related...
    private static boolean canWrite(String filename) {
        File f = new File(filename);
        if (f.canWrite())
            return true;
        // -- show dialog
        // ---- cancel
        // ---- try again
        return false;
    }
}
