/**
 * 
 */
package org.tellervo.desktop.graph;

import org.tellervo.desktop.cross.TScore;
import org.tellervo.desktop.sample.Sample;

public class QuickScorer {
	public QuickScorer() {}

	private class graphTScore extends org.tellervo.desktop.cross.TScore {
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
		
		TScore.TScoreBundle scores = p_tscore.compute(o1, o2, new TScore.TScoreBundle());
		tscore = scores.tscore;
		rvalue = scores.rval;
	}
	
	public float tscore, rvalue;

	private Sample sample1 = null, sample2 = null;
	private graphTScore p_tscore;
}