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

package corina.cross;

import corina.Year;
import corina.Range;
import corina.Element;
import corina.graph.GraphFrame;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteNotFoundException;
import corina.map.MapFrame;
import corina.gui.XFrame;
import corina.gui.XMenubar;
import corina.gui.PrintableDocument;
import corina.gui.HasPreferences;
import corina.gui.Tree;
import corina.gui.ButtonLayout;
import corina.gui.DialogLayout;
import corina.gui.XButton;
import corina.editor.CountRenderer;
import corina.util.Platform;
import corina.util.Sort;
import corina.util.OKCancel;
import corina.util.NoEmptySelection;
import corina.util.JLinedLabel;
import corina.print.Printer;
import corina.prefs.Prefs;
import corina.index.IndexDialog.DecimalRenderer; // extract class!

import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import java.text.DecimalFormat;

import java.awt.*; // !!!
import java.awt.event.*; // !!!
import java.awt.print.Printable;
import java.awt.print.Pageable;
import java.awt.print.PageFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*; // !!!
import javax.swing.event.*; // !!!
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

/**
   A crossdate.  Displays all scores and significant scores, and lets
   the user step forward/backward through the Sequence, graphing any
   desired cross.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class CrossFrame extends XFrame implements PrintableDocument, HasPreferences {
    // gui
    private JTable scoresTable=null, sigsTable, histoTable;
    private JTabbedPane tabPane;
    private JButton prevButton, nextButton, graphButton, mapButton, closeButton;

    // l10n
    private ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // data
    private Sequence seq;
    private Cross cross=null;
    private Histogram histo;

    // titles on top
    private JLabel fixedTitle;
    private JLabel movingTitle;
    private Tree fixedTree;
    private Tree movingTree;

    /** A table model for displaying all the scores in a Cross. */
    public class CrossTableModel extends AbstractTableModel {
	// cross to display
	private Cross c;

	// range of rows
	private int row_min, row_max;

	/** Create a TableModel to display the given Cross.
	    @param c the Cross to be displayed */
	public CrossTableModel(Cross c) {
	    // copy data
	    this.c = c;

	    // compute range of rows
	    row_min = c.range.getStart().row();
	    row_max = c.range.getEnd().row();
	}

	/** The column name: "Year" for the first column, and a digit (0-9) for the others.
	    @param col column number to query
	    @return the column name */
	public String getColumnName(int col) {
	    if (col == 0)
		return msg.getString("year");
	    else
		return Integer.toString(col-1);
	}

	/** The number of rows.
	    @return the number of rows */
	public int getRowCount() {
	    return (row_max - row_min + 1);
	}

	/** The number of columns.
	    @return the number of columns */
	public int getColumnCount() {
	    return 11;
	}

        /** Compute the year of a particular (row, col) cell.
            @param row the cell's row
            @param col the cell's column
            @return the Year of that cell */
        protected Year getYear(int row, int col) {
            return new Year(10 * (row + row_min) + col - 1);
	    // REFACTOR: this looks really friggin' familiar...
        }

        /** The value at a (row, col) location.  The crossdate score,
            except the lefthand column, which is the decade label.
            @param row row number to query
            @param col column number to query
            @return the score at that year, or the decade label */
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                if (row == 0)
                    return c.range.getStart();
                else if (row + row_min == 0)
                    return new Year(1); // special case
                else
                    return getYear(row, col+1);
            } else {
                if ((row + row_min == 0) && (col == 1))
                    return null; // year-zero
                Year y = getYear(row, col);
                if (!c.range.contains(y))
                    return null;
                else
                    return new Double(c.data[y.diff(c.range.getStart())]); // BUG: fails here if c.data.length=0 or some such crap
            }
        }
    }

    // REFACTORING STRATEGY:
    // -- abstract out sigscorestable (extends jtable), allscorestable (ditto), histogramtable(ditto)
    // -- they get recreated each time anyway, so don't worry about that
    // -- back/next recreate tables
    // -- if no overlap, put up label instead of tables (or entire tabset?)
    // -- "graph" does getselectedtab -> getselectedcrossdate (which doesn't exist for histogram ... huh.)

    /**
       A table model for displaying the statistically significant scores
       in a Cross.
    */
    private class CrossSigsTableModel extends AbstractTableModel {
        // the cross to display
        private Cross c;

        // formatter for the scores
        private DecimalFormat df;

        /** Create a Tablemodel to display the significant scores of a
            given Cross.
            @param c the Cross to be displayed */
        public CrossSigsTableModel(Cross c) {
            // copy data
            this.c = c;

            // pick up its format
            formatChanged();
        }

        public void formatChanged() {
            df = new DecimalFormat(c.getFormat());
        }

        /** The column name.  The columns are:
            <ul>
            <li>Nr.
            <li>Fixed (f<sub>start</sub> - f<sub>end</sub>)
            <li>Moving (m<sub>start</sub> - m<sub>end</sub>)
            <li>Algorithm
            <li>Overlap
            <li>Confidence
            </ul>
            @param col the column to query
            @return the column's name */
        public String getColumnName(int col) {
            switch (col) {
	    case 0: return msg.getString("number");
	    case 1: return msg.getString("fixed") + " (" + c.fixed.range + ")";
	    case 2: return msg.getString("moving") + " (" + c.moving.range + ")";
	    case 3: return c.getName();
	    case 4: return msg.getString("overlap");
	    case 5: return msg.getString("confidence") + " (%)"; // hack!
	    default: throw new IllegalArgumentException(); // never happens
            }
        }

        /** The number of rows.
            @return the number of rows */
        public int getRowCount() {
            return (c==null ? 0 : c.highScores.size());
        }

        /** The number of columns.
            @return the number of columns */
        public int getColumnCount() {
            return 6; // nr, fixed, moving, score, overlap, confidence
        }

        /** The class of a given column.  This is used so the default
            table renderer right-aligns Integers.
            @param col the column to query
            @return Class of this column's data */
        public Class getColumnClass(int col) {
	    switch (col) {
	    case 0: return Integer.class;
	    case 1: /* fall-through */
	    case 2: return Range.class;
	    case 3: return String.class; // ...formatted by DecimalRenderer (sorting uses the M, not the V)
	    case 4: return Integer.class;
	    case 5: return String.class; // do i want to align this somehow?  is this bad?
	    default: throw new IllegalArgumentException(); // never happens
	    }
        }
        
        /** The value at a (row, col) location.
            @param row row number to query
            @param col column number to query
            @return the cell's value */
        public Object getValueAt(int row, int col) {
            if (c == null)
                return null; // (when does this happen?)

            Score s = (Score) c.highScores.get(row);

            switch (col) {
                case 0:
                    return new Integer(s.number);
                case 1:
                    return (fixedFloats ? s.fixedRange : c.fixed.range);
                case 2:
                    return (movingFloats ? s.movingRange : c.moving.range);
                case 3:
                    return df.format(s.score);
                case 4:
                    return new Integer(s.span);
	    case 5:
		// return (s.confidence > 0.90 ? Bayesian.format.format(s.confidence) : null);
                return (s.confidence > 0.90 ? formatNoPercent.format(s.confidence*100) : ""); // i.e., not the string "null"
                default:
                    throw new IllegalArgumentException(); // never happens
            }
        }
    }

    // hack!
    private static DecimalFormat formatNoPercent = new DecimalFormat("#.0#");

    private static class RangeRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
						       boolean isSelected, boolean hasFocus,
						       int row, int column) {
	    // WRITEME

	    // ugh!
	    setText(value.toString()); // range->string

	    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // ???
	}
	public void paintComponent(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;

	    Object value = new Range(getText()); // string->range

	    // compute baseline (from DecimalRenderer)
            int baseline = getSize().height - g2.getFontMetrics().getDescent(); // (is this right?)

	    // get value -- as (start, end)
	    Year start = ((Range) value).getStart();
	    Year end = ((Range) value).getEnd();

	    // fill background -- (is this needed/allowed/automatic?)
	    g2.setColor(getBackground());
	    g2.fillRect(0, 0, getSize().width, getSize().height); // on isOpaque() only?

	    // set foreground
	    g2.setColor(getForeground());

	    // center
	    int center = getSize().width / 2;
	    int dashWidth = g2.getFontMetrics().stringWidth(" - "); // inefficient -- same every time
	    g2.drawString(" - ", center - dashWidth/2, baseline); // (hey, at least the string is interned.)

	    // start
	    int startWidth = g2.getFontMetrics().stringWidth(start.toString());
	    g2.drawString(start.toString(), center - dashWidth/2 - startWidth, baseline);

	    // end
	    int endWidth = g2.getFontMetrics().stringWidth(end.toString());
	    int right = getSize().width - 10; // HACK!  (type-(2) hack, even!)
	    g2.drawString(end.toString(), right - endWidth, baseline);

	    // WRITEME
	}
    }

    public class HistogramTableModel extends AbstractTableModel {
        public void formatChanged() {
            // histo.formatChanged(); -- need to reset |memo|, |fmt|
            // WRITEME
        }

        /** The column name.  The columns are:
        <ul>
        <li>Score range (the column header is the name, like "T-Score")
        <li># (number of scores in that range)
        <li>Histogram (as a bar)
        </ul>
        @param col the column to query
        @return the column's name */
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return cross.getName();
                case 1:
                    return msg.getString("quantity");
                case 2:
                    return msg.getString("histogram");
                default:
                    throw new IllegalArgumentException(); // never happens
            }
        }

        /** The number of rows.
        @return the number of rows */
        public int getRowCount() {
            return histo.countBuckets();
        }

        /** The number of columns.
        @return the number of columns */
        public int getColumnCount() {
            return 3;
        }

        /** The class of a given column.  This is used so the default
        table renderer right-aligns Integers.
        @param col the column to query
        @return Class of this column's data */
        public Class getColumnClass(int col) {
            switch (col) {
                case 0: return String.class;
                case 1: return Integer.class;
                case 2: return Integer.class;
                default: throw new IllegalArgumentException(); // never happens
            }
        }

        /** The value at a (row, col) location.
            @param row row number to query
            @param col column number to query
            @return the cell's value */
        public Object getValueAt(int row, int col) {
            // ICK: calling format all the time can't be great for preformance, especially with all the garbage from strings.
            // there won't be many of these, so why not just make an array?  or compute in-place?
            int n = histo.getNumber(row);
            switch (col) {
                case 0:
                    return histo.getRange(row);

                case 1:
                    /*when*/ if (n == 0)
                        return "";
                    return new Integer(n);

                    // -- there's a lot of new Integer creation going on around here, but it
                    // doesn't seem to be hurting performance.  if it becomes a problem,
                    // it's probably reasonable to start memoizing.

                case 2:
                    return new Integer(n);

                default:
                    throw new IllegalArgumentException(); // nevers happens
                                 // possible solution: return thisrow[col]; -- no default needed!

                    /*
                     then [with decimalrenderer?] this becomes:
                     -- ouch, still can't render ranges that way.  oh well.
                     case 0: return ...;
                     case 1: return (n==0 ? "" : b[r].n);
                     case 2: return (n==0 ? "" : b[r].pct); // DecRend
                     case 3: return b[r].n; // CountRend
                     */
            }
        }
    }

    // convert fixedFloats/movingFloats to a string, for storing in the prefs
    private String encode() {
        if (fixedFloats && !movingFloats)
            return "fixed";
        else if (!fixedFloats && movingFloats)
            return "moving";
        else
            return "both";
    }
    // set fixedFloats/movingFloats based on a string, which was stored in the prefs
    private void decode(String pref) {
        if (pref.equals("fixed")) {
            fixedFloats = true;
            movingFloats = false;
        } else if (pref.equals("both")) {
            fixedFloats = true;
            movingFloats = true;
        } else { // "moving"
            fixedFloats = false;
            movingFloats = true;
        }
    }

    private JPanel defaultView;

    // gui setup
    private void initGui() {
        // cards!
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards);

        // default view
        defaultView = new JPanel(new BorderLayout());
        cards.add(defaultView, "default");
        
        // bottom panel ------------------------------------------------------------
        JPanel buttons = new JPanel(new ButtonLayout());
        defaultView.add(buttons, BorderLayout.SOUTH);
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // HACK!

        // classloader, for icons
        ClassLoader cl = this.getClass().getClassLoader();

        // prev
        prevButton = new XButton("prev");
        if (!Platform.isMac)
            prevButton.setIcon(new ImageIcon(cl.getResource("toolbarButtonGraphics/navigation/Back16.gif")));

        // next
        nextButton = new XButton("next");
        if (!Platform.isMac)
            nextButton.setIcon(new ImageIcon(cl.getResource("toolbarButtonGraphics/navigation/Forward16.gif")));

        // actionlistener for prev/next
        Action prevNext = new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                // disable buttons during processing
                disableButtons();

                try {
                    if (ae.getSource() == prevButton) {
                        seq.prevPairing();
                    } else { // nextButton
                        seq.nextPairing();
                    }
                    cross = seq.getCross();
                } catch (IOException ioe) {
                    System.out.println("ioe! -- " + ioe);
                    // DEAL WITH ME INTELLIGENTLY!
                }

                // run it in its own thread, and then do the gui stuff
		new RunThread().start();
            }
        };
        prevButton.addActionListener(prevNext);
        nextButton.addActionListener(prevNext);

        // graph
        graphButton = new XButton("plot");
        graphButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                switch (tabPane.getSelectedIndex()) {

                    case 0: // "significant scores" selected
                    {
                        // which cross to use -- guaranteed not to be -1, by NoEmptySelection
                        int nr = sigsTable.getSelectedRow();

                        // get year (== moving sample end-year) for this row
                        Year y = ((Score) cross.highScores.get(nr)).movingRange.getEnd();

                        // new cross at this offset
                        new GraphFrame(cross, y);

                        break;
                    }
                    case 1: // "all scores" selected
                            // BUG: this shouldn't exist, either (but make sure the selected-cell is visible here)
                    {
                        int r = scoresTable.getSelectedRow();
                        int c = scoresTable.getSelectedColumn();
                        if (r==-1 || c==-1) {
                            // should never happen: instead, this button should be dimmed if this is the case
                            JOptionPane.showMessageDialog(null,
                                                          msg.getString("selecterror"),
                                                          msg.getString("cross_error"),
                                                          JOptionPane.ERROR_MESSAGE);
                        } else {
                            Year y = ((CrossTableModel) scoresTable.getModel()).getYear(r, c);
                            new GraphFrame(cross, y);
                        }
                        break;
                    }

                    case 2: // "histogram" selected -- can't happen, by changelistener on tabpane disabling graphbutton
                        throw new IllegalArgumentException();
                }
            }
        });

        // map (!)
        mapButton = new XButton("map");
        mapButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    // figure out the locations
                    Site s1 = SiteDB.getSiteDB().getSite(cross.fixed);
                    Site s2 = SiteDB.getSiteDB().getSite(cross.moving);

                    // draw the map
                    new MapFrame(s1, s2);
                } catch (SiteNotFoundException e) {
                    throw new IllegalArgumentException();
                    // now that mapbutton gets disabled, this can no longer happen
                    // (though that code is untested with live data)
                }
            }
        });

        // put buttons in panel
        buttons.add(graphButton);
        buttons.add(mapButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(prevButton);
        buttons.add(nextButton);

        // center panel ----------------------------------------------------------------------
        tabPane = new JTabbedPane();
        defaultView.add(tabPane, BorderLayout.CENTER);

        // (dim "graph" button for histogram tab)
        tabPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                graphButton.setEnabled(tabPane.getSelectedIndex() < 2); // 2=histogram
            }
        });

        // top panel ----------------------------------------------------------------------
        // i'd rather use a dialoglayout to right-align the "fixed"/"moving" labels,
        // but the spacing isn't quite right.  investigate.
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        /* goal:
            Fixed: [@] [Zonguldak, Karabuk 3A]
            Moving: [@] [Zonguldak, Karabuk 4A]
        */
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fixedTree = new Tree(cross.fixed);
        fixedTitle = new JLabel(cross.fixed.toString());
        f.add(new JLabel(msg.getString("fixed") + ":"));
        f.add(fixedTree);
//        f.add(Box.createHorizontalStrut(8));
        f.add(fixedTitle);
        top.add(f);

        JPanel m = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        movingTree = new Tree(cross.moving);
        movingTitle = new JLabel(cross.moving.toString());
        m.add(new JLabel(msg.getString("moving") + ":"));
        m.add(movingTree);
//        m.add(Box.createHorizontalStrut(8));
        m.add(movingTitle);
        top.add(m);

        // BUG: titles/icons don't get updated on next/prev

        // TODO: add "swap f/m" button to right side, 1-line high,
        // align-right, centered vertically in the 2-line space (its
        // own panel, somehow)
        
        defaultView.add(top, BorderLayout.NORTH);
    }

    private class RunThread extends Thread {
	public void run() {
	    try {
		if (bad) {
		    EventQueue.invokeLater(new Runnable() {
			    public void run() {
				// remove last component -- not terribly reliable ...
				defaultView.remove(defaultView.getComponentCount()-1);
				bad = false;
				defaultView.add(tabPane, BorderLayout.CENTER);

				// repaint();
			    }
			});
		}

		// run the cross! -- can throw IllegalArgumentException, if it's not a valid cross
		cross.run();

		// update tables
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    // show tables
			    if (scoresTable == null) // -- this special case should be handled inside updateTables()
				initTables();
			    else
				updateTables();
			}
		    });

	    } catch (IllegalArgumentException iae) {
		// crossdate couldn't run --> exception says why
		// JLabel label = new JLabel(iae.getMessage(), JLabel.CENTER);
		final String msg = iae.getMessage();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    JLinedLabel label = new JLinedLabel(msg);
			    defaultView.remove(tabPane);
			    defaultView.add(label, BorderLayout.CENTER);
			    bad=true;
			    // repaint();
			}
		    });

		// DESIGN: if overlap too short, offer to "Set minimum overlap to <x> for this crossdate only."?
		// DESIGN: if can't run WJ cross, offer to swap f/m?  no, better: if swapping them would help,
		// say so, and say how to do it (cmd-E, swap)

		// DESIGN: if you're looking at A-vs-B-with-X, and hit cmd-E, and return, return as close
		// as possible -- A-vs-B-with-Y, or B-vs-A-with-X, or something like that.  not always 1-1-1.

		// BUG: if the overlap is too short, and it's changed in the prefs, it should update the view properly
	    } finally {
		// these better run no matter what
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    // update titles
			    fixedTitle.setText(cross.fixed.toString());
			    movingTitle.setText(cross.moving.toString());
			    fixedTree.setSample(cross.fixed);
			    movingTree.setSample(cross.moving);

			    // enable buttons (very important!)
			    enableButtons();

			    // window title
			    setTitle(cross.toString());

			    // repaint(); // needed?
			}
		    });
	    }
	}
    }

    private boolean bad=false;

    /** Update both tables, the frame title, and the prev/next buttons. */
    private void updateTables() {
        // tables
        if (sort != null) // (only if some sort done -- otherwise this is null)
            Sort.sort(cross.highScores, sort, !sort.equals("number")); // sort by last sort, again -- BUG: doesn't store |reverse| flag
        scoresTable.setModel(new CrossTableModel(cross));
        sigsTable.setModel(new CrossSigsTableModel(cross));
        sigsTable.getColumnModel().getColumn(0).setPreferredWidth(24); // number column is too big
        sigsTable.getColumnModel().getColumn(0).setMaxWidth(36);
        sigsTable.getColumnModel().getColumn(1).setCellRenderer(new RangeRenderer());
        sigsTable.getColumnModel().getColumn(2).setCellRenderer(new RangeRenderer());
        sigsTable.getColumnModel().getColumn(3).setCellRenderer(new DecimalRenderer(cross.getFormat()));
        sigsTable.getColumnModel().getColumn(4).setCellRenderer(new DecimalRenderer("000")); // assume overlap is usually around 3 digits
        sigsTable.getColumnModel().getColumn(5).setCellRenderer(new DecimalRenderer("00.00")); // ???

        // [[ why'd i ever take this out? ]]
        // testing: hilite sig scores
        // scoresTable.setDefaultRenderer(Double.class, new ScoreRenderer(cross));
        ScoreRenderer sr = new ScoreRenderer(cross);
        for (int i=1; i<=10; i++)
            scoresTable.getColumnModel().getColumn(i).setCellRenderer(sr);

        // histogram
        int w = histoTable.getSize().width;
        histo = new Histogram(cross);
        histoTable.setModel(new HistogramTableModel());
        histoTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        histoTable.getColumnModel().getColumn(0).setPreferredWidth(w/4);
        histoTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        histoTable.getColumnModel().getColumn(1).setPreferredWidth(w/4);
        histoTable.getColumnModel().getColumn(2).setCellRenderer(new CountRenderer(histo.getFullestBucket()));

        // select highest score
        selectHighest();

        // hilite sig scores -- can't set a defaultrenderer, but i'm not sure why.
	//        ScoreRenderer sr = new ScoreRenderer(cross);
	//        for (int i=1; i<=10; i++)
	//            scoresTable.getColumnModel().getColumn(i).setCellRenderer(sr);

        // title
        // setTitle(cross.toString());
        // JVM BUG?: on OS X this sometimes doesn't set the title until you move the mouse.  ack.
    }

    // for performance reasons, extend DefaultTableCellRenderer, not JLabel+TableCellRenderer
    // (see DefaultTableCellRenderer javadoc for why using a stock JLabel probably isn't so great.)
    private class ScoreRenderer extends DefaultTableCellRenderer {
        private DecimalFormat df;
        private boolean fontSetYet=false;
        private Color fore, back, lite;
        public ScoreRenderer(Cross c) {
            super();
            setHorizontalTextPosition(SwingConstants.RIGHT); // FIXME: isn't this ignored?
            setOpaque(true); // is this still needed?
            df = new DecimalFormat(c.getFormat());
            fore = Color.getColor("corina.cross.foreground");
            back = Color.getColor("corina.cross.background");
            lite = Color.getColor("corina.grid.highlightcolor");
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            // null: bail
            if (value == null) {
                super.setBackground(back);
                setText("");
                return this;
            }

            // font -- needed here?  (well, at least minimize the number of calls)
            if (!fontSetYet) {
                super.setFont(table.getFont());
                super.setForeground(Color.getColor("corina.cross.foreground")); // do fore, too
                fontSetYet = true;
            }

            // score
            double score = ((Number) value).doubleValue();

            // format the number
            setText(df.format(score));

            // hilite sig scores
            if (!isSelected)
                super.setBackground(score > cross.getMinimumSignificant() ? lite : back);
            else
                super.setBackground(table.getSelectionBackground());

            return this;
        }
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getSize().width, getSize().height); // ??
            super.paintComponent(g);
        }
    }

    // select the highest score in the sigs table
    private void selectHighest() {
        double high = Double.NEGATIVE_INFINITY;
        for (int i=0; i<cross.highScores.size(); i++) {
            double test = ((Score) cross.highScores.get(i)).score;
            if (test > high) {
                sigsTable.setRowSelectionInterval(i, i);
                high = test;
            }
        }
    }

    /** Initialize both tables, the frame title, and the prev/next
        buttons. */
    private void initTables() {
        // scores
        scoresTable = new JTable(new CrossTableModel(cross));
        scoresTable.setRowSelectionAllowed(false);
        scoresTable.getTableHeader().setReorderingAllowed(false);

        // testing: hilite sig scores
        // scoresTable.setDefaultRenderer(Double.class, new ScoreRenderer(cross));
        ScoreRenderer sr = new ScoreRenderer(cross);
        for (int i=1; i<=10; i++)
            scoresTable.getColumnModel().getColumn(i).setCellRenderer(sr);

        // double-click-able
        scoresTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // respond only to double-clicks
                if (e.getClickCount() != 2)
                    return;

                // get the (row,col) of the click
                int row = scoresTable.rowAtPoint(e.getPoint());
                int col = scoresTable.columnAtPoint(e.getPoint());

                // get the year (== end-date of moving sample)
                Year y = ((CrossTableModel) scoresTable.getModel()).getYear(row, col);

                // blank spot -- better to just look for empty cell?
                if (!cross.range.contains(y))
                    return;
                if (col == 0)
                    return;
                if ((row + cross.range.getStart().row() == 0) && (col == 1))
                    return;

                // new graph at this place
                new GraphFrame(cross, y);
            }
        });

        // sigs
        sigsTable = new JTable(new CrossSigsTableModel(cross));
        NoEmptySelection.noEmptySelection(sigsTable);
        sigsTable.getTableHeader().setReorderingAllowed(false);
        sigsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sigsTable.getColumnModel().getColumn(0).setPreferredWidth(24); // number column is too big
        sigsTable.getColumnModel().getColumn(0).setMaxWidth(36);
        sigsTable.getColumnModel().getColumn(1).setCellRenderer(new RangeRenderer());
        sigsTable.getColumnModel().getColumn(2).setCellRenderer(new RangeRenderer());
        sigsTable.getColumnModel().getColumn(3).setCellRenderer(new DecimalRenderer(cross.getFormat()));
        sigsTable.getColumnModel().getColumn(4).setCellRenderer(new DecimalRenderer("000")); // assume overlap is usually around 3 digits
        sigsTable.getColumnModel().getColumn(5).setCellRenderer(new DecimalRenderer("00.00")); // ???
	// REFACTOR: this is exactly the same as in updatetables (almost)

        selectHighest();

        // double-click-able
        sigsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // respond only to double-clicks
                if (e.getClickCount() != 2)
                    return;

                // get the row of the click
                int row = scoresTable.rowAtPoint(e.getPoint());

                // get the year (== end-date of moving sample)
                Year y = ((Score) cross.highScores.get(row)).movingRange.getEnd();

                // new cross at this offset
                new GraphFrame(cross, y);
            }
        });

        // sortable
        sigsTable.getTableHeader().addMouseListener(new MouseAdapter() {
            private void dataChanged() {
                // old comment: "is this needed?" (i think so -- but would something weaker work?)
                ((AbstractTableModel) sigsTable.getModel()).fireTableDataChanged();
            }
            private int nr; // the value of the # column -- (it's an index on the data)
            private void saveSelection() {
                int row = sigsTable.getSelectedRow();
                Score s = (Score) cross.highScores.get(row);
                nr = s.number;
            }
            private void restoreSelection() {
                for (int i=0; i<cross.highScores.size(); i++) {
                    Score s = (Score) cross.highScores.get(i);
                    if (s.number == nr) {
                        sigsTable.setRowSelectionInterval(i, i);
                        return;
                    }
                }
            }
            public void mouseClicked(MouseEvent e) {
                int col = sigsTable.getColumnModel().getColumnIndexAtX(e.getX());

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
		    Collections.reverse(cross.highScores);

		} else {
		    // clicked on a new column --> sort by that field
		    sort = newSort;
		    boolean reverse=false;
		    if (col==3 || col==4 || col==5)
			reverse = true; // these 3 columns we want to sort decreasing by default
		    Sort.sort(cross.highScores, newSort, reverse);
		}

		dataChanged();
		restoreSelection();
            }
//	    private String oldSort=null;
        });

        // histogram
        histo = new Histogram(cross);
        histoTable = new JTable(new HistogramTableModel());
        int w = histoTable.getSize().width;
        histoTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        histoTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        histoTable.getColumnModel().getColumn(0).setPreferredWidth(w/4);
        histoTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        histoTable.getColumnModel().getColumn(1).setPreferredWidth(w/4);
        histoTable.getColumnModel().getColumn(2).setCellRenderer(new CountRenderer(histo.getFullestBucket()));
        histoTable.getTableHeader().setReorderingAllowed(false);

	// set preference-able stuff
	refreshFromPreferences();

	// stuff tables into tabs
	tabPane.addTab(msg.getString("sig_scores"), new JScrollPane(sigsTable));
	tabPane.addTab(msg.getString("all_scores"), new JScrollPane(scoresTable));
	tabPane.addTab(msg.getString("score_distro"), new JScrollPane(histoTable));

	// label
	// setTitle(cross.toString());
    }

    private String sort=null;

    // do both fixed and moving sites have a site+location in the sitedb?
    // BUG: theoretically, i should have a sitedblistener to watch this,
    // and update mapButton if anything changes
    private boolean mapAvailable() {
        try {
            Site s1 = SiteDB.getSiteDB().getSite(cross.fixed);
            Site s2 = SiteDB.getSiteDB().getSite(cross.moving);
            return (s1.getLocation()!=null && s2.getLocation()!=null);
        } catch (SiteNotFoundException snfe) {
            return false;
        }
    }

    // for centered columns of stuff in a table
    // no, THIS IS STUPID.  just call ((defaultrenderer) getrenderer()).sethorizalign('center);
    private static DefaultTableCellRenderer centerRenderer;
    static {
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }

    private void enableButtons() {
        prevButton.setEnabled(!(seq.isFirst()));
        nextButton.setEnabled(!(seq.isLast()));
        graphButton.setEnabled(true);
        mapButton.setEnabled(mapAvailable());
    }
    private void disableButtons() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        graphButton.setEnabled(false);
        mapButton.setEnabled(false);
    }

    /** Create a crossdate for a given Sequence.
	@param s the Sequence to display */
    public CrossFrame(Sequence s) {
        // REFACTOR: the getCross(), disableButtons(), start()/start() exists in several places.

        // copy data
        seq = s;
        try {
            cross = seq.getCross();
        } catch (IOException ioe) {
            // DEAL WITH ME
        }

        // init gui
        initGui();

        // menubar: in testing
        setJMenuBar(new XMenubar(this, makeMenus())); // REFACTOR: move makeMenus() part of XFrame?

        // disable buttons: don't let user do anything until the first cross is done
        disableButtons();

// HEY!  the line before and after this are just the same as Action prevNext, apart from the seq.*pairing() call.  refactor?
        
        // run it in its own thread, and then do the gui stuff
	new RunThread().start();
        
        // show
        pack();
        setSize(new Dimension(640, 480));
        show();
    }

    // TODO: note that what's printed (and even the options to present the user)
    // depend on which view is shown -- crossprinter, tableprinter, gridprinter.
    // tableprinter should be refactored to use corina.print, as well.

    // feedback for longer operations
    private Cursor was;
    private void startWaiting() {
        was = getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }
    private void stopWaiting() {
        setCursor(was);
    }

    // what i'm actually displaying
    private CardLayout cardLayout;
    private JPanel cards;

    // null=not made yet
    private JComponent tableView = null;
    private JComponent gridView = null;
    private Grid grid = null; // if you want me to print it, i'll need to remember it
    private Table table = null; // likewise
    private int view = 0; // 0=default
    
    private JMenu[] makeMenus() {
        JMenuItem asCross = new XMenubar.XRadioButtonMenuItem("as Crossdates");
        asCross.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control 1")));
        asCross.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // nothing need be done, right?
                cardLayout.show(cards, "default");
                view = 0;
            }
        });

        final JFrame glue = this;
        
        JMenuItem asTable = new XMenubar.XRadioButtonMenuItem("as Table");
        asTable.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control 2")));
        asTable.setEnabled(true); // enabled for 1-by-n crossdates _only_
        asTable.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // make table, if null
                    if (tableView == null) {
                        startWaiting();
                        table = new Table(seq); // this can take a little while
                        stopWaiting();
                        final JTable jtable = new JTable(new TableTableModel(table)); // pizza pizza!
                        // should i make my own crossdatingtable extends jtable?  i'll need some more stuff...

                        // click-to-sort
                        jtable.getTableHeader().addMouseListener(new MouseAdapter() {
                            private int oldSort = -1;
                            private boolean reverse = false; // (initial value doesn't matter)
                            public void mouseClicked(MouseEvent me) {
                                // sort once per click
                                for (int i=0; i<me.getClickCount(); i++) {

                                    int col = jtable.getColumnModel().getColumnIndexAtX(me.getX());
                                    final String fields[] = new String[] { "title", "t", "tr", "d", "overlap", "dist" };
                                    if (col != oldSort)
                                        reverse = (col == 1 || col == 2 || col == 3 || col == 4); // t, tr, d, overlap
                                    else
                                        reverse = !reverse;

                                    // BUG: .dist are often null, but sort() doesn't check for that, yet -- catch it here, there, i don't care
                                    Sort.sort(table.table, fields[col], reverse);
                                    ((AbstractTableModel) jtable.getModel()).fireTableDataChanged();

                                    // BUG: doesn't store old selection (!)

                                    oldSort = col;
                                }
                            }
                        });

                        tableView = new JScrollPane(jtable);
                        jtable.getColumnModel().getColumn(0).setPreferredWidth((int) (glue.getWidth() * 0.5f));
                        
                        // add card
                        cards.add(tableView, "table");
                    }

                    // show card
                    cardLayout.show(cards, "table");
                    view = 1;

                } catch (IOException ioe) {
                    // (eh, how graceful)
                    System.out.println("ack -- " + ioe);
                }
                // but:
                // -- show it, THEN start computing it
                // (idea: not-yet-loaded rows are shown as filename-only, with a gray background)

                // other things to keep in mind:
                // -- select the crossdate the user was just looking at
                // -- double-clicking (or return) on any crossdate does what?  sets view=cross, or graphs, i don't know
                // -- need graph/map buttons still?  sure!
                // -- write cell renderer so "3.85" gets centered around the "." (and ranges around their "-"'s)
                // -- highlight significant crosses on this view, as well; or just use even-odd coloring
                // -- overlap (and others, until they're decimal-aligned) should be align-right
                // -- click-to-sort, of course
                // -- hook up printing and saving to the current view
                // -- (well, saving should save everything all the time, but have a view="table" arg)
                // -- window title is wrong
                // -- as-table should only be enabled for 1-by-n crossdates (should it?)
                // -- context menu: open sample, graph sample, ---, view this crossdate, graph this crossdate, ---, graph all
                // -- still need "recompute" button?  (no, recompute is evil.  but how else to know when a file changed?)
                // -- accept drops: samples are added to moving-list; collections(?) have all of their items added
                // -- edit menu: "select-all, copy" should be supported

                // add fourth, initial view, "samples"?  left column is fixed, right column is moving, "run" switches to cross-view
            }
        });

        JMenuItem asGrid = new XMenubar.XRadioButtonMenuItem("as Grid");
        asGrid.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control 3")));
        asGrid.setEnabled(true); // enabled for n-by-n crossdates _only_
        asGrid.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
//                try {
                    // make grid, if null
                    if (gridView == null) {
                        // make gridview, however you want

                        startWaiting();
                        grid = new Grid(seq); // this can take a little while
                        stopWaiting();

                        // -- extracted from GridFrame.initTable():
                        JTable output = new JTable(new GridFrame.GridTableModel(grid));

                        // cell-selection only
                        output.setRowSelectionAllowed(false);

                        // set cell height/width from Grid
                        output.setRowHeight((int) (Grid.getCellHeight()*1.0) + 2); // was: *scale
                        for (int i=0; i<output.getColumnCount(); i++)
                            output.getColumnModel().getColumn(i).setPreferredWidth((int) (Grid.getCellWidth()*1.0) + 2); // was: *scale

                        // no top-header
                        output.setTableHeader(null);
                        output.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                        // renderer -- use same as for printer
                        output.setDefaultRenderer(Object.class, new GridFrame.GridRenderer());
                        output.setShowGrid(false);

                        // -- end extraction
                        
                        gridView = new JScrollPane(output);

                        // add card
                        cards.add(gridView, "grid");
                    }

                    // show card
                    cardLayout.show(cards, "grid");
                view = 2;

/*                } catch (IOException ioe) {
                    // (eh, how graceful)
                    System.out.println("ack -- " + ioe);
                }
*/
//                JComponent gridComponent = new GridComponent(new Grid(seq)); // write GridComponent!
//                glue.setContentPane(new JScrollPane(gridComponent));
                
                // normal grid operations:
                // -- save, print.  (keep operations, override usual menuitems.)
                // -- graph all -- i'll probably want to keep it -- make available anywhere? -- seq.getAllSamples()?
                // -- adjust zoom -- i guess i'll keep it -- it's not much use for other views, is it? -- why am i now just using the standard font here?  (same view for screen and print.) -- hmm...
            }
        });

        // menuitems (final for inner class use)
        final JMenuItem moving = new XMenubar.XRadioButtonMenuItem("First sample fixed, second sample moving");
        final JMenuItem fixed = new XMenubar.XRadioButtonMenuItem("First sample moving, second sample fixed");
        final JMenuItem both = new XMenubar.XRadioButtonMenuItem("Both samples moving");

        // prefs -> window
        if (System.getProperty("corina.cross.dating") != null) {
            decode(System.getProperty("corina.cross.dating"));
            if (!fixedFloats && movingFloats)
                moving.setSelected(true);
            else if (fixedFloats && !movingFloats)
                fixed.setSelected(true);
            else
                both.setSelected(true);
        } else {
            moving.setSelected(true);
        }

        // an action to call when one of the 3 menuitems is selected
        Action a = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // based on menuitem, set flags
                Object source = e.getSource();
                if (source == moving) {
                    fixedFloats = false;
                    movingFloats = true;
                } else if (source == fixed) {
                    fixedFloats = true;
                    movingFloats = false;
                } else { // both
                    fixedFloats = true;
                    movingFloats = true;
                }

                // update view
                repaint();

                // set pref, and save
                System.setProperty("corina.cross.dating", encode()); // save pref
                try {
                    Prefs.save();
                } catch (IOException ioe) {
                    System.out.println("oops..."); // FIXME: make an intelligent warning here (with "don't warn again"?)
                }
            }
        };
        moving.addActionListener(a);
        fixed.addActionListener(a);
        both.addActionListener(a);

        { // views radio button group -- this always starts as-cross
            ButtonGroup views = new ButtonGroup();
            views.add(asCross);
            views.add(asTable);
            views.add(asGrid);
            asCross.setSelected(true);
        }

        { // ranges radio button group
            ButtonGroup ranges = new ButtonGroup();
            ranges.add(fixed);
            ranges.add(moving);
            ranges.add(both);
        }

        JMenuItem kit = new XMenubar.XMenuItem("Edit Fixed/Moving Lists");
        kit.setAccelerator(KeyStroke.getKeyStroke(XMenubar.macize("control E")));
        kit.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CrossdateKit(seq);
            }
        });

        // make a menu
        JMenu view = new XMenubar.XMenu("View");
        view.add(asCross);
        view.add(asTable);
        view.add(asGrid);
        view.addSeparator();
        view.add(moving);
        view.add(fixed);
        view.add(both);
        view.addSeparator();
        view.add(kit);

        return new JMenu[] { view };
    }

    private boolean fixedFloats = false, movingFloats = true;

    // DESIGN: does this need to re-run the crossdate?  if the overlap changes, i'll need more/less data... ouch.
    // BUG: after cmd-E, put window back where it was
    public void refreshFromPreferences() {
	new RunThread().start(); // hack!  (testing)

	// gridlines
	boolean gridlines = Boolean.getBoolean("corina.cross.gridlines");
	scoresTable.setShowGrid(gridlines);
	sigsTable.setShowGrid(gridlines);

        // format strings
        for (int i=1; i<=10; i++) // format strings updated
            scoresTable.getColumnModel().getColumn(i).setCellRenderer(new ScoreRenderer(cross));
        ((CrossSigsTableModel) sigsTable.getModel()).formatChanged();
        ((HistogramTableModel) histoTable.getModel()).formatChanged();
        // need to call something here to update histogram tab?

        // font
        if (System.getProperty("corina.cross.font") != null) {
            Font f = Font.getFont("corina.cross.font");
            scoresTable.setFont(f);
            sigsTable.setFont(f);
            histoTable.setFont(f);
            scoresTable.setRowHeight(f.getSize() + 3);
            sigsTable.setRowHeight(f.getSize() + 3);
            histoTable.setRowHeight(f.getSize() + 3);
        }

        // colors
        if (System.getProperty("corina.cross.background") != null) {
            scoresTable.setBackground(Color.getColor("corina.cross.background"));
            sigsTable.setBackground(Color.getColor("corina.cross.background"));
        }
        if (System.getProperty("corina.cross.foreground") != null) {
            scoresTable.setForeground(Color.getColor("corina.cross.foreground"));
            sigsTable.setForeground(Color.getColor("corina.cross.foreground"));
        }
    }

    // printing
    public int getPrintingMethod() {
        return (view==2 ? PrintableDocument.PAGEABLE : PrintableDocument.PRINTABLE);
    }
    public Printable makePrintable(PageFormat pf) {
        // hack: look at the view, figure out what to print
        if (view == 2) {
            // grid's a pageable, bad!
            throw new IllegalArgumentException();
        } else if (view == 1) {
            // print table -- not done yet! -- but it'll be 
            // throw new IllegalArgumentException();
            TablePrinter printer = new TablePrinter(table, pf);
            return Printer.print(printer);
        }
        // ... else normal view

        // ask what to print
        // REFACTOR: move this into its own class, like inner class PrintViewsDialog
        final JDialog d = new JDialog(this, "Print Views", true);
        JPanel p = new JPanel(new BorderLayout());
        d.setContentPane(p);
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));

        p.add(new JLabel("Print which views?"), BorderLayout.NORTH);

        p.add(Box.createHorizontalStrut(20), BorderLayout.WEST);

        int defaultView = tabPane.getSelectedIndex();
        JCheckBox sigsCheckbox = new JCheckBox("Significant Scores", defaultView==0);
        JCheckBox allCheckbox = new JCheckBox("All Scores", defaultView==1);
        JCheckBox histoCheckbox = new JCheckBox("Histogram", defaultView==2);
        
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(Box.createVerticalStrut(10));
        c.add(sigsCheckbox);
        c.add(Box.createVerticalStrut(6));
        c.add(allCheckbox);
        c.add(Box.createVerticalStrut(6));
        c.add(histoCheckbox);
        c.add(Box.createVerticalStrut(12));
        p.add(c, BorderLayout.CENTER);
                        
        JPanel b = new JPanel(new ButtonLayout());
        JButton cancel = new XButton("cancel");
        JButton ok = new XButton("ok");
        b.add(Box.createHorizontalStrut(24));
        b.add(cancel);
        b.add(ok);
        p.add(b, BorderLayout.SOUTH);

//        boolean reallyPrint=false;
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        ok.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
//                reallyPrint = true;
            }
        });

        OKCancel.addKeyboardDefaults(d, ok);
        d.setResizable(false);
        d.pack();
        d.show();

        // print it
        //        return (reallyPrint ? new CrossPrinter(cross) : null); // need UserCancelled here!
//        return new CrossPrinter(cross, sigsCheckbox.isSelected(), allCheckbox.isSelected(), histoCheckbox.isSelected());
        CrossPrinter printer = new CrossPrinter(cross,
                                                sigsCheckbox.isSelected(),
                                                allCheckbox.isSelected(),
                                                histoCheckbox.isSelected());
        return Printer.print(printer);
        // need UserCancelled here!
    }
    public Pageable makePageable(PageFormat pf) {
        if (view == 2)
            return grid.makeHardcopy(pf);
//        else if (view == 1) {
//            return new TablePrinter(table, pf);
        else
            throw new IllegalArgumentException();
    }
    public String getPrintTitle() {
        return cross.toString();
    }
}
