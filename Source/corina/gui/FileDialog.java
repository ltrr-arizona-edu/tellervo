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

import java.io.File;

import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;

import java.awt.Dimension;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
   A convenient wrapper for JFileChooser, for typical XCorina usage.

   <p>Here's what they provide, and why you'd want to use this class
   over JFileChooser directly:</p>

   <ul>

       <li>automatically uses a bunch of default filters</li>

       <li>gets its initial directory from corina.dir.data; subsequent
       calls start from the last-used-directory</li>

       <li>incredibly neat preview component</li>

       <li>multiple-file-chooser lets the user select several
       files from one dialog</li>

       <li>simple return values: null, or a String, or a List of
       Strings.  No need to mess with JFileChooser.APPROVE_OPTION</li>

   </ul>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class FileDialog {

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("FileDialogBundle");

    public static class ExtensionFilter extends FileFilter {
	private String TLA, tla;
	private String name;
	/** Create a new filter with the given name, for the given
	    TLA.
	    @param tla 3-letter-acronym to match as an extension
	    @param name the name of this filter */
	public ExtensionFilter(String tla, String name) {
	    this.tla = tla.toLowerCase();
	    this.TLA = tla.toUpperCase();
	    this.name = name + " (*." + this.TLA + ")";
	}
	/** Decide whether this file ends in the given TLA.
	    @param f the file to test
	    @return true, if and only if the filename ends in the TLA */
	public boolean accept(File f) {
	    // users can always traverse a directory
	    if (f.isDirectory())
		return true;
	    // it's okay if it ends with the TLA, either case
	    String filename = f.getName();
	    return (filename.endsWith(tla) || filename.endsWith(TLA));
	}
	/** The user-readable description of this filter.  Specified
	    as "name (*.TLA)".
	    @return the user-readable name of this filter */
	public String getDescription() {
	    return name;
	}
    }

    /** The default list of filters to use, as a list of extensions;
        the names of these are in FileDialogBundle. */
    public static String FILTERS[] = new String[] {
	"raw",
	"sum",
	"rec",
	"ind",
	"cln",
	"trn",
    };

    // add all default filters to this target, and then reset to default (*.*)
    private static void addFilters(JFileChooser f) {
	for (int i=0; i<FILTERS.length; i++)
	    f.addChoosableFileFilter(new ExtensionFilter(FILTERS[i],
							 msg.getString(FILTERS[i])));
	f.setFileFilter(f.getAcceptAllFileFilter());
    }

    // working directory -- this gets updated whenever OK is clicked
    private static String wd = System.getProperty("corina.dir.data");

    /** Show a file selection dialog.  This allows the user to select
	one file.  It shows a preview component, and has the default
	filters available.
	@param prompt the text string to use for both the title bar
	and approve button
	@return the filename that was selected, or null if the user
	cancelled */
    public static String showSingle(String prompt) {
	// create chooser
	JFileChooser f = new JFileChooser(wd);

	// add filters
	addFilters(f);

	// preview component
	f.setAccessory(new SamplePreview(f));

	// make the window a resonable size to see everything
	f.setPreferredSize(new Dimension(480, 360));

	// show the dialog
	if (f.showDialog(null, prompt) != JFileChooser.APPROVE_OPTION)
	    return null;

	// store wd
	wd = f.getCurrentDirectory().getPath();

	// return file
	return f.getSelectedFile().getPath();
    }

    // --- multi ----------------------------------------
    public static List showMulti(String prompt) {
	// create a new list to use
	List list = new ArrayList();
	return showMultiReal(prompt, list);
    }

    public static List showMulti(String prompt, List list) { // to edit a list
	// use the given list
	return showMultiReal(prompt, list);
    }

    private static List showMultiReal(String prompt, List list) {
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

	// return samples -- automatically null, if user cancelled
	return mp.getSamples();
    }

}
