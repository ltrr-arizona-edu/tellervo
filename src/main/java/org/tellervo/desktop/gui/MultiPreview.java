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

package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.tellervo.desktop.Preview;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.sample.ElementFactory;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.ui.Builder;


@SuppressWarnings("serial")
public class MultiPreview extends JPanel implements PropertyChangeListener {

	// gui
	private JScrollPane previewScroller;

	private JButton add, remove;

	private ElementsPanel panel;

	private JFileChooser chooser;

	// data
	//private File file;
	private File files[];

	private ElementList set;

	// because only FileDialog knows what happens when a file is double-clicked...blah
	public void addClicked() {
		if(files != null) {			
			for(int i = 0; i < files.length; i++)
				// add to set
				set.add(ElementFactory.createElement(files[i].getPath()));

			// update view
			panel.update();
		}
	}

	public void hook(JFileChooser f) {
		chooser = f;
		chooser.addPropertyChangeListener(this);
	}

	public MultiPreview(ElementList ss) {
		// boilerplate
		set = ss;

		// gui -- layout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setMinimumSize(new Dimension(480, 360)); // ??
		setPreferredSize(new Dimension(400, 200)); // at least...

		// left panel
		JPanel left = new JPanel();
		left.setMaximumSize(new Dimension(180, 640));
		left.setLayout(new BorderLayout(6, 6));
		add(left);

		// left: preview
		previewScroller = new JScrollPane();
		previewScroller.setMinimumSize(new Dimension(240, 100));
		previewScroller.setBorder(BorderFactory.createEmptyBorder());
		left.add(previewScroller, BorderLayout.CENTER);

		// left: button panel
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(0, 1, 6, 6));
		buttons.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6)); // top-left-bottom-right
		left.add(buttons, BorderLayout.SOUTH);

		// left: buttons (add)
		add = Builder.makeButton("add");
		if (!Platform.isMac())
			add.setIcon(Builder.getIcon("1downarrow.png", 32));
		add.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				if(files != null) {			
					for(int i = 0; i < files.length; i++)
						// add to set
						set.add(ElementFactory.createElement(files[i].getPath()));

					// update view
					panel.update();
				}
				
			}
		});
		buttons.add(add);

		// left: buttons (remove)
		remove = Builder.makeButton("remove");
		if (!Platform.isMac())
			remove.setIcon(Builder.getIcon("1uparrow.png", 32));
		remove.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				panel.removeSelectedRows();
			}
		});
		buttons.add(remove);

		// left: buttons (ok)
		JButton okay = Builder.makeButton("ok");
		okay.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				// if (set.size() == 0) // okay with zero samples is like cancel
				// set = null;
				chooser.cancelSelection();
			}
		});
		buttons.add(okay);

		// left: buttons (cancel)
		JButton cancel = Builder.makeButton("cancel");
		cancel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				set = null;
				chooser.cancelSelection();
			}
		});
		buttons.add(cancel);

		// right: elements panel (table)
		panel = new ElementsPanel(set);
		panel.setView(ElementsPanel.VIEW_FILENAMES_MINIMAL);
		add(panel);
	}

	// PropertyChangeListener helper
	// (REFACTOR: use SamplePreview here!)
	private void loadSample() {
		// no file?
		if (files == null || files.length == 0)
			return;
		
		// if multiple files are selected, we can't really show a preview for all of them...
		if (files.length > 1) {
			showPreview(new Preview.TooManySelectedPreview(files.length));
			return;
		}

		showPreview(new Preview.NotDendroDataPreview());

		/*
		try {
			Previewable s = null;

			// new: loop to find a Previewable
			try {
				s = new Grid(files[0].getPath());
			} catch (WrongFiletypeException wfte) {
				s = ElementFactory.createElement(files[0].getPath()).load();
			} // but can't string catches here ... darn

			// get preview, and show it.
			showPreview(s.getPreview());

		} catch (WrongFiletypeException wfte) {
			showPreview(new Preview.NotDendroDataPreview());

		} catch (IOException ioe) {
			showPreview(new Preview.ErrorLoadingPreview(ioe));
		}
		*/
	}

	// implements PropertyChangeListener
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (prop.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
			files = chooser.getSelectedFiles();
			//file = (File) e.getNewValue();
			if (isShowing()) {
				loadSample();
				repaint();
			}
		}
	}

	private void showPreview(Preview p) {
		// just put a new previewcomponent inside the previewScroller...
		previewScroller.getViewport().setView(new PreviewComponent(p));		
	}

	// get the result
	public ElementList getElementList() {
		return set;
	}
}
