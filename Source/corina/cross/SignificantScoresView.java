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

package corina.cross;

import corina.Year;
import corina.Range;
import corina.cross.TopScores.HighScore;
import corina.ui.I18n;
import corina.index.DecimalRenderer;
import corina.graph.GraphWindow;
import corina.util.Sort;
import corina.util.NoEmptySelection;
import corina.prefs.Prefs;
import corina.prefs.PrefsListener;
import corina.prefs.PrefsEvent;

import java.util.Collections;

import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
    A view of the significant scores of a crossdate.  The view can be
    changed at any time to a different crossdate.

    <p>WRITEME: describe this view, exactly what columns it displays, a
    sample view (table), the fixed/moving nonsense, the sorting, the
    column sizing (well, maybe not that), the score formatting, what
    double-clicking does, what right-clicking does (nothing, yet), how
    to use it (only 3 public methods), what prefs it uses, ...</p>

    <h2>Left to do</h2>
    <ul>
        <li>this should be simply a view of TopScores.  as such, each
            top score should also contain a flag "isFuture".  this is
	    true if: one sample is fixed, one sample is moving, and
	    the moving sample's end date for this crossdate is greater
	    than the current year.  this class (TopScoresView) will
	    display isFuture samples in a lighter shade (50% between
	    foreground and background).

        <li>use Prefs, not properties

        <li>next/prev don't update the header ("t-score"->"trend"->etc.)
        <li>next/prev don't update the format used for the new cross
            (e.g., see "0.60" for trend instead of "60.0%")
        <li>next/prev don't even update the number of sig scores properly!
            -- but it does after you resize the column

        <li>get fixedFloats, movingFloats from CrossFrame;
            would an enum be helpful?

        <li>... and figure out if i can integrate this without screwing up
            the threading maze i've set up.  (i think so.  i just need to
            call it in an InvokeLater, because it's going to get hit from
            a non-event-handling thread.)

        <li>move sorting from CrossdateWindow into here

        <li>clean up variable names: "df"!
        <li>show the current sort in the header -- copy from Browser.java
            (use vars |sort|, |reverse|)
            earlier i said:
            - BETTER: abstract out column-header-modifier, given sort-field, sort-reverse
        <li>right-click on header should let you change both the sort,
            and the meaning of the fixed/moving ranges.  think up an
            elegant way to do that.
        <li>mark row if fixed,moving are where they're dated in the file
        <li>rename to TopScoresView
        <li>extract .highScores field to TopScores class
	    -- it'll be a member of the Crossdate class (getHighScores())
	    -- it'll be constructed at the end of run()
	    -- computeHighScores() will be gone, so Crossdate will be simpler
	    -- it'll have the data fields from HighScore, as well
	    -- good abstraction!
	    -- (what's its interface?)
        <li>let me change pref from menu: min overlap
        <li>(not me:) make "fixed", "moving" labels popups
        <li>only show ranges in "fixed"/"moving" headers if they're not
            being displayed below?
        <li>let me change pref from menu: format (0.00)
        <li>(save column widths to a hidden pref?)

        <li>IDEA: each view is printable.  (SequenceView prints
            whatever it's viewing right now.)  choosing "print" prints
            whatever the current view is.
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class SignificantScoresView extends JPanel implements PrefsListener {

    // a table model for displaying the statistically significant scores
    // in a Cross.
    private class CrossSigsTableModel extends AbstractTableModel {
        // formatter for the scores
        private DecimalFormat df;

        // create a Tablemodel to display the significant scores of the
	// enclosing class's crossdate.
        CrossSigsTableModel() {
            // pick up its format
            formatChanged();
        }

	// called when the user's pref for the crossdate format
	// (e.g., "0.00") was changed
        void formatChanged() {
            df = new DecimalFormat(crossdate.getFormat());
        }

	/*
	  columns names:
	  -- nr.
	  -- fixed (start - end)
	  -- moving (start - end)
	  -- algorithm name
	  -- overlap
	  -- confidence
	*/
        public String getColumnName(int col) {
          
            switch (col) {
	    case 0: return I18n.getText("number");
	    case 1: return I18n.getText("fixed") +
		    " (" + crossdate.getFixed().range + ")";
	    case 2: return I18n.getText("moving") +
		    " (" + crossdate.getMoving().range + ")";
	    case 3: 
      new Exception("GETTIGN COLUMN NAME").printStackTrace();
      System.out.println("Crossdate header: " + crossdate.getName());
      return crossdate.getName();
	    case 4: return I18n.getText("overlap");
	    case 5: return I18n.getText("confidence") + " (%)"; // hack!
		// FIXME: make this "confidence%", in case i need w/o-% also
	    default: throw new IllegalArgumentException(); // never happens
            }
        }

        // the number of rows.
        public int getRowCount() {
            return (crossdate==null ? 0 : crossdate.highScores.size());
	    // ??? - why can cross be null?
        }

        // the number of columns.
        public int getColumnCount() {
            return 6; // (#, fixed, moving, score, overlap, confidence)
        }

	// used so Integer columns get rendered right-aligned.
	// TODO: can i do this for other classes/columns?  if not, why not?
        public Class getColumnClass(int col) {
	    switch (col) {
	    case 0: return Integer.class;
	    case 1: case 2: return Range.class;
	    case 3: return String.class; // ...formatted by DecimalRenderer (sorting uses the model, not the view)
	    case 4: return Integer.class;
	    case 5: return String.class; // is this right?
	    default: throw new IllegalArgumentException(); // never happens
	    }
        }
        
        // the value at a (row, col) location.
        public Object getValueAt(int row, int col) {
            if (crossdate == null)
                return null; // (BUG?: when does this happen?)

            HighScore s = (HighScore) crossdate.highScores.get(row);

	    // PERF: since s.number and s.span are only used for
	    // (1) being returned as Objects and (2) being sorted by,
	    // wouldn't it make more sense for them to be declared in
	    // the HighScore class as Integers?

System.out.println("fixedFloats: " + fixedFloats);
System.out.println("movingFloats: " + movingFloats);
            switch (col) {
	    case 0: return new Integer(s.number);
	    case 1: return (fixedFloats ? s.fixedRange
			                : crossdate.getFixed().range);
	    case 2: return (movingFloats ? s.movingRange
			                 : crossdate.getMoving().range);
	    case 3: return df.format(s.score);
	    case 4: return new Integer(s.span); // PERF!
	    case 5: return (s.confidence > 0.90
			    ? formatNoPercent.format(s.confidence*100)
			    : "");
		// as %:
		// return (s.confidence > 0.90 ?
		//         Bayesian.format.format(s.confidence) : null);
	    default:
		throw new IllegalArgumentException(); // never happens
            }
        }
    }

    // i'm not sure what this is, but i need it.
    private final static DecimalFormat formatNoPercent =
	                               new DecimalFormat("#.0#");

    // select the highest score in the sigs table
    private void selectHighest() {
	if (crossdate.highScores.size() == 0)
	    return;

	int row = 0;
        double high = ((HighScore) crossdate.highScores.get(0)).score;

        for (int i=1; i<crossdate.highScores.size(); i++) {
            double test = ((HighScore) crossdate.highScores.get(i)).score;
            if (test > high) {
		row = i;
                high = test;
            }
        }

	table.setRowSelectionInterval(row, row);
    }

    // mouse listener (double-click to graph)
    private void makeDoubleClickable() {
        // double-click-able
	table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) // double-clicks only
		    graphSelectedCrossdate();
            }
        });
    }

    // mouse listener for header (click to sort)
    private void makeSortable() {
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            private void dataChanged() {
                model.fireTableDataChanged();
		repaint(); // ??
            }
            private int nr; // the value of the # column -- (it's an index on the data)
            private void saveSelection() {
                int row = table.getSelectedRow();
                HighScore s = (HighScore) crossdate.highScores.get(row);
                nr = s.number;
            }
            private void restoreSelection() {
                for (int i=0; i<crossdate.highScores.size(); i++) {
                    HighScore s = (HighScore) crossdate.highScores.get(i);
                    if (s.number == nr) {
                        table.setRowSelectionInterval(i, i);
                        return;
                    }
                }
            }
            public void mouseClicked(MouseEvent e) {
                int col = table.getColumnModel().getColumnIndexAtX(e.getX());

		// if the user clicked on a range column, well, what does that mean?
		// (you should click on the number column to sort by range).
		// so i'll just ignore it, for now.
		// TODO: this should probably sort by range, earliest-to-latest.
		// (but that doesn't make a whole lot of sense if they're all the same -- ???)
		if (col==1 || col==2)
		    return;

                // when sorting, let's count double-click as 2 clicks, because (1)
                // that's what the finder does, and (2) that's probably what users
                // would normally expect, anyway.

		String newSort=""; // compiler is paranoid
		switch (col) {
		case 0: newSort = "number"; break;
		case 3: newSort = "score"; break;
		case 4: newSort = "span"; break;
		case 5: newSort = "confidence"; break;
		}

		saveSelection();

		if (newSort.equals(sort)) {
		    // clicked on the same column --> just reverse everything
		    Collections.reverse(crossdate.highScores);

		} else {
		    // clicked on a new column --> sort by that field
		    sort = newSort;
		    boolean reverse=false;
		    if (col==3 || col==4 || col==5)
			reverse = true; // these 3 columns we want to sort decreasing by default
		    Sort.sort(crossdate.highScores, newSort, reverse);
		}

		dataChanged();
		restoreSelection();
            }
//	    private String oldSort=null; --- ???
        });
    }

    // DOCUMENT THIS!  apparently i need it.
    private String sort=null;

    private void initTable() {
        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        NoEmptySelection.noEmptySelection(table);
        table.getSelectionModel().setSelectionMode(
				 ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

	table.setModel(new CrossSigsTableModel());

	setupColumns();

	selectHighest();
        
        initPrefs();
      table.setAutoCreateColumnsFromModel(true);
    }

    private void setupColumns() {
	TableColumnModel columns = table.getColumnModel();

	// number column is too big
        columns.getColumn(0).setPreferredWidth(24);
        columns.getColumn(0).setMaxWidth(36);

        columns.getColumn(1).setCellRenderer(new RangeRenderer());
        columns.getColumn(2).setCellRenderer(new RangeRenderer());

	String format = crossdate.getFormat();
        columns.getColumn(3).setCellRenderer(new DecimalRenderer(format));

	// assume overlap is usually around 3 digits:
        columns.getColumn(4).setCellRenderer(new DecimalRenderer("000"));

	// ???
        columns.getColumn(5).setCellRenderer(new DecimalRenderer("00.00"));
    }

    // these methods taken directly from TableView.java -- EXTRACT to .util?
    // idea: if it saved widths as %ages, you could store them in prefs
    // from the same code.  (nah, it wouldn't be too hard to do that, anyway.)
    private int[] saveColumnWidths() {
	TableColumnModel columns = table.getColumnModel();
	int n = columns.getColumnCount();
	int columnWidths[] = new int[n];
	for (int i=0; i<n; i++)
	    columnWidths[i] = columns.getColumn(i).getWidth();
	return columnWidths;
    }
    private void restoreColumnWidths(int columnWidths[]) {
	TableColumnModel columns = table.getColumnModel();
	int n = columnWidths.length;
	for (int i=0; i<n; i++)
	    columns.getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    private void updateTable() {
	// data changed -- have to say "structure changed" because
	// one of the headers also changed.

	// for perceived stability, record the sizes of the columns,
	// and restore it after the columns are reset.
	int widths[] = saveColumnWidths();
	model.fireTableStructureChanged();

  System.err.println("header value: " + table.getColumnModel().getColumn(3).getHeaderValue());
	restoreColumnWidths(widths);

	System.out.println("model: table structure changed fired");

	// reset columns
	setupColumns();

	// select new highest
	selectHighest();

	revalidate(); // ??
	repaint(); // ??
    }

    /**
       Change the view to show a different crossdate.

       @param crossdate the new crossdate to view
    */
    public void setCrossdate(Cross crossdate) {
	this.crossdate = crossdate;

	updateTable();
    }

    /**
       Make a new view of the significant scores of a crossdate.

       @param crossdate the initial crossdate to view
    */
    public SignificantScoresView(Cross crossdate) {
	this.crossdate = crossdate;

	model = new CrossSigsTableModel();
	initTable(); // -- didn't these used to get made in a separate thread?

	makeDoubleClickable(); // MOVE: to initTable()?  refr, too?
	makeSortable();

	JScrollPane scroll = new JScrollPane(table);
	scroll.setVerticalScrollBarPolicy(
			 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scroll.setHorizontalScrollBarPolicy(
			   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	setLayout(new BorderLayout());
	add(scroll);
    }

    private Cross crossdate;

    private JTable table;

    private AbstractTableModel model;

    /**
       Make a new graph (in a new window) from the selected crossdate.
    */
    public void graphSelectedCrossdate() {
	// get the row
	int row = table.getSelectedRow();

	// get the year (== end-date of moving sample)
	HighScore score = (HighScore) crossdate.highScores.get(row);
	Year y = score.movingRange.getEnd();

	// new cross at this offset
	new GraphWindow(crossdate, y);
    }

    // WRITEME: these aren't hooked up yet
    private boolean fixedFloats=true, movingFloats=true;

    private void initPrefs() {
        refreshGridlines();
        
        refreshFormat();

        // font
        refreshFont();

        // colors
        refreshBackground();
        refreshForeground();
    }

    private void refreshGridlines() {
	// gridlines; WAS: ...cross...
        boolean gridlines = Boolean.valueOf(Prefs.getPref("corina.edit.gridlines")).booleanValue();
	table.setShowGrid(gridlines);
    }
    private void refreshFormat() {
	// format strings
        ((CrossSigsTableModel) model).formatChanged();
    }
    private void refreshFont() {
	// WAS: corina.cross.font (merged with corina.edit.font)
        if (Prefs.getPref("corina.edit.font") != null) {
            Font f = Font.decode(Prefs.getPref("corina.edit.font"));
	    table.setFont(f);
            table.setRowHeight(f.getSize() + 3);
            // FIXME: instead of 3, use (defaultRowHeight - defaultFontSize)
        }
    }
    private void refreshBackground() {
        if (System.getProperty("corina.edit.background") != null) // WAS: ...cross...
            table.setBackground(Color.getColor("corina.edit.background"));
    }
    private void refreshForeground() {
        if (System.getProperty("corina.edit.foreground") != null) // WAS: ...cross...
            table.setForeground(Color.getColor("corina.edit.foreground"));
    }

    public void prefChanged(PrefsEvent e) {
        String pref = e.getPref();
        
        if (pref.equals("corina.edit.gridlines"))
            refreshGridlines();
        else if (pref.equals("???"))
            refreshFormat();
        else if (pref.equals("corina.edit.font"))
            refreshFont();
        else if (pref.equals("corina.edit.background"))
            refreshForeground();
        else if (pref.equals("corina.edit.foreground"))
            refreshBackground();
    }
    
    public void addNotify() {
        super.addNotify();
        
      Prefs.addPrefsListener(this);
    }
    
    public void removeNotify() {
        super.removeNotify();
        
      Prefs.removePrefsListener(this);
    }
}
