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

import corina.Year;
import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.Weiserjahre;
import corina.manip.MeanSensitivity;
import corina.gui.Tree;
import corina.gui.ElementsPanel;

import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;

/**
   <p>An emacs-style modeline for an Editor's data table.  Displays
   the currently selected year, and that year's value, count, and
   weiserjahre up/down values.</p>

   <p>Watching for row-change-events requires implementing a
   ListSelectionListener, while watching for column-change-events
   requires implementing a TableColumnModelListener.  One Modeline
   class can implement them both, though it is incredibly
   non-intuitive.</p>

   <p>To add: a modeline for the "Elements" tab: "126 elements, 120
   used, 6 selected".</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Modeline extends JPanel
                   implements ListSelectionListener,
			      TableColumnModelListener,
			      SampleListener {
    // icon
    private Tree icon;

    // label
    private JLabel label;

    // mean sensitivity
    private JLabel ms;

    // optional: table
    private JTable table;

    // the sample to get data values from
    private Sample sample;

    /** Update the modeline, when a selection change occurs.  The
	modeline will look something like this:

	<pre>
	1001: 52 [3] 2/1
	</pre>

	which means year 1001 is selected, and has an average value of
	52 for 3 samples; 2 are increasing in size, and 1 is
	decreasing. */
    private void update() {
	// get the point
	int row = table.getSelectedRow();
	int col = table.getSelectedColumn();

	// bail out if not on data
	if (col<1 || col>10) {
	    label.setText(" ");
	    return;
	}

	// get year from (row,col)
	Year y = ((DecadalModel) table.getModel()).getYear(row, col);

	// get index from year
	int i = y.diff(sample.range.getStart());

	// bail out if out of range
	if (i<0 || i>=sample.data.size()) {
	    label.setText(" ");
	    return;
	}

	// get modeline from year, index
	String modeline = y + ": " + sample.data.get(i);
	if (sample.count != null)
	    modeline += " [" + sample.count.get(i) + "]";
	if (sample.hasWeiserjahre())
	    modeline += " " + Weiserjahre.toString(sample, i);

	// set modeline
	label.setText(modeline);
    }

    // data changed
    public void sampleDataChanged(SampleEvent e) {
        // update mean sensitivity
        float m = MeanSensitivity.ms(sample);
        DecimalFormat f = new DecimalFormat("ms = 0.000");
        String str = (Float.isNaN(m) ? "ms undefined" : f.format(m));
        ms.setText(str);
    }
    public void sampleRedated(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) { }
    public void sampleFormatChanged(SampleEvent e) { }
    public void sampleElementsChanged(SampleEvent e) { }

    // row-change
    public void valueChanged(ListSelectionEvent e) {
	// wait until it stops
	if (e.getValueIsAdjusting())
	    return;

	update();
    }

    public void columnAdded(TableColumnModelEvent e) { }
    public void columnMarginChanged(ChangeEvent e) { }
    public void columnMoved(TableColumnModelEvent e) { }
    public void columnRemoved(TableColumnModelEvent e) { }

    // column-change
    public void columnSelectionChanged(ListSelectionEvent e) {
	// wait until it stops
	if (e.getValueIsAdjusting())
	    return;

	update();
    }

    /** Create a new Modeline, for a given data table and sample.
	@param table data table to watch for selection changes
	@param sample sample to get data values */
    public Modeline(JTable table, Sample sample) {
	// copy refs
	this.sample = sample;
	this.table = table;

	// add myself as a listener
	table.getSelectionModel().addListSelectionListener(this);
	table.getColumnModel().addColumnModelListener(this);
	sample.addSampleListener(this);

	// label, border
	label = new JLabel();
	label.setBorder(BorderFactory.createEmptyBorder());

	// icon
	icon = new Tree(sample);

	// ms
	ms = new JLabel();
	ms.setToolTipText("Mean sensitivity of this dataset");

	// initial text
	update();
	sampleDataChanged(null); // ok to pass a null event source?

	// pack stuff
	setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	add(Box.createHorizontalStrut(2));
	add(icon);
	add(Box.createHorizontalStrut(4));
	add(label);
	add(Box.createHorizontalGlue());
	add(ms);
	add(Box.createHorizontalStrut(2));
    }

    // here's a modeline for elements panels
    public Modeline(ElementsPanel ep) {
	// what it'll do: "%d samples, %d KB", or "%d of %d samples selected, %d of %d KB"
	// -> it'll need a list selection listener
    }
}
