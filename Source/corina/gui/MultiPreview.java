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
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.gui;

import corina.Sample;
import corina.Element;
import corina.Previewable;
import corina.files.WrongFiletypeException;
import corina.cross.Grid;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class MultiPreview extends JPanel implements PropertyChangeListener {

    // gui
    private JLabel label;
    private JButton add, remove;
    private ElementsPanel panel;
    private JFileChooser chooser;

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("FileDialogBundle");

    // data
    private File file;
    private List set;

    // because only FileDialog knows what happens when a file is double-clicked...blah
    public void addClicked() {
	// add to set
	set.add(new Element(file.getPath()));

	// update view
	panel.update();
    }

    public void hook(JFileChooser f) {
	chooser = f;
	chooser.addPropertyChangeListener(this);
    }

    public MultiPreview(List ss) {
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
	label = new JLabel();
	label.setVerticalAlignment(JLabel.TOP);
	label.setMaximumSize(new Dimension(150, 150));
	left.add(label, BorderLayout.CENTER);

	// left: button panel
	JPanel buttons = new JPanel();
	buttons.setLayout(new GridLayout(0, 1, 6, 6));
	buttons.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6)); // top-left-bottom-right
	left.add(buttons, BorderLayout.SOUTH);

	// left: buttons (add)
	add = new JButton(msg.getString("add"),
			  JarIcon.getJavaIcon("toolbarButtonGraphics/navigation/Forward16.gif"));
	add.setMnemonic(msg.getString("add_key").charAt(0));
	add.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // add to set
		    set.add(new Element(file.getPath())); // NullPointerException if no file selected!

		    // update view
		    panel.update();
		}
	    });
	buttons.add(add);

	// left: buttons (remove)
	remove = new JButton(msg.getString("remove"),
			     JarIcon.getJavaIcon("toolbarButtonGraphics/navigation/Back16.gif"));
	remove.setMnemonic(msg.getString("remove_key").charAt(0));
	remove.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    panel.removeSelectedRows();
		}
	    });
	buttons.add(remove);

	// left: buttons (ok)
	JButton okay = new JButton(msg.getString("ok"));
	okay.setMnemonic(msg.getString("ok_key").charAt(0));
	okay.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // if (set.size() == 0) // okay with zero samples is like cancel
		    // set = null;
		    chooser.cancelSelection();
		}
	    });
	buttons.add(okay);

	// left: buttons (cancel)
	JButton cancel = new JButton(msg.getString("cancel"));
	cancel.setMnemonic(msg.getString("cancel_key").charAt(0));
	cancel.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    set = null;
		    chooser.cancelSelection();
		}
	    });
	buttons.add(cancel);

	// right: elements panel (table)
	panel = new ElementsPanel(set);
	add(panel);
    }

    // PropertyChangeListener helper
    private void loadSample() {
	// no file?
        if (file == null)
            return;
 
	// REFACTOR: use SamplePreview here!
	try {
	    Previewable s=null;

	    // new: loop to find a Previewable
	    try {
		s = new Grid(file.getPath());
	    } catch (WrongFiletypeException wfte) {
		s = new Sample(file.getPath());
	    } // but can't string catches here ... darn

	    // old: Sample s = new Sample(file.getPath());

	    // see SamplePreview.java
	    final String glue = s.getHTMLPreview();
	    (new Thread() {
		    public void run() {
			label.setText(glue); // got a nullpe here once -- how?
		    }
		}).start();

	} catch (IOException ioe) {
	    label.setText("(not a data file)");
	}
    }

    // implements PropertyChangeListener
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            file = (File) e.getNewValue();
            if (isShowing()) {
                loadSample();
                repaint();
            }
        }
    }

    // get the result
    public List getSamples() {
	return set;
    }

}
