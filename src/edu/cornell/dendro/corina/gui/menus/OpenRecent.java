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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.gui.menus;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.CanOpener;
import edu.cornell.dendro.corina.sample.CorinaWebElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;

/**
    A menu which shows recently-opened files.

    <p>(It's actually implemented as a list of recently-opened files,
    and a factory method to generate <code>JMenu</code>s.  But this will
    change in the near future.)</p>

    <p>To use, simply call <code>OpenRecent.generateMenu()</code>, and
    use that as you would any other <code>JMenu</code>.  It will
    automatically be updated whenever the list changes due to some new
    file being opened.  (When it's no longer strongly
    reachable, it'll be garbage collected.)</p>
    
    <h2>Left to do:</h2>
    <ul>
        <li>rename to OpenRecentMenu
        <li>extend JMenu; the c'tor should take care of notification stuff (add/removeNotify(), if necessary)
        <li>i don't need to use refs if i use add/remove notify, right?
        <li>doesn't use special 1.3 font hack any more -- do i care?
        <li>refactor -- separate model and view?
        <li>catch errors gracefully - "this file may have been moved...", etc.
        <li>if document is already open, just toFront() (needs other work first)
    </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class OpenRecent {
	// number of recent docs to remember
	private final static int NUMBER_TO_REMEMBER = 10;

	// list of loaders, most-recent-first
	private static List<Object> recent;

	// list of created menus -- soft references
	private static List<WeakReference<JMenu>> menus = new ArrayList<WeakReference<JMenu>>();

	// on class load: load previous recent-list from system property into static
	// structure
	static {
		loadList();
	}

	/**
	 * Indicate to the recent-file list that a file was just opened. This also
	 * updates every recent-file menu automatically.
	 * 
	 * @param filename
	 *            the (full) name of the file that was opened
	 */
	public static void fileOpened(String filename) {
		// if already in spot #0, don't need to do anything
		if (!recent.isEmpty() && recent.get(0).equals(filename))
			return;

		// if this item is in the list, remove me, too
		recent.remove(filename);

		// remove last item, if full
		if (recent.size() == NUMBER_TO_REMEMBER)
			recent.remove(NUMBER_TO_REMEMBER - 1);

		// prepend fileelement
		recent.add(0, filename);

		// update menu(s)
		updateAllMenus();

		// update disk
		saveList();
	}

	/**
	 * Indicate to the recent-file list that a file was just opened. This also
	 * updates every recent-file menu automatically.
	 * 
	 * @param sl the sample loader associated with this object
	 *            the (full) name of the file that was opened
	 */
	public static void sampleOpened(SampleLoader sl) {
		// if already in spot #0, don't need to do anything
		if (!recent.isEmpty() && recent.get(0).equals(sl))
			return;

		// if this item is in the list, remove me, too
		recent.remove(sl);

		// remove last item, if full
		if (recent.size() == NUMBER_TO_REMEMBER)
			recent.remove(NUMBER_TO_REMEMBER - 1);

		// prepend element
		recent.add(0, sl);

		// update menu(s)
		updateAllMenus();

		// update disk
		saveList();
	}

	
	/**
	 * Generate a new recent-file menu. This menu will contain the names of (up
	 * to) the last 4 files opened. As long as the menu returned by this method
	 * is referenced, it will automatically be kept updated.
	 */
	public static JMenu makeOpenRecentMenu() {
		// create a new menu
		JMenu menu = Builder.makeMenu("open_recent");

		// generate its elements
		updateMenu(menu);

		// add it to the list
		menus.add(new WeakReference<JMenu>(menu));

		// return it
		return menu;
	}

	// update all existing open-recent menus. if any has since
	// disappeared, remove it from my list.
	private static void updateAllMenus() {
		// for each menu...
		for (int i = 0; i < menus.size(); i++) {
			// dereference it
			JMenu m = menus.get(i).get();

			// already gone? remove it from my list
			if (m == null) {
				menus.remove(i);
				continue;
			}

			// update it
			updateMenu(m);
		}
	}
	
	private static String getSavableName(Object o) {
		if(o instanceof CorinaWebElement) {
			ResourceIdentifier rid = ((CorinaWebElement)o).getResourceIdentifier();

			XMLOutputter outputter;			
			Format format = Format.getCompactFormat();

			format.setEncoding("UTF-8");
			return new XMLOutputter(format).outputString(rid.asXMLElement());
		}		
		
		// default
		return o.toString();
	}
	
	/**
	 * Return a textual description to display in the menu
	 * @param o
	 * @return
	 */
	private static String getDescription(Object o) {
		if(o instanceof CorinaWebElement) {
			CorinaWebElement cwe = (CorinaWebElement)o;
			ResourceIdentifier rid = cwe.getResourceIdentifier();
			
			return cwe.getName() + " from " + rid.asXMLElement().getAttribute("url");
		}
		
		// default
		return o.toString();
	}
	
	/**
	 * Actually open the file
	 * @param o
	 */
	private static void doOpen(Object o) throws IOException {
		if(o instanceof SampleLoader) {
			new Editor(((SampleLoader) o).load());
			return;
		}

		CanOpener.open(o.toString());		
	}

	// BUG: error handling in here is miserable-to-nonexistant
	private static void updateMenu(JMenu menu) {
		
		menu.removeAll();
		for (int i = 0; i < recent.size(); i++) {
			Object o = recent.get(i);
			String menuDesc = getDescription(o);
			JMenuItem r = new JMenuItem(menuDesc);

			final int glue = i;
			r.addActionListener(new AbstractAction() {
				// todo: if already open, just toFront() it!
				public void actionPerformed(ActionEvent e) {
					try {
						doOpen(recent.get(glue));
					} catch (FileNotFoundException fnfe) {
						// file moved?
						Alert.error("File Isn't There", "The file called '" + recent.get(glue) + "'\n"
								+ "isn't there any more.  If it was moved, " + "you'll have to open it with File -> Open...");

						// remove it from the list
						recent.remove(glue);
						updateAllMenus(); // (doesn't really update all menus on
											// mac. ack.)

						// FUTURE: search for it, or allow user to.

						return;
					} catch (IOException ioe) {
						Alert.error("Error loading", "Can't open: " + ioe.toString());
						return;
					}

					/*
					 * -- don't do this; CanOpener.open() calls fileOpened(),
					 * which takes care of this // move this entry to top of
					 * list, now if (glue != 0) { String me = (String)
					 * recent.remove(glue); recent.add(0, me); updateAllMenus();
					 * // ok?
					 * 
					 * // update disk saveList(); }
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
					recent.clear();
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
		recent = new ArrayList<Object>();

		// parse |corina.recent.files| pref, splitting by |path.separator|
		// chars.
		// (ASSUMES (path.separator).length()==1?
		StringTokenizer tok = new StringTokenizer(App.prefs.getPref("corina.recent.documents", ""), System.getProperty("path.separator"));

		// add all files to recent
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();

			// is it xml? (kludgy, I know)
			if(next.charAt(0) == '<') {
				try {
					Document doc = new SAXBuilder().build(new StringReader(next));
					org.jdom.Element e = doc.getRootElement();
					ResourceIdentifier rid = ResourceIdentifier.fromElement(e);
					
					recent.add(new CorinaWebElement(rid));			
				} catch (JDOMException jdome) {
					System.out.println("bad xml element: " + next + ":" + jdome.toString());
				} catch (IOException ioe) {
					System.out.println("bad xml element: " + next + ":(ioe)" + ioe.toString());
				}
			}
			else
				recent.add(next);
		}
	}

	// store the recent-list in a string, in |corina.recent.files|
	private static synchronized void saveList() {
		StringBuffer buf = new StringBuffer();

		char sep = File.pathSeparatorChar;

		for (int i = 0; i < recent.size(); i++) {
			buf.append(getSavableName(recent.get(i)));
			if (i < recent.size() - 1)
				buf.append(sep);
		}

		// store in pref
		App.prefs.setPref("corina.recent.documents", buf.toString());
	}
}
