/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.cross;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.cross.CrossdateDialog.StatType;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.gui.SortedHeaderArrowRenderer;
import edu.cornell.dendro.corina.index.DecimalRenderer;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ColorUtils;

@SuppressWarnings("serial")
public class SigScoresTableModel extends AbstractTableModel {
	/**
	 * A class that represents what score column is
	 * 
	 * @author Lucas Madar
	 *
	 * @param <T> cross type of the column
	 */
	protected static class ScoreColumn<T extends Cross> {
		private final static String DEFAULT_FORMATTING = "00.00";

		public final String heading;
		public final Class<T> scoreType;
		public final String formatting;
		
		public ScoreColumn(String heading, Class<T> scoreType, String formatting) {
			this.heading = heading;
			this.scoreType = scoreType;
			this.formatting = formatting;
		}
		
		public ScoreColumn(String heading, Class<T> scoreType) {
			this(heading, scoreType, DEFAULT_FORMATTING);
		}
	}
		
	/**
	 * This class encapsulates score info, which is represented by a table row
	 * @author Lucas Madar
	 */
	private class ScoreInfo {
		public Integer overlap;
		
		public Range range;
		public String[] scores;
		public boolean[] significant;
		
		@SuppressWarnings("unchecked")
		public Comparable[] scoreValue;
		
		public ScoreInfo() {
			scores = new String[columns.size()];
			scoreValue = new Comparable[columns.size()];
			
			// by default, everything score is marked as significant
			significant = new boolean[columns.size()];
			Arrays.fill(significant, true);
		}
	}
	
	/**
	 * This class actually compares scoreinfo fields
	 * @author Lucas Madar
	 *
	 */
	private class ScoreInfoComparator implements Comparator<ScoreInfo> {
		private int column;
		
		public ScoreInfoComparator(int column) {
			this.column = column;
		}

		@SuppressWarnings("unchecked")
		public int compare(ScoreInfo s1, ScoreInfo s2) {
			Comparable v1 = getVal(s1);
			Object v2 = getVal(s2);
			
			if(v1 == null && v2 == null)
				return 0;
			
			if(v1 == null)
				return +1;
			if(v2 == null)
				return -1;
			
			return -(v1.compareTo(v2));
		}
		
		private Comparable<?> getVal(ScoreInfo sc) {
			switch(column) {
			case 0:
				return sc.range;
			case 1:
				return sc.overlap;
			default:
				return sc.scoreValue[column - 2];	
			}
		}	
	}
	
	private class SigDecimalRenderer extends DecimalRenderer {
		public SigDecimalRenderer(String sample) {
			super(sample);
		}
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			ScoreInfo score = scores.get(row);
			setForeground(score.significant[column - 2]
					? table.getForeground() 
					: ColorUtils.blend(table.getForeground(), 0.3f, table.getBackground(), 0.7f));
			
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}

	}
	
	/**
	 * This class handles clicks on table row headers, for sorting
	 */
	private class SigScoresSorter extends MouseAdapter {
		private int lastSortedCol = -1;
		private SigScoresTableModel model;
				
		private JTable table;
		
		public SigScoresSorter(SigScoresTableModel model, JTable table) {
			this.model = model;
			this.table = table;
		}

		public void sort(int col, boolean forceReverse) {
			Collections.sort(model.scores, new ScoreInfoComparator(col));
			
			if(forceReverse)
				Collections.reverse(model.scores);
			
			this.lastSortedCol = col;
			model.headerRenderer.setSortColumn(col);
			model.headerRenderer.setReversed(!forceReverse); // we default to reversed!
			
			// make the table header repaint (for new arrows)
			table.getTableHeader().repaint();
		}
		
		@Override
		public void mouseClicked(MouseEvent me) {
			int col = table.getColumnModel().getColumnIndexAtX(me.getX());
			
			// sanity check
			if(col < 0)
				return;			
			
			if(col == lastSortedCol) {
				Collections.reverse(model.scores);

				// reverse and repaint arrows
				model.headerRenderer.setReversed(!model.headerRenderer.isReversed());
				table.getTableHeader().repaint();
			}
			else {
				sort(col, false);
			}
			
			// notify the table
			model.fireTableDataChanged();
			
		}

		public void setUnsorted() {
			lastSortedCol = -1;
		}
	}
	
	// initialize the score columns
	protected final static List<ScoreColumn<? extends Cross>> columns = new ArrayList<ScoreColumn<? extends Cross>>();
	
	static {
		columns.add(new ScoreColumn<Trend>(Trend.getNameStatic(), Trend.class, "0.0%"));
		columns.add(new ScoreColumn<TScore>(TScore.getNameStatic(), TScore.class));
		columns.add(new ScoreColumn<RValue>(RValue.getNameStatic(), RValue.class));
		columns.add(new ScoreColumn<DScore>(DScore.getNameStatic(), DScore.class));
		columns.add(new ScoreColumn<Weiserjahre>(Weiserjahre.getNameStatic(), Weiserjahre.class));
	}
	
	private SortedHeaderArrowRenderer headerRenderer;
	private CrossdateCollection.Pairing pairing;
	
	private List<ScoreInfo> scores;
	
	private SigScoresSorter sorter;
	
	private JTable table;
	
	public SigScoresTableModel(JTable table) { 
		this.table = table;
		// a sorter based on us...
		sorter = new SigScoresSorter(this, table);
		// a header renderer with no default 
		headerRenderer = new SortedHeaderArrowRenderer(table, null);
		
		table.getTableHeader().addMouseListener(sorter);
		table.getTableHeader().setDefaultRenderer(headerRenderer);
	}
	
	public void applyFormatting() {
    	TableColumnModel c = table.getColumnModel();
    	
    	c.getColumn(0).setCellRenderer(new RangeRenderer());
    	c.getColumn(1).setCellRenderer(new DecimalRenderer("0000"));
    	
    	for(int i = 0; i < columns.size(); i++)
    		c.getColumn(2 + i).setCellRenderer(new SigDecimalRenderer(columns.get(i).formatting));
	}
	
	public void sortByStatType(StatType stat)
	{
		int col = -1;
		if(stat.equals(StatType.TSCORE)) col = 3;
		else if (stat.equals(StatType.RVALUE)) col=4;
		else if (stat.equals(StatType.TREND)) col = 2;
		else if (stat.equals(StatType.DSCORE)) col = 5;
		else if (stat.equals(StatType.WJ)) col=6;
		
		// Do sort
		if (col>-1)	sorter.sort(col, false);
		fireTableDataChanged();
	}
	

	public void clearCrossdates() {
		this.pairing = null;
		this.scores = null;
		
		fireTableDataChanged();
	}

	public Class<?> getColumnClass(int col) {
		switch(col) {
		case 0:
			return Range.class;
		case 1:
			return Integer.class;
			
		default:
			return String.class; // it's formatted!
		}
	}

	public int getColumnCount() {
		return 2 + columns.size();
	}
	
	// tablemodel stuff
	public String getColumnName(int col) {
		switch(col) {
		case 0:
			return I18n.getText("crossdate.range");
			
		case 1:
			return I18n.getText("crossdate.overlap");
			
		default:
			return columns.get(col - 2).heading;
		}
	}
	
	/**
	 * Returns a list of graphs based on data at the given row
	 * 
	 * @param row
	 * @return
	 */
	public List<Graph> getGraphForRow(int row) {
		if(row < 0 || row > getRowCount())
			return null;
		
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		graphs.add(new Graph(pairing.getPrimary()));
		graphs.add(new Graph(getSecondaryForRow(row)));
				
		return graphs;
	}

	public int getRowCount() {
		if(pairing == null || scores == null)
			return 0;
		
		return scores.size();
	}
	
	/**
	 * Returns the redated (secondary) sample for the specific row
	 * 
	 * @param row
	 * @return
	 */
	public Sample getSecondaryForRow(int row) {
		if(row < 0 || row > getRowCount())
			return null;

		Sample redate = new Sample();

		Sample.copy(pairing.getSecondary(), redate);
		redate.setRange(scores.get(row).range);
		
		return redate;
	}

	/**
	 * Returns the redated (secondary) sample range for the specific row
	 * 
	 * @param row
	 * @return
	 */
	public Range getSecondaryRangeForRow(int row) {
		if(row < 0 || row > getRowCount())
			return null;

		return new Range(scores.get(row).range.getStart(), scores.get(row).range.getEnd());
	}	

	/**
	 * Get the row index which represents the specified range
	 * 
	 * @param rng
	 * @return
	 */
	public Integer getRowForRange(Range rng)
	{
		for(int i = 0; i < getRowCount(); i++)
		{
			if (rng.equals(getSecondaryRangeForRow(i)))
			{
				return i;
			}
		}
		return null;
	}
	
	public Object getValueAt(int row, int col) {
		ScoreInfo score = scores.get(row);
		
		switch(col) {
		case 0:
			return score.range;
		case 1:
			return score.overlap;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return score.scores[col - 2];
		}
		
		return null;
	}

	public void setCrossdates(CrossdateCollection.Pairing pairing) {
		this.pairing = pairing;
		calculateScores();

		// unsorted by default
		sorter.setUnsorted();
		headerRenderer.setReversed(false);
		headerRenderer.setSortColumn(-1);
		
		fireTableDataChanged();
	}

	private void calculateScores() {
		// TreeMap automatically sorts by Range & Overlap for us
		TreeMap<String, ScoreInfo> scores = new TreeMap<String, ScoreInfo>();
		
		// iterate through each kind of cross
		int nColumns = columns.size();
		for(int classIdx = 0; classIdx < nColumns; classIdx++) {
			// score type for this column
			Class<?> scoreClass = columns.get(classIdx).scoreType;
			
			// get the cross for this score type
			Cross cross = pairing.getCrossForClass(scoreClass);
			
			// this cross failed?
			if(cross == null)
				continue;

			// create a new way to format...
			DecimalFormat df = new DecimalFormat(cross.getFormat());
						
			// get the list of scores
			List<HighScore> cscores = cross.getHighScores().getScores();
			for(HighScore cscore : cscores) {
				// get a score for this range & overlap
				String key = cscore.movingRange.toString() + "," + cscore.span;
				ScoreInfo thisScore = scores.get(key);

				// score for this range & overlap doesn't exist yet, create it
				if(thisScore == null) {
					thisScore = new ScoreInfo();
					thisScore.range = cscore.movingRange;
					thisScore.overlap = cscore.span;
										
					scores.put(key, thisScore);
				}
				
				// and store the score!
				thisScore.scores[classIdx] = df.format(cscore.score);
				thisScore.scoreValue[classIdx] = cscore.score;
			}
		}
		
		// now, we only care about them in a list
		ArrayList<ScoreInfo> newScores = new ArrayList<ScoreInfo>();
		for(Map.Entry<String, ScoreInfo> e : scores.entrySet()) {
			// fill in empty gaps
			fillInGaps(e.getValue());
			// add to the list
			newScores.add(e.getValue());
		}
		
		this.scores = newScores;
	}
	
	private void fillInGaps(ScoreInfo s) {	
		// iterate through each kind of cross
		int nColumns = columns.size();
		for(int classIdx = 0; classIdx < nColumns; classIdx++) {
			// score type for this column
			Class<?> scoreClass = columns.get(classIdx).scoreType;
			
			// get the cross for this score type
			Cross cross = pairing.getCrossForClass(scoreClass);
			
			// this cross failed?
			if(cross == null)
				continue;
			
			// if no score exists, populate it and set it as insignificant
			if(s.scores[classIdx] == null) {
				DecimalFormat df = new DecimalFormat(cross.getFormat());
				
				// populate!
				float val = cross.getScore(s.range.getEnd());
				s.scoreValue[classIdx] = val;
				s.scores[classIdx] = df.format(val);
				s.significant[classIdx] = false;
			}
		}
	}
}
