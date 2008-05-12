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

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.graph.BargraphFrame;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.site.LegacySite;
import edu.cornell.dendro.corina.site.LegacySiteDB;
import edu.cornell.dendro.corina.site.SiteNotFoundException;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.awt.print.PageFormat;

/**
   The file menu for browser windows.

   <p>Currently, it holds:</p>
   <ul>
     <li>New Sample
     <li>New Folder</li>
     <br>
     <li>Open
     <li>Graph
     <li>Bargraph</li>
     <br>
     <li>Make Sum...</li>
     <br>
     <li>Page Setup...
     <li>Print...
   </ul>

   <h2>Left to do:</h2>
   <ul>
     <li>Implement "New Folder"
     <li>Dim menuitems (like "Open") when nothing is selected
     <li>Better error handling
     <li>Add "Quit" menuitem for non-Mac platforms?
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class BrowserFileMenu extends JMenu {

    private Browser browser;

    // TODO: if i extend FileMenu directly, do i get these for free?
    private PrinterJob printJob=null;
    private PageFormat pageFormat=new PageFormat();

    /**
       Make a new file menu for this Browser.

       @param browser the Browser to operate on
    */
    public BrowserFileMenu(Browser browser) {
        setText(I18n.getText("file"));

        this.browser = browser;

        init();
    }

    private void init() {
        addNewSampleMenuItem();

        addNewFolderMenuItem();

        // ---
        addSeparator();

        // crossdate
        add(Builder.makeMenuItem("new_crossdate",
                                 "new edu.cornell.dendro.corina.cross.CrossdateKit()"));

        // ---
        addSeparator();

        addOpenMenuItem();

        addGraphMenuItem();
        addBargraphMenuItem();

        addSeparator();

        addMakeSumMenuItem();

        // BUG: with nothing selected, "sum" menu should be dimmed, no?  or should "sum" be
        // just a context menu?

        addSeparator();

        // add(Builder.makeMenuItem("move_to_trash")); // WRITEME!
        // add(Builder.makeMenuItem("empty_trash...")); // WRITEME!
        // addSeparator();

        addPageSetupMenuItem();
        addPrintMenuItem();
    }

    /** Add a "New Sample" menuitem. */
    protected void addNewSampleMenuItem() {
        // new sample
        JMenuItem newSample = Builder.makeMenuItem("new_sample");
        newSample.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // try to figure out what site this is (from the folder)

                LegacySite s = null;
                try {
                    s = LegacySiteDB.getSiteDB().getSite(new File(browser.getFolder()));
                } catch (SiteNotFoundException snfe) {
                    // can't tell what site: so just leave it blank.
                }

                // new editor, with that title as a suggestion.
                // (title guess is "<site name> {put cursor here}")
                if (s == null)
                    new Editor();
                else
                    new Editor(s);
            }
        });
        add(newSample);
    }

    /** Add a "New Folder" menuitem. */
    protected void addNewFolderMenuItem() {
        JMenuItem newFolder = Builder.makeMenuItem("new_folder", /*WRITEME:*/false);
         // TODO: create new folder
         // TODO: select it, and start editing its name inline (need renaming first!)
         // -- what if i'm not displaying folders in the browser?  dialog?  no, edit on top ... what if it's a popup?
         // TODO: undoable, as long as nothing has been created/moved inside it
        // -- in which case moving/creating something inside it is undoable, and then this is undoable.
        add(newFolder);
    }

    /** Add an "Open" menuitem. */
    protected void addOpenMenuItem() {
        // open
        JMenuItem open = Builder.makeMenuItem("open");
        open.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new Editor(browser.getSelectedRow().getElement().load());
                } catch (IOException ioe) {
                    // TODO: display USER error message
                    new Bug(ioe);
                }
            }
        });
        add(open);
    }

    /** Add a "Graph" menuitem. */
    protected void addGraphMenuItem() {
        // graph
        JMenuItem graph = Builder.makeMenuItem("graph");
        graph.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // get selected samples
                ElementList elements = new ElementList();
                Iterator<Row> iter = browser.getSelectedRows();
                while (iter.hasNext()) {
                    Row row = (Row) iter.next();
                    elements.add(new Element(row.getElement())); // PERF: why not just pass in row.getElement()?
                }

                // BUG: not all rows will be loadable ... only get samples
                // BUG: what if no files are selected?  (add listener so if none are, this isn't even enabled?)

                // HMM: something feels wrong about having disposed
                // these Elements already, creating them again, only
                // to have them dumped and real Samples loaded.  i'd
                // feel a lot better with an i/o subsystem that let me
                // create the Element once, then promote it to a real
                // Sample.  but that's not exactly trivial.

                // graph
                new GraphWindow(elements);
            }
        });
        add(graph);
    }

    /** Add a "Bargraph" menuitem. */
    protected void addBargraphMenuItem() {
        // REFACTOR: exactly the same as the context menu bargraph menuitem (copied directly from there!)
        JMenuItem bargraph = Builder.makeMenuItem("bargraph");
        bargraph.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ElementList list = new ElementList();
                Iterator iter = browser.getSelectedRows();
                while (iter.hasNext()) {
                    Row row = (Row) iter.next();
                    list.add(new Element(row.getElement())); // re-use elements, which makes this really fast!
                }
                new BargraphFrame(list);
            }
        });
        add(bargraph);
    }

    /** Add a "Make Sum..." menuitem. */
    protected void addMakeSumMenuItem() {
        // sum
        JMenuItem sum = Builder.makeMenuItem("make_sum...");
        sum.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                browser.makeSumFromSelection();
            }
        });
        add(sum);
    }

    /** Add a "Page Setup..." menuitem. */
    protected void addPageSetupMenuItem() {
        JMenuItem setup = Builder.makeMenuItem("page_setup...");
        setup.addActionListener(new AbstractAction() { // REFACTOR: this taken verbatim from XMenubar
            public void actionPerformed(ActionEvent ae) {
                // make printer job, if none exists yet
                if (printJob == null)
                    printJob = PrinterJob.getPrinterJob();

                // get page format
                pageFormat = printJob.pageDialog(pageFormat);
            }
        });
        add(setup);
    }

    /** Add a "Print..." menuitem. */
    protected void addPrintMenuItem() {
        JMenuItem print = Builder.makeMenuItem("print...");
        // TODO: (if it's too wide, should i switch to multiple pages across, or landscape mode?)
        print.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                browser.print(printJob, pageFormat);
            }
        });
        add(print);
    }
}
