/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.gui.menus;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.CopyDialog;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.editor.PopulateEditorDialog;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.gui.menus.actions.EditMeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.FilePrintAction;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.io.TwoColumn;
import org.tellervo.desktop.io.WrongFiletypeException;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.FileElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.AskNumber;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.PureStringWriter;
import org.tellervo.desktop.util.TextClipboard;
import org.tridas.schema.TridasDerivedSeries;


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

 @see org.tellervo.desktop.io.TwoColumn

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */
public class EditorEditMenu extends EditMenu implements SampleListener {

	private static final long serialVersionUID = 1L;
	private Sample sample;
	private final AbstractEditor editor;
	private SeriesDataMatrix dataView;
	private JMenuItem insert, insertMR, delete;
	private JMenuItem toggleMeasureMenuItem;
	private JMenu measureModeMenu;
	private JMenuItem undoMenu, redoMenu;
	private JRadioButtonMenuItem btnRingWidth;
	private JRadioButtonMenuItem btnEWLWWidth;
	private final static Logger log = LoggerFactory.getLogger(EditorEditMenu.class);

	
	/**
	 Make a new Edit menu for an Editor.  This stores the parameters,
	 and adds itself as a SampleListener.
	 
	 @param sample the Sample to watch
	 @param dataView the SampleDataView to use for inserting and deleting years
	 */
	public EditorEditMenu(Sample sample, SeriesDataMatrix dataView, Editor editor) {
		super(editor);

		this.sample = sample;
		this.dataView = dataView;
		this.editor = editor;

		sample.addSampleListener(this);
		
		// Disable measuring fields if this is a derivedSeries
		if(sample.getSeries() instanceof TridasDerivedSeries)
		{
			setMeasuringEnabled(false);
		}
		
		// Set initial state of measuring 
		if(sample!=null)
		{
			
			try{
			if(sample.containsSubAnnualData() && btnEWLWWidth!=null)
			{
				btnEWLWWidth.setSelected(true);
			}
			else if (!sample.containsSubAnnualData() && btnRingWidth!=null)
			{
				btnRingWidth.setSelected(true);
			}
			} catch (Exception e)
			{
				log.error(e.getLocalizedMessage());
			}
		}
		
	}

	@Override
	protected void addUndo() {
		/*undoMenu = Builder.makeMenuItem("menus.edit.undo", true, "undo.png");
		undoMenu.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				// DISABLED: undoManager.undo();
				// DISABLED: refreshUndoRedo();
			}
		});
		undoMenu.setEnabled(false);
		add(undoMenu);*/
	}

	@SuppressWarnings("serial")
	@Override
	protected void addRedo() {
		/*redoMenu = Builder.makeMenuItem("menus.edit.redo", true, "redo.png");
		redoMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// DISABLED: undoManager.redo();
				// DISABLED: refreshUndoRedo();
			}
		});
		redoMenu.setEnabled(false);
		add(redoMenu);*/
	}

	/**
	 Add a Copy menuitem that copies this Sample to the clipboard
	 in 2-column format.
	 */
	@SuppressWarnings("serial")
	@Override
	protected void addCopy() {
		// copy: put all data unto clipboard in 2-column format
		JMenuItem copy = Builder.makeMenuItem("menus.edit.copy", true, "editcopy.png");
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
	@SuppressWarnings("serial")
	@Override
	protected void addPaste() {
		// paste: replace (insert?) data from clipboard (any format) into this sample
		JMenuItem paste = Builder.makeMenuItem("menus.edit.paste", true, "editpaste.png");
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
		//addUndoRedo();
		//addSeparator();

		addClipboard();
		addSeparator();

		addSelectAll();

		addSeparator();

		// insert, insert MR, delete
		addInsert();
		addInsertBackwards();
		addInsertMR();
		addInsertMRBackwards();
		addDelete();
		addInsertYears();

		addSeparator();
		addMeasure();


		addPreferences();
		

	}
	
	private void setMeasuringEnabled(Boolean b)
	{
		if (toggleMeasureMenuItem != null)
		{
			toggleMeasureMenuItem.setEnabled(b);
		}
		
		/*if(measureModeMenu != null)
		{
			measureModeMenu.setEnabled(b);
		}*/
		
		
	}

	@SuppressWarnings("serial")
	private void addMeasure() {
		
		if(editor==null) log.error("Editor is null");
		
		Action measureAction = new EditMeasureToggleAction(editor);
		//toggleMeasureMenuItem = new JMenuItem(measureAction);		
		toggleMeasureMenuItem = Builder.makeMenuItem("menus.edit.start_measuring", true, "measure.png");
		toggleMeasureMenuItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				editor.toggleMeasuring();
			}
		});
		add(toggleMeasureMenuItem);
		
		measureModeMenu = Builder.makeMenu("menus.edit.measuremode");
		btnRingWidth = new JRadioButtonMenuItem(I18n.getText("menus.edit.measuremode.ringwidth"));
		btnEWLWWidth = new JRadioButtonMenuItem(I18n.getText("menus.edit.measuremode.ewlwwidth"));
		
		ButtonGroup group = new ButtonGroup();
		group.add(btnRingWidth);
		group.add(btnEWLWWidth);
		
		measureModeMenu.add(btnRingWidth);
		measureModeMenu.add(btnEWLWWidth);
		

		btnRingWidth.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (sample.containsSubAnnualData()          && 
					sample.getEarlywoodWidthData()!=null    &&
					sample.getLatewoodWidthData()!=null     &&
				   (sample.getEarlywoodWidthData().size()>0 || 
				    sample.getLatewoodWidthData().size()>0)    )
				{
	
					int n = JOptionPane.showConfirmDialog(editor, 
							"Switching to ring width measuring mode " +
							"will delete any\n"+
							"early/late wood values.\n\n"+
							"Are you sure you want to continue?");
			
					if(n != JOptionPane.YES_OPTION) 
					{
						btnEWLWWidth.setSelected(true);
						return;
					}
				}
					
				sample.setToAnnualMode();
				
				App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.RING_WIDTH.toString());
				sample.fireMeasurementVariableChanged();		
				
			}
			
		});
		
		btnEWLWWidth.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sample.getRingWidthData().size()>0)
				{
					int n = JOptionPane.showConfirmDialog(editor, 
							"Switching to early/latewood measuring mode will\n"+"" +
							"delete any ring width values you have.\n\n"+
							"Are you sure you want to continue?");
			
					if(n != JOptionPane.YES_OPTION) 
					{
						btnRingWidth.setSelected(true);
						return;
					}
				}
					
				sample.setToSubAnnualMode();
				
				App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH.toString());
				sample.fireMeasurementVariableChanged();
				
				
			}
			
			
		});
		

		// Only enable measure button if we have serial device capabilities
		if(!AbstractSerialMeasuringDevice.hasMeasuringDeviceCapability())
		{
			toggleMeasureMenuItem.setEnabled(false);
			btnRingWidth.setEnabled(false);
			btnEWLWWidth.setEnabled(false);
		}

		add(measureModeMenu);
	}

	@SuppressWarnings("serial")
	private void addInsert() {
		insert = Builder.makeMenuItem("menus.edit.insert_year", true, "insertyear.png");
		insert.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				dataView.insertYear();
			}
		});
		add(insert);
	}
	
	@SuppressWarnings("serial")
	private void addInsertBackwards() {
		insert = Builder.makeMenuItem("menus.edit.insert_year.back", true, "insertyear.png");
		insert.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				dataView.insertYear(0, true, null, false);
			}
		});
		
		add(insert);
	}
	
	
	@SuppressWarnings("serial")
	private void addInsertYears() {
		insert = Builder.makeMenuItem("menus.edit.insert_years");
		insert.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				try {
					// first, get the number of years...
					int value = AskNumber.getNumber(
							editor, I18n.getText("menus.edit.insert_years"),
							I18n.getText("question.insertHowManyYears"), 2);
					
					String labels[] = {I18n.getText("general.blank"), I18n.getText("general.defaultValue") +" "+Sample.missingRingValue+"", I18n.getText("general.cancel")};
					
					int ret = JOptionPane.showOptionDialog(
							editor,
							I18n.getText("question.whatShouldInsertYearsBeSetTo"),
							I18n.getText("general.insert") + " " + value + " " + I18n.getText("general.years"),
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, labels,
							labels[0]);
					
					switch(ret) {
					case 0:
						dataView.insertYears(0, value);
						break;
					case 1:
						dataView.insertYears(new Integer(Sample.missingRingValue), value);
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
		
		
		JMenuItem prepopulate = Builder.makeMenuItem("menus.edit.populateditor");
		prepopulate.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae){
				PopulateEditorDialog dialog = new PopulateEditorDialog(editor, dataView);
				dialog.setVisible(true);
			}
		});
		
		add(prepopulate);
		
			
	}

	@SuppressWarnings("serial")
	private void addInsertMR() {
		insertMR = Builder.makeMenuItem("menus.edit.insert_mr", true, "insertmissingyear.png");
		insertMR.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				dataView.insertMissingRing();
			}
		});
		add(insertMR);
	}

	@SuppressWarnings("serial")
	private void addInsertMRBackwards() {
		insertMR = Builder.makeMenuItem("menus.edit.insert_mr.back", true, "insertmissingyear.png");
		insertMR.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				dataView.insertMissingRingBackwards();
			}
		});
		insertMR.setEnabled(false);
		add(insertMR);
	}
	
	@SuppressWarnings("serial")
	private void addDelete() {
		delete = Builder.makeMenuItem("menus.edit.delete_year", true, "deleteyear.png");
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

			// use $(TMP)/tellervoXXXXX.clip for the filename
			File tmpFile = File.createTempFile("tellervo", ".clip");
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
			Sample tmp = new FileElement(tmpFilename).load();

			// copy it here, except for the filename
			Sample.copy(tmp, sample);
			sample.removeMeta("filename");
		} catch (WrongFiletypeException wfte) {
			// unreadable format.  tell user.
			Alert.error(I18n.getText("error"),
					I18n.getText("error.clipboardError"));
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
			Alert.error(I18n.getText("error"),
					I18n.getText("error.clipboardError"));
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

		 quick hack just to make it work: save to /tmp/tellervoXXXXX.clip, ...
		 */
	}

	@SuppressWarnings("unchecked")
	private String asTwoColumn(Range range) {
		try {
			int inindex = range.getStart().compareTo(sample.getRange().getStart());
			
			List tmpData = sample.getRingWidthData().subList(inindex, inindex + range.getSpan());
			List tmpCount = sample.hasCount() ? null : sample.getCount().subList(inindex, inindex + range.getSpan());
			Sample tmpSample = new Sample();
			
			tmpSample.setRange(range);
			tmpSample.setRingWidthData(tmpData);
			tmpSample.setCount(tmpCount);
			
			int estimatedLength = 10 * tmpSample.getRingWidthData().size();
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
	
	// change the text in the menu to match what we're doing
	public void setMeasuring(boolean measuring) {
		if (toggleMeasureMenuItem == null)
			return;

		if(!measuring)
			toggleMeasureMenuItem.setText(I18n.getText("menus.edit.start_measuring"));
		else
			toggleMeasureMenuItem.setText(I18n.getText("menus.edit.stop_measuring"));
	}
	
	

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
		this.setMeasuringEnabled(!sample.isSummed());

	}

	public void sampleElementsChanged(SampleEvent e) {
	}

	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}
