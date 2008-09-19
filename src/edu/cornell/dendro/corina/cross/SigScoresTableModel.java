package edu.cornell.dendro.corina.cross;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.sample.Sample;

public class SigScoresTableModel extends AbstractTableModel {
	private CrossdateCollection.Pairing pairing;
	private List<ScoreInfo> scores;
	
	/*
	 * Header information. Note that score classes MUST match
	 * their order in headings!
	 */
	private final String[] headings = { "Range", "Overlap", "Trend", "T-Score", "R-Value", "D-Score", "WJ" };
	private final Class<?> scoreClasses[] = { Trend.class, TScore.class, RValue.class, DScore.class, Weiserjahre.class };
	
	public SigScoresTableModel() { 
	}
	
	public void clearCrossdates() {
		this.pairing = null;
		this.scores = null;
		
		fireTableDataChanged();
	}
	
	public void setCrossdates(CrossdateCollection.Pairing pairing) {
		this.pairing = pairing;
		calculateScores();
		fireTableDataChanged();
	}

	// score info
	private class ScoreInfo {
		public ScoreInfo() {
			scores = new String[scoreClasses.length];
		}
		
		public Range range;
		public Integer overlap;
		
		public String[] scores;
	}
	
	private void calculateScores() {
		// TreeMap automatically sorts by Range & Overlap for us
		TreeMap<String, ScoreInfo> scores = new TreeMap<String, ScoreInfo>();
		
		// iterate through each kind of cross
		for(int classIdx = 0; classIdx < scoreClasses.length; classIdx++) {
			Class<?> scoreClass = scoreClasses[classIdx];
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
			}
		}

		// now, we only care about them in a list
		ArrayList<ScoreInfo> newScores = new ArrayList<ScoreInfo>();
		for(Map.Entry<String, ScoreInfo> e : scores.entrySet()) {
			newScores.add(e.getValue());
		}
		
		this.scores = newScores;
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
	
	// tablemodel stuff
	public String getColumnName(int col) {
		return headings[col];
	}
	
	public int getColumnCount() {
		return headings.length;
	}

	public int getRowCount() {
		if(pairing == null || scores == null)
			return 0;
		
		return scores.size();
	}
	
	public Class<?> getColumnClass(int col) {
		switch(col) {
		case 0:
			return Range.class;
		case 1:
			return Integer.class;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return String.class; // it's formatted!
		}
		
		return String.class;
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
}
