/**
 * 
 */
package edu.cornell.dendro.corina.index;

import java.text.DecimalFormat;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.ui.I18n;

/**
 * Simple model for displaying indexing algorithms and their statistical fits
 * 
 * @author peterbrewer
 *
 */
public class IndexTableModel extends AbstractTableModel {
	// formatting for decimals
	public static final String CHI2_FORMAT = "#,##0.0";
	public static final String RHO_FORMAT = "0.000";

	private DecimalFormat fmtChi2 = new DecimalFormat(CHI2_FORMAT);
	private DecimalFormat fmtR = new DecimalFormat(RHO_FORMAT);
	private IndexSet iset;

	public IndexTableModel(IndexSet iset) {
		this.iset = iset;
	}

	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return I18n.getText("index.algorithm");
		case 1:
			return "\u03C7\u00B2"; // should be "Chi\overline^2" =
									// \u03C7\u0304\u00B2
			// unfortunately, Mac OS X (at least -- so probably others)
			// apparently doesn't
			// combine combining diacritics after greek letters, so it looks
			// just plain bad.
			// (but a\u0304\u00B2 looks fine, so it's not completely
			// ignorant of combining diacritics)
			// (actually, a\u0304 looks fine in window titles, but not
			// tables headers, so ... yeah. ick.)
			// Windows 2000 report: swing labels can do the greek but not
			// the diacritic (chi, followed
			// by a box), window titles can do the diacritic but not the
			// greek (box, with an overline).

		case 2:
			return "\u03C1"; // "rho"
		default:
			throw new IllegalArgumentException(); // can't happen
		}
	}

	public int getRowCount() {
		return iset.indexes.size();
	}

	public Object getValueAt(int row, int col) {
		Index i = iset.indexes.get(row);
		switch (col) {
		case 0:
			return i.getName();
		case 1:
			return fmtChi2.format(i.getChi2()); // (is defined, as long as
												// N!=0)
		case 2:
			if (Double.isNaN(i.getR())) // can happen
				return "-";
			else
				return fmtR.format(i.getR());
		default:
			throw new IllegalArgumentException(); // can't happen
		}
	}

	public void setIndexSet(IndexSet iset) {
		this.iset = iset;
		fireTableDataChanged();
	}
}