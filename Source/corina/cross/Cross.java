//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.cross;

import corina.Year;
import corina.Range;
import corina.Sample;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.text.DecimalFormat;

import java.io.File;

/**
   <p>Abstract class representing the scores of a crossdate.  The data
   vector holds the crossdate scores; the range holds the range of end
   dates for the moving sample.  After construction, the run() method
   must be called; making this a good candidate for running in a
   thread.</p>

   <p>Instead of an abstract class, this might be better off as an
   interface (extends Runnable) with a couple of public accessor
   methods.  I can still provide an abstract class that implements
   this interface, like Swing does in many places.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public abstract class Cross implements Runnable {

    /** Resource bundle for localization. */
    public static ResourceBundle msg = ResourceBundle.getBundle("CrossdatingBundle");

    /** The crossdate scores. */
    public double data[];

    /** The cross's range, which is the range of end-years the moving
        sample can be placed at. */
    Range range;

    /** Reference to the sample being held fixed. */
    protected Sample fixed;

    /** Returns the fixed sample.
	@return the fixed sample */
    public Sample getFixed() {
	return fixed;
    }

    /** Reference to the sample being moved to compute the vector of
        scores. */
    protected Sample moving;

    /** Returns the moving sample.
	@return the moving sample */
    public Sample getMoving() {
	return moving;
    }

    /** Set to true just before the run() method returns.  To be used
        to determine if a cross has been run, or merely been
        constructed. */
    private boolean finished=false;

    /** Is the run finished? */
    public boolean isFinished() {
	return finished;
    }

    /** A vector of high (statistically significant) scores.  A vector
	of Integer indices into the data.  The (private) method
	computeHighScores() computes this, and should be called in
	run() after the scores have been computed, but before
	<code>finished</code> is set. */
    public List highScores = new ArrayList();

    /** The minimum allowable crossdate overlap between two samples;
        its value is 10. */
    public final static int OVERLAP = 10;

    // don't use me
    protected Cross() { }

    /** Create a crossdate, given fixed and moving samples.
	@param fixed sample to hold fixed
	@param moving sample to compare against the fixed sample */
    public Cross(Sample fixed, Sample moving) {
	// copy references to fixed, moving
	this.fixed = fixed;
	this.moving = moving;

	// set the range (= end date of moving sample)
	Year start = fixed.range.getStart().add(OVERLAP - 1);
	Year end = fixed.range.getEnd().add(moving.range.span() - OVERLAP - 1);
	range = new Range(start, end);
    }

    /** Return a human-readable name for the crossdate.
	@return a user-readable name for this type of cross */
    public abstract String getName();

    /** Return a format string for this cross to be used with DecimalFormat.
	@return a format string for this cross */
    public abstract String getFormat();

    /** Return the cross as a String, in the format "Cross:
	SampleA versus SampleB".
	@return a String value of the cross */
    public final String toString() {
	String f = (String) fixed.meta.get("filename") + " (" + fixed.range + ")";
	String m = (String) moving.meta.get("filename") + " (" + moving.range + ")";

	f = (new File(f)).getName();
	m = (new File(m)).getName();

	return msg.getString("cross") + ": " + f + " " + msg.getString("versus") + " " + m;
	// was: return getName() + ": " + f + " versus " + m;
    }

    /** For a given crossdating algorithm, the minimum statistically
	significant score; assumes higher scores are better fits.
	@return the minimum statistically significant score this
	algorithm can return */
    public abstract double getMinimumSignificant();

    // search for high scores in the list, after scores have been
    // computed.  run() calls this last.
    private final void computeHighScores() {
        int nr=0;
        double threshold = getMinimumSignificant();

        for (int i=0; i<data.length; i++)
            if (data[i] >= threshold)
                highScores.add(new Score(this, i, ++nr));
    }

    /** Crossdate preamble: any setup that needs to be done before the
        main loop to compute individual scores. */
    protected void preamble() {
	// nothing need be done by default
    }

    /** Compute an individual score, starting at the given offsets
        into the fixed and moving samples.  (One of the offsets will
        always be zero, so you never need to look backwards in the
        data.)
	@param offset_fixed index of the fixed data to start at
	@param offset_moving index of the moving data to start at
	@return score at this position */
    public abstract double compute(int offset_fixed, int offset_moving);

    /** Run the crossdate.  Calls the preamble, and loops to compute
        the scores.
	@see #preamble
	@see #compute */
    public final void run() {
	// this cross was already run, by somebody
	if (finished)
	    return;

	// allocate space for data
        int n = fixed.data.size() + moving.data.size() - 2*OVERLAP;
        if (n < 0) {
            data = new double[0];
            return;
        }
        data = new double[n];
        int done = 0;

	// preamble
	preamble();

	// TODO: run phase1 and phase2 in separate threads, and
	// document that subclasses should keep them independent.
	// (preamble only exists for t-score, and it's probably not
	// easily threadable.)  then benchmark on a dual-g4 to see how
	// well it actually performs.  wait, no: the 2 phases aren't
	// equal-sized; split in half (even-odd?).

	// phase 1:
	for (int im = moving.data.size()-OVERLAP; im>0; im--)
	    data[done++] = compute(0, im);

	// phase 2:
	for (int if_ = 0; if_<fixed.data.size()-OVERLAP; if_++)
	    data[done++] = compute(if_, 0);

	// finish up...
	computeHighScores();
	finished = true;
    }

    /** Run a single crossdate on the (absolutely-dated) samples.
	Member <code>highScores</code> remains empty, and
	<code>finished</code> remains <code>false</code>.  The offsets
	are derived from the dates of the samples, which are assumed
	to be absolutely dated.
	@return the crossdate score between the samples at their saved
	positions */
    public final double single() {
	// preamble
	preamble();

	// extract (calculate) offsets
	int off_f, off_m;
	if (moving.range.getStart().compareTo(fixed.range.getStart()) > 0) {
	    off_f = moving.range.getStart().diff(fixed.range.getStart());
	    off_m = 0;
	} else {
	    off_f = 0;
	    off_m = fixed.range.getStart().diff(moving.range.getStart());
	}

	// return the score
	return compute(off_f, off_m);
    }
}
