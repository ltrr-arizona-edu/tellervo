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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.CanOpener;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.HTMLEntities;

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
	private static Map<String, List<Object> > recentMap = new HashMap<String, List<Object> >();

	// list of created menus -- soft references
	private static List<WeakReference<JMenu>> menus = new ArrayList<WeakReference<JMenu>>();

	// on class load: load previous recent-list from system property into static
	// structure
	static {
		loadList("documents");
		loadList("reconcile");
	}

	public static void fileOpened(String filename) {
		fileOpened(filename, "documents");
	}
	
	/**
	 * Indicate to the recent-file list that a file was just opened. This also
	 * updates every recent-file menu automatically.
	 * 
	 * @param filename
	 *            the (full) name of the file that was opened
	 * @param tag
	 * 			  the tag associated with this (e.g., documents, reconcile, etc)
	 */
	public static void fileOpened(String filename, String tag) {
		List<Object> recent = getListForTag(tag);
		
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
		saveList(tag);
	}

	public static void sampleOpened(SampleLoader sl) {
		sampleOpened(sl, "documents");
	}
	
	/**
	 * Indicate to the recent-file list that a file was just opened. This also
	 * updates every recent-file menu automatically.
	 * 
	 * @param sl the sample loader associated with this object
	 *            the (full) name of the file that was opened
	 */
	public static void sampleOpened(SampleLoader sl, String tag) {
		List<Object> recent = getListForTag(tag);

		// cache some useful stuff...
		LoaderHolder lh = new LoaderHolder();
		lh.loader = sl;
		lh.displayName = sl.getName();
		
		if (sl instanceof CorinaWsiTridasElement) {
			TridasIdentifier identifier = ((CorinaWsiTridasElement) sl).getTridasIdentifier();
			
			// marshall the identifier to XML, escape it
			try {
				JAXBContext context = JAXBContext.newInstance(TridasIdentifier.class);
				Marshaller marshaller = context.createMarshaller();
				StringWriter writer = new StringWriter();

				marshaller.marshal(identifier, writer);
								
				lh.resText = HTMLEntities.htmlentities(writer.toString());
			} catch (JAXBException e) {
				e.printStackTrace();
				return;
			}
		}

		// if already in spot #0, don't need to do anything
		// also make sure their displaynames are the same!
		if (!recent.isEmpty() && recent.get(0).equals(lh) && 
				((LoaderHolder) recent.get(0)).displayName.equals(lh.displayName)) {
			return;
		}

		// if this item is in the list, remove me, too
		recent.remove(lh);

		// remove last item, if full
		if (recent.size() == NUMBER_TO_REMEMBER)
			recent.remove(NUMBER_TO_REMEMBER - 1);

		// prepend element
		recent.add(0, lh);

		// update menu(s)
		updateAllMenus();

		// update disk
		saveList(tag);
	}


	public static JMenu makeOpenRecentMenu() {
		return makeOpenRecentMenu("documents", null, null);
	}
	
	/**
	 * Generate a new recent-file menu. This menu will contain the names of (up
	 * to) the last 4 files opened. As long as the menu returned by this method
	 * is referenced, it will automatically be kept updated.
	 */
	public static JMenu makeOpenRecentMenu(String tag, JMenu menu, Integer itemsToKeep) {
		// create a new menu if we have to
		if(menu == null)
			menu = Builder.makeMenu("open_recent");
		
		menu.putClientProperty("corina.open_recent_tag", tag);
		menu.putClientProperty("corina.open_recent_itemsToKeep", itemsToKeep);

		// generate its elements
		updateMenu(menu);

		// add it to the list
		menus.add(new WeakReference<JMenu>(menu));

		// return it
		return menu;
	}

	/**
	 * Create or retrieve a list for a tag
	 */
	private static List<Object> getListForTag(String tag) {
		List<Object> l = recentMap.get(tag);
		
		if(l == null) {
			l = new ArrayList<Object>();
			recentMap.put(tag, l);
		}
		
		return l;
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
		if(o instanceof LoaderHolder) {
			LoaderHolder holder = (LoaderHolder)o;
			
			// cwte:Some wonderful &amp; tasty>&gt;xxxxx....&lt
			if(holder.loader instanceof CorinaWsiTridasElement) {
				return "cwte:" + HTMLEntities.htmlentities(holder.displayName) + '>' + holder.resText;
			}
			
			// default...?
			return holder.displayName;
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
		if(o instanceof LoaderHolder) {
			LoaderHolder holder = (LoaderHolder)o;
			
			if(holder.loader instanceof CorinaWsiTridasElement) {
				CorinaWsiTridasElement cwte = (CorinaWsiTridasElement) holder.loader;
				
				return holder.displayName + " (TriDaS), from " + cwte.getTridasIdentifier().getDomain();
			}
			
			return holder.displayName;
		}
		
		// default
		return o.toString();
	}
	
	/**
	 * Actually open the file
	 * @param o
	 */
	private static void doOpen(Object o, Object opener) throws IOException {
		if(o instanceof LoaderHolder) {
			if(opener == null)
				opener = defaultSampleOpener;
			
			((SampleOpener)opener).performOpen(((LoaderHolder) o).loader.load());
			return;
		}

		CanOpener.open(o.toString());		
	}

	public static class SampleOpener {
		private String tag;
		
		public SampleOpener() {
			this("documents");
		}
		
		public SampleOpener(String tag) {
			this.tag = tag;
		}
		
		public String getTag() {
			return tag;
		}
		
		public void performOpen(Sample s) {
			OpenRecent.sampleOpened(s.getLoader(), getTag());
			new Editor(s);
		}
	}
	
	private static SampleOpener defaultSampleOpener = new SampleOpener();
	
	// BUG: error handling in here is miserable-to-nonexistant
	private static void updateMenu(final JMenu menu) {
		String possibleTag = (String) menu.getClientProperty("corina.open_recent_tag");
		final String tag = (possibleTag == null) ? "documents" : possibleTag;
		final List<Object> recent = getListForTag(tag);
		
		Integer itemsToKeep = (Integer) menu.getClientProperty("corina.open_recent_itemsToKeep");

		// clear the bottom n items from the menu
		if(itemsToKeep != null) {
			while(menu.getMenuComponentCount() > itemsToKeep)
				menu.remove(itemsToKeep);
		}
		else
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
						doOpen(recent.get(glue), menu.getClientProperty("corina.open_recent_action"));
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
					saveList(tag);
				}
			});
			menu.addSeparator();
			menu.add(clear);
		}
	}

	// load recent-list from |corina.recent.files|.
	// only called on class-load, so doesn't need to be synch.
	private static void loadList(String tag) {
		// create recent list
		List<Object> recent = getListForTag(tag);

		// use "?" as a separator, as it's illegal in both filenames and XML
		// TODO: Make this less kludgier!
		StringTokenizer tok = new StringTokenizer(App.prefs.getPref("corina.recent." + tag, ""), "?");

		// add all files to recent
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();

			// it's a CorinaWsiTridasElement?
			if(next.startsWith("cwte:")) {
				System.out.println("cwte: " + next);
				String[] parts = next.substring(5).split("\\>", 2);
				if(parts.length == 2) {
					String displayName = HTMLEntities.unhtmlentities(parts[0]);
					String idXML = HTMLEntities.unhtmlentities(parts[1]);
					TridasIdentifier identifier;
					
					try {
						JAXBContext context = JAXBContext.newInstance(TridasIdentifier.class);
						Unmarshaller unmarshaler = context.createUnmarshaller();
						
						StringReader reader = new StringReader(idXML);
						identifier = (TridasIdentifier) unmarshaler.unmarshal(reader);
					} catch (JAXBException e) {
						e.printStackTrace();
						continue;
					}
					
					LoaderHolder holder = new LoaderHolder();
					holder.displayName = displayName;
					holder.resText = idXML;
					holder.loader = new CorinaWsiTridasElement(identifier);
					recent.add(holder);
				}
				else
					System.out.println("Bad cwte string: " + next);
			}
			else
				recent.add(next);
		}
	}

	// store the recent-list in a string, in |corina.recent.<tag>|
	private static synchronized void saveList(String tag) {
		List<Object> recent = getListForTag(tag);
		StringBuffer buf = new StringBuffer();

		char sep = '?';

		for (int i = 0; i < recent.size(); i++) {
			buf.append(getSavableName(recent.get(i)));
			if (i < recent.size() - 1)
				buf.append(sep);
		}

		// store in pref
		App.prefs.setPref("corina.recent." + tag, buf.toString());
	}

	// kludge!
	private static class LoaderHolder {
		public String displayName;
		public String resText;
		public SampleLoader loader;
		
		@Override
		public boolean equals(Object oo) {
			if(!(oo instanceof LoaderHolder))
				return false;
			
			SampleLoader o = ((LoaderHolder)oo).loader;

			// easy if they're both tridas elements! :)
			if(o instanceof CorinaWsiTridasElement && loader instanceof CorinaWsiTridasElement) {
				TridasIdentifier id1 = ((CorinaWsiTridasElement)o).getTridasIdentifier();
				TridasIdentifier id2 = ((CorinaWsiTridasElement)loader).getTridasIdentifier();
				
				return id1.equals(id2);
			}
						
			return false;
		}
	}
}
