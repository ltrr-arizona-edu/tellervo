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
    public static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

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
        its value is 15. */
    public static int getMinimumOverlap() {
        return Integer.getInteger("corina.cross.overlap", 15).intValue();
    }

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
        int overlap = getMinimumOverlap();
        Year start = fixed.range.getStart().add(overlap - 1);
        Year end = fixed.range.getEnd().add(moving.range.span() - overlap - 1);
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
        // BROKEN
        // -- i think i see what i was trying to do, but i fucked it up.  d'oh.
        String f = (String) fixed.meta.get("filename") + " (" + fixed.range + ")";
        String m = (String) moving.meta.get("filename") + " (" + moving.range + ")";

        f = (new File(f)).getName();
        m = (new File(m)).getName();

        return msg.getString("cross") + ": " + f + " " + msg.getString("versus") + " " + m;
        // was: return getName() + ": " + f + " versus " + m;
    }

    // search for high scores in the list, after scores have been
    // computed.  run() calls this last.
    private void computeHighScores() {
        int nr=0;
        Range fixedRange = fixed.range;
        Range movingRange = moving.range.redateEndTo(fixedRange.getStart().add(getMinimumOverlap()-1));
        for (int i=0; i<data.length; i++) {
            movingRange = movingRange.redateBy(+1); // slide it by 1
            if (isSignificant(data[i], fixedRange.overlap(movingRange)))
		try {
		    highScores.add(new Score(this, i, ++nr));
		} catch (Exception e) {
		    System.out.println("trouble with bayes!");
		}
        }
        // convert to array now?  all i do with it is .size(), .get(), and sort(comparable)
    }

    public abstract boolean isSignificant(double score, int overlap);

    // temporary hack -- used only by crossprinter and crossframe(all-scores)
    public abstract double getMinimumSignificant();

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

        // careful: it can be true that n>0 but there are 0 crosses that should be run here.
        // try this...
        int overlap = getMinimumOverlap();
        if (fixed.data.size() < overlap || moving.data.size() < overlap) {
	    throw new IllegalArgumentException("These samples (n=" + fixed.data.size() + ", n=" +
					       moving.data.size() + ") aren't long enough for your " +
					       "minimum specified overlap (n=" + overlap + ")");
	    // and say how to fix it!
        }

        // allocate space for data
        int n = fixed.data.size() + moving.data.size() - 2*getMinimumOverlap();
        if (n <= 0) {
            data = new double[0];
            finished = true;
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
        // equal-sized; split in half (maybe even-odd?).

        // phase 1:
        for (int i=moving.data.size()-getMinimumOverlap(); i>0; i--)
            data[done++] = compute(0, i);

        // phase 2:
        for (int i=0; i<fixed.data.size()-getMinimumOverlap(); i++)
            data[done++] = compute(i, 0);

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
    public float single() { // overridden by d-score, but only as a hack!
        // preamble
        preamble();

        // temps
        Year fixedStart = fixed.range.getStart();
        Year movingStart = moving.range.getStart();

        // calculate offsets
        int offset_fixed, offset_moving;
        if (movingStart.compareTo(fixedStart) > 0) {
	    // moving starts later, use an offset on the fixed sample
            offset_fixed = movingStart.diff(fixedStart);
            offset_moving = 0;
        } else {
	    // fixed starts later, use an offset on the moving sample
            offset_fixed = 0;
            offset_moving = fixedStart.diff(movingStart);
        }

        // return the score
        return (float) compute(offset_fixed, offset_moving);
    }
}
