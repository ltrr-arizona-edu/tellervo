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

package corina.editor;

/*
  this can become a panel when preferences is made into tabs-of-panels
  instead of tabs-of-controls-from-xml
*/

import corina.Metadata;

import java.util.List;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.SwingConstants;

import javax.swing.AbstractAction;

public class ElementsViewOptions extends JFrame {
    private JCheckBox boxes[];
    private boolean noneSelected() {
	for (int i=0; i<boxes.length; i++)
	    if (boxes[i].isSelected())
		return false;
	return true;
    }

    // make a checkbox for the metadata field |field|
    private JCheckBox makeCheckbox(Metadata.Field field) {
	JCheckBox check = new JCheckBox(field.description);
	// set it somehow --?

	check.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // writeme: re/set |var| here

		    boolean on = ((JCheckBox) e.getSource()).isSelected();
		    // WORKING HERE

		    if (noneSelected())
			remainder.setSelected(true);
		}
	    });

	return check;
    }

    private JCheckBox remainder;

    private JPanel makeFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	panel.add(Box.createVerticalStrut(10));

	JPanel label = new JPanel(new BorderLayout());
	label.add(new JLabel("Show Fields"), BorderLayout.WEST);
	panel.add(label);

	JPanel left = new JPanel();
	left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
	JPanel right = new JPanel();
	right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
	JPanel grid = new JPanel();
	grid.setLayout(new BoxLayout(grid, BoxLayout.X_AXIS));
	grid.add(Box.createHorizontalStrut(20));
	grid.add(left);
	grid.add(right);
	grid.add(Box.createHorizontalStrut(10));
	panel.add(grid);

	// 0 => Active
	// 1 => Range
	// 2 => Filename

	// stuff checkboxes into a list (array)
	boxes = new JCheckBox[Metadata.fields.length + 3];
	int n=0;
	boxes[n++] = new JCheckBox("Active");
	boxes[n++] = new JCheckBox("Range");
	boxes[n++] = new JCheckBox("Filename");
	for (int i=0; i<Metadata.fields.length; i++)
	    boxes[n++] = makeCheckbox(Metadata.fields[i]);

	// remainder: if user disables everything else, this remains
	remainder = boxes[2]; // filename

	// print checkboxes
	for (int i=0; i<(boxes.length+1)/2; i++)
	    left.add(boxes[i]);
	for (int i=(boxes.length+1)/2; i<boxes.length; i++)
	    right.add(boxes[i]);

	return panel;
    }

    // for combo box ("sort field")
    static String descriptions[] = new String[Metadata.fields.length + 3];
    static {
	descriptions[0] = "Active";
	descriptions[1] = "Range";
	descriptions[2] = "Filename";
	for (int i=0; i<Metadata.fields.length; i++)
	    descriptions[i+3] = Metadata.fields[i].description;
    }

    private JComboBox sortBy;
    private ComboBoxModel sortByModel;

    private JPanel makeSortPanel() {
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	panel.add(Box.createVerticalStrut(10));

	JPanel label = new JPanel(new BorderLayout()); // (FlowLayout.LEFT));
	label.add(new JLabel("Sort List"), BorderLayout.WEST);
	panel.add(label);

	JPanel grid = new JPanel(new GridLayout(0, 2, 3, 3));
	JLabel label2 = new JLabel("By Field:", JLabel.RIGHT);
	grid.add(label2);
	grid.add(sortBy = new JComboBox(descriptions));
	sortByModel = sortBy.getModel();
	grid.add(new JPanel()); // kosher?
	grid.add(new JCheckBox("In Reverse Order"));
	panel.add(grid);

	return panel;
    }

    private JPanel makeButtonPanel() {
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	JPanel subpanel = new JPanel(new GridLayout(1, 0, 6, 6));

	JButton cancel = new JButton("Cancel");
	cancel.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });
	subpanel.add(cancel);

	JButton ok = new JButton("OK");
	ok.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // WRITEME: save fields, sort, reverse

		    dispose();
		}
	    });
	subpanel.add(ok);

	panel.add(subpanel);

	return panel;
    }

    public ElementsViewOptions() {
	super("Metadata view");

	JPanel panel = new JPanel(new BorderLayout());
	JPanel subpanel = new JPanel();
	subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.Y_AXIS));
	panel.add(subpanel, BorderLayout.CENTER);
	panel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
	panel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);

	subpanel.add(makeFieldsPanel()); // "fields" section
	subpanel.add(makeSortPanel()); // "sort" section
	subpanel.add(makeButtonPanel()); // cancel/ok section

	// show
	getContentPane().add(panel);
	pack();
	setResizable(false);
	show();
    }

    public static void main(String args[]) {
	new ElementsViewOptions();
    }
}
