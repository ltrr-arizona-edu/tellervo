/*
 * Created on Jan 22, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.cornell.dendro.corina.cross.sigscores;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import edu.cornell.dendro.corina.cross.HighScore;
import edu.cornell.dendro.corina.util.Sort;


final class TableHeaderSortMouseListener extends MouseAdapter {
  private final SignificantScoresView view;

  TableHeaderSortMouseListener(SignificantScoresView view) {
    this.view = view;
    // TODO Auto-generated constructor stub
  }

  private void dataChanged() {
    this.view.getTableModel().fireTableDataChanged();
    this.view.repaint(); // ??
  }

  private int nr; // the value of the # column -- (it's an index on the data)

  private void saveSelection() {
    int row = this.view.getTable().getSelectedRow();
    HighScore s = (HighScore) this.view.getCrossdate().getHighScores().getScores().get(row);
    nr = s.number;
  }

  private void restoreSelection() {
    for (int i = 0; i < this.view.getCrossdate().getHighScores().getScores().size(); i++) {
      HighScore s = (HighScore) this.view.getCrossdate().getHighScores().getScores().get(i);
      if (s.number == nr) {
        this.view.getTable().setRowSelectionInterval(i, i);
        return;
      }
    }
  }

  @Override
public void mouseClicked(MouseEvent e) {
    int col = this.view.getTable().getColumnModel().getColumnIndexAtX(e.getX());

    // if the user clicked on a range column, well, what does that mean?
    // (you should click on the number column to sort by range).
    // so i'll just ignore it, for now.
    // TODO: this should probably sort by range, earliest-to-latest.
    // (but that doesn't make a whole lot of sense if they're all the same -- ???)
    if (col == 1 || col == 2)
      return;

    // when sorting, let's count double-click as 2 clicks, because (1)
    // that's what the finder does, and (2) that's probably what users
    // would normally expect, anyway.

    String newSort = ""; // compiler is paranoid
    switch (col) {
      case 0: newSort = "number"; break;
      case 3: newSort = "score"; break;
      case 4: newSort = "span"; break;
      case 5: newSort = "confidence"; break;
    }

    saveSelection();

    if (newSort.equals(this.view.getSort())) {
      // clicked on the same column --> just reverse everything
      Collections.reverse(this.view.getCrossdate().getHighScores().getScores());
    } else {
      // clicked on a new column --> sort by that field
      this.view.setSort(newSort);
      boolean reverse = false;
      if (col == 3 || col == 4 || col == 5)
        reverse = true; // these 3 columns we want to sort decreasing by default
      Sort.sort(this.view.getCrossdate().getHighScores().getScores(), newSort, reverse);
    }

    dataChanged();
    restoreSelection();
  }
  //	    private String oldSort=null; --- ???
}