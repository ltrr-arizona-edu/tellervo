/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.cross;

import java.io.File;
import java.lang.reflect.Constructor;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;

/**
   Abstract class representing a crossdating algorithm, and its scores.

   WRITEME: documentation should be organized as:
   -- introduction/overview
   -- to use this class (run a crossdate, extract results)
   -- to write a new crossdate (subclasses)
   -- prefs i use (the other inputs)
   -- events i watch; events views should watch

   <p>The data array holds the crossdate scores; the range holds the
   range of end dates for the moving sample.  After construction, the
   run() method must be called; making this a great candidate for
   running in a thread (hint, hint).</p>

   <p>To run a crossdate... WRITEME</p>

   <p>Subclasses must implement... WRITEME</p>

   <h2>Left to do</h2>
   <ul>
     <li>rename to "Crossdate"
     <li>make highScores an iterator (getHighScores()) instead of a public list
         -- but i need random access for the table model!  what to do?
     <li>use Prefs instead of Integer.getInteger()
     <li>get rid of Cross(){} c'tor -- needs other work...
     <li>get rid of preamble()?
     <li>make run() threaded so it's faster on dual-processor systems?
     <li>isFinished() and the threading stuff needs cleaning up
     <li>compute all scores (overlap=1 and up), and only show requested ones;
         idea: 1 = remove all minimum-overlap code, 2 = add new-style overlap
	 -- "high scores" list computes all; sig scores list should
	    only display those with len>min
	 -- "all scores" table shows all; should only show with len>min
	 -- "histo" list shows all; should only show with len>min
	    (is this harder?  need to recompute histogram each time?)
	 -- overlap bug: in last crossdate score, moving has overlap=2!

     <li>instead of the old getMinimumOverlap() method, use
         Prefs.getPref("tellervo.cross.overlap"); default value is 15
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class Cross implements Runnable {
	/** The crossdate scores. */
	private float data[];
	private int signifigance[];

	/**
	 * Get a crossdate score.
	 * 
	 * Used only inside cross implementation by other crosses
	 * 
	 * @param index
	 *            the index of the score to get
	 * @return the score at that index
	 */
	protected float getScore(int index) {
		return data[index];
	}

	/**
	 * Get a crossdate score.
	 * 
	 * TODO -- (what if you want data.length? use getRange().span().)
	 * 
	 * @param year
	 *            the end year of the moving sample
	 * @return the score at that position
	 */
	public float getScore(Year year) {
		int index = year.diff(range.getStart());
		return data[index];
	}

	/**
	 * Get a crossdate score signifigance.
	 * 
	 * @param year
	 *            the end year of the moving sample
	 * @return the score signifigance at that position
	 */
	public int getScoreSignifigance(Year year) {
		int index = year.diff(range.getStart());
		return signifigance[index];
	}

	/**
	 * The cross's range, which is the range of end-years the moving sample can
	 * be placed at.
	 * 
	 * @return the span of years the moving sample can end at
	 */
	public Range getRange() {
		return range;
	}

	private Range range;

	/*
	 * IDEA: -- getRange() operates old-style, i.e., (range.start+overlap,
	 * range.end-overlap) -- instead of public float data[] (which is bad
	 * anyway), make it getScore(int index) and/or getScore(Year year). this
	 * automatically takes into account the overlap, and requires no changes in
	 * AllScoresView.
	 */

	/*
	 * IDEAS: -- deprecate getScore(int) -- write getScore(Year) -- (it'll work
	 * great with DecadalModel(?)) -- Crossdate takes care of overlap; views
	 * don't need to -- watch for events: -- fixed.title changes, change text --
	 * fixed.data edited, re-run crossdate -- fixed.range changes, range changes
	 * (but scores don't!) -- moving.title changes, change text -- moving.data
	 * edited, re-run crossdate -- (moving.range changes, i don't care!)
	 */

	/*
	 * IDEA: (but not a good one) -- a crossdate is different than an algorithm
	 * -- what i kind of want is cross.Algorithm to be an interface -- (no,
	 * abstract class, if it needs an output array) -- it defines cross on Data
	 * x Data (each a List of Numbers) -- Crossdate uses Algorithm, and creates
	 * them, but isn't one -- does that work? -- (i'll need to use Data first)
	 * -- no, problem: some crossdates might need other Datas, like WJ.
	 */

	/*
	 * another IDEA:
	 * 
	 * -- instead of an abstract class, this might be better off as an interface
	 * - extends Runnable - with a couple of public accessor methods. I can
	 * still provide an abstract class that implements this interface...)
	 */

	// reference to the sample being held fixed
	private Sample fixed;

	/**
	 * Returns the fixed sample.
	 * 
	 * @return the fixed sample
	 */
	public Sample getFixed() {
		return fixed;
	}

	// reference to the sample being moved to compute the vector of
	// scores
	private Sample moving;

	/**
	 * Returns the moving sample.
	 * 
	 * @return the moving sample
	 */
	public Sample getMoving() {
		return moving;
	}

	// set to true just before the run() method returns. to be used
	// to determine if a cross has been run, or merely been
	// constructed
	private boolean finished = false;

	/**
	 * Is the run finished?
	 * 
	 * @return true, if the crossdate is finished being run
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * A vector of high (statistically significant) scores. A vector of Integer
	 * indices into the data. The (private) method computeHighScores() computes
	 * this, and should be called in run() after the scores have been computed,
	 * but before <code>finished</code> is set.
	 */
	// public List highScores = new ArrayList();
	// TODO: make this private, write a getHighScores() method,
	// switch everything to use that, then remove this list
	// and switch getHighScores() to use the topScores object.
	// WRITEME: document this method!
	public TopScores getHighScores() {
		return topScores;
	}

	private TopScores topScores;

	/**
	 * Returns the minimum overlap
	 * 
	 * This is done by calling getprefs. Done in a method so it can be
	 * overridden by derived classes.
	 * 
	 * @return the minimum overlap.
	 */

	public int getOverlap() {
		return App.prefs.getIntPref(PrefKey.STATS_OVERLAP_REQUIRED, 15);
	}

	/**
	 * Returns the number of signifigant samples.
	 * 
	 * Useful only now for Weiserjahre.
	 * 
	 * @return the number of signifigant samples
	 */

	public int getSignifigant() {
		return 0;
	}

	// don't use me
	protected Cross() {
	}

	// FIXME: then why is this here?
	// -- for some object method that should be a class method, i think.
	// then you should just fix that. yeah, i should. all right, then!
	// -- Single.java, TableView.java -- "new Trend().getFormat()", etc.

	/**
	 * Create a crossdate, given fixed and moving samples.
	 * 
	 * @param fixed
	 *            sample to hold fixed
	 * @param moving
	 *            sample to compare against the fixed sample
	 */
	public Cross(Sample fixed, Sample moving) {
		// copy references to fixed, moving
		this.fixed = fixed;
		this.moving = moving;

		// set the range (= end date of moving sample)
		Year start = fixed.getRange().getStart();
		Year end = fixed.getRange().getEnd().add(moving.getRange().span() - 1);

		// TESTING: shrink it by 14 _more_, for min overlap = 15
		// start = start.add(14);
		// end = end.add(-14);

		range = new Range(start, end);
	}

	/**
	 * Return a human-readable name for the crossdate.
	 * 
	 * @return a user-readable name for this type of cross
	 */
	public abstract String getName();

	/**
	 * Return a format string for this cross to be used with DecimalFormat.
	 * 
	 * @return a format string for this cross
	 */
	public abstract String getFormat();

	/**
	 * Return the cross as a String, in the format "Cross: Sample A versus
	 * Sample B".
	 * 
	 * @return a String value of the cross
	 */
	@Override
	public final String toString() {
		// get filenames of both samples
		String f = fixed.getDisplayTitle();
		String m = moving.getDisplayTitle();

		// remove folder names
		f = (new File(f)).getName();
		m = (new File(m)).getName();

		// add range to string
		f = f + " (" + fixed.getRange() + ")";
		m = m + " (" + moving.getRange() + ")";

		// make it in the form "Cross: %1 versus %2"
		String result = I18n.getText("cross") + ": " + f + " "
				+ I18n.getText("versus") + " " + m;
		return result;
	}

	/*
	 * IDEA: make Crossdate and Algorithm separate classes
	 * 
	 * -- package org.tellervo.desktop.cross.algorithms -- Algorithm has
	 * just compute(int,int), isSig(float,int) -- name and format aren't really
	 * coded -- put in prefs defaults? i18n? -- (also need: letter -- "t", "D",
	 * etc.) -- rationale: Algorithm is optimized for simplicity to implementors
	 * -- (make Trend, etc., as easy as possible to read/write)
	 * 
	 * -- Crossdate holds results, runs crossdates, synchro, fixed/moving,
	 * run/single -- rationale: Crossdate is optimized for simplicity to users
	 * -- (make Crossdate as easy as possible to use; it adapts Algorithms for
	 * use)
	 */

	/**
	 * Is a score considered significant, on the specified overlap?
	 * 
	 * @param score
	 *            the score to check
	 * @param overlap
	 *            the overlap which generated this score
	 * @return true, if this score is significant on this overlap
	 */
	public abstract boolean isSignificant(float score, int overlap);

	/**
	 * @deprecated This method ignores the overlap, which it shouldn't do. Use
	 *             isSignificant() instead.
	 */
	@Deprecated
	public abstract float getMinimumSignificant();

	// temporary hack -- used only by crossprinter and crossframe(all-scores)

	/**
	 * Crossdate preamble: any setup that needs to be done before the main loop
	 * to compute individual scores.
	 * 
	 * ----deprecated the preamble was always kind of silly
	 * Not really - it's nice to separate this stuff!
	 */
	protected void preamble() {
		// nothing need be done by default

		// DELETE ME!
	}

	/**
	 * Compute an individual score, starting at the given offsets into the fixed
	 * and moving samples. (One of the offsets will always be zero, so you never
	 * need to look backwards in the data.)
	 * 
	 * @param offset_fixed
	 *            index of the fixed data to start at
	 * @param offset_moving
	 *            index of the moving data to start at
	 * @return score at this position
	 */
	public abstract float compute(int offset_fixed, int offset_moving);

	/**
	 * Run the crossdate. Calls the preamble, and loops to compute the scores.
	 * 
	 * @see #preamble
	 * @see #compute
	 */
	public final void run() {
		// this cross was already run, by somebody
		if (finished)
			return;

		// careful: it can be true that n>0 but there are 0 crosses
		// that should be run here. check first:
		int overlap = getOverlap(); //App.prefs.getIntPref("tellervo.cross.overlap"
									// , 15);
		if (fixed.getRingWidthData().size() < overlap
				|| moving.getRingWidthData().size() < overlap) {
			String problem = "These samples (n=" + fixed.getRingWidthData().size()
					+ ", " + "n=" + moving.getRingWidthData().size()
					+ ") aren't long enough for "
					+ "your minimum specified overlap (n=" + overlap + ")";
			// and say how to fix it!
			throw new IllegalArgumentException(problem);
		}

		// allocate space for data
		int n = fixed.getRingWidthData().size() + moving.getRingWidthData().size() - 1; // was:
																		// ...
																		// -2*
																		// getMinimumOverlap
																		// ();
		if (n <= 0) {
			data = new float[0];
			signifigance = new int[0];
			finished = true;
			return;
		}
		data = new float[n];
		signifigance = new int[n];
		int done = 0;

		// preamble
		preamble();

		// TODO: run phase1 and phase2 in separate threads, and
		// document that subclasses should keep them independent.
		// (preamble only exists for t-score, and it's probably not
		// easily threadable.) then benchmark on a dual-g4 to see how
		// well it actually performs. wait, no: the 2 phases aren't
		// equal-sized; split in half (maybe even-odd?).

		// phase 1:
		for (int i = moving.getRingWidthData().size() - 1 /* getMinimumOverlap() */; i > 0; i--) {
			data[done] = compute(0, i);
			signifigance[done++] = this.getSignifigant();
		}

		// phase 2:
		for (int i = 0; i < fixed.getRingWidthData().size() - 1 /* getMinimumOverlap() */; i++) {
			data[done] = compute(i, 0);
			signifigance[done++] = this.getSignifigant();
		}

		// finish up...
		topScores = new TopScores(this);
		finished = true;
	}

	/**
	 * Run a single crossdate on the (absolutely-dated) samples. Member
	 * <code>highScores</code> remains empty, and <code>finished</code> remains
	 * <code>false</code>. The offsets are derived from the dates of the
	 * samples, which are assumed to be absolutely dated.
	 * 
	 * @return the crossdate score between the samples at their saved positions
	 */
	public float single() { // overridden by d-score, but only as a hack!
		// preamble
		preamble();

		// temps
		Year fixedStart = fixed.getRange().getStart();
		Year movingStart = moving.getRange().getStart();

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
		return compute(offset_fixed, offset_moving);
	}

	/**
	 * Factory method: make a new crossdate, using a specified algorithm,
	 * between the given fixed and moving samples. The algorithm is given as the
	 * fully-qualified name of the class (subclass of Cross) to use. Neither
	 * sample is modified. The crossdate is created, but not run. If the class
	 * can't be loaded, or it isn't a Cross subclass, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param algorithm
	 *            the class name of the crossdate to use, like
	 *            "tellervo.cross.TScore"
	 * @param fixed
	 *            the fixed sample
	 * @param moving
	 *            the moving sample
	 * @return the new crossdate, ready to be run
	 * @exception IllegalArgumentException
	 *                if the class can't be found, it isn't a Cross subclass, or
	 *                there's an exception thrown when instantiating it
	 */
	@SuppressWarnings("unchecked")
	public static Cross makeCross(String algorithm, Sample fixed, Sample moving)
			throws IllegalArgumentException {

		try {
			Class<Cross> c = (Class<Cross>) Class.forName(algorithm);
			Constructor<Cross> cons = c.getConstructor(new Class[] { Sample.class,
					Sample.class });
			Cross x = cons.newInstance(new Object[] { fixed, moving });
			return x;
		} catch (Exception e) {
			// possible exceptions that would dump you down here:
			// -- ClassNotFoundException (forName())
			// -- NoSuchMethodException (getConstructor())
			// -- InstantiationException,
			// IllegalAccessException,
			// InvocationTargetException (newInstance())
			throw new IllegalArgumentException("Cross.makeCross(): "
					+ algorithm + " is not a valid crossdate " + "("
					+ e.getMessage() + ")");
		}
	}

	/**
	 * All of the classes that implement Cross. (It is assumed that this list is
	 * <b>not</b> empty.) The order here is the order used whenever they're
	 * presented to the user: T-score, R-value, trend, D-score, weiserjahre.
	 */
	public final static String ALL_CROSSDATES[] = new String[] {
			"org.tellervo.desktop.cross.TScore",
			"org.tellervo.desktop.cross.RValue",
			"org.tellervo.desktop.cross.Trend",
			"org.tellervo.desktop.cross.DScore",
			"org.tellervo.desktop.cross.Weiserjahre",
	// -> add new crossdates here
	};

	/**
	 * The crossdates that the user will want to see, usually. Used as defaults
	 * for sequences, tables, and grids. (It is assumed that this list is a
	 * proper subset of ALL_CROSSDATES; it should not be empty. The order of
	 * entries does not matter: the order of ALL_CROSSDATES is always used.)
	 */
	public final static String DEFAULT_CROSSDATES[] = new String[] {
			"org.tellervo.desktop.cross.TScore",
			"org.tellervo.desktop.cross.RValue",
			"org.tellervo.desktop.cross.Trend",
			"org.tellervo.desktop.cross.Weiserjahre", };
}
