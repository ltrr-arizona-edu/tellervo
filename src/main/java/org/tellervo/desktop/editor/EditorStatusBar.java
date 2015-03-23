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

package org.tellervo.desktop.editor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;
import org.tridas.io.util.SafeIntYear;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.DatingSuffix;
import org.tridas.schema.NormalTridasDatingType;


/**
   An emacs-style statusbar for an Editor's data table.

   <p>Displays the currently selected year, and that year's value,
   count, and weiserjahre up/down values.</p>

   <p>Watching for row-change-events requires implementing a
   ListSelectionListener, while watching for column-change-events
   requires implementing a TableColumnModelListener.  One statusbar
   class can implement them both, though it is incredibly
   non-intuitive.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
@SuppressWarnings("serial")
public class EditorStatusBar extends JPanel
                   implements ListSelectionListener, TableColumnModelListener {

    // label
    private JLabel widthLabel;
    private JLabel variableChooser;
    
    // statistics
    private Statistics stats;
    private UnitsChooser unitsChooser;
    private CalendarChooser calendarChooser;

    // optional: table
    private JTable table;

    // the sample to get data values from
    private Sample sample;
    
    // if i set it to "", it would have no height, either.  force it
    // to have normal line height by making this a non-empty string.
    private static final String EMPTY = " ";


    /** Update the statusbar, when a selection change occurs.  The
	statusbar will look something like this:

	<pre>
	1001: 52 [3] 2/1
	</pre>

	which means year 1001 is selected, and has an average value of
	52 for 3 samples; 2 are increasing in size, and 1 is
	decreasing. */
    private void update() 
    {
		// get the point
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
	
		// bail out if not on data
		if (col<1 || col>10) {
		    widthLabel.setText(EMPTY);
		    return;
		}
	
		// get year from (row,col)
		Year y = ((UnitAwareDecadalModel) table.getModel()).getYear(row, col);
	
		// get index from year
		int i = y.diff(sample.getRange().getStart());

		// bail out if out of range
		if (i<0 || i>=sample.getRingWidthData().size()) {
			widthLabel.setText(EMPTY);
		    return;
		}

		// get ring value 
		String strRingWidthValue = sample.getRingWidthData().get(i).toString();
		
		SafeIntYear yr = new SafeIntYear(y.tridasYearValue());
		
		NormalTridasDatingType datingtype = null;
		try{
			datingtype = sample.getSeries().getInterpretation().getDating().getType();
		} catch (Exception e)
		{
			
		}
		
		DatingSuffix ds = null;
		try{
			String strds = App.prefs.getPref(PrefKey.DISPLAY_DATING_SUFFIX, DatingSuffix.AD.name().toString());
			ds = TridasUtils.getDatingSuffixFromName(strds);
		} catch (Exception e)
		{
			ds = DatingSuffix.AD;
		}

		widthLabel.setText(I18n.getText("editor.year")+ ": " + yr.formattedYear(datingtype, ds) + " = " + strRingWidthValue+"\u03BCm");
	}

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

    /** Create a new status bar, for a given data table and sample.
	@param table data table to watch for selection changes
	@param sample sample to get data values 
     * @wbp.parser.constructor*/
    public EditorStatusBar(JTable table, Sample sample) 
    {
		// copy refs
		this.sample = sample;
		this.table = table;
	
		// add myself as a listener
		table.getSelectionModel().addListSelectionListener(this);
		table.getColumnModel().addColumnModelListener(this);
		// BUG: if data changes (how can this happen?), might need to change text.
		// so add myself as a samplelistener, too.
	
		// Width label
		widthLabel = new JLabel();
		//widthLabel.setBorder(BorderFactory.createEtchedBorder());
		
		// Stats, like mean sensitivity
		stats = new Statistics(sample);
		//stats.setBorder(BorderFactory.createEtchedBorder());
		
		// Units 
		unitsChooser = new UnitsChooser(sample);
		
		variableChooser = new VariableChooser(sample);
		
		calendarChooser = new CalendarChooser(sample);
		
		// initial text
		update();
	
		// pack stuff
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createHorizontalStrut(2));
		// add(icon);
		// add(Box.createHorizontalStrut(4));
		
		add(calendarChooser);
		add(new JLabel("  |  "));
		add(widthLabel);
		add(new JLabel("  |  "));
		
		add(Box.createHorizontalGlue());
		
		add(new JLabel("  |  "));
		add(variableChooser);
		add(new JLabel("  |  "));
		add(unitsChooser);
		add(new JLabel("  |  "));
		add(stats);
		add(Box.createHorizontalStrut(2));
    }

}
