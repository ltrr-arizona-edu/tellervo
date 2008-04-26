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

package edu.cornell.dendro.corina.browser;

import edu.cornell.dendro.corina.Element;
import edu.cornell.dendro.corina.ElementFactory;
import edu.cornell.dendro.corina.ElementList;
import edu.cornell.dendro.corina.FileElement;
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.cross.CrossdateKit;
import edu.cornell.dendro.corina.cross.CrossdateWindow;
import edu.cornell.dendro.corina.cross.Sequence;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.graph.BargraphFrame;
import edu.cornell.dendro.corina.gui.TextWindow;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.PopupListener;

import java.util.Collections;
import java.util.List;
import java.io.IOException;

import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.AbstractAction;

/**
   A context (right-click) menu for browser entries.

   <p>The entries are:</p>
   <ul>
     <li>Open
     <li>Graph
     <li>Bargraph</li>
     <br>
     <li>Make Sum</li>
     <br>
     <li>View Source</li>
     <br>
     <li>Crossdate
     <li>&nbsp;&nbsp;&nbsp;&nbsp;1-by-1
     <li>&nbsp;&nbsp;&nbsp;&nbsp;1-by-N
     <li>&nbsp;&nbsp;&nbsp;&nbsp;N-by-1
     <li>&nbsp;&nbsp;&nbsp;&nbsp;N-by-N
   </ul>

   <h2>Left to do:</h2>
   <ul>
     <li>Use different methods for accessing browser data?
         (browser.table, browser.getSelectedFilenames(), browser.getSelectedElements())
     <li>Better error handling
     <li>Add "Move to Trash"?
     <li>Add "Get Info"?
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class BrowserContextMenu extends JPopupMenu {

    private JMenuItem open, graph, bargraph;
    private JMenuItem makeSum;
    private JMenuItem viewSource;
    private JMenuItem A, B, C, D; // the 4 crossdating sequences

    private Browser browser;

    /**
        Make a new browser context menu.  (This attaches a listener to the browser's
        JTable, too.)
        
        @param browser the Browser to add this context menu to.
    */
    public BrowserContextMenu(Browser browser) {
	this.browser = browser;

	initMenuItems();

	browser.table.addMouseListener(new PopupListener(this) {
		@Override
		public void showPopup(MouseEvent e) {
		    // enable/disable the menuitems on the popup, as necessary
		    enableDisable();

		    // select this row, if necessary, and show the popup
		    super.showPopup(e);
		}
	    });
    }

    private void enableDisable() {
	int n = browser.table.getSelectedRows().length;
	open.setEnabled(n == 1);
	graph.setEnabled(n >= 1);
	bargraph.setEnabled(n >= 1);
	makeSum.setEnabled(n >= 2);
	viewSource.setEnabled(n == 1);
	A.setEnabled(n == 2);
	B.setEnabled(n >= 1);
	C.setEnabled(n >= 1);
	D.setEnabled(n >= 2);
    }

    private void initMenuItems() {
	open = Builder.makeMenuItem("open");
	open.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    try {
		    	String filename = browser.getSelectedRow().getPath();
		    	Element el = ElementFactory.createElement(filename);
		    	new Editor(el.load());
		    } catch (IOException ioe) {
			// TODO: display USER error message
			Bug.bug(ioe);
		    }
		}
	    });

	graph = Builder.makeMenuItem("graph");
	graph.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    ElementList list = browser.getSelectedElements();
		    new GraphWindow(list);
		}
	    });
	bargraph = Builder.makeMenuItem("bargraph");
	bargraph.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    ElementList list = browser.getSelectedElements();
		    // here, i re-use elements, which makes this really fast
		    new BargraphFrame(list);
		}
	    });

	makeSum = Builder.makeMenuItem("make_sum...");
	makeSum.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    browser.makeSumFromSelection();
		}
	    });

	viewSource = Builder.makeMenuItem("view_source");
	// Builder.addAction(source, "corina.browser.Browser.viewSource()"); // ???
	viewSource.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    try {
                        String filename = browser.getSelectedRow().getPath();
			new TextWindow(filename);
		    } catch (IOException ioe) {
			// TODO: display USER error message
			Bug.bug(ioe);
		    }
		}
	    });

	add(open);
	add(graph);
	add(bargraph);
	addSeparator();
	add(makeSum);
	addSeparator();
	{
	    JMenu cross = Builder.makeMenu("crossdate");

	    A = Builder.makeMenuItem("1_by_1");
	    B = Builder.makeMenuItem("1_by_n");
	    C = Builder.makeMenuItem("n_by_1");
	    D = Builder.makeMenuItem("n_by_n");

	    A.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			List files = browser.getSelectedFilenames();

			List fixed = Collections.singletonList(files.get(0));
			List moving = Collections.singletonList(files.get(1));
			new CrossdateWindow(new Sequence(fixed, moving));
		    }
		});

	    B.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			List files = browser.getSelectedFilenames();
			new CrossdateKit(new Sequence(Collections.EMPTY_LIST, files));
			// REFACTOR: should crossdatekit take lists, too?
			// RENAME: "CrossdateKit" isn't terribly descriptive ... "EditLists?" need something better
		    }
		});

	    C.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			List files = browser.getSelectedFilenames();
			new CrossdateKit(new Sequence(files, Collections.EMPTY_LIST));
			// REFACTOR: should crossdatekit take lists, too?
		    }
		});

	    D.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			List files = browser.getSelectedFilenames();
			new CrossdateWindow(new Sequence(files, files));
		    }
		});

	    cross.add(A);
	    cross.add(B);
	    cross.add(C);
	    cross.add(D);

	    add(cross);
	}
	addSeparator();
	add(viewSource);
	// WRITEME:
	// addSeparator();
	// add(Builder.makeMenuItem("move_to_trash", false));
    }
}
