/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import edu.cornell.dendro.corina.sample.Sample;

public class QuickScorer {
	public QuickScorer() {}

	private class graphTScore extends edu.cornell.dendro.corina.cross.TScore {
		public graphTScore(Sample s1, Sample s2) {
			super(s1, s2);
			preamble();
		}
	}
	
	public void calculate(Sample s1, Sample s2, int o1, int o2) {			
		if(sample1 != s1 || sample2 != s2) {
			p_tscore = new graphTScore(s1, s2);
			sample1 = s1;
			sample2 = s2;
		}
		
		tscore = p_tscore.compute(o1, o2);
		rvalue = p_tscore.rval;
	}
	
	public float tscore, rvalue;

	private Sample sample1 = null, sample2 = null;
	private graphTScore p_tscore;
}