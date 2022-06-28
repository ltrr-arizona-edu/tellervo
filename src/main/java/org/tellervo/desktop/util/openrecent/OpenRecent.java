/*******************************************************************************
 * Copyright (C) 2003 Ken Harris
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/


package org.tellervo.desktop.util.openrecent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.sample.TellervoWSILoader;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.io.formats.tucson.TucsonFormat;


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
	private static Map<String, List<OpenableDocumentDescriptor>> recentMap = 
		new HashMap<String, List<OpenableDocumentDescriptor> >();

	// list of created menus -- soft references
	private static List<WeakReference<JMenu>> menus = new ArrayList<WeakReference<JMenu>>();

	/** The name of our file */
	private final static String OPENRECENT_FILENAME_PREFIX = "recent-";
	private final static String OPENRECENT_FILENAME_SUFFIX = ".xml";
	
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
		SeriesDescriptor sd = new SeriesDescriptor();
		File file = new File(filename);
		
		sd.setDisplayName(file.getName());
		sd.setFileName(file.getAbsolutePath());
		sd.setSampleType(SampleType.UNKNOWN);
		sd.setLoaderType(SeriesDescriptor.LoaderType.FILE);
		
		sampleOpened(sd, tag);
	}

	/**
	 * Open a SampleDescriptor in the default category
	 * @param sl
	 */
	public static void sampleOpened(SeriesDescriptor sd) {
		sampleOpened(sd, "documents");
	}
	
	/**
	 * Indicate to the recent-file list that a file was just opened. This also
	 * updates every recent-file menu automatically.
	 * 
	 * @param sl a sample descriptor for the object
	 */
	public static void sampleOpened(SeriesDescriptor sd, String tag) {
		List<OpenableDocumentDescriptor> recent = getListForTag(tag);
		
		// if already in spot #0, don't need to do anything
		if (!recent.isEmpty() && recent.get(0).equals(sd))
			return;

		// if this item is in the list, remove me, too
		recent.remove(sd);

		// remove last item, if full
		if (recent.size() == NUMBER_TO_REMEMBER)
			recent.remove(NUMBER_TO_REMEMBER - 1);

		// prepend element
		recent.add(0, sd);

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
			menu = Builder.makeMenu("menus.file.open_recent");
		
		menu.putClientProperty("tellervo.open_recent_tag", tag);
		menu.putClientProperty("tellervo.open_recent_itemsToKeep", itemsToKeep);

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
	private static List<OpenableDocumentDescriptor> getListForTag(String tag) {
		List<OpenableDocumentDescriptor> l = recentMap.get(tag);
		
		if(l == null) {
			l = new ArrayList<OpenableDocumentDescriptor>();
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
	
	/**
	 * Return a textual description to display in the menu
	 * @param o
	 * @return
	 */
	private static String getDescription(Object o) {
		// nicely push our data together...
		if(o instanceof SeriesDescriptor) {
			SeriesDescriptor desc = (SeriesDescriptor) o;
			StringBuffer sb = new StringBuffer();

			// first, the display name
			sb.append(desc.getDisplayName());
			
			// the range
			if(desc.getRange() != null) {
				sb.append(" (");
				sb.append(desc.getRange());
				sb.append(')');
			}
			
			// derived stuff...
			if(desc.getSampleType().isDerived()) {
				sb.append(" [");
				sb.append(desc.getSampleType().toString());
				
				if(desc.getVersion() != null) {
					sb.append(", version: ");
					sb.append(desc.getVersion());
				}

				sb.append(']');
			}
			
			return sb.toString();
		}
		
		// default
		return o.toString();
	}
	
	/**
	 * Actually open the file
	 * @param o
	 */
	private static void doOpen(Object o, Object opener) throws IOException {
		if(opener == null)
			opener = defaultSampleOpener;
				
		if(o instanceof SeriesDescriptor) {
			SeriesDescriptor desc = (SeriesDescriptor) o;
			
			switch(desc.getLoaderType()) {
			case TELLERVO_WSI: {
				TellervoWSILoader element = new TellervoWSILoader(desc.getIdentifier());
				element.setLoadProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
				
				((SampleOpener)opener).performOpen(element.load());
				break;
			}
				
			case FILE: {
				//TODO Remember file type 
				
				LiteEditor editor = LiteEditor.getNewInstance();
				try {
					TucsonFormat format = new TucsonFormat();
					editor.loadFile(null, new File(desc.getFileName()), format);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				return;
			}
				
			case UNKNOWN:
				new BugDialog(new IllegalArgumentException("UNKNOWN type element in OpenRecent"));
				break;
				
			default:
				new BugDialog(new IllegalArgumentException("Unhandled type element in OpenRecent"));
				break;
				
			}
			
			return;
		}

		new BugDialog(new IllegalArgumentException("Unknown item in OpenRecent"));

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
			OpenRecent.sampleOpened(new SeriesDescriptor(s), getTag());
			FullEditor editor = FullEditor.getInstance();
			editor.addSample(s);
		}
	}
	
	private static SampleOpener defaultSampleOpener = new SampleOpener();
	
	// BUG: error handling in here is miserable-to-nonexistant
	private static void updateMenu(final JMenu menu) {
		String possibleTag = (String) menu.getClientProperty("tellervo.open_recent_tag");
		final String tag = (possibleTag == null) ? "documents" : possibleTag;
		final List<OpenableDocumentDescriptor> recent = getListForTag(tag);
		
		Integer itemsToKeep = (Integer) menu.getClientProperty("tellervo.open_recent_itemsToKeep");

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
			r.addActionListener(new ActionListener() {
				// todo: if already open, just toFront() it!
				public void actionPerformed(ActionEvent e) {
					try {
						doOpen(recent.get(glue), menu.getClientProperty("tellervo.open_recent_action"));
					} catch (FileNotFoundException fnfe) {
						// file moved?
						Alert.error(I18n.getText("error.loadingSample"), I18n.getText("error.cantOpenFile")+ " " + recent.get(glue) + ".\n"
								+ I18n.getText("error.fileNotFound"));

						// remove it from the list
						recent.remove(glue);
						updateAllMenus(); // (doesn't really update all menus on
											// mac. ack.)

						// FUTURE: search for it, or allow user to.

						return;
					} catch (IOException ioe) {
						Alert.error(I18n.getText("error.loadingSample"), I18n.getText("error.cantOpenFile")+ ": " + ioe.toString());
						return;
					}					
				}
			});
			menu.add(r);
		}

		JMenuItem clear = Builder.makeMenuItem("menus.clear_menu", true, "trashcan_full.png");
		if (recent.isEmpty()) {
			// no recent items: just "Clear Menu", but dimmed
			clear.setEnabled(false);
			menu.add(clear);
		} else {
			// some recent items: spacer, then "Clear Menu"
			clear.addActionListener(new ActionListener() {
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

	// load recent-list from |tellervo.recent.files|.
	// only called on class-load, so doesn't need to be synch.
	private static void loadList(String tag) {
		File file = OpenRecent.getOpenRecentFile(tag);
		
		if(!file.exists() || !file.isFile()) {
			// doesn't exist, so make a blank list
			getListForTag(tag);
			return;
		}
		else {
			RecentlyOpenedDocuments docs;
			
			try {
				JAXBContext context = JAXBContext.newInstance(RecentlyOpenedDocuments.class);
				Unmarshaller unmarshaler = context.createUnmarshaller();
				
				docs = (RecentlyOpenedDocuments) unmarshaler.unmarshal(file);
			} catch (JAXBException e) {
				e.printStackTrace();
				docs = new RecentlyOpenedDocuments();
			}
			
			// steal the list and put it in our map!
			recentMap.put(tag, docs.getOpenables());
		}
	}

	/**
	 * Store the recent documents...
	 * @param tag
	 */
	private static synchronized void saveList(String tag) {
		// get the recent list
		List<OpenableDocumentDescriptor> recent = getListForTag(tag);		

		RecentlyOpenedDocuments docs = new RecentlyOpenedDocuments();
		docs.getOpenables().addAll(recent);
		
		// marshall it to xml
		try {
			JAXBContext context = JAXBContext.newInstance(RecentlyOpenedDocuments.class);
			Marshaller marshaller = context.createMarshaller();
			
			// marshall pretty-like!
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			marshaller.marshal(docs, getOpenRecentFile(tag));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return a File representing our OpenRecent file
	 */
	private static File getOpenRecentFile(String tag) {
		return new File(App.prefs.getTellervoDir() + OPENRECENT_FILENAME_PREFIX + 
				tag + OPENRECENT_FILENAME_SUFFIX);
	}
}
