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

package corina.cross.sigscores;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import corina.Year;
import corina.core.App;
import corina.cross.Cross;
import corina.cross.RangeRenderer;
import corina.cross.TopScores.HighScore;
import corina.graph.GraphWindow;
import corina.index.DecimalRenderer;
import corina.prefs.Prefs;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import corina.util.NoEmptySelection;

/**
 * A view of the significant scores of a crossdate. The view can be changed at any time to a different crossdate.
 * 
 * <p>
 * WRITEME: describe this view, exactly what columns it displays, a sample view (table), the fixed/moving nonsense, the sorting, the column sizing (well, maybe not that), the score formatting, what
 * double-clicking does, what right-clicking does (nothing, yet), how to use it (only 3 public methods), what prefs it uses, ...
 * </p>
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <li>this should be simply a view of TopScores. as such, each top score should also contain a flag "isFuture". this is true if: one sample is fixed, one sample is moving, and the moving sample's
 * end date for this crossdate is greater than the current year. this class (TopScoresView) will display isFuture samples in a lighter shade (50% between foreground and background).
 * 
 * <li>use Prefs, not properties
 * 
 * <li>next/prev don't update the format used for the new cross (e.g., see "0.60" for trend instead of "60.0%")
 * <li>next/prev don't even update the number of sig scores properly! -- but it does after you resize the column
 * 
 * <li>get fixedFloats, movingFloats from CrossFrame; would an enum be helpful?
 * 
 * <li>... and figure out if i can integrate this without screwing up the threading maze i've set up. (i think so. i just need to call it in an InvokeLater, because it's going to get hit from a
 * non-event-handling thread.)
 * 
 * <li>move sorting from CrossdateWindow into here
 * 
 * <li>clean up variable names: "df"!
 * <li>show the current sort in the header -- copy from Browser.java (use vars |sort|, |reverse|) earlier i said: - BETTER: abstract out column-header-modifier, given sort-field, sort-reverse
 * <li>right-click on header should let you change both the sort, and the meaning of the fixed/moving ranges. think up an elegant way to do that.
 * <li>mark row if fixed,moving are where they're dated in the file
 * <li>rename to TopScoresView
 * <li>extract .highScores field to TopScores class -- it'll be a member of the Crossdate class (getHighScores()) -- it'll be constructed at the end of run() -- computeHighScores() will be gone, so
 * Crossdate will be simpler -- it'll have the data fields from HighScore, as well -- good abstraction! -- (what's its interface?)
 * <li>let me change pref from menu: min overlap
 * <li>(not me:) make "fixed", "moving" labels popups
 * <li>only show ranges in "fixed"/"moving" headers if they're not being displayed below?
 * <li>let me change pref from menu: format (0.00)
 * <li>(save column widths to a hidden pref?)
 * 
 * <li>IDEA: each view is printable. (SequenceView prints whatever it's viewing right now.) choosing "print" prints whatever the current view is.
 * </ul>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i style="color: gray">dot </i> edu&gt;
 * @version $Id$
 */
public class SignificantScoresView extends JPanel implements PrefsListener {
  /**
   * Make a new view of the significant scores of a crossdate.
   * 
   * @param crossdate
   *          the initial crossdate to view
   */
  public SignificantScoresView(Cross crossdate) {
    this.crossdate = crossdate;

    model = new CrossSigsTableModel(this);
    initTable(); // -- didn't these used to get made in a separate thread?

    makeDoubleClickable(); // MOVE: to initTable()? refr, too?
    makeSortable();

    JPanel fixedmovingpanel = new JPanel();
    fixedmovingpanel.setBorder(BorderFactory.createTitledBorder("fixed/moving floats"));
    ((FlowLayout) fixedmovingpanel.getLayout()).setHgap(0);
    ((FlowLayout) fixedmovingpanel.getLayout()).setVgap(0);
    final JCheckBox fixedcheckbox = new JCheckBox("fixed floats");
    final JCheckBox movingcheckbox = new JCheckBox("moving floats");
    ChangeListener floatchangelistener = new ChangeListener() {
      public void stateChanged(ChangeEvent ce) {
        if (ce.getSource() == fixedcheckbox) {
          fixedFloats = fixedcheckbox.isSelected();
        } else {
          movingFloats = movingcheckbox.isSelected();
        }
        model.fireTableDataChanged();
      }
    };
    fixedcheckbox.addChangeListener(floatchangelistener);
    movingcheckbox.addChangeListener(floatchangelistener);
    fixedmovingpanel.add(fixedcheckbox);
    fixedmovingpanel.add(movingcheckbox);
    JScrollPane scroll = new JScrollPane(table);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    setLayout(new BorderLayout());
    add(fixedmovingpanel, BorderLayout.NORTH);
    add(scroll);
  }

  // select the highest score in the sigs table
  private void selectHighest() {
    if (crossdate.getHighScores().getScores().size() == 0)
      return;

    int row = 0;
    double high = ((HighScore) crossdate.getHighScores().getScores().get(0)).score;

    for (int i = 1; i < crossdate.getHighScores().getScores().size(); i++) {
      double test = ((HighScore) crossdate.getHighScores().getScores().get(i)).score;
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
    table.addMouseListener(new TableGraphMouseListener(this));
  }

  // mouse listener for header (click to sort)
  private void makeSortable() {
    table.getTableHeader().addMouseListener(new TableHeaderSortMouseListener(this));
  }

  // DOCUMENT THIS! apparently i need it.
  private String sort = null;

  String getSort() {
    return sort;
  }
  void setSort(String sort) {
    this.sort = sort;
  }

  private void initTable() {
    table = new JTable();
    table.setAutoCreateColumnsFromModel(true);
    NoEmptySelection.noEmptySelection(table);
    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getTableHeader().setReorderingAllowed(false);
    table.setModel(model);

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
  // from the same code. (nah, it wouldn't be too hard to do that, anyway.)
  private int[] saveColumnWidths() {
    TableColumnModel columns = table.getColumnModel();
    int n = columns.getColumnCount();
    int columnWidths[] = new int[n];
    for (int i = 0; i < n; i++)
      columnWidths[i] = columns.getColumn(i).getWidth();
    return columnWidths;
  }

  private void restoreColumnWidths(int columnWidths[]) {
    TableColumnModel columns = table.getColumnModel();
    int n = columnWidths.length;
    for (int i = 0; i < n; i++)
      columns.getColumn(i).setPreferredWidth(columnWidths[i]);
  }

  private void updateTable() {
    // data changed -- have to say "structure changed" because
    // one of the headers also changed.

    // for perceived stability, record the sizes of the columns,
    // and restore it after the columns are reset.
    int widths[] = saveColumnWidths();

    model.fireTableStructureChanged();
    
    restoreColumnWidths(widths);

    // reset columns
    setupColumns();

    // select new highest
    selectHighest();

    revalidate(); // ??
    repaint(); // ??
  }

  /**
   * Change the view to show a different crossdate.
   * 
   * @param crossdate
   *          the new crossdate to view
   */
  public void setCrossdate(Cross crossdate) {
    this.crossdate = crossdate;
    updateTable();
  }

  private Cross crossdate;
  private JTable table;
  private AbstractTableModel model;

  Cross getCrossdate() {
    return crossdate;
  }
  AbstractTableModel getTableModel() {
    return model;
  }
  JTable getTable() {
    return table;
  }

  /**
   * Make a new graph (in a new window) from the selected crossdate.
   */
  public void graphSelectedCrossdate() {
    // get the row
    int row = table.getSelectedRow();

    // get the year (== end-date of moving sample)
    HighScore score = (HighScore) crossdate.getHighScores().getScores().get(row);
    Year y = score.movingRange.getEnd();

    // new cross at this offset
    new GraphWindow(crossdate, y);
  }

  // WRITEME: these aren't hooked up yet
  private boolean fixedFloats = true;
  private boolean movingFloats = true;

  boolean isFixedFloats() {
    return fixedFloats;
  }
  boolean isMovingFloats() {
    return movingFloats;
  }

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
    boolean gridlines = Boolean.valueOf(App.prefs.getPref("corina.edit.gridlines"))
        .booleanValue();
    table.setShowGrid(gridlines);
  }

  private void refreshFormat() {
    // format strings
    ((CrossSigsTableModel) model).formatChanged();
  }

  private void refreshFont() {
    // WAS: corina.cross.font (merged with corina.edit.font)
    if (App.prefs.getPref("corina.edit.font") != null) {
      Font f = Font.decode(App.prefs.getPref("corina.edit.font"));
      table.setFont(f);
      table.setRowHeight(f.getSize() + 3);
      // FIXME: instead of 3, use (defaultRowHeight - defaultFontSize)
    }
  }

  private void refreshBackground() {
    table.setBackground(App.prefs.getColorPref(Prefs.EDIT_BACKGROUND, Color.white));
  }

  private void refreshForeground() {
    table.setForeground(App.prefs.getColorPref(Prefs.EDIT_FOREGROUND, Color.black));
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

    App.prefs.addPrefsListener(this);
  }

  public void removeNotify() {
    super.removeNotify();

    App.prefs.removePrefsListener(this);
  }
}