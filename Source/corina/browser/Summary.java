package corina.browser;

import corina.Element;
import corina.util.Platform;

import java.io.File;
import java.io.IOException;

public class Summary {

    /*
     methods:
     -- how to name (.summary / summary.sys)
     -- create (for a given folder)
     -- load from disk (actually, load all metadata, statting the files first, then loading them if need be.)
     -- save to disk
     -- i/o access (file locking?)
     */

    // save all elements to ???
    private void save() {
        /*
         format:
         "SAMPLE:filename.blah
         field1=val1
         field2=val2
         field3=val3
         SAMPLE:filename2.blah
         field1=val1
         ..."
         -- what about multi-line comments fields?  => they're the only multi-line field, and i use |, anyway
         -- this only saves SAMPLE metadata, not file metadata (modified, size, etc.)
         -- this needs to store the range, too -- "range = 1001-1036" -- make that a special case in both load/save.
         */
    }

    // load elemens from file.
    public void load() {
        // load into a List of Elements
        // --> should Row() be built from an Element, rather than a filename, then?  or Row.setElement(blah)?
    }
    
    /*
     design notes:

     in each directory, create a .summary file (win32: summary.sys?  make it hidden, whatever it is), that looks like
     chil001.crn	pith=P range=blah this=that ...
     chil002.crn	...
     (no folders)

     to load the metadata for an entire directory,
     -- snarf up the summary file.  that'll get most of 'em.
     -- stat each file for size, moddate, etc.  (you'd need to do this, anyway)
     -- for each file that isn't in the summary file, or whose moddate is more recent than the summary file's moddate,
     -- load that file
     -- put its data into the summary file
     -- for files that no longer exist on disk, remove from the summary
     -- when done, re-save the summary file

     -- encapsulate this in its own class, browser.Summary
     -- I/O on the summary file had better be
     -- fast. (or it's not worth the trouble)
     -- in the background. (or it'll be frustrating for users)
     -- thread (and process) safe.  (or multiple users could screw each other up)

     -- how about for the ITRDB?
     -- either don't use one (slow!  bad idea!), or
     -- put it in a system-shared place, like {corina.dir.data}/itrdb/<blah>
     */

    /* issue: what if one person changes a file while somebody else has a browser window open?
    it won't know to update itself, so it'll be out-of-date.  ouch.
possibility: re-stat the summary file fairly often (10-15s?), and make sure file edits make it back there immediately.
    */

    public Summary(String folder) {
        this.folder = folder;

        // load summary file
        // WRITEME
    }

    private String folder;

    private File lock;

    // create Element, any which way you can.
    // -- best case, stat() the filename, it's already in the summary (which is in memory), so just return that.
    // -- worst case, stat() the filename, it's newer than the summary file (or not there), so load it.
    public Element getElement(String filename) {
        return null;
    }

    // no: getElement() gets it if i know it, otherwise, returns null (gah!)
    // if it's not there, provide a way to load it, like loadElement()
    // (which also updates the summary file)

    // REFACTOR: i now have a Lock class (it's in site right now, but util might be a better location)

    private void acquireLock() throws IOException {
        lock = new File(folder + File.separator + (Platform.isWindows ? "summary.lock" : ".summary.lock"));
        for (;;) {
            boolean gotIt = lock.createNewFile();
            if (gotIt)
                break;
            else
                try {
                    Thread.sleep(500); // 1/2 sec?
                } catch (InterruptedException ie) {
                    // ignore
                }
        }
    }
    private void releaseLock() {
        lock.delete();
    }
}
