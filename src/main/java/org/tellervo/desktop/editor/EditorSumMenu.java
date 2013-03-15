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
package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.FileDialog;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.manip.Clean;
import org.tellervo.desktop.manip.Sum;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementFactory;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;


// sum
// - re-sum
// - clean
// ---
// - add...
// - remove

// (in the FUTURE, this menu won't exist: re-summing will be
// automatic, add/remove will be buttons on the elements panel, and
// clean will be under the manip menu.)

public class EditorSumMenu extends JMenu implements SampleListener {

	private static final long serialVersionUID = 1L;

	private JMenuItem resumMenu;

	private JMenuItem cleanMenu;

	private JMenuItem addMenu, remMenu;

	private Sample sample;
	
	public EditorSumMenu(Sample s) {
		super(I18n.getText("sum"));

		this.sample = s;

		sample.addSampleListener(this);

		// re-sum
		resumMenu = Builder.makeMenuItem("resum");
		resumMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// resum it
				try {
					sample = Sum.sum(sample);
				} catch (IOException ioe) {
					// FIXME: be more specific here! -- maybe even use Finder.java
					Alert.error("Error Summing",
							"There was an error while summing these files:\n"
									+ ioe.getMessage());
				} catch (Sum.GapInSumException gise) {
					// WRITEME
					/*
					 design:
					 -- say there's a gap
					 -- offer to draw a graph/bargraph so you can see where it is!
					 -- "i couldn't sum these %d samples, because there
					 -- would be a gap in the sum."
					 -- (show graph) (cancel)
					 -- possible to scroll graph to first gap?
					 -- if more than one gap, list them?
					 -- 1001-1036, 1055-1056, ...
					 */
					Alert.error("Error Summing", "There was a gap in the sum.");
				} catch (Sum.InconsistentUnitsException iue) {
					// WRITEME
					/*
					 design:
					 -- say there are inconsistent units
					 -- show 2 lists
					 ---- let user open files by double-clicking them
					 -- if >75%(?) of the samples are one format, say
					 ---- "of the 17 samples, all but the following 3 are indexed:"
					 */
					Alert
							.error("Error Summing",
									"Some elements are raw, some indexed: can't mix types.");
				} catch (Exception e) {
					new Bug(e);

					// BUG: if the user deletes all elements from
					// this sample, sum throws something to here...
					// otherwise, this clause ISN'T NEEDED (right?)
				}
			}
		});
		add(resumMenu);

		// clean
		cleanMenu = Builder.makeMenuItem("clean");
		cleanMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// clean, and add to undo-stack
				sample.postEdit(Clean.clean(sample));
				// FIXME: move undo stack from Sample to Editor (really?)
				// FIXME: make Clean implement Manipulation,
				// and give it a good interface like Clean(sample), run()?
			}
		});
		add(cleanMenu);

		/*
		 so, eventually, this list of menus and menuitems will reduce to a sequence of
		 menuX = Builder.makeMenuItem("blah");
		 menuX.addActionListener(real code!);
		 menubar.add(menuX);
		 ...
		 which in turn could be reduced to
		 menubar = Builder.makeMenuBar("blah");
		 where "blah" is defined in a resource file as
		 menubar "blah" = menu "oink", menu "moo", menu "baa", ...
		 menu "oink" = menuitem "oink 1", menuitem "oink 2", ...
		 but putting these in a resource file might not be the best idea for performance
		 so put them all in one java class
		 and let that java class be generated at compile-time from a resource file
		 (when performance doesn't matter, so XML is ok)
		 */

		// ---
		addSeparator();

		// add item -- ENABLED IFF THERE EXIST OTHER ELEMENTS OR DATA IS-EMPTY
		addMenu = Builder.makeMenuItem("element_add...");
		addMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// open file dialog
				String filename;
				try {
					filename = FileDialog.showSingle("Add");
				} catch (UserCancelledException uce) {
					return;
				}

				// new elements, if needed
				if (sample.getElements() == null)
					sample.setElements(new ElementList());

				// ADD CHECK: make sure indexed+indexed, or raw+raw

				// remember how many elements there used to be
				//int numOld = sample.getElements().size();

				// add -- if summed, add each one
				Element src = ElementFactory.createElement(filename);
				Sample testSample = null;
				try {
					// TODO: Use an ElementFactory and then load!
					testSample = src.load();
				} catch (IOException ioe) {
					int x = JOptionPane.showConfirmDialog(null,
							"The file \"" + filename
									+ "\" could not be loaded.  Add anyway?",
							"Unloadable file.", JOptionPane.YES_NO_OPTION);
					if (x == JOptionPane.NO_OPTION)
						return;
				}

				// for a summed sample, don't add it, but add its elements
				if (testSample.getElements() != null) {
					for (int i = 0; i < testSample.getElements().size(); i++)
						sample.getElements().add(testSample.getElements().get(i)); // copy ref only
				} else {
					sample.getElements().add(src);
				}

				// modified, and update
				sample.setModified();
				sample.fireSampleElementsChanged();
				sample.fireSampleMetadataChanged(); // meta's "modified?" flag
			}
		});
		add(addMenu);

		// remove item
		remMenu = Builder.makeMenuItem("element_remove");
		remMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// fixme: make this undoable
			}
		});
		add(remMenu);

		// kick to set state
		sampleMetadataChanged(null);
	}

	//
	// listener
	//
	public void sampleRedated(SampleEvent e) {
	}

	public void sampleDataChanged(SampleEvent e) {
	}

	public void sampleMetadataChanged(SampleEvent e) {
		// resum: only if elements present
		// QUESTION: what if it has zero elements?
		resumMenu.setEnabled(sample.getElements() != null);

		// clean: if summed (what's that mean, exactly?)
		cleanMenu.setEnabled(sample.isSummed());

		// add/remove: are always enabled.  they shouldn't be,
		// but they apparently have been before, and they'll
		// be gone soon, anyway, so it's not worth the effort.
	}

	public void sampleElementsChanged(SampleEvent e) {
		// clean: if summed (what's that mean, exactly?)
		cleanMenu.setEnabled(sample.isSummed());
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
