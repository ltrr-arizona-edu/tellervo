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

package corina.gui;

import corina.prefs.Prefs;
import corina.ui.I18n;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

import java.awt.Dimension;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
   A wrapper for JFileChooser, providing typical (Corina) usage.

   <p>Here's what they provide, and why you'd want to use this class
   over JFileChooser directly:</p>

   <ul>

       <li>automatically uses a bunch of default filters</li>

       <li>gets its initial directory from corina.dir.data; subsequent
       calls start from the last-used-directory</li>

       <li>incredibly neat preview component</li>

       <li>multiple-file-chooser lets the user select several
       files from one dialog</li>

       <li>simple return values: a String (for single-selection) or a
       List of Strings (for multiple-selection), or throws a
       UserCancelledException if the user cancelled.  No need to mess
       with JFileChooser.APPROVE_OPTION</li>

   </ul>

   @see corina.gui.UserCancelledException
   @see javax.swing.JFileChooser

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class FileDialog {

    private FileDialog() {
	// (don't instantiate me)
    }

    private static class ExtensionFilter extends FileFilter {
	private String TLA, tla;
	private String name;
	/**
	   Create a new filter with the given name, for the given TLA.

	   @param tla 3-letter-acronym to match as an extension
	   @param name the name of this filter
	*/
	public ExtensionFilter(String tla, String name) {
	    this.tla = tla.toLowerCase();
	    this.TLA = tla.toUpperCase();
	    this.name = name + " (*." + this.TLA + ")";
	}
	/**
	   Decide whether this file ends in the given TLA.

	   @param f the file to test
	   @return true, if and only if the filename ends in the TLA
	*/
	public boolean accept(File f) {
	    // users can always traverse a directory
	    if (f.isDirectory())
		return true;
	    // it's okay if it ends with the TLA, either case
	    String filename = f.getName();
	    return (filename.endsWith(tla) || filename.endsWith(TLA));
	}
	/**
	   The user-readable description of this filter.  Specified as
	   "name (*.TLA)".

	   @return the user-readable name of this filter
	*/
	public String getDescription() {
	    return name;
	}
    }

    /** The default list of filters to use, as a list of
	extensions. */
    public static String FILTERS[] = new String[] {
	"raw", "sum", "rec", "ind", "cln", "trn",
    };

    // add all default filters to this target, and then reset to default (*.*)
    private static void addFilters(JFileChooser f) {
	for (int i=0; i<FILTERS.length; i++)
	    f.addChoosableFileFilter(new ExtensionFilter(FILTERS[i],
							 I18n.getText("." + FILTERS[i])));
	// REFACTOR: should only need to pass FILTERS[i] to constructor here
	f.setFileFilter(f.getAcceptAllFileFilter());
    }

    // working directory -- this gets updated whenever OK is clicked
    // XXX: can you say race condition - aaron... dependence on static
    // initializers, data and methods needs to be fixed
    // If this class is referenced before Prefs is, we are SOL
    private static String wd = Prefs.getPref("corina.dir.data");

    /**
       Show a file selection dialog.  This allows the user to select
       one file.  It shows a preview component, and has the default
       filters available.

       @param prompt the text string to use for both the title bar
       and approve button
       @return the filename that was selected
       @exception UserCancelledException if the user cancelled
    */
    public static String showSingle(String prompt) throws UserCancelledException {
        // create chooser
        JFileChooser f = new JFileChooser(wd);

        // add filters
        addFilters(f);

        // preview component
        f.setAccessory(new SamplePreview(f));

        // make the window a resonable size to see everything
        f.setPreferredSize(new Dimension(480, 360));

        // show the dialog
	int result = f.showDialog(null, prompt);

	if (result == JFileChooser.APPROVE_OPTION) {
	    // ok: store wd, and return file
	    wd = f.getCurrentDirectory().getPath();
	    return f.getSelectedFile().getPath();
	} else {
	    // cancel
            throw new UserCancelledException();
	}
    }

    // --------------------------------------------------
    // multi
    //

    /**
       Show a multiple file selection dialog.  This allows the user
       to select any number of files.  It shows a preview component,
       and has the default filters available.

       @param prompt the text string to use for both the title bar
       and approve button
       @return a List of filenames that were selected
       @exception UserCancelledException if the user cancelled
    */
    public static List showMulti(String prompt) throws UserCancelledException {
	// create a new list to use
	List list = new ArrayList();
	return showMultiReal(prompt, list);
    }

    /**
       Show a multiple file selection dialog, with a list of files to
       start with.  This allows the user to select any number of
       files.  It shows a preview component, and has the default
       filters available.

       @param prompt the text string to use for both the title bar
       and approve button
       @param list a List of filenames to have already selected
       @return a List of filenames that were selected
       @exception UserCancelledException if the user cancelled
    */
    public static List showMulti(String prompt, List list) throws UserCancelledException { // to edit a list
	// use the given list
	return showMultiReal(prompt, list);
    }

    private static List showMultiReal(String prompt, List list) throws UserCancelledException {
	// big-preview-list-component-thingy ... yeah.
	final MultiPreview mp = new MultiPreview(list);

	// make double-clicking a file call MultiPreview's addClicked() method
	JFileChooser f = new JFileChooser(wd) {
		public void approveSelection() {
		    mp.addClicked(); // heh heh heh...
		}
	    };

	// filters
	addFilters(f);

	// hide ok/cancel, we'll do that ourselves
	f.setControlButtonsAreShown(false);
	
	// preview component + multi-list
	mp.hook(f); // add reference to this jfilechooser
	f.setAccessory(mp);
	
	// make the window a resonable size to see everything
	f.setMinimumSize(new Dimension(640, 480));
	f.setPreferredSize(new Dimension(640, 480));

	// show dialog
	f.showDialog(null, prompt); // don't care what the return value is, it's always CANCEL
	
	// store wd, if ok
	if (mp.getSamples() != null)
	    wd = f.getCurrentDirectory().getPath();

        // null?  have to deal, now.
        if (mp.getSamples() == null)
            throw new UserCancelledException();

        // return samples
        return mp.getSamples();
    }

}
