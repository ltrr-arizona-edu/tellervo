package corina.browser;

public class Summary {

    /*
     methods:
     -- how to name (.summary / summary.sys)
     -- create (for a given folder)
     -- load from disk
     -- save to disk
     -- i/o access (file locking?)
     */

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
}
