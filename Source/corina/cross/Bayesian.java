package corina.cross;

import corina.Sample;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import java.text.DecimalFormat;

/*
  TODO:
  -- refactor
  -- javadoc
  -- consider splitting into BayesianGenerator(Sampler?) and BayesianConfidence
  ---- how much is shared
  -- should be able to compute this for any score, not just highScores
*/

// -- "dijkstra would not like this" is the understatement of the year.

/*
  if i'm going to be doing this much, the interface of Cross isn't ideal --
  -- it computes the preamble per-crossdate, when it should be only per-file (wasteful)
  -- it computes the high scores list, which i never use (wasteful, but not nearly as much)
  -- probably other things, if i looked
*/

public class Bayesian {

    /*

      if i take random pairs of samples (A,B) -- hold that thought -- ok, i'm
      back.  anyway, to generate a distribution, i can just take 1000 or 10,000
      or some number of random pairs of samples from a folder and do a full
      crossdate run on them, and save that distribution.  more info on how to
      do this is under "filters", below.  basically, i can use the user-mode
      tool i plan to write to generate my first system-mode distributions that
      will ship with corina.

      ----
      filters:

      i should add a filter feature to my bayesian computation, so i can come up
      with different distributions for different preconditions.  the distribution
      of scores for oak might be different than that for pine, for example.
      it would be handy to be able to try these sorts of things.

      better yet, it would be VERY handy if users could play around with these
      sorts of things.  what sort of interface should it have?  Tools -> Crossdating
      Confidence brings up a window that lists available distribution profiles.
      by default, there are 3: t-score, trend, and d-score, each created with (say)
      10,000 random pairs from cornell data, but users can create their own
      (where will they live?  ~/.corina/distributions/ or ~\Corina Preferences\Distributions\,
      i would say.)  at any time, a user can create a new distribution (or delete an
      existing one, except for the builtins), by specifying (1) which algorithm it
      is for, (2) what filter on samples to use (e.g., species matches "quer*"), (3) what
      folder to get random files from (e.g., G:\data\), and (4) how many random pairs to
      get (e.g., 1000).  it then starts building a distribution.  after it's created,
      you can always create a new distribution by clicking a "rebuild" button.  when
      it's selected, it shows a graph of the distribution, with markers showing 90%,
      99%, 99.9%, 99.99%.  each distribution also has a checkbox next to its name,
      with table header "on?".  when you run a crossdate, if the samples you're crossdating
      meet the filter for an enabled distribution, it uses that distribution to compute
      the confidence interval.

      excellent description!  now implement it...

      oh, one last note.  if you have a "t-score, species=quercus" distribution, when you're running
      the t-score on 2 oak samples, it should use that; if your next crossdate is oak
      versus pine, it should fall back to the system t-score distribution.  what if it
      matches 2 distributions you've made, like "t-score, species=quercus" and also
      "t-score, range.end>1950"?  it'll take them in order, then.  in the list you show
      the user, let the rows be dragged around, and indicate that the first distribution
      in the list that matches is the one that'll be used.  the system distributions
      will have a different icon, or be in a different color, and always be at the end
      of the list, and not be draggable.  problem solved!

      ----
      usage:

      make an ant rule "bayes" that computes bayesian stats for a bunch of samples,
      and puts the expected distribution in a file (or files -- one per algorithm)
      that gets included in the jar.  it should never be run by default, because it'll
      take a while to run.

      ----
      file:

      the distribution file might look something like this:

      0.0000 - 0.0001: 23
      0.0001 - 0.0002: 52
      0.0002 - 0.0003: 72
      ...
      or a list of ranges, and numbers of scores seen in that range, one per line.

      using real ranges might be clearer:

      [0.0000, 0.0001): 23
      [0.0001, 0.0002): 52
      [0.0002, 0.0003): 72

      ok, given a distribution (Bayesian.Distribution?), how do i display this?
      as number-of-nines, of course.  if it's <90%, don't display anything;
      if it's <99%, display "90%"; if it's <99.9%, display "99%"; etc.

      ----
      graph:

      if i'm going to display a full-resolution graph for all scores, i ought to use
      the same graph for scores-of-this-crossdate.  in other words, when a user clicks
      on the "histogram" tab of a crossdate, it ought to show a full-resolution graph
      of the score distribution of this crossdate, and also a standard distribution
      (on the same axes), along with the standard 90%,99%,99.9%,... markers on the graph.

      i'll need to rewrite that crossframe tab, perhaps the histogram class, and that
      crossprinter row (and its usage).

      ----
      99%, etc.

      actually, the "traditional" way is to add fives first, then nines.  so the order
      would be 50%, 90%, 95%, 99%, 99.5%, 99.9%, 99.95%, 99.99%.

      ----
      memory

      say you do 1000 pairs -- that's enough for .999 or 99.9% confidence. (note:
      can't do higher, so be sure to [1] not report higher, and [2] tell user
      ahead of time what 99.999...% their chosen number-of-pairs will allow.)
      so for the system distributions, 10,000 pairs is better.  a random (raw) file is
      maybe 1K, so 10,000 pairs is at most 20,000KB = 20MB, and conceivably a lot less
      (because there will be duplicates).  of course, we'll have to traverse the entire
      available directory tree first to count files to ensure randomness, which will
      be the big bottleneck, but once that's done, i can just snarf <20,000 samples into
      memory and run the crosses fairly quickly.

      actually, no, i lied.  for 2 files A and B, the cross of A x B results in
      len(A)+len(B)-2*min_overlap scores.  if they're each 100 years long, i only
      need to do about 1/100 as many pairs as i'd thought i'd needed, or 100 pairs
      for 99.99%.  100 pairs @ 1K ea = 200KB, which is far more reasonable.
      (i'll still run at least 1000 for robustness for the default/system distributions.)

      ----
      generating distribution

      the first step would be to traverse the entire source tree, counting (enumerating?)
      files.  once you know how many exist, pick a fixed number of them randomly.
      if the user wants "100" pairs, pick 200 samples, overlaps allowed (but not for
      samples 2i and 2i+1).  then load all of these into memory (but not duplicates),
      run the crosses, add the results of the crosses into a Distribution object, and
      save it.

      so there are 3 phases: enumerating possible files, picking some random ones and
      loading them, and running the crossdates.  the first two are probably the slowest,
      but benchmark first to see.  in the end, on a typical computer, it should look
      like the progressbar goes smoothly and evenly.  it might be 40% in phase 1, 40%
      in phase 2, and 20% in phase 3.  the text above the progressbar should indicate
      what it's working on, as well: "Looking at possible samples...", "Loading random
      samples...", "Running crossdates...".  (if phase 3 is really so fast, "show graph
      generated so far" might be useless.)

      what sort of performance are we looking at here?  let's see...
    */

    // make inner classes for guis here: confidence manager, new distribution, generating distribution.
    // (much easier after i get lisp->java stuff done.)

    public final static DecimalFormat format = new DecimalFormat("#.##%");

    // ouch!  there's a (slight) catch-22 here: in order to compute the bayesian distribution,
    // you need to compute the crossdate, and in order to compute the crossdate, you need
    // to compute the bayesian distribution.  (fortunately, it's not a fatal error, and it
    // only happens the first time.  but i really should add a flag or something so i can
    // run crossdates without computeHighScores() being called.)

    /*
      
    */
    public static float getSignificance(Cross cross, float score) {
	try {

	    // if distribution for |cross| isn't loaded, load it
	    if (!distros.containsKey(cross.getClass().getName()))
		distros.put(cross.getClass().getName(), new Bayesian(cross.getClass()));

	    // look it up (it's guaranteed to be there now)
	    Bayesian b = (Bayesian) distros.get(cross.getClass().getName());

	    // look up the significance of |score| for |cross|: it's the
	    // confidence of the LARGEST value which is SMALLER than my value
	    int N = b.intervals.length;
	    for (int i=1; i<N; i++)
		if (b.scores[i] > score)
		    return b.intervals[i-1];

	    // it's bigger than anything we've got: take the last one
	    return b.intervals[N-1];

	} catch (IOException ioe) {
	    // bad bad bad! -- actually, since this is part of my jar,
	    // it's a bug, pure and simple.
	    // (fault, missed, blame, responsibility(6)
	    System.out.println("ioe in getSignificance() -- " + ioe);
	    return 0f;
	} catch (NullPointerException npe) {
	    // HACK!  load() fails with this if there's no blah.intervals file,
	    // so we end up here.

	    // no bayesian data -- what to do?  return 0.0;
	    // that will get rendered in the table as a blank cell,
	    // which is exactly what i want.
	    return 0f;
	}
    }

    // keep distributions in memory -- they're so small, i won't worry
    // about keeping them weakly-referenced or something looney like that.
    // this is a (classname => bayesian) map.
    private static Map distros = new HashMap();

    // ------------------------------------------------------------
    // everything above here is class, below is instance ('cept main)
    // ------------------------------------------------------------

    // a (%confidence => score) hash
    private float intervals[]; // a list of floats, like [0.5 0.9 0.95 ...]
    private float scores[]; // a corresponding list of scores, like [0.5 0.5366 0.5499 0.5909 ...]

    // the name(class) of the algorithm
    private String algorithm;

    // constructor: from generateDistribution(), originally
    // constructor: from a jar resource, by Cross, later
    // (need load-from-file, then)
    // (need save-to-file, then)

    // REFACTOR: why doesn't this just load it through Properties?
    public void load() throws IOException {
	String input = algorithm + ".intervals";
	InputStream stream = getClass().getClassLoader().getResourceAsStream(input);
	BufferedReader r = new BufferedReader(new InputStreamReader(stream)); // NULL!
	String line;
	List intBuf = new ArrayList();
	List scoreBuf = new ArrayList();
	while ((line = r.readLine()) != null) { // THIS IS THE ONLY THING THAT CAN THROW AN IOE (?)
	    line = line.trim();
	    if (line.startsWith("#") || line.length()==0)
		continue;
	    int equals = line.indexOf('=');
	    intBuf.add(new Float(line.substring(0, equals)));
	    scoreBuf.add(new Float(line.substring(equals+1)));
	}

	// copy 
	int n = intBuf.size();
	intervals = new float[n];
	scores = new float[n];
	for (int i=0; i<n; i++) {
	    intervals[i] = ((Float) intBuf.get(i)).floatValue();
	    scores[i] = ((Float) scoreBuf.get(i)).floatValue();
	}
    }

    // load an existing bayesian distribution
    public Bayesian(Class algorithm) throws IOException {
	this.algorithm = algorithm.getName();
	load();
    }

    // (no filtering capabilities yet -- that would require, uh, closures to do nicely.  crap.)
    // this pretty much demands threading ability, which means it pretty much needs to be its
    // own class (getProgress, getState, extends Thread), but that can come with the next iteration.
    // FIXME: |algorithm| param can simply be the name (as a string) -- why a class?
    public Bayesian(Class algorithm, String folder, int numberOfPairs) throws IOException {
	// this is done in 3 phases:

	// PHASE 1: enumerate files -- assume there might be 50,000 or so
	File root = new File(folder);
	long t1 = System.currentTimeMillis();
	List filenames = new ArrayList();
	addAllFiles(filenames, root);
	long t2 = System.currentTimeMillis();
	System.out.println("PHASE 1 (enumeration) took " + (t2-t1) + " milliseconds");

	// PHASE 2: pick random files, and load into buffer
	t1 = System.currentTimeMillis();
	Random random = new Random();
	Map samples = new HashMap();
	List pairs = new ArrayList();
	for (int i=0; i<numberOfPairs; i++) {
	    // pick A, and load
	    int A=-1; // compiler is stupid

	    // do
	    //   pick a number [B only: ... that isn't the same as A]
	    //   try to load it
	    // until it's been loaded successfully
	    // then add it to my hash

	    boolean done=false;
	    Sample s=null; // compiler is stupid
	    do {
		try {
		    A = random.nextInt(filenames.size());
		    if (samples.containsKey(filenames.get(A))) {
			done = true;
			break;
		    }
		    s = new Sample((String) filenames.get(A));
		    // next line is skipped if it couldn't be loaded
		    done = true;
		} catch (IOException ioe) {
		    // don't do anything, |done| stays false in this branch
		}
	    } while (!done);
	    if (!samples.containsKey(filenames.get(A)))
		samples.put(filenames.get(A), s);

	    // pick B, distinct from A
	    int B=-1; // compiler is stupid

	    /* boolean */ done = false;
	    //	    Sample s;
	    do {
		try {
		    do {
			B = random.nextInt(filenames.size());
		    } while (B == A); // until B != A
		    if (samples.containsKey(filenames.get(B))) {
			done = true;
			break;
		    }
		    s = new Sample((String) filenames.get(B));
		    // next line is skipped if it couldn't be loaded
		    done = true;
		} catch (IOException ioe) {
		    // don't do anything, |done| stays false in this branch
		}
	    } while (!done);
	    if (!samples.containsKey(filenames.get(B)))
		samples.put(filenames.get(B), s);

	    // refactor -- that's (virtually) the same crap for A and B

	    // add filenames[A]-filenames[B] to list of pairs
	    Pair pair = new Pair((String) filenames.get(A), (String) filenames.get(B));
	    pairs.add(pair);
	}
	filenames = null; // (the compiler+GC probably isn't otherwise smart enough to realize this)
	t2 = System.currentTimeMillis();
	System.out.println("PHASE 2 (load random files) took " + (t2-t1) + " milliseconds");

	// PHASE 3: run crossdates
	t1 = System.currentTimeMillis();
	long total = 0; // total number of scores checked
	List allScores = new ArrayList();
	for (int i=0; i<pairs.size(); i++) {
	    // get samples to crossdate
	    Pair pair = (Pair) pairs.get(i);
	    Sample fixed = (Sample) samples.get(pair.fixed);
	    Sample moving = (Sample) samples.get(pair.moving);

	    // run crossdate
	    // FIXME: use reflection to use |algorithm| as specified
	    Cross c = Cross.makeCross(algorithm.getName(), fixed, moving);
	    c.run();

	    // add all scores
	    int n = c.getRange().span();
	    for (int j=0; j<n; j++) {
		float score = c.getScoreOLD(j);

		// bad value; FIXME: shouldn't inf's count?
		if (Float.isNaN(score) || Float.isInfinite(score))
		    continue;

		allScores.add(new Float(score));
	    }

	    // update total
	    total += n;
	}
	Collections.sort(allScores); // yes, it seems bad, but it's only ~10% as expensive as all the run()s
	t2 = System.currentTimeMillis();
	System.out.println("PHASE 3 (run crosses) took " + (t2-t1) + " milliseconds");

	// BAD ABSTRACTION -- REFACTOR!  Cross.getFormat() shouldn't be abstract, it should
	// be a static method: Cross.getFormat(Class algorithm).  it's ugly, and it makes
	// a smalltalkers cringe, but it's Better Than The Alternatives.

	// store the confidence intervals (in |this|)
	intervals = new float[INTERVALS.length];
	scores = new float[INTERVALS.length];
	for (int i=0; i<INTERVALS.length; i++) { // (loop for i in +intervals+ do ...)
	    // must be increasing: double-check that now
	    if (i>0 && INTERVALS[i]<=INTERVALS[i-1])
		throw new IllegalArgumentException("oops, bug: INTERVALS array must be increasing");

	    // figure out which score to take, and get that value
	    int which = (int) (INTERVALS[i] * allScores.size()); // +/-1, maybe?
	    float score = ((Float) allScores.get(which)).floatValue();

	    // store it
	    intervals[i] = INTERVALS[i];
	    scores[i] = score;
	}
	this.algorithm = algorithm.getName(); // fixme: just the class name, not fqdn
    }

    public void save() throws IOException {
	// open, and write header
	String output = algorithm + ".intervals";
	BufferedWriter w = new BufferedWriter(new FileWriter(output));
	w.write("#");
	w.newLine();
	w.write("# Significance intervals for " + algorithm);
	w.newLine();
	w.write("#");
	w.newLine();

	// write out the intervals
	for (int i=0; i<scores.length; i++) {
	    w.write(intervals[i] + " = " + scores[i]);
	    w.newLine();
	}

	w.close();
    }

    // in theory, you can change these at any time, and it won't break an existing
    // corina that has old stats files.  but they do have to be monotonically increasing
    // (a bug is flagged at runtime if they're not).
    private static final float INTERVALS[] = new float[] {
	0.5f,    0.9f,
	0.95f,   0.99f,
	0.995f,  0.999f,
	0.9995f, 0.9999f,
    };
    // (why can't this be automatically generated?  well,
    // it could be, but it's probably more trouble than it's worth in java.)

    private void addAllFiles(List filenames, File root) {
	File children[] = root.listFiles();
	for (int i=0; i<children.length; i++) {
	    File child = children[i];
	    if (child.isDirectory())
		addAllFiles(filenames, child);
	    else
		filenames.add(child.getPath());
	}
    }

    // lisp programmers, cover your eyes.  yes, this takes 7 lines.
    private static class Pair {
	String fixed, moving;
	Pair(String fixed, String moving) {
	    this.fixed = fixed;
	    this.moving = moving;
	}
    }

    // inputs:
    // -- args[0] = folder
    // -- args[1] = algorithm (optional, default=t-score)
    // -- args[2] = number-of-pairs (optional, default=100)
    public static void main(String args[]) throws Exception /* don't! */ {
	if (args.length<1 || args.length>3) {
	    System.err.println("whoops: i'm expecting 1-3 arguments:");
	    System.err.println("    -- folder to analyze");
	    System.err.println("    -- algorithm to use (optional, default t-score)");
	    System.err.println("    -- number-of-pairs (optional, default 100)");
	    System.err.println("N.B., 1000 (~1mil scores, 99.999%) takes around 2 minutes,");
	    System.err.println("      100 (~100K scores, 99.99%) takes around 20 seconds, and");
	    System.err.println("      10 (~10K scores, 99.9%) takes around 2 seconds");
	    System.exit(1);
	}

	String folder = args[0];

	Class algorithm;
	if (args.length >= 2) {
	    algorithm = Class.forName(args[1]);
	} else {
	    algorithm = Class.forName("corina.cross.TScore");
	}

	int numberOfPairs;
	if (args.length >= 3) {
	    numberOfPairs = Integer.parseInt(args[2]);
	} else {
	    numberOfPairs = 100;
	}

	Bayesian b = new Bayesian(algorithm, folder, numberOfPairs);
	b.save();
    }
}
