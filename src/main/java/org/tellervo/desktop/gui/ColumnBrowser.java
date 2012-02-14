/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui;

import java.io.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultTreeCellRenderer;

// a NeXTstep-style column browser.  it's not a complete replacement for
// the Finder (hey steve, you listening?), but it's good for some things,
// and i think what we have here is a prime example.

@SuppressWarnings("serial")
public class ColumnBrowser extends JPanel {

    /*
      new strategy:
      -- each column is a jtable in a jscrollpane
      ---- need: Column.java class
      -- they're packed together in a jpanel using gridlayout
      -- if there's fewer than 3 components, there are 2 dummy components
      -- if there's more than 3 components, set the sizes to force 3 columns visible
      -- big jscrollpane around everything, horizontal scrollpane only

      idea for summary.java:
      -- would it be better to have one "Corina File Cache" per folder, or per source?
      -- per folder:
      ---- fewer concurrency issues, smaller files (per source would be 50 MB?)
      -- per source:
      ---- fewer files, quicker to search everything.
      ---- (wasn't it 18 sec to list all folders?)
      -- if so, not enough of a reason -- searching 50,000 files which aren't even in
      a database in under a minute is plenty fast.  if you want faster, use an RDBMS.

      manual:
      -- be sure to document "Corina File Cache" in the "files" section
      ---- they're created automatically
      ---- one per folder
      ---- you can delete them if you want, but they'll be created again
      ---- (should i use some native call to make them hidden?  not very xplat.)
      ---- they're XML files that store the metadata of all files in that folder.
      ---- they make searching much faster -- sort of like an RDBMS "index".
    */

    /*
      strategy:
      -- flowlayout of jscrollpanes of jlists?
      -- icons!  folder/document for 1.3, but can get real icon for 1.4
      -- show folders only; files get shown below.
      -- click any jlist entry,
      ---- the jlist to the right updates with its contents
      ---- every jlist to the right of that, if there are any, get zapped
      ---- scroll all the way to the right
      ---- update table-browser-component below
      ------ need event framework?
      ------ addFolderChangeListener() -- is there a listener similar to this already, or do i have to make one?
      -- should work for both local and remote (ftp) folders.
      -- keyboard nav very important!
      ---- right = descend folder
      ---- left = ascend folder
      ---- up/down = prev/next folder
      ---- start typing = jump to that folder
      -- need column-width-dragger?  (hard to add to jscrollpane, if i'm using that, but probably useful.)
      ---- probably not -- just sizing it to the largest item in the list seems to work pretty well.
      -- everything's a folder, so no special need to have arrow> to indicate folders (but might be nice).
      ---- use jtree "can-expand" icon for this: on mac os, it's right; on win32, it's a [+], which isn't bad
      -- might be helpful to give some indication of the number of items in a folder, without entering it.
      -- use ListPopup so you see popups (jcomboboxes) if the area above the split is too small?
      -- be nice for slow FTP sites
      ---- show hourglass?
      ---- list-as-you-get-them?
    */

    // TODO: add click listener -- doesn't really work yet
    // TODO: add key listener(s)?
    // IDEA: use jtable instead of jlist, so i can have a "Folder" header.  (do i really want that?)
    @SuppressWarnings("unchecked")
	JList makeList(String folder) {
	File f = new File(folder);
	File children[] = f.listFiles();
	final Vector list = new Vector(); // evil evil
	if (children != null)
	    for (int i=0; i<children.length; i++)
		if (children[i].isDirectory() && !children[i].isHidden())
		    list.add(children[i].getName());

	if (list.size() == 0)
	    return null; // FIXME: null is bad, but if there aren't any folders, i shouldn't add anything.

	Collections.sort(list, new Comparator() {
		public int compare(Object o1, Object o2) {
		    String s1 = (String) o1;
		    String s2 = (String) o2;

		    // case-insensitive sort
		    s1 = s1.toUpperCase();
		    s2 = s2.toUpperCase();

		    return s1.compareTo(s2);
		}
	    });

	JList l = new JList(list);

	// try forcing width to 100?
	l.setMinimumSize(new Dimension(100, 0));
	l.setPreferredSize(new Dimension(100, 100));
	l.setMaximumSize(new Dimension(100, 10000)); // BAD!

	l.setCellRenderer(new DefaultListCellRenderer() {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
							      int index,
							      boolean isSelected, boolean cellHasFocus) {
		    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		    ((JLabel) c).setIcon(isSelected ? openIcon : closedIcon);
		    return c;
		}
	    });

	final JList glue = l;
	final String folderGlue = folder;
	l.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
		    if (e.getValueIsAdjusting()) // wait until it stops
			return;

		    // WRITEME: remove all lists to the right of this one (how?)

		    // add a new one!
		    addFolder(folderGlue + File.separator + list.get(glue.getSelectedIndex()));

		    // WRITEME: scroll all the way to the right

		    // repack components in window
		    // QUESTION: does this do too much redrawing?
		    Window window = (Window) getTopLevelAncestor();
		    Dimension oldSize = window.getSize(); // save/restore window size
		    window.pack(); // -- hey, this means i don't need to pass Windows around!
		    window.setSize(oldSize);
		    getParent().invalidate();
		    invalidate();
		    repaint();
		}
	    });

	return l;
    }

    // open and closed folder icons
    private static Icon openIcon = new DefaultTreeCellRenderer().getOpenIcon();
    private static Icon closedIcon = new DefaultTreeCellRenderer().getClosedIcon();

    private void addFolder(String folder) {
	JList list = makeList(folder);
	if (list == null)
	    return;

	JScrollPane sp = new JScrollPane(list);
	sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	sp.setMinimumSize(new Dimension(100, 0));
	sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	foldersPanel.add(sp);

	list.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			System.out.println("-- right!"); // WRITEME: descend into this folder
		    else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			System.out.println("-- left!"); // WRITEME: go up one folder
		}
	    });
    }

    private JPanel foldersPanel;
    public ColumnBrowser(String folder) {
	// setLayout(new FlowLayout(FlowLayout.LEFT));
	setLayout(new BorderLayout());
	// setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	foldersPanel = new JPanel(new GridLayout(1, 0));
	add(foldersPanel, BorderLayout.WEST);

	addFolder(folder);

	addComponentListener(new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
		    if (useLists && e.getComponent().getHeight()<THRESHOLD) {
			// System.out.println("switch to popups");
			useLists = false;
		    } else if (!useLists && e.getComponent().getHeight()>THRESHOLD) {
			// System.out.println("switch to lists");
			useLists = true;
		    }
		}
	    });
    }
    private static final int THRESHOLD = 50; // make this 3 x row height, or maybe 2 x popup height.
    private boolean useLists = true;
    // REFACTOR: abstract this all out!

    // for debugging
    public static void main(String args[]) throws Exception {
	JFrame f = new JFrame();

	f.getContentPane().add(new ColumnBrowser("/"));

	f.pack();
	f.setSize(640, 280);
	f.setVisible(true);
    }

    /*
      on browser's top component, generally:
      -- it will have to differ by source type, just as itunes' does.
      -- columnbrowser works for Library and ITRDB, but not all
      -- lists and smartlists won't need a top component at all.
      -- a Database doesn't need one, but instead could have:
      ---- a "site" popup, to select one site, or "all".
      ---- a "search" field, where you can search, realtime.  it'll be plenty fast for that.
      -- so each Source will need a getTopComponent() method -- throw something to mean "none"?
    */
}
