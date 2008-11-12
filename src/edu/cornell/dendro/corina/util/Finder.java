package edu.cornell.dendro.corina.util;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.gui.Layout;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;

/*
  TODO:
  -- dialog, asking user which to use -- almost done
  LATER:
  -- better searching algorithm -- string difference
*/

// search for a file:
// -- same name, different case (if case matters on this system -- assume it doesn't)
// -- same root, different extension
// -- any of the above, in the parent folder
// -- any of the above, in any subfolder of the parent folder
// if all that fails, then i've done my best, and i don't know where the file is.
// (perhaps i can search for small a small stringDifference - ?)
// (or where the first ~90% is identical?  acm256aa might want to turn up any acm256*)
// (-- but acm256ac will match acm256aa with the stringDifference method)

// as ??? said, "the computer should never report that it can't find a file,
// until it has tried to look for that file."  this class looks for a file
// that the user moved or renamed.  in corina, the most common way to lose a
// file is to change its extension (raw->rec, pik->f99, rec->ind, etc.), or
// to move it to a parent or sibling folder.  this class handles these cases
// (but not every case, e.g., moving to a grandparent folder).

public class Finder implements Runnable {

    private List result = new ArrayList();

    public Finder(String filename) {
	this.filename = filename;
    }

    private String filename;

    public void run() {
	// what i'm looking for
	File file = new File(filename);
	String target = normalize(file.getName());

	// search this folder
	File thisFolder = file.getParentFile();
	// search(target, thisFolder, result);
	// -- redundant, since i'll later search ../*

	// search ..
	File parentFolder = thisFolder.getParentFile();
	search(target, parentFolder, result);

	// search ../* -- SLOW!
	File otherFolders[] = parentFolder.listFiles();
	for (int i=0; i<otherFolders.length && !stop; i++)
	    if (otherFolders[i].isDirectory())
		search(target, otherFolders[i], result);
    }

    public List getResult() {
	return result;
    }

    private boolean stop = false;

    public void pleaseStop() {
	stop = true;
    }

    // given a filename, return a list of other possible filenames
    // which this file may have been used for -- see class def for ideas.
    // preferably, in order of most-to-least likely, so they can be
    // presented to the user directly.
    // this method may return an empty list, but it may not return null.

	// given a filename, return all filenames that match it
	// (and actually exist) mod case.
	// (DESIGN: this method checks only alternate cases for the filename,
	// not for all the folders it's in -- i should probably do that, too.)

	/*
	  DESIGN ISSUE:
	  -- searching . and .. takes <200ms
	  -- searching ../* takes >3000ms = 3sec
	  -- that's a non-trivial amount of time
	  -- best solution: be like the mac finder
	  -- display search results as i get them
	  -- (threading!)
	  -- also display a thermometer, spinner, and/or some "Searching..." text
	  -- if user clicks "ok" (or whatever) early, stop searching!
	*/

    // -- target is a lower-case normalized filename
    // -- folder is the folder to search (but not recursively)
    // -- result is the list (non-null) to add the results to
    private void search(String target, File folder, List result) {
	File siblings[] = folder.listFiles();
	for (int i=0; i<siblings.length && !stop; i++)
	    if (normalize(siblings[i].getName()).equals(target)) {
		result.add(siblings[i].getPath());
		hooks.run();
	    }
    }

    /*
      strategy:
      -- take the filename
      -- [ lower-case it
      --   strip the extension ]
      -- that's my "target"
      -- that function is my normalizer
      -- for each file in ./*, ../*, and ../* /*, normalize, and add to results if same
    */

    // given a filename, return its root filename, lower-case.
    // for example, "/etc/PASSWORD.TXT" => "password"
    private String normalize(String filename) {
	// get filename
	String n = new File(filename).getName();

	// lower-case it
	n = n.toLowerCase();

	// strip the extension
	int dot = n.lastIndexOf('.');
	if (dot != -1)
	    n = n.substring(0, dot);

	// return it
	return n;
    }

    private Hooks hooks = new Hooks();

    public void addHook(Runnable r) {
	hooks.addHook(r);
    }

    public static void main(String args[]) throws Throwable {
	ask("/Users/kharris/Documents/Corina/DATA/acm/acmearly.oink");
    }

    // --------------------------------------------------

    // given a filename the user asked for,
    // ask the user which one, if any, he really meant.
    // this method must return a filename, or throw a FNFE.
    // it may not return null.
    // suggestion:
    // -- show a list (jlist) of "filename" or "filename in folder/f2".
    // -- if there's only one, just ask -- "replace with this?" yes/no/other...
    // -- if there's >1, show list ok/cancel/other...
    // what about "remove"?  "deactivate"?  this could be tricky.
    public static String ask(String filename) throws FileNotFoundException {

	JList l = new JList();
	final DefaultListModel m = new DefaultListModel();
	l.setModel(m);

	JLabel line1 = new JLabel("The file \"" + filename + "\" was");
	JLabel line2 = new JLabel("moved, renamed, or deleted.  Perhaps " +
				  "it was changed to one of these?");
	JPanel question = Layout.boxLayoutY(line1, line2);
	question.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // ?

	final JLabel looking = new JLabel("Looking...");
	// !!!
	JButton cancel = Builder.makeButton("cancel");
	JButton ok = Builder.makeButton("ok");
	JPanel buttons = Layout.buttonLayout(looking, null, cancel, ok);
	buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // ?

	JComponent list = new JScrollPane(l);

	JPanel content = Layout.borderLayout(question,
					     null, list, null,
					     buttons);
	content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ?

	JDialog d = new JDialog();
	d.setTitle(""); // (use maybeTitle() for win32?)
	d.setContentPane(content);

	// NOTE: in order to use okcancel here, i'll have to
	// override this dialog's dispose(), since esc will only call
	// that, and i'll need to stop the worker thread.  (on the
	// upside, doing it in dispose() means i only have to do it
	// once.)
	OKCancel.addKeyboardDefaults(ok);

	d.pack();
	d.show();

	final Finder x = new Finder(filename);
	x.addHook(new Runnable() {
		public void run() {
		    List r = x.getResult();
		    for (int i=m.getSize(); i<r.size(); i++)
			m.addElement(r.get(i));
		}
	    });
	Thread t = new Thread(new Runnable() {
		public void run() {
		    x.run();
		    looking.setText("");
		}
	    });
	t.start();

	// WRITEME
	throw new FileNotFoundException();

	// TODO: make ok/cancel return the result (how?)
	// TODO: on ok/cancel, stop the thread!
	// TODO: integrate with editor, and wherever else it might be useful
	// TODO: make "other..." ask for any filename, then return that
	// TODO: don't allow deselecting the only selected row
	// TODO: don't allow selecting more than one row
	// TODO: double-clicking should be the same as click, ok
	// TODO: in q, show filename only?
	// TODO: in list, show filename only, if in the same folder?
	// TODO: show icon, as for other alerts?
	// FIXME: other... is awkward ... ?
	// TODO: if nothing found, put (dimmed) "no similar files found" text in list
	// TODO: preview?  how's the user supposed to decide?
	// TODO: "looking" text isn't lined up with "ok","cancel" text (?)
	// TODO: select first entry by default

	// TODO: for a list of filenames, if they appear to have all
	// been moved to the same place, ask, then just change the
	// folder for all (hard!) -- this makes the "change
	// directory..." popup obsolete, if i can pull it off.

	/*
	  The file "blah.oink" is missing.
	  Perhaps it was moved or renamed.
	  Replace it with one of these?

	  [blah.moo ===================== ] -- hey, preview component for these?
	   blah.oink2
	   ...
	  Looking... -- this text then disappears

	  (Other...)       (Cancel) (( OK )) -- what does "cancel" mean here?
	*/
    }
}
