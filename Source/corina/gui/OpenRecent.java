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
import corina.gui.XMenubar.XMenuItem; // delete me!
import corina.ui.Builder;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;

// todo:
// - refactor -- separate model and view?
// - catch errors gracefully - "this file may have been moved...", etc.
// - if document is already open, just toFront() (needs other work first)

/**
   A list of recently-opened files, and a factory method to generate
   <code>JMenu</code>s.

   <p>To use, simply call <code>OpenRecent.generateMenu()</code>, and
   use that as you would any other <code>JMenu</code>.  It will
   automatically be updated whenever the list changes due to some new
   file being opened.  (When it's no longer strongly
   reachable, it'll be garbage collected.)</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class OpenRecent {
    // number of recent docs to remember
    private final static int NUMBER_TO_REMEMBER = 10;

    // list of files, most-recent-first
    private static List recent;

    // list of created menus -- soft references
    private static List menus=new ArrayList();

    // on class load: load previous recent-list
    static {
	// load list from prefs
	loadList();

	// migrate from old list to new list (which is just a pref, and needs no explicit loading)
	migrate();
    }

    // if ~/<corina>/recent exists, store in pref and delete file
    private static void migrate() {
	try {
	    // old file to load
	    // BUG: don't rely on prefs directory!
	    final String RECENT_LIST_FILENAME = Prefs.USER_PROPERTIES_DIR + File.separator + "recent";
	    File old = new File(RECENT_LIST_FILENAME);

	    if (old.exists()) {
		// load
		loadListLEGACY(RECENT_LIST_FILENAME);

		// store as pref
		saveList();

		// delete file
		old.delete();
	    }
	} catch (IOException ioe) {
	    // ???
	    System.out.println("ioe -- " + ioe);
	    ioe.printStackTrace();
	}
    }

    /** Indicate to the recent-file list that a file was just opened.
	This also updates every recent-file menu automatically.
	@param filename the (full) name of the file that was opened
     */
    public static void fileOpened(String filename) {
	// if already in spot #0, don't need to do anything
	if (!recent.isEmpty() && ((String) recent.get(0)).equals(filename))
	    return;

	// if this item is in the list, remove me, too
	recent.remove(filename);

	// remove last item, if full
	if (recent.size() == NUMBER_TO_REMEMBER)
	    recent.remove(NUMBER_TO_REMEMBER-1);

	// prepend filename
	recent.add(0, filename);

	// update menu(s)
	updateAllMenus();

	// update disk
	saveList();
    }

    /** Generate a new recent-file menu.  This menu will contain the
	names of (up to) the last 4 files opened.  As long as the menu
	returned by this method is referenced, it will automatically
	be kept updated. */
    public static JMenu makeOpenRecentMenu() {
        // create a new menu
        JMenu menu = Builder.makeMenu("open_recent");

        // generate its elements
        updateMenu(menu);

        // add it to the list
        menus.add(new WeakReference(menu));

        // return it
        return menu;
    }

    // update all existing open-recent menus.  if any has since
    // disappeared, remove it from my list.
    private static void updateAllMenus() {
	// for each menu...
	for (int i=0; i<menus.size(); i++) {
	    // dereference it
	    JMenu m = (JMenu) ((Reference) menus.get(i)).get();

	    // already gone?  remove it from my list
	    if (m == null) {
		menus.remove(i);
		continue;
	    }

	    // update it
	    updateMenu(m);
	}
    }

    // BUG: error handling in here is miserable-to-nonexistant
    private static void updateMenu(JMenu menu) {
	menu.removeAll();
	for (int i=0; i<recent.size(); i++) {
	    String fn = (String) recent.get(i);
	    JMenuItem r = new XMenuItem(fn.substring(fn.lastIndexOf(File.separator) + 1));

	    final int glue=i;
	    r.addActionListener(new AbstractAction() {
		    // todo: if already open, just toFront() it!
		    public void actionPerformed(ActionEvent e) {
			try {
			    CanOpener.open((String) recent.get(glue));
			} catch (FileNotFoundException fnfe) {
			    // file moved?  remove from list?  search for it?
			    return;
			} catch (IOException ioe) {
			    // !!!
			    return;
			}

			/* -- don't do this; CanOpener.open() calls fileOpened(), which takes care of this
			// move this entry to top of list, now
			if (glue != 0) {
			    String me = (String) recent.remove(glue);
			    recent.add(0, me);
			    updateAllMenus(); // ok?

			    // update disk
			    saveList();
			}
			*/
		    }
		});
	    menu.add(r);
	}

	JMenuItem clear = Builder.makeMenuItem("clear_menu");
	if (recent.isEmpty()) {
	    // no recent items: just "Clear Menu", but dimmed
	    clear.setEnabled(false);
	    menu.add(clear);
	} else {
	    // some recent items: spacer, then "Clear Menu"
	    clear.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			recent = new ArrayList();
			updateAllMenus();
			saveList();
		    }
		});
	    menu.addSeparator();
	    menu.add(clear);
	}
    }

    // load recent-list from |corina.recent.files|.
    // only called on class-load, so doesn't need to be synch.
    private static void loadList() {
	// create recent list
	recent = new ArrayList();

	// parse |corina.recent.files| pref, splitting by |path.separator| chars.
	// (ASSUMES (path.separator).length()==1?
	StringTokenizer tok = new StringTokenizer(System.getProperty("corina.recent.files", ""),
						  System.getProperty("path.separator"));

	// add all files to recent
	while (tok.hasMoreTokens()) {
	    String next = tok.nextToken();
	    recent.add(next);
	}
    }

    // store the recent-list in a string, in |corina.recent.files|
    private static synchronized void saveList() {
	StringBuffer buf = new StringBuffer();

	char sep = File.pathSeparatorChar;

	for (int i=0; i<recent.size(); i++) {
	    buf.append(recent.get(i).toString());
	    if (i < recent.size()-1)
		buf.append(sep);
	}

	// store in pref
	System.setProperty("corina.recent.files", buf.toString());

	// save now?  (was: save after 4sec or so)
	Prefs.save();
    }

    // --
    // LEGACY: load the recent-list from disk.
    // (only for migrating old setups.)
    private static void loadListLEGACY(String filename) throws IOException {
        recent = new ArrayList();

        BufferedReader r = new BufferedReader(new FileReader(filename));
        for (;;) {
            String line = r.readLine();
            if (line == null)
                break;
            recent.add(line);
        }
    }
}
