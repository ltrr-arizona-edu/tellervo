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
import corina.Element;
import corina.graph.GraphFrame;
import corina.site.Site;
import corina.site.SiteDB;
import corina.map.MapFrame;
import corina.gui.XFrame;
import corina.gui.XMenubar;
import corina.gui.PrintableDocument;
import corina.gui.HasPreferences;
import corina.gui.Tree;
import corina.gui.ButtonLayout;
import corina.gui.DialogLayout;
import corina.editor.CountRenderer;
import corina.util.Platform;

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
import javax.swing.Timer;
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
    private Timer timer;

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
            return new Year(10 * (row + row_min) + col - 1); // REFACTOR: this looks really friggin' familiar...
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

            df = new DecimalFormat(c.getFormat());
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
            return 5; // nr, fixed, moving, score, overlap
        }

        /** The class of a given column.  This is used so the default
            table renderer right-aligns Integers.
            @param col the column to query
            @return Class of this column's data */
        public Class getColumnClass(int col) {
            return getValueAt(0, col).getClass();
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
                default:
                    throw new IllegalArgumentException(); // never happens
            }
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
                case 0: return cross.getName();
                case 1: return msg.getString("quantity");
                case 2: return msg.getString("histogram");
                default: throw new IllegalArgumentException(); // never happens
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

                    // what i'll need to do this right:
                    // -- new DecimalRenderer(String fmt) -- renders Numbers as decimals
                    // (don't render non-Numbers, so the model can always send up "" to mean "blank")
                    /*
                     then this becomes:
                     -- ouch, still can't render ranges that way.  oh well.
                     case 0: return ...;
                     case 1: return (n==0 ? "" : b[r].n);
                     case 2: return (n==0 ? "" : b[r].pct); // DecRend
                     case 3: return b[r].n; // CountRend
                     */
            }
        }
    }

    // gui setup
    private void initGui() {
        // timer ------------------------------------------------------------
        timer = new Timer(10 /* ms */, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cross.isFinished()) {
                    // stop timer
                    ((Timer) e.getSource()).stop();

                    // show tables
                    if (scoresTable == null)
                        initTables();
                    else
                        updateTables();

                    // update titles
                    fixedTitle.setText(cross.fixed.toString());
                    movingTitle.setText(cross.moving.toString());
                    fixedTree.setSample(cross.fixed);
                    movingTree.setSample(cross.moving);
                }
            }
        });

	// bottom panel ------------------------------------------------------------
        JPanel buttons = new JPanel(new ButtonLayout());
        getContentPane().add(buttons, BorderLayout.SOUTH);
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // HACK!

        // classloader, for icons
        ClassLoader cl = this.getClass().getClassLoader();

        // prev
        prevButton = new JButton(msg.getString("prev"),
                                 new ImageIcon(cl.getResource("toolbarButtonGraphics/navigation/Back16.gif")));
        if (Platform.isMac) prevButton.setIcon(null);
        prevButton.setMnemonic(msg.getString("prev_key").charAt(0));
        prevButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                // disable buttons during processing
                disableButtons();

                // get prev cross
                if (!seq.isFirst()) {
                    seq.prevPairing();
                    try {
                        cross = seq.getCross();
                    } catch (IOException ioe) {
                        // DEAL WITH ME
                    }
                }

                // run it in its own thread
                Thread thread = new Thread(cross);
                timer.start();
                thread.start();
            }
        });

        // next
        nextButton = new JButton(msg.getString("next"),
                                 new ImageIcon(cl.getResource("toolbarButtonGraphics/navigation/Forward16.gif")));
        if (Platform.isMac) nextButton.setIcon(null);
        nextButton.setMnemonic(msg.getString("next_key").charAt(0));
        nextButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                // disable buttons during processing
                disableButtons();

                // get next cross
                if (!seq.isLast()) {
                    seq.nextPairing();
                    try {
                        cross = seq.getCross();
                    } catch (IOException ioe) {
                        // DEAL WITH ME
                    }
                }

                // run it in its own thread
                Thread thread = new Thread(cross);
                timer.start();
                thread.start();
            }
        });

        // graph
        graphButton = new JButton(msg.getString("plot"));
        graphButton.setMnemonic(msg.getString("plot_key").charAt(0));
        graphButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                // "significant scores" selected
                if (tabPane.getSelectedIndex() == 0) {
                    int nr = sigsTable.getSelectedRow(); // which cross to use

                    // error-checking! -- make sure user picked something
                    if (nr == -1) {
                        JOptionPane.showMessageDialog(null,
                                                      msg.getString("selecterror"),
                                                      msg.getString("cross_error"),
                                                      JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // get year (== moving sample end-year) for this row
                    Year y = ((Score) cross.highScores.get(nr)).movingRange.getEnd();

                    // new cross at this offset
                    new GraphFrame(cross, y);

                    // "all scores" selected
                } else {
                    int r = scoresTable.getSelectedRow();
                    int c = scoresTable.getSelectedColumn();
                    if (r==-1 || c==-1) {
                        JOptionPane.showMessageDialog(null,
                                                      msg.getString("selecterror"),
                                                      msg.getString("cross_error"),
                                                      JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Year y = ((CrossTableModel) scoresTable.getModel()).getYear(r, c);
                    new GraphFrame(cross, y);
                }
            }
        });

	// map (!)
	mapButton = new JButton(msg.getString("map"));
	mapButton.setMnemonic(msg.getString("map_key").charAt(0));
	mapButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // 1-by-N crosses are special
		    /* -- need a new way to figure out if the're special now, sucka
		    if (seq instanceof Sequence1xN) {
			Sequence1xN seq1n = (Sequence1xN) seq;
			Site s1, s2[];
			try {
			    s1 = SiteDB.getSiteDB().getSite(seq1n.f);
			    s2 = new Site[seq1n.m.size()];
			    for (int i=0; i<seq1n.m.size(); i++)
				s2[i] = SiteDB.getSiteDB().getSite(((Element) seq1n.m.get(i)).load()); // wasteful!
			    new MapFrame(s1, s2);
			} catch (IOException ioe) {
			    System.out.println("ioe = " + ioe.getMessage());
			} catch (Exception e) {
			    System.out.println("e = " + e.getMessage());
			}

			return;
		    }
		    */

		    // figure out the locations
		    Site s1, s2;
		    try {
			s1 = SiteDB.getSiteDB().getSite(cross.fixed);
			s2 = SiteDB.getSiteDB().getSite(cross.moving);
		    } catch (Exception e) {
			JOptionPane.showMessageDialog(null,
						      msg.getString("maperror"),
						      msg.getString("cross_error"),
						      JOptionPane.ERROR_MESSAGE);
			return;
		    }

                    // draw the map
                    try {
                        new MapFrame(s1, s2);
                    } catch (IOException ioe) {
                        System.out.println("ioe! -- " + ioe);
                        // DEAL WITH ME HERE
                    }
                }
        });

	// close
/* -- obsolete
        closeButton = new JButton(msg.getString("close"));
	closeButton.setMnemonic(msg.getString("close_key").charAt(0));
	closeButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    dispose();
		}
	    });
	buttons.add(closeButton);
*/

        // put buttons in panel
        buttons.add(graphButton);
        buttons.add(mapButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(prevButton);
        buttons.add(nextButton);

	// center panel ----------------------------------------------------------------------
	tabPane = new JTabbedPane();
	getContentPane().add(tabPane, BorderLayout.CENTER);

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

	getContentPane().add(top, BorderLayout.NORTH);
    }

    /** Update both tables, the frame title, and the prev/next
        buttons. */
    private void updateTables() {
        // tables
        scoresTable.setModel(new CrossTableModel(cross));
        sigsTable.setModel(new CrossSigsTableModel(cross));
        sigsTable.getColumnModel().getColumn(0).setPreferredWidth(24); // number column is too big
        sigsTable.getColumnModel().getColumn(0).setMaxWidth(36);

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

        // hilite sig scores
        // scoresTable.setDefaultRenderer(Double.class, new ScoreRenderer(cross)); -- ???
        ScoreRenderer sr = new ScoreRenderer(cross);
        for (int i=1; i<=10; i++)
            scoresTable.getColumnModel().getColumn(i).setCellRenderer(sr);

        // title
        setTitle(cross.toString());

        // buttons
        prevButton.setEnabled(!(seq.isFirst()));
        nextButton.setEnabled(!(seq.isLast()));
        graphButton.setEnabled(true);
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
            super.setBackground(score > cross.getMinimumSignificant() ? lite : back);

            return this;
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
	sigsTable.getTableHeader().setReorderingAllowed(false);
	sigsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	sigsTable.getColumnModel().getColumn(0).setPreferredWidth(24); // number column is too big
	sigsTable.getColumnModel().getColumn(0).setMaxWidth(36);

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

	// sortable -- fixme: maintain selected row
	final JTable glue = sigsTable;
	sigsTable.getTableHeader().addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    int col = glue.getColumnModel().getColumnIndexAtX(e.getX());
		    if (e.getClickCount() == 1) { // wtf was i thinking?
			switch (col) {
			case 0: // number
			    Collections.sort(cross.highScores, new Comparator() {
				    public int compare(Object o1, Object o2) {
					int s1 = ((Score) o1).number;
					int s2 = ((Score) o2).number;
					return (s1 - s2);
				    }
				});
			    ((AbstractTableModel) glue.getModel()).fireTableDataChanged(); // is this needed?
			    break;
			case 1: // fixed range
			    // sort by fixedRange?
			    break;
			case 2: // moving range
			    // sort by movingRange?
			    break;
			case 3: // score
			    Collections.sort(cross.highScores, new Comparator() {
				    public int compare(Object o1, Object o2) {
					double s1 = ((Score) o1).score;
					double s2 = ((Score) o2).score;
					return (s1 > s2 ? -1 : 1); // no zero possible?
				    }
				});
			    ((AbstractTableModel) glue.getModel()).fireTableDataChanged();
			    break;
			case 4: // overlap
			    Collections.sort(cross.highScores, new Comparator() {
				    public int compare(Object o1, Object o2) {
					int s1 = ((Score) o1).span;
					int s2 = ((Score) o2).span;
					return (s2 - s1);
				    }
				});
			    ((AbstractTableModel) glue.getModel()).fireTableDataChanged();
			    break;
			}
		    }
		}
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
	setTitle(cross.toString());

	// buttons
	prevButton.setEnabled(!(seq.isFirst()));
	nextButton.setEnabled(!(seq.isLast()));
	graphButton.setEnabled(true);
    }

    // for centered columns of stuff in a table
    // no, THIS IS STUPID.  just call ((defaultrenderer) getrenderer()).sethorizalign('center);
    private static DefaultTableCellRenderer centerRenderer;
    static {
        centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }

    private void disableButtons() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        graphButton.setEnabled(false);
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

        // init gui (incl. timer)
        initGui();

        // menubar: in testing
        setJMenuBar(new XMenubar(this, makeMenus())); // REFACTOR: move makeMenus() part of XFrame?

        // run cross
        Thread thread = new Thread(cross);
        disableButtons();
        timer.start();
        thread.start();

        // show
        pack();
        setSize(new Dimension(640, 480));
        show();
    }

    // TODO: note that what's printed (and even the options to present the user)
    // depend on which view is shown -- crossprinter, tableprinter, gridprinter.
    // crossprinter and tableprinter should be refactored to use corina.print, as well.
    
    private JMenu[] makeMenus() {
        JMenuItem asCross = new XMenubar.XRadioButtonMenuItem("as Crossdates");
        asCross.setSelected(true);
        asCross.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("WRITEME: stub to switch to crossdate-view");
            }
        });

        final JFrame glue = this;
        
        JMenuItem asTable = new XMenubar.XRadioButtonMenuItem("as Table");
        asTable.setEnabled(true); // enabled for 1-by-n crossdates _only_
        asTable.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // this might be as simple as ...
                try {
                    JTable table = new JTable(new TableTableModel(new Table(seq)));
                    glue.setContentPane(new JScrollPane(table));
                    table.getColumnModel().getColumn(0).setPreferredWidth((int) (glue.getWidth() * 0.40)); // not table.getWidth?  (doesn't work, dunno why not)
                    // i think a cardlayout would be more like what i'm looking for, and probably not require validate+repaint
                    glue.validate();
                    glue.repaint();
                } catch (IOException ioe) {
                    System.out.println("ack -- " + ioe);
                }
                // but:
                // -- memoize it
                // -- show it, THEN start computing it
                // (idea: not-yet-loaded rows are shown as filename-only, with a gray background)

                // other things to keep in mind:
                // -- "title" column should be wider
                // -- select the crossdate the user was just looking at
                // -- double-clicking (or return) on any crossdate does what?  sets view=cross, or graphs, i don't know
                // -- need graph/map buttons still?  sure!
                // -- doesn't invalidate/repaint correctly
                // -- write cell renderer so "3.85" gets centered around the "." (and ranges around their "-"'s)
                // -- highlight significant crosses on this view, as well; or just use even-odd coloring
                // -- overlap (and others, until they're decimal-aligned) should be align-right
                // -- click-to-sort, of course
                // -- hook up printing and saving to the current view
                // -- (well, saving should save everything all the time, but have a view="table" arg)
                // -- window title is wrong
                // -- as-table should only be enabled for 1-by-n crossdates (should it?)
                // -- context menu: open sample, graph sample, ---, view crossdate, graph crossdate
                // -- still need "recompute" button?  (no, recompute is evil.  but how else to know when a file changed?)
                // -- accept drops: samples are added to moving-list; collections(?) have all of their items added
                // -- edit menu: "select-all, copy" should be supported

                // add fourth, initial view, "samples"?  left column is fixed, right column is moving, "run" switches to cross-view
            }
        });

        JMenuItem asGrid = new XMenubar.XRadioButtonMenuItem("as Grid");
        asGrid.setEnabled(true); // enabled for n-by-n crossdates _only_
        asGrid.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("WRITEME: stub to switch to grid-view");
//                JComponent gridComponent = new GridComponent(new Grid(seq)); // write GridComponent!
//                glue.setContentPane(new JScrollPane(gridComponent));
                glue.validate();
                glue.repaint();
                
                // normal grid operations:
                // -- save, print.  (keep operations, override usual menuitems.)
                // -- graph all -- i'll probably want to keep it -- make available anywhere? -- seq.getAllSamples()?
                // -- adjust zoom -- i guess i'll keep it -- it's not much use for other views, is it? -- why am i now just using the standard font here?  (same view for screen and print.) -- hmm...
            }
        });

        JMenuItem fixed = new XMenubar.XRadioButtonMenuItem("Fixed floats");
        fixed.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fixedFloats = true;
                movingFloats = false;
                repaint();
            }
        });
        JMenuItem moving = new XMenubar.XRadioButtonMenuItem("Moving floats");
        moving.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fixedFloats = false;
                movingFloats = true;
                repaint();
            }
        });
        moving.setSelected(true);
        JMenuItem both = new XMenubar.XRadioButtonMenuItem("Both float");
        both.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fixedFloats = true;
                movingFloats = true;
                repaint();
            }
        });
        
        { // views radio button group
            ButtonGroup views = new ButtonGroup();
            views.add(asCross);
            views.add(asTable);
            views.add(asGrid);
        }

        { // ranges radio button group
            ButtonGroup ranges = new ButtonGroup();
            ranges.add(fixed);
            ranges.add(moving);
            ranges.add(both);
        }

        // make a menu
        JMenu view = new XMenubar.XMenu("View");
//        view.add(asCross);
//        view.add(asTable);
//        view.add(asGrid);
//        view.addSeparator();
        view.add(fixed);
        view.add(moving);
        view.add(both);

        return new JMenu[] { view };
    }

    private boolean fixedFloats = false, movingFloats = true;

    public void refreshFromPreferences() {
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
        return PrintableDocument.PRINTABLE;
    }
    public Printable makePrintable(PageFormat pf) {
        // ask what to print
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
        JButton cancel = new JButton("Cancel");
        JButton ok = new JButton("OK");
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
        
        corina.util.OKCancel.addKeyboardDefaults(d, ok);
        d.setResizable(false);
        d.pack();
        d.show();

        // print it
        //        return (reallyPrint ? new CrossPrinter(cross) : null); // need UserCancelled here!
        return new CrossPrinter(cross, sigsCheckbox.isSelected(), allCheckbox.isSelected(), histoCheckbox.isSelected());
        // need UserCancelled here!
    }
    public Pageable makePageable(PageFormat pf) {
        return null;
    }
    public String getPrintTitle() {
        return cross.toString();
    }
}
