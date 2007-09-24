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

package edu.cornell.dendro.corina.cross;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.CountRenderer;
import edu.cornell.dendro.corina.prefs.PrefsEvent;
import edu.cornell.dendro.corina.prefs.PrefsListener;
import edu.cornell.dendro.corina.ui.I18n;

/**
    A view of a histogram of scores of a crossdate.  The view can be
    changed at any time to a different crossdate.

    <h2>Left to do</h2>
    <ul>
        <li>implement prefs listener: just fix "???" in format pref
    
        <li>clean up swing.* import
        <li>right-click on a row should show a popup menu, showing which years
            each of those crossdates occurs in; selecting it should graph it?
        <li>first column should be centered around "-" char
        <li>second column should be a DecimalRenderer?
        <li>refactor?
        <li>get rid of private javadocs that i don't need ("** the column name...")
        <li>store column widths during next/prev, like sigs view does
            -- extract saveColumnWidths()/restoreColumnWidths() (to util?)
        <li>the column-sizing code doesn't do anything (but then, it never did)
        <li>can i use decimal-renderer around the '-' char to center the
            range-column?
        <li>allow copying/exporting?
        <li>i don't much like this view ... i think the scores should go across
            the bottom, and stack up.
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class HistogramView extends JPanel implements PrefsListener {

    private Histogram histo; // RENAME ME!

    private class HistogramTableModel extends AbstractTableModel {
        void formatChanged() {
            // histo.formatChanged(); -- need to reset |memo|, |fmt|
            // WRITEME
        }

        /**
            The column name.  The columns are:
            <ul>
                <li>Score range (the column header is the name, like "T-Score")
                <li># (number of scores in that range)
                <li>Histogram (as a bar)
            </ul>
            
            @param col the column to query
            @return the column's name
        */
        public String getColumnName(int col) {
            switch (col) {
                case 0: return crossdate.getName();
                case 1: return I18n.getText("quantity");
                case 2: return I18n.getText("histogram");
                default: throw new IllegalArgumentException(); // can't happen
            }
        }

        // number of rows
	public int getRowCount() {
	    if (histo == null) return 0; // WHY would this happen?
            return histo.getNumberOfBuckets();
        }

	// number of columns
        public int getColumnCount() {
            return 3; // (range, number, bar)
        }

	// column class; so ints are right-aligned
        public Class getColumnClass(int col) {
            switch (col) {
                case 0: return String.class;
                case 1: return Integer.class;
                case 2: return Integer.class;
                default: throw new IllegalArgumentException(); // can't happen
            }
        }

        // the value at a (row, col) location.
        public Object getValueAt(int row, int col) {
            // ICK: calling format all the time can't be great for preformance,
	    // especially with all the garbage from strings.
            // there won't be many of these, so why not just make an array?
	    // or compute in-place?
            int n = histo.getBucketItems(row);
            switch (col) {
                case 0: return histo.getBucketRange(row);

                case 1:
		    if (n == 0)
			return "";
		    else
			return new Integer(n);

                    // -- there's a lot of new Integer creation going on
		    // around here, but it doesn't seem to be hurting
		    // performance.  if it becomes a problem,
                    // it's probably reasonable to start memoizing.

                case 2: return new Integer(n);

                default: throw new IllegalArgumentException(); // can't happen
            }
        }
    }

    private Cross crossdate;

    /**
       Make a new histogram view for a crossdate.

       @param crossdate the initial crossdate to view
    */
    public HistogramView(Cross crossdate) {
	this.crossdate = crossdate;

	initTable();

	setLayout(new BorderLayout());
	JScrollPane scroll = new JScrollPane(table);
	scroll.setVerticalScrollBarPolicy(
			 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	add(scroll);
    }

    private JTable table;

    private void initTable() {
        table = new JTable(new HistogramTableModel());
        table.getSelectionModel().setSelectionMode(
				     ListSelectionModel.SINGLE_SELECTION);
	table.getTableHeader().setReorderingAllowed(false);

	updateTable();
        
        initPrefs();
    }

    private void updateTable() {
        // histogram
        histo = new Histogram(crossdate);
        table.setModel(new HistogramTableModel());

        int width = table.getWidth();
	TableColumnModel columns = table.getColumnModel();
	columns.getColumn(0).setCellRenderer(centerRenderer);
	columns.getColumn(0).setPreferredWidth(width/4);
	columns.getColumn(1).setCellRenderer(centerRenderer);
	columns.getColumn(1).setPreferredWidth(width/4);
	int max = histo.getFullestBucket();
	columns.getColumn(2).setCellRenderer(new CountRenderer(max));
    }

    /**
       Change the view to a different crossdate.

       @param crossdate the new crossdate to view
    */
    public void setCrossdate(Cross crossdate) {
	this.crossdate = crossdate;

	updateTable();
    }

    private void initPrefs() {
        refreshFormat();
        refreshFont();
    }
    
    private void refreshFormat() {
	// format
        ((HistogramTableModel) table.getModel()).formatChanged();
    }

    private void refreshFont() {
	// font
        // WAS: corina.cross.font (merged)
        if (App.prefs.getPref("corina.edit.font") != null) {
            Font f = Font.decode(App.prefs.getPref("corina.edit.font"));
            table.setFont(f);
            table.setRowHeight(f.getSize() + 3);
        }
    }

    // for centered columns of stuff in a table.
    // no, THIS IS STUPID.
    // just call ((defaultrenderer) getrenderer()).sethorizalign('center);
    private static DefaultTableCellRenderer centerRenderer;
    static {
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }

    public void prefChanged(PrefsEvent e) {
        String pref = e.getPref();
        
        if (pref.equals("corina.edit.font"))
            refreshFont();
        else if (pref.equals("???"))
            refreshFormat();
    }
    
    public void addNotify() {
        super.addNotify();
        
        App.prefs.addPrefsListener(this);
    }
    
    public void removeNotify() {
        super.removeNotify();
        
        App.prefs.removePrefsListener(this);
    }
}
