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
import corina.gui.XMenubar.XMenu;
import corina.gui.XMenubar.XMenuItem;
import corina.util.Platform;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    // filename - ~/.xcorina/recent
    private final static String RECENT_LIST_FILENAME = Prefs.USER_PROPERTIES_DIR + File.separator + "recent";
    
    // on class load: load previous recent-list
    static {
        try {
            loadList();
        } catch (IOException ioe) {
            // can't load?  just use empty list
            recent = new ArrayList();
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

	// update menu
	updateAllMenus();

	// update disk
	saveList();
    }

    private static ResourceBundle msg = ResourceBundle.getBundle("MenubarBundle");

    /** Generate a new recent-file menu.  This menu will contain the
	names of (up to) the last 4 files opened.  As long as the menu
	returned by this method is referenced, it will automatically
	be kept updated. */
    public static JMenu makeOpenRecentMenu() {
        // create a new menu
        JMenu menu = new XMenu(msg.getString("open_recent"));
        if (!Platform.isMac)
            menu.setMnemonic(msg.getString("open_recent_key").charAt(0));

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

	JMenuItem clear = new XMenuItem(msg.getString("clear_menu"));
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

    // load the recent-list from disk
    private static void loadList() throws IOException {
        recent = new ArrayList();

        BufferedReader r = new BufferedReader(new FileReader(RECENT_LIST_FILENAME));
        for (;;) {
            String line = r.readLine();
            if (line == null)
                break;
            recent.add(line);
        }
    }

    // save the recent-list to disk
    private static synchronized void saveList() {
        // save it ... when i get around to it.  this certainly
        // doesn't need to get done before the "open" command is done.
        // the user *probably* won't quit right after opening
        // something, but even so, it wouldn't be that horrible,
        // anyway.
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000); // 4s
                                        // todo: should probably merge with any other saveList threads, if they're started in here.
                } catch (InterruptedException ie) {
                    // ignore
                }
                try {
                    BufferedWriter w = new BufferedWriter(new FileWriter(RECENT_LIST_FILENAME));
                    for (int i=0; i<recent.size(); i++) {
                        w.write(recent.get(i).toString());
                        w.newLine();
                    }
                    w.close();
                } catch (IOException ioe) {
                    // store me?
                }
            }
        };
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

        // flag errors somehow!
    }
}
