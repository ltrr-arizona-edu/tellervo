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

package corina.editor;

import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.ui.Alert;
import corina.util.TextClipboard;
import corina.util.PureStringWriter;
import corina.formats.TwoColumn;
import corina.formats.WrongFiletypeException;
import corina.gui.menus.EditMenu;
import corina.gui.Bug;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
   The Edit menu for editor windows.

   <p>An Editor Edit menu differs from the default EditMenu in that:</p>
   <ul>
     <li>It implements Undo and Redo
     <li>It implements Copy ("copy to clipboard in 2-column format")
     <li>It implements Paste ("replace this data with whatever's on the clipboard")
     <li>It adds 3 new menu items: Insert Year, Insert MR, and Delete Year
   </ul>
 
   <h2>Left to do:</h2>
   <ul>
     <li>Fix Undo/Redo (put manager <i>here</i>?)
     <li>Instead of calling Bug, fix the bug!
     <li>Document in manual (also, in a menu reference appendix?)
     <li>I shouldn't need SampleDataView here: I should be able to add
         a year, and have the data view be listening for that
         (need to fix in SampleDataView)
     <li>Can/should I remove myself as a SampleListener on, say,
         removeNotify()?  Can/should I only add myself as a SampleListener
         on addNotify()?
   </ul>

   @see corina.formats.TwoColumn

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class EditorEditMenu extends EditMenu implements SampleListener {

    private Sample sample;
    private SampleDataView dataView;

    /**
       Make a new Edit menu for an Editor.  This stores the parameters,
       and adds itself as a SampleListener.
        
       @param sample the Sample to watch
       @param dataView the SampleDataView to use for inserting and deleting years
    */
    public EditorEditMenu(Sample sample, SampleDataView dataView) {
        super();

	this.sample = sample;
	this.dataView = dataView;

	sample.addSampleListener(this);
    }

    protected void addUndo() {
	undoMenu = Builder.makeMenuItem("undo");
	undoMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // DISABLED: undoManager.undo();
		    // DISABLED: refreshUndoRedo();
		}
	    });
        undoMenu.setEnabled(false);
	add(undoMenu);
    }

    protected void addRedo() {
	redoMenu = Builder.makeMenuItem("redo");
	redoMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // DISABLED: undoManager.redo();
		    // DISABLED: refreshUndoRedo();
		}
	    });
        redoMenu.setEnabled(false);
	add(redoMenu);
    }

    /**
       Add a Copy menuitem that copies this Sample to the clipboard
       in 2-column format.
    */
    protected void addCopy() {
        // copy: put all data unto clipboard in 2-column format
	JMenuItem copy = Builder.makeMenuItem("copy");
        copy.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
		// copy the sample to the clipboard, as 2-column
		TextClipboard.copy(asTwoColumn());
            }
        });
        add(copy);
    }

    /**
       Add a Paste menuitem that replaces this data with whatever
       is on the clipboard.
    */
    protected void addPaste() {
        // paste: replace (insert?) data from clipboard (any format) into this sample
        JMenuItem paste = Builder.makeMenuItem("paste");
        paste.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        });
        add(paste);
    }

    /**
       Put all the normal items in the menu, along with Insert,
       Insert MR, and Delete.
    */
    protected void init() {
        addUndoRedo();
        addSeparator();

        addClipboard();
        addSeparator();

        addSelectAll();

        addSeparator();
        
        // insert, insert MR, delete
        addInsert();
        addInsertMR();
        addDelete();

	/*
        // if comm present, start/stop measure
        if (false) { // Measure.hasSerialAPI()) {
            addSeparator();
            measureMenu = new Measure(this).makeMenuItem();
            add(measureMenu);
        }
	*/

        addPreferences();
	// TODO: hit listeners to make initial state right
    }

    private void addInsert() {
	insert = Builder.makeMenuItem("insert_year");
	insert.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    dataView.insertYear();
		}
	    });
	add(insert);
    }

    private void addInsertMR() {
	insertMR = Builder.makeMenuItem("insert_mr");
	insertMR.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    dataView.insertMR();
		}
	    });
	add(insertMR);
    }

    private void addDelete() {
	delete = Builder.makeMenuItem("delete_year");
	delete.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    dataView.deleteYear();
		}
	    });
	add(delete);
    }

    // helper functions follow:

    private void paste() {
        // get the stuff that's on the clipboard
        // REFACTOR: would my TextClipboard abstraction help out here?
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(null);
        if (t == null)
            return; // clipboard contains no data

        try {
            // copy text from the clipboard to $(TMP).  (why?
            // because Sample(filename) takes arbitrary
            // formats, load(Reader) does not.  (fixable?))

            // use $(TMP)/corinaXXXXX.clip for the filename
            File tmpFile = File.createTempFile("corina", ".clip");
            tmpFile.deleteOnExit();
            final String tmpFilename = tmpFile.getPath();

            // get ready to transfer the data
            DataFlavor f = DataFlavor.selectBestTextFlavor(t.getTransferDataFlavors());
            BufferedReader r = new BufferedReader(f.getReaderForText(t));
            BufferedWriter w = new BufferedWriter(new FileWriter(tmpFilename));

            // transfer it
            for (;;) {
                String l = r.readLine();
                if (l == null)
                    break;
                w.write(l + '\n');
            }

            // clean up
            w.close();
            r.close();

            // now try to load it
            Sample tmp = new Sample(tmpFilename);

            // copy it here, except for the filename
            Sample.copy(tmp, sample);
            sample.meta.remove("filename");
        } catch (WrongFiletypeException wfte) {
            // unreadable format.  tell user.
            Alert.error("Problem Pasting",
                        "The clipboard doesn't appear to have a dendro dataset.");
            return;
        } catch (IOException ioe) {
            // shouldn't ever happen.  this means there was a problem reading or
            // writing a "clipboard" file.  probably a bug, then.
            // FUTURE: if i refactor so i don't need to use $TMP, this will
            // probably become a "can't happen" case.  (really?)
            new Bug(ioe);
            return;
        } catch (UnsupportedFlavorException ufe) {
            // clipboard doesn't have text on it.  tell user.
            Alert.error("Problem Pasting",
                        "The clipboard doesn't appear to have text on it.");
            return;
        }

        // fire all sorts of alarms
        sample.fireSampleRedated();
        sample.fireSampleDataChanged();
        sample.fireSampleMetadataChanged();
        sample.fireSampleElementsChanged();

        /* oh crap.  it iterates over the LOADERS[], passing them filenames.
            i'll need to abstract out the iteration, and put loading from a
            filename in sample(filename) only.  i guess that cleans up some
            low-level things, but it means more work now.

            err, no.  what do you want it to do, iterate over readers?
            can you guarantee that a reader is reset()able to its start?
            you might have to save the entire thing to a buffer ... er, that's
            where it is.  you might have to get a reader passed, clone it,
            try, clone, try, but readers probably don't work that way.  there's
            got to be a way to do this.

            (it's good this came up, though.  opening a file 5 times because
             it's not the most popular format is killing i/o performance.)

            quick hack just to make it work: save to /tmp/corinaXXXXX.clip, ...
            */
    }

    // return the sample in 2-column format, as a string
    private String asTwoColumn() {
	try {
	    int estimatedLength = 10 * sample.data.size();
	    PureStringWriter w = new PureStringWriter(estimatedLength);
	    BufferedWriter b = new BufferedWriter(w);
	    new TwoColumn().save(sample, b);
	    b.close();
	    return w.toString();
	} catch (IOException ioe) {
	    // can't happen: i'm just writing to a buffer
	    return "";
	}
    }

    // menuitems that i'll need refs for
    private JMenuItem insert, insertMR, delete;

    private JMenuItem measureMenu;

    private JMenuItem undoMenu, redoMenu;

    //
    // listener
    //
    public void sampleRedated(SampleEvent e) { }
    public void sampleDataChanged(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) {
	// insert/delete
	insert.setEnabled(sample.isEditable());
	insertMR.setEnabled(sample.isEditable());
	delete.setEnabled(sample.isEditable());

        // measure menu: only if not summed
        if (measureMenu != null)
            measureMenu.setEnabled(!sample.isSummed());
    }
    public void sampleElementsChanged(SampleEvent e) { }
}
