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

package edu.cornell.dendro.corina.editor;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.SampleEvent;
import edu.cornell.dendro.corina.SampleListener;
import edu.cornell.dendro.corina.ui.AskNumber;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.TextClipboard;
import edu.cornell.dendro.corina.util.PureStringWriter;
import edu.cornell.dendro.corina.formats.TwoColumn;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.gui.menus.EditMenu;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.io.SerialSampleIO;
import edu.cornell.dendro.corina.core.App;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.Toolkit;

import java.util.List;
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

 @see edu.cornell.dendro.corina.formats.TwoColumn

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */
public class EditorEditMenu extends EditMenu implements SampleListener {
	private Sample sample;

	private Editor editor;

	private SampleDataView dataView;

	/**
	 Make a new Edit menu for an Editor.  This stores the parameters,
	 and adds itself as a SampleListener.
	 
	 @param sample the Sample to watch
	 @param dataView the SampleDataView to use for inserting and deleting years
	 */
	public EditorEditMenu(Sample sample, SampleDataView dataView, Editor editor) {
		super();

		this.sample = sample;
		this.dataView = dataView;
		this.editor = editor;

		sample.addSampleListener(this);
	}

	@Override
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

	@Override
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
	@Override
	protected void addCopy() {
		// copy: put all data unto clipboard in 2-column format
		JMenuItem copy = Builder.makeMenuItem("copy");
		copy.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				CopyDialog d = new CopyDialog(editor, sample.getRange());
				if(d.isOk())
					TextClipboard.copy(asTwoColumn(d.getChosenRange()));
				// copy the sample to the clipboard, as 2-column
				//TextClipboard.copy(asTwoColumn());
			}
		});
		add(copy);
	}

	/**
	 Add a Paste menuitem that replaces this data with whatever
	 is on the clipboard.
	 */
	@Override
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
	@Override
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
		addInsertYears();

		if (SerialSampleIO.hasSerialCapability()
				&& App.prefs.getPref("corina.serialsampleio.port") != null) {
			addSeparator();
			addMeasure();
		}

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

	private void addMeasure() {
		measureMenu = Builder.makeMenuItem("start_measuring");
		measureMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				editor.toggleMeasuring();
			}
		});
		add(measureMenu);
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
	
	private void addInsertYears() {
		insert = Builder.makeMenuItem("insert_years");
		insert.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				try {
					// first, get the number of years...
					int value = AskNumber.getNumber(
							editor, "Insert years...",
							"Insert how many years?", 2);
					
					String labels[] = {"Blank", "Default Value ["+Sample.MR+"]", "Cancel"};
					
					int ret = JOptionPane.showOptionDialog(
							editor,
							"What would you like the newly inserted years to be set to?",
							"Insert " + value + " years...",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, labels,
							labels[0]);
					
					switch(ret) {
					case 0:
						dataView.insertYears("", value);
						break;
					case 1:
						dataView.insertYears(new Integer(Sample.MR), value);
						break;
					case 2: // cancel
						break;
					}					
				} catch(UserCancelledException uce) {
					return;
				}
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
			DataFlavor f = DataFlavor.selectBestTextFlavor(t
					.getTransferDataFlavors());
			BufferedReader r = null;
			BufferedWriter w = null;

			try {
				r = new BufferedReader(f.getReaderForText(t));
				w = new BufferedWriter(new FileWriter(tmpFilename));
				// transfer it
				for (;;) {
					String l = r.readLine();
					if (l == null)
						break;
					w.write(l + '\n');
				}
			} finally {
				if (r != null)
					try {
						r.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				if (w != null)
					try {
						w.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
			}

			// now try to load it
			Sample tmp = new Sample(tmpFilename);

			// copy it here, except for the filename
			Sample.copy(tmp, sample);
			sample.removeMeta("filename");
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
			int estimatedLength = 10 * sample.getData().size();
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

	private String asTwoColumn(Range range) {
		try {
			int inindex = range.getStart().compareTo(sample.getRange().getStart());
			
			List tmpData = sample.getData().subList(inindex, inindex + range.span());
			List tmpCount = (sample.getCount() == null) ? null : sample.getCount().subList(inindex, inindex + range.span());
			Sample tmpSample = new Sample();
			
			tmpSample.setRange(range);
			tmpSample.setData(tmpData);
			tmpSample.setCount(tmpCount);
			
			int estimatedLength = 10 * tmpSample.getData().size();
			PureStringWriter w = new PureStringWriter(estimatedLength);
			BufferedWriter b = new BufferedWriter(w);
			new TwoColumn().save(tmpSample, b);
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
	public void sampleRedated(SampleEvent e) {
	}

	public void sampleDataChanged(SampleEvent e) {
	}

	public void sampleMetadataChanged(SampleEvent e) {
		// insert/delete
		insert.setEnabled(sample.isEditable());
		insertMR.setEnabled(sample.isEditable());
		delete.setEnabled(sample.isEditable());

		// measure menu: only if not summed
		if (measureMenu != null)
			measureMenu.setEnabled(!sample.isSummed());
	}

	// change the text in the menu to match what we're doing
	public void setMeasuring(boolean measuring) {
		if (measureMenu == null)
			return;

		if(!measuring)
			measureMenu.setText(I18n.getText("start_measuring"));
		else
			measureMenu.setText(I18n.getText("stop_measuring"));
	}

	public void sampleElementsChanged(SampleEvent e) {
	}
}
