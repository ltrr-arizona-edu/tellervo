package edu.cornell.dendro.corina.manip;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.sample.Sample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 * <p>
 * Class for assisting users in reconciling two Samples (actually two different
 * measurements of the same sample).
 * </p>
 * 
 * <p>
 * Two measurements are considered "reconciled" when:
 * </p>
 * 
 * <ol>
 * <li>they are the same length</li>
 * 
 * <li>all corresponding measurements are within 3% (3% of the smaller of the
 * measurments)</li>
 * 
 * <li>all corresponding trends are the same</li>
 * </ol>
 * 
 * <p>
 * This class will point out whether two samples are reconciled, and if not,
 * where they differ.
 * </p>
 */

public class Reconciler {
	private Sample s1, s2;
	private int failureCount;

	public enum FailureType {
		TRENDNEXT, TRENDPREV, THREEPERCENT
	}
	
	private Map<Year, Set<FailureType>> failureMap;
	
	/**
	 * Construct a new reconciliation from two given samples.
	 * 
	 * @param a
	 *            the A-reading
	 * @param c
	 *            the C-reading
	 */
	public Reconciler(Sample a, Sample c) {
		this.s1 = a;
		this.s2 = c;
		
		failureMap = new HashMap<Year, Set<FailureType>>();

		rereconcile();
	}
	
	public void rereconcile() {
		failureMap.clear();
		failureCount = 0;
		
		// first, they must be equal in length!
		if(s1.getData().size() != s2.getData().size())
			failureCount++;

		// compute minimum length, in case they're not equal in length...
		minLen = Math.min(s1.getData().size(), s2.getData().size());

		check3Percent();
		checkTrends();		
	}
	
	public Set<FailureType> getFailuresForYear(Year y) {
		return failureMap.get(y);
	}
	
	public int getFailureCount() {
		return failureCount;
	}
	
	private void addFailureForYear(Year y, FailureType failure) {
		Set<FailureType> failures = failureMap.get(y);
		
		if(failures == null) {
			failures = new HashSet<FailureType>();
			failureMap.put(y, failures);
		}
		
		failures.add(failure);
		this.failureCount++;
	}


	// length
	private int minLen;

	// given 2 values, compute the trend between them:
	// decreasing==-1, increasing==+1, stayssame==0
	// -- WHA? why don't i use trend.java?
	private int trend(int d1, int d2) {
		if (d1 < d2)
			return +1;
		if (d1 > d2)
			return -1;
		return 0;
	}

	// check trends
	private void checkTrends() {

		int w1 = ((Number) s1.getData().get(0)).intValue();
		int w2 = ((Number) s2.getData().get(0)).intValue();
		for (int i = 1; i < minLen; i++) {
			// store widths/"previous"
			int w1p = w1;
			int w2p = w2;

			// get next year's widths
			w1 = ((Number) s1.getData().get(i)).intValue();
			w2 = ((Number) s2.getData().get(i)).intValue();

			// compute trends -- possible (desireable) to use cross.Trend here?
			int trend1 = trend(w1p, w1);
			int trend2 = trend(w2p, w2);

			// compare trends -- "with trends like these, who needs ..."
			if (trend1 != trend2) {
				// report bad trend
				Year y = s1.getRange().getStart().add(i-1);
				
				// does this make sense?
				//addFailureForYear(y, FailureType.TRENDNEXT);
				addFailureForYear(y.add(1), FailureType.TRENDPREV);
			}
		}
	}

	// check 3%
	private void check3Percent() {

		for (int i = 0; i < minLen; i++) {
			// get widths, as floats
			float w1 = ((Number) s1.getData().get(i)).floatValue();
			float w2 = ((Number) s2.getData().get(i)).floatValue();

			// w_min = minimum(w1, w2); w_max = max.
			float w_min = Math.min(w1, w2);
			float w_max = Math.max(w1, w2);

			// threePct = 3% of w_min, rounded up
			float threePct = (float) Math.ceil(0.03 * w_min); // EXTRACT
																// CONSTANT!

			// is w_max <= w_min + threePct?
			if (w_max > w_min + threePct) {
				// report bad 3%
				addFailureForYear(s1.getRange().getStart().add(i), FailureType.THREEPERCENT);
			}
		}
	}
}
