/*
 * Created on Jan 22, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package corina.cross.sigscores;

import java.text.DecimalFormat;

import javax.swing.table.AbstractTableModel;

import corina.Range;
import corina.cross.HighScore;
import corina.ui.I18n;


class CrossSigsTableModel extends AbstractTableModel {
  // i'm not sure what this is, but i need it.
  private final static DecimalFormat formatNoPercent = new DecimalFormat("#.0#");

  private final SignificantScoresView view;
  // formatter for the scores
  private DecimalFormat df;

  // create a Tablemodel to display the significant scores of the
  // enclosing class's crossdate.
  CrossSigsTableModel(SignificantScoresView view) {
    this.view = view;
    // pick up its format
    formatChanged();
  }

  // called when the user's pref for the crossdate format
  // (e.g., "0.00") was changed
  void formatChanged() {
    df = new DecimalFormat(this.view.getCrossdate().getFormat());
  }

  /*
   * columns names: -- nr. -- fixed (start - end) -- moving (start - end) -- algorithm name -- overlap -- confidence
   */
  public String getColumnName(int col) {
    switch (col) {
      case 0: return I18n.getText("number");
      case 1: return I18n.getText("fixed") + " (" + this.view.getCrossdate().getFixed().range + ")";
      case 2: return I18n.getText("moving") + " (" + this.view.getCrossdate().getMoving().range + ")";
      case 3: return this.view.getCrossdate().getName();
      case 4: return I18n.getText("overlap");
      case 5:
        return I18n.getText("confidence") + " (%)"; // hack!
        // FIXME: make this "confidence%", in case i need w/o-% also
      default:
        throw new IllegalArgumentException(); // never happens
    }
  }

  // the number of rows.
  public int getRowCount() {
    return (this.view.getCrossdate() == null ? 0 : this.view.getCrossdate().getHighScores().getScores().size());
    // ??? - why can cross be null?
  }

  // the number of columns.
  public int getColumnCount() {
    return 6; // (#, fixed, moving, score, overlap, confidence)
  }

  // used so Integer columns get rendered right-aligned.
  // TODO: can i do this for other classes/columns? if not, why not?
  public Class getColumnClass(int col) {
    switch (col) {
      case 0: return Integer.class;
      case 1:
      case 2: return Range.class;
      case 3: return String.class; // ...formatted by DecimalRenderer (sorting uses the model, not the view)
      case 4: return Integer.class;
      case 5: return String.class; // is this right?
      default:
        throw new IllegalArgumentException(); // never happens
    }
  }

  // the value at a (row, col) location.
  public Object getValueAt(int row, int col) {
    if (this.view.getCrossdate() == null)
      return null; // (BUG?: when does this happen?)

    HighScore s = (HighScore) this.view.getCrossdate().getHighScores().getScores().get(row);

    // PERF: since s.number and s.span are only used for
    // (1) being returned as Objects and (2) being sorted by,
    // wouldn't it make more sense for them to be declared in
    // the HighScore class as Integers?

    switch (col) {
      case 0: return new Integer(s.number);
      case 1: return (this.view.isFixedFloats() ? s.fixedRange : this.view.getCrossdate().getFixed().range);
      case 2: return (this.view.isMovingFloats() ? s.movingRange : this.view.getCrossdate().getMoving().range);
      case 3: return df.format(s.score);
      case 4: return new Integer(s.span); // PERF!
      case 5: return (s.confidence > 0.90 ? formatNoPercent.format(s.confidence * 100) : "");
      // as %:
      // return (s.confidence > 0.90 ?
      //         Bayesian.format.format(s.confidence) : null);
      default:
        throw new IllegalArgumentException(); // never happens
    }
  }
}