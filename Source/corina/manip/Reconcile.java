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

package corina.manip;

import corina.Sample;

import java.io.File;
import java.io.FileNotFoundException;

/**
   <p>Class for assisting users in reconciling two Samples (actually
   two different measurements of the same sample).</p>

   <p>Two measurements are considered "reconciled" when:</p>

   <ol>

     <li>they are the same length</li>

     <li>all corresponding measurements are within 3% (3% of the
     smaller of the measurments)</li>

     <li>all corresponding trends are the same</li>

   </ol>

   <p>This class will point out whether two samples are reconciled,
   and if not, where they differ.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Reconcile implements Runnable {
    private Sample s1, s2;

    private String report;

    // to do:
    // - store bad length/values/intervals in *private members*
    // - generate html report on demand only, like generateReport(), getReport()
    // - provide alternative interface?  buttons for "open a-reading", etc.?
    // - interface to graph?
    // - print summary?  e.g., "3 bad intervals"

    // given the filename of an A reading, return the filename of the
    // C reading, or vice versa.  the return value is guaranteed to be
    // a file on disk (though not necessarily loadable as a sample).
    // if it's not obvious what the other reading is, throws filenotfoundexception.
    public static String guessOtherReading(String filename) throws FileNotFoundException {
        // assume it's of the form ".../blah/aaa{A,C}.ext".  (this routine
        // fails for filename=".abc")

        // step 1: look for the last dot
        int dot = filename.lastIndexOf('.');

        // step 2: pick out the reading, usu. 'A' or 'C'
        int reading = (dot==-1 ? filename.length()-1 : dot-1);
        char ac = Character.toUpperCase(filename.charAt(reading));

        // step 3: what's the other reading?  [A,B,C,D] => [C,D,A,B]
        switch (ac) {
            case 'A': ac = 'C'; break;
            case 'B': ac = 'D'; break;
            case 'C': ac = 'A'; break;
            case 'D': ac = 'B'; break;
            default: // dunno, give up
                throw new FileNotFoundException();
        }

        // step 4: construct the hypothetical filename
        StringBuffer target = new StringBuffer(filename);
        target.setCharAt(reading, ac);

        // good?
        if (new File(target.toString()).exists())
            return target.toString();

        // try lower-case, just in case
        target.setCharAt(reading, Character.toLowerCase(ac));
        if (new File(target.toString()).exists())
            return target.toString();

        // no?  give up.
        throw new FileNotFoundException();
    }

    /** Construct a new reconciliation from two given samples.
	@param a the A-reading
	@param c the C-reading */
    public Reconcile(Sample a, Sample c) {
        this.s1 = a;
        this.s2 = c;
    }

    /** Return a title for this reconciliation.
        @return this reconciliation's title */
    public String toString() {
        return "Reconciliation of \"" + s1 + "\" and \"" + s2 + "\"";
    }

    /** Return a title for this reconciliation, in HTML.
        @return this reconciliation's title */
    public String toHTML() {
        return "Reconciliation of <i>\"" + s1 + "\"</i> and <i>\"" + s2 + "\"</i>";
    }

    // length
    private int n;

    // record problems in lists [ -- IMPLEMENT ME -- ]
    // private List threePct = new ArrayList();
    // private List trends = new ArrayList();

    /** Run the reconciliation.  This method compares the lengths,
        checks each value for 3% precision, and makes sure all the
        trends agree. */
    public void run() {
	// start report
	report = new String();
	report += "<html><body><h3>" + toHTML() + "</h3><ol>"; // <h1> is too big

	// compute total length of first sample
	n = s1.data.size();

	report += checkLength();

        // fast-fail here -- UGLY, DUPLICATE CODE
        if (s1.data.size() != s2.data.size())
            return;

        report += check3Percent();
        report += checkTrends();
    }

    // check length -- needed?
    private String checkLength() {
        String report = "<li><b>Length: </b>";
        int m = s2.data.size();
        if (m != n)
            report += "<font color=\"red\">fail</font>, do not match: " + n + " versus " + m;
        else
            report += "<font color=\"green\">pass</font>, both are n=" + n;

        return report;
    }

    // given 2 values, compute the trend between them:
    // decreasing==-1, increasing==+1, stayssame==0
    private int trend(int d1, int d2) {
        if (d1 < d2)
            return +1;
        if (d1 > d2)
            return -1;
        return 0;
    }

    // check trends
    private String checkTrends() {
	String report = "<li><b>Trends: </b>";
	boolean bad = false;
	int w1 = ((Number) s1.data.get(0)).intValue();
	int w2 = ((Number) s2.data.get(0)).intValue();
	for (int i=1; i<n; i++) {
	    // store widths/"previous"
	    int w1p = w1;
	    int w2p = w2;

	    // get next year's widths
	    w1 = ((Number) s1.data.get(i)).intValue();
	    w2 = ((Number) s2.data.get(i)).intValue();

	    // compute trends -- possible (desireable) to use cross.Trend here?
	    int trend1 = trend(w1p, w1);
	    int trend2 = trend(w2p, w2);

	    // compare trends -- "with trends like these, who needs ..."
	    if (trend1 != trend2) {
		// first bad trend: start list
		if (!bad)
		    report += "<ul>";

		// report bad trend
		report += "<li><font color=\"red\">fail</font>, differing trend between years " +
		    s1.range.getStart().add(i-1) +
		    " and " +
		    s1.range.getStart().add(i);

		// now you've done it!
		bad = true;
	    }
	}
	if (!bad)
	    report += "<font color=\"green\">pass</font>, all trends match";
	else
	    report += "</ul>";

	return report;
    }

    // check 3%
    private String check3Percent() {
	String report = "<li><b>3%-rule: </b>";
	boolean bad = false;
	for (int i=0; i<n; i++) {
	    // get widths, as doubles
	    double w1 = ((Number) s1.data.get(i)).doubleValue();
	    double w2 = ((Number) s2.data.get(i)).doubleValue();

	    // w_min = minimum(w1, w2); w_max = max.
	    double w_min = Math.min(w1, w2);
	    double w_max = Math.max(w1, w2);

	    // threePct = 3% of w_min, rounded up
	    double threePct = Math.ceil(0.03 * w_min);

	    // is w_max <= w_min + threePct?
	    if (w_max > w_min + threePct) {
		// first bad 3%: start list
		if (!bad)
		    report += "<ul>";

		// report bad 3%
		report += "<li><font color=\"red\">fail</font>, differing measurements in year " +
		    s1.range.getStart().add(i) + ": " +
		    (int) w1 + " versus " + (int) w2;

		// now you've done it!
		bad = true;
	    }
	}
	if (!bad)
	    report += "<font color=\"green\">pass</font>, all within 3%";
	else
	    report += "</ul>";

	return report;
    }

    /** Return the report, which is an HTML summary of differences
	between these samples.
	@return the report */
    public String getReport() {
        return report;
    }
}
