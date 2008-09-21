package edu.cornell.dendro.corina.cross;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

public class AllScoresTableModel extends AbstractTableModel {
	private CrossdateCollection.Pairing pairing;
	private Class<?> shownCrossdateClass;
	private Cross cross;
	private ScoreCellRenderer scoreRenderer;
	private JTable table;

	public AllScoresTableModel(JTable table) {
		this.table = table;
		scoreRenderer = new ScoreCellRenderer(this);	
	}
	
	public void applyFormatting() {
		for(int i = 1; i < getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(scoreRenderer);		
	}
	
	public void clearCrossdates() {
		this.pairing = null;
		this.cross = null;
		
		fireTableDataChanged();
	}
	
	public void setCrossdates(CrossdateCollection.Pairing pairing) {
		this.pairing = pairing;
		
		// update the cross (shownCrossdateClass can be null?)
		cross = pairing.getCrossForClass(shownCrossdateClass);

		if(cross != null)
			updateTable();
		
		fireTableDataChanged();
	}
	
	/**
	 * Set the type of crossdate to display (e.g., TScore.class, RScore.class)
	 * @param clazz
	 */
	public void setScoreClass(Class<?> clazz) {
		shownCrossdateClass = clazz;

		// update the cross
		if(pairing != null)  {			
			cross = pairing.getCrossForClass(clazz);
			if(cross != null)
				updateTable();
		}
		
		fireTableDataChanged();
	}

	private int row_min;
	private int row_max;
	
	private void updateTable() {
		scoreRenderer.updateCross();
		// this stuff is copied out of DecadalModel
		// TODO: REFACTOR at some point
		row_min = cross.getRange().getStart().row();
		row_max = cross.getRange().getEnd().row();
	}
	
	@Override
	public int getColumnCount() {
		return 11;
	}

	@Override
	public int getRowCount() {
		if(cross == null)
			return 0;
		return (row_max - row_min + 1);
	}

	@Override
	public String getColumnName(int col) {
	    if (col == 0)
	    	return I18n.getText("year");
	    else
	    	return Integer.toString(col-1);
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		// for the year column
		if (col == 0) {
			if (row == 0)
				return cross.getRange().getStart();
			else if (row + row_min == 0)
				return new Year(1); // special case
			else
				return getYear(row, col + 1);
		} else {
			return getScoreAt(row, col);
		}
	}

	/**
	 * Get the score at this row, col
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Float getScoreAt(int row, int col) {
		// for the year column
		if (col == 0) {
			return null;
		} else {
			// check for year zero
			if ((row + row_min == 0) && (col == 1))
				return null;
			
			Year year = getYear(row, col);

			if (cross.getRange().contains(year))
				return cross.getScore(year);
			else
				return null;
		}
	}

	
	/**
	 * Given the cell at row, col, give a range for
	 * the secondary sample that produced that score
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Range getSecondaryRangeForCell(int row, int col) {
		if(col == 0)
			return null; // nothing in this column
		
		// check for year zero
		if ((row + row_min == 0) && (col == 1))
			return null;

		// I think the logic behind this makes sense... but it makes my head hurt
		Year year = getYear(row, col);
		// essentially, redate the moving's end to the year in this row,col
		return cross.getMoving().getRange().redateEndTo(year);
	}
	
	/**
	 * Gets a redated sample, given a cell
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Sample getSecondaryForCell(int row, int col) {
		Range newRange = getSecondaryRangeForCell(row, col);
		
		if(newRange == null)
			return null;
		
		Sample redate = new Sample();
		
		Sample.copy(pairing.getSecondary(), redate);
		redate.setRange(newRange);
		
		return redate;
	}
	
	/**
	 * Get graphs for a given row/col
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Graph> getGraphForCell(int row, int col) {
		Sample secondary = getSecondaryForCell(row, col);
		
		if(secondary == null)
			return null;
		
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		graphs.add(new Graph(pairing.getPrimary()));
		graphs.add(new Graph(secondary));
				
		return graphs;
	}
	
	/**
	 * Get the overlap
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Integer getOverlapAt(int row, int col) {		
		Range newRange = getSecondaryRangeForCell(row, col);
		
		// now compute the overlap
		return getOverlapAt(newRange);
	}
	
	/**
	 * Get the overlap, given a range already
	 * 
	 * @param newRange
	 * @return
	 */
	public Integer getOverlapAt(Range newRange) {
		return (newRange == null) ? null : newRange.overlap(cross.getMoving().getRange());		
	}
	
	protected Year getYear(int row, int col) {
		return new Year(row + row_min, // offset row by row_min (top row is 0)
				col - 1); // offset col by 1 (left col is year label)
	}
	
	protected Cross getCross() {
		return cross;
	}
	
    private class ScoreCellRenderer extends DefaultTableCellRenderer {
        private DecimalFormat df;
        private Color fore, back, lite;
        private AllScoresTableModel model;
        
        public ScoreCellRenderer(AllScoresTableModel model) {
            super();
            
            this.model = model;
            
            setHorizontalAlignment(SwingConstants.RIGHT); // right align
            setOpaque(true); // so our background displays
            
            df = new DecimalFormat("0000"); // sane default?
            
            fore = App.prefs.getColorPref(Prefs.EDIT_FOREGROUND, Color.black);
            back = App.prefs.getColorPref(Prefs.EDIT_BACKGROUND, Color.white);
            lite = App.prefs.getColorPref(Prefs.GRID_HIGHLIGHTCOLOR, Color.green);
        }
        
        public void updateCross() {
        	if(model.getCross() != null)
        		df = new DecimalFormat(model.getCross().getFormat());
        }
               
        @Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

        	Component cell = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

        	// null: empty cell
			if (value == null) {
				cell.setBackground(back);
				return cell;
			}
			
			// hilight sig scores
			if (isSelected)
				cell.setBackground(table.getSelectionBackground());
			else if(model.getCross() != null) {
				Range newRange = model.getSecondaryRangeForCell(row, column);
				Integer overlap = (newRange == null) ? null : model.getOverlapAt(newRange);
				Float score = model.getScoreAt(row, column);
				boolean significant = (overlap == null || score == null) ? false : 
					model.getCross().isSignificant(score, overlap);

				cell.setBackground(significant ? lite : back);
				if(newRange != null && overlap != null)
					((JLabel)cell).setToolTipText("<html>Primary: " + 
							model.getCross().getFixed().getRange() + 
							"<br>Secondary: " + newRange + "<br>Overlap: " + 
							overlap);
			}

			return cell;
		}
        
        public void setValue(Object value) {
        	Object res = value;
        	
        	if(res != null && res instanceof Number) {
        		res = df.format(((Number) value).floatValue());
        	}
        	
        	super.setValue(res);
        }
    }
}
