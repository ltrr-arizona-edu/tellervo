package edu.cornell.dendro.corina.util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateFolder implements Runnable {

    // update folder TO with folder FROM
    private String from, to;

    public UpdateFolder(File from, File to) {
	this.from = from.getPath();
	this.to = to.getPath();
    }

    // start time
    private long startTime;

    // its runnable
    public void run() {
	startTime = System.currentTimeMillis();
	update(from, to);
    }

    // number of files updated
    private int done = 0;

    // current file
    private String current;

    public int getNumberDone() {
	return done;
    }

    public String getCurrent() {
	return current;
    }

    // stop (nicely)
    public void stop() {
	stop = true;
    }
    private boolean stop=false;

    // oops, i shouldn't need to pass in count -- Count.java should be integrated, then.
    // note: returns -1 to mean "unknown"
    // aah: i should return a string then: "unknown", "5 sec", etc.
    public int estimateTimeRemaining(int total) {
	// nothing done yet, we have no idea
	if (done == 0 || lastDone == -1)
	    return -1;

	// what fraction of the way through am i?
	float fracDone = done / (float) total;

	// what fraction is left?
	float fracLeft = 1 - fracDone;

	// ok, how much time have i spent so far?
	long timeSpent = lastDone - startTime; // was: currentTime - startTime

	// BETTER: weigh the more recent files more heavily than the first few

	// multiply by fracLeft/fracDone, and convert to sec
	int timeLeft = Math.round((fracLeft/fracDone) * timeSpent / 1000);

	return timeLeft;
    }

    // the time when the last update finished, or -1=unknown
    private long lastDone = -1;

    // updates directory FROM with directory TO.
    private void update(String from, String to) {
	File fromDir = new File(from);
	File toDir = new File(to);

	File fromFileList[] = fromDir.listFiles();
	if (fromFileList == null)
	    return;

	// create "to" if it doesn't exist.  i think there's overlap here...
	if (!toDir.exists())
	    toDir.mkdir();

	for (int i=0; i<fromFileList.length; i++) {

	    if (stop)
		return;

	    File toFile = new File(to + File.separator + fromFileList[i].getName());

	    // stats
	    current = toFile.getPath();

	    // doesn't exist, or have old version, or is a different size
	    if (!toFile.exists() ||
		(toFile.lastModified() < fromFileList[i].lastModified()) ||
		(toFile.length() != fromFileList[i].length())) {

		if (fromFileList[i].isDirectory()) {
		    toFile.mkdir();
		} else {
		    try {
			copy(fromFileList[i], toFile);
		    } catch (IOException ioe) {
			// FIXME: this is bad ... stuff in a vector or something ...
			System.out.println("exception");
			System.out.println("   message=" + ioe.getMessage());
			System.out.println("   string=" + ioe.toString());
			System.out.println("   class=" + ioe.getClass());
		    }
		}
	    }

	    // stats
	    done++;
	    lastDone = System.currentTimeMillis();

	    // if this is a directory, recurse into it
	    if (fromFileList[i].isDirectory()) {  
		update(fromFileList[i].getPath(), to + File.separator + fromFileList[i].getName());
	    }
	}

	// if any files exist in |to| that aren't in |from|, delete them.
	// TODO: what if it's a folder?
	File toFileList[] = toDir.listFiles();
	for (int i=0; i<toFileList.length; i++) {
	    File toFile = toFileList[i];
	    File fromFile = new File(from + File.separator + toFileList[i].getName());
	    if (!fromFile.exists()) {
		boolean success = delete(toFile);
		if (!success)
		    System.out.println("ERROR!  can't delete " + toFile);
	    }
	}
    }

    // ----
    // DELETE FILE
    //

    // try to delete a file, and (if it's a folder) all of its subfolders, too.
    private static boolean delete(File f) {
	if (f.isDirectory()) {
	    File children[] = f.listFiles();
	    for (int i=0; i<children.length; i++)
		delete(children[i]);
	}

	return f.delete();
    }

    // ----
    // COPY FILE
    //

    // what's a good buffer size?  i first used 1024, flanagan uses 4096
    // IDEA: compute at runtime?
    public static final int BUFSIZE = 16386; // 16K will cover most data files

    // static buffer -- good for performance, not good for multiple copy()s running at once.
    private char cbuf[] = new char[BUFSIZE+1];

    // copies file FROM to file TO.  note: uses member field for buffer, so
    // in any given instance of UpdateFolder, only one call at a time should call this.
    // DESIGN: should i enforce this by setting a flag on enter/exit from function?
    public void copy(File from, File to) throws IOException {
	System.out.println("(note: copying file " + from + ")");

	InputStreamReader r = null;
	OutputStreamWriter w = null;

  try {
    r = new InputStreamReader(new FileInputStream(from));
    w = new OutputStreamWriter(new FileOutputStream(to));
  	int n;
  	do {
	    n = r.read(cbuf, 0, BUFSIZE);
	    if (stop)
	      return;
	    if (n > 0)
	      w.write(cbuf, 0, n);
  	} while (n == BUFSIZE);
  } finally {
    if (r != null) try {
      r.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    if (w != null) try {
      w.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  // set the last-modified time
	long mod = from.lastModified();
	to.setLastModified(mod);
  }
}
