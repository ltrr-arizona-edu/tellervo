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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.logging.Log;

import corina.Year;
import corina.core.App;
import corina.graph.GraphWindow;
import corina.logging.CorinaLog;
import corina.prefs.Prefs;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import corina.ui.I18n;

/**
    A view of all of the scores of a crossdate.  The view can be
    changed at any time to a different crossdate.

    <h2>Left to do</h2>
    <ul>
        <li>a sample view, for the javadoc
        <li>clean up the prefs handling stuff
        <li>bug: if corina.cross.overlap(??) pref changes, need to
	    re-run cross thread?  NO: it should only change the display,
	    not the length of cross.data
        <li>use Prefs.getPref() (or similar) instead of properties:
            Font.getFont, System.getProperty(), etc.
        <li>it's possible to deselect everything, and then "graph" seems
	    silly; fix.
        <li>should be able to right-click any score, but what to offer?
            "graph this crossdate", (dimmed) "99.5% confidence", others?
        <li>all scores view: cursor on table should go 1009->1010.
	    (extract that.)
        <li>general stuff for all views: printing, copying?
        <li>don't allow resizing columns (maybe year column)
        <li>(graphwindow: add undo support for dragging graphs)
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class AllScoresView extends JPanel implements PrefsListener {
  private static final Log log = new CorinaLog(AllScoresView.class);

    // the crossdate
    private Cross crossdate;

    // a table model for displaying all the scores in a Cross
    private class ScoresTableModel extends AbstractTableModel {
	// range of rows
	private int row_min, row_max;

	// create a TableModel to display the given Cross
	ScoresTableModel() {
	    // compute range of rows
	    row_min = crossdate.getRange().getStart().row();
	    row_max = crossdate.getRange().getEnd().row();
	}

	// The column name: "Year" for the first column, and a digit
	// (0-9) for the others.
	public String getColumnName(int col) {
	    if (col == 0)
		return I18n.getText("year");
	    else
		return Integer.toString(col-1);
	}

	// the number of rows.
	public int getRowCount() {
	    return (row_max - row_min + 1);
	}

	// the number of columns.
	public int getColumnCount() {
	    return 11;
	}

        // compute the year of a particular (row, col) cell.
        protected Year getYear(int row, int col) {
            return new Year(10 * (row + row_min) + col - 1);
	    // REFACTOR: this looks really friggin' familiar...
	    // PERF: new!
        }

        // the value at a (row, col) location: the crossdate score,
	// except the lefthand column, where it's the decade label.
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                if (row == 0)
                    return crossdate.getRange().getStart();
                else if (row + row_min == 0)
                    return new Year(1); // special case
                else
                    return getYear(row, col+1);
            } else {
                if ((row + row_min == 0) && (col == 1))
                    return null; // zero-year
                Year year = getYear(row, col);
                
                // shift one to start of moving slide 
                if (!crossdate.getRange().contains(year))
                    return null;
                else {
		    float score = crossdate.getScore(year);
        log.debug("Year: " + year + " score: " + score);
                    return new Float(score);
		}
		// BUG: fails here if c.data.length=0 or some such crap
		// PERF: new Float() each time -- is that smart?
            }
        }
    }

    // for performance reasons, extend DefaultTableCellRenderer,
    // not JLabel+TableCellRenderer. (see DefaultTableCellRenderer
    // javadoc for why using a stock JLabel probably isn't a great
    // idea.)
    private class ScoreRenderer extends DefaultTableCellRenderer {
        private DecimalFormat df;
        private boolean fontSetYet=false;
        private Color fore, back, lite;
        ScoreRenderer() {
            super();
            setHorizontalTextPosition(SwingConstants.RIGHT); // FIXME: isn't this ignored?
	    // ...BETTER: align around decimal point
            setOpaque(true); // is this still needed?
            df = new DecimalFormat(crossdate.getFormat());
            fore = App.prefs.getColorPref(Prefs.EDIT_FOREGROUND, Color.black); // WAS: ...cross...
            back = App.prefs.getColorPref(Prefs.EDIT_BACKGROUND, Color.white); // WAS: ...cross...
            lite = App.prefs.getColorPref(Prefs.GRID_HIGHLIGHTCOLOR, Color.green);
        }
        public Component getTableCellRendererComponent(JTable table,
						       Object value,
                                                       boolean isSelected,
						       boolean hasFocus,
                                                       int row, int column) {
            // null? bail [why would that happen?]
            if (value == null) {
                super.setBackground(back);
                setText("");
                return this;
            }

            // font -- needed here?  (well, at least minimize the number of calls)
            if (!fontSetYet) {
                super.setFont(table.getFont());
                // super.setForeground(Color.getColor("corina.cross.foreground")); // do fore, too
                super.setForeground(App.prefs.getColorPref(Prefs.EDIT_FOREGROUND, Color.black)); // do fore, too
                // BUG: what if corina.edit.foreground == null?
                fontSetYet = true;
            }

            // score
            float score = ((Number) value).floatValue();

            // format the number
            setText(df.format(score));

            // hilite sig scores
            if (isSelected)
                super.setBackground(table.getSelectionBackground());
            else
		// BUG: getMinimumSignificant() is deprecated!
                super.setBackground(score > crossdate.getMinimumSignificant()
				    ? lite
				    : back);

            return this;
        }
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

	    // FIXME: why, even with setOpaque(true), do i still need
	    // to fill my own background?
            g2.setColor(this.getBackground());
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());

            super.paintComponent(g);
        }
    }

    private JTable table;

    /**
       Create a new view of all of a crossdate's scores.

       @param the crossdate to view initially
    */
    public AllScoresView(Cross crossdate) {
	this.crossdate = crossdate;

	initTable();

	JScrollPane scroll = new JScrollPane(table);
	scroll.setVerticalScrollBarPolicy(
			 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scroll.setHorizontalScrollBarPolicy(
			   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	setLayout(new BorderLayout());
	add(scroll);
    }

    /**
       Change the view to a new crossdate.

       @param crossdate the new crossdate to view
    */
    public void setCrossdate(Cross crossdate) {
	this.crossdate = crossdate;

	updateTable();
    }

    private void initTable() {
        // scores
        table = new JTable(new ScoresTableModel());
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);

	// BUG: this isn't accurate!  it uses getMinimumSignificant()
	// instead of isSignificant(), so it ignores the overlap length.
	// also: why can't i use setDefaultRenderer(Double.class, ...) here?
        ScoreRenderer sr = new ScoreRenderer();
        for (int i=1; i<=10; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(sr);

	makeDoubleClickable();

	// set preference-able stuff
        initPrefs();
    }

    private void makeDoubleClickable() {
        // double-click-able
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) // double-clicks only
		    graphSelectedCrossdate();
            }
        });
    }

    /**
       Make a new graph (in a new window) from the selected crossdate.
    */
    public void graphSelectedCrossdate() {
	// get the (row,col) of the click
	int row = table.getSelectedRow();
	int col = table.getSelectedColumn();

	// get the year (== end-date of moving sample)
	Year y = ((ScoresTableModel) table.getModel()).getYear(row, col);

	// blank spot (TODO: better to just look for empty cell?)
	if (!crossdate.getRange().contains(y))
	    return;
	if (col == 0)
	    return;
	if ((row + crossdate.getRange().getStart().row() == 0) && (col == 1))
	    return;

	// new graph at this place
	new GraphWindow(crossdate, y);
    }

    private void updateTable() {
	// WRITEME

        table.setModel(new ScoresTableModel());

	// BUG: this isn't accurate!  it uses getMinimumSignificant()
	// instead of isSignificant(), so it ignores the overlap length.
	// also: why can't i use setDefaultRenderer(Double.class, ...) here?
        ScoreRenderer sr = new ScoreRenderer();
        for (int i=1; i<=10; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(sr);
	// PERF: why make new score renderers?  why not have one instance
	// of that per instance of me?
    }

    private void initPrefs() {
        refreshGridlines();

        refreshFormat();
        refreshFont();

        refreshBackground();
        refreshForeground();
    }

    private void refreshGridlines() {
	// gridlines
        boolean gridlines = Boolean.valueOf(App.prefs.getPref("corina.edit.gridlines")).booleanValue();
        // WAS: corina.cross.gridlines
	table.setShowGrid(gridlines);
    }
    private void refreshFormat() {
        // format strings
        ScoreRenderer sr = new ScoreRenderer();
        for (int i=1; i<=10; i++) // format strings updated
            table.getColumnModel().getColumn(i).setCellRenderer(sr);
    }
    private void refreshFont() {
        // font
        // WAS: corina.cross.font (merged)
         Font f = App.prefs.getFontPref(Prefs.EDIT_FONT, null);
         if (f != null) {
           table.setFont(f);
           table.setRowHeight(f.getSize() + 3);
         }
    }
    private void refreshBackground() {
      table.setBackground(App.prefs.getColorPref(Prefs.EDIT_BACKGROUND, Color.white));
    }
    private void refreshForeground() {
      table.setForeground(App.prefs.getColorPref(Prefs.EDIT_FOREGROUND, Color.black));
      // WAS: corina.cross.background, corina.cross.foreground
    }

    public void prefChanged(PrefsEvent e) {
        String pref = e.getPref();

        if (pref.equals(Prefs.EDIT_GRIDLINES)) {
            refreshGridlines();
        } else if (pref.startsWith("corina.cross.") && pref.endsWith(".format")) {
            refreshFormat();
        } else if (pref.equals(Prefs.EDIT_FONT)) {
            refreshFont();
        } else if (pref.equals(Prefs.EDIT_BACKGROUND)) {
            refreshBackground();
            refreshFormat();
        } else if (pref.equals(Prefs.EDIT_FOREGROUND)) {
            refreshForeground();
            refreshFormat();
        } else if (pref.equals(Prefs.GRID_HIGHLIGHTCOLOR)) {
            refreshFormat();
        }
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
