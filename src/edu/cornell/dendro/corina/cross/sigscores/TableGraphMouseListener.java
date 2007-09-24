package edu.cornell.dendro.corina.cross.sigscores;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class TableGraphMouseListener extends MouseAdapter {
  private final SignificantScoresView view;

  TableGraphMouseListener(SignificantScoresView view) {
    this.view = view;
  }

  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) // double-clicks only
      this.view.graphSelectedCrossdate();
  }
}