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

package edu.cornell.dendro.corina.manip;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.ui.Builder;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Reconcile {
    private Sample s1, s2;

    // to do:
    // - provide buttons for "open a-reading", etc.?
    // - interface to graph?

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

    /**
       Construct a new reconciliation from two given samples.

       @param a the A-reading
       @param c the C-reading
    */
    public Reconcile(Sample a, Sample c) {
        this.s1 = a;
        this.s2 = c;

        // compute total length of first sample
        n = s1.getData().size();

        checkLength();

        // fast-fail here -- DESIGN: is this what i want?
	//        if (s1.data.size() != s2.data.size())
	//            return;

        check3Percent();
        checkTrends();
    }

    /**
       Return a title for this reconciliation.

       @return this reconciliation's title
    */
    public String toString() {
        return "Reconciliation of \"" + s1 + "\" and \"" + s2 + "\""; // DOES THIS EVER GET USED?
    }

    // length
    private int n;

    interface Rule {
	// String toString();
	Icon getIcon();
    }

    List length = new ArrayList();

    class LengthRule implements Rule {
	int n, m;
	public LengthRule(int n, int m) {
	    this.n = n;
	    this.m = m;
	}
	public String toString() {
	    return "Lengths don't match: " + n + " versus " + m;
	}
	public Icon getIcon() {
	    return lengthIcon;
	}
    }

    Icon lengthIcon, trendIcon, percentIcon;
    {
	lengthIcon = Builder.getIcon("bad-length.png");
	trendIcon = Builder.getIcon("bad-trend.png");
	percentIcon = Builder.getIcon("bad-percent.png");
    }

    // check length -- needed?
    // BUG: this isn't the sort of length rule i want to have
    // i want to report _missing_years_
    private void checkLength() {
        int m = s2.getData().size();
	if (m != n)
	    length.add(new LengthRule(n, m));
    }

    // given 2 values, compute the trend between them:
    // decreasing==-1, increasing==+1, stayssame==0
    // -- WHA?  why don't i use trend.java?
    private int trend(int d1, int d2) {
        if (d1 < d2)
            return +1;
        if (d1 > d2)
            return -1;
        return 0;
    }

    List trends = new ArrayList();

    class TrendRule implements Rule {
	private Year y;
	public TrendRule(Year y) {
	    this.y = y;
	}
	public String toString() {
	    return "Differing trend between years " + y + " and " + (y.add(1));
	}
	public Icon getIcon() {
	    return trendIcon;
	}
    }

    // check trends
    private void checkTrends() {

        int w1 = ((Number) s1.getData().get(0)).intValue();
        int w2 = ((Number) s2.getData().get(0)).intValue();
        for (int i=1; i<n; i++) {
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
		trends.add(new TrendRule(s1.getRange().getStart().add(i-1)));
	    }
	}
    }

    List percents = new ArrayList();

    class PercentRule implements Rule {
	Year y;
	int a, c;
	public PercentRule(Year y, int a, int c) {
	    this.y = y;
	    this.a = a;
	    this.c = c;
	}
	public String toString() {
	    return "Differing measurements in year " + y + ": " + a + " versus " + c;
	}
	public Icon getIcon() {
	    return percentIcon;
	}
    }

    // check 3%
    private void check3Percent() {

	for (int i=0; i<n; i++) {
	    // get widths, as floats
	    float w1 = ((Number) s1.getData().get(i)).floatValue();
	    float w2 = ((Number) s2.getData().get(i)).floatValue();

	    // w_min = minimum(w1, w2); w_max = max.
	    float w_min = Math.min(w1, w2);
	    float w_max = Math.max(w1, w2);

	    // threePct = 3% of w_min, rounded up
	    float threePct = (float) Math.ceil(0.03 * w_min); // EXTRACT CONSTANT!

	    // is w_max <= w_min + threePct?
	    if (w_max > w_min + threePct) {
		// report bad 3%
		percents.add(new PercentRule(s1.getRange().getStart().add(i), (int) w1, (int) w2));
	    }
	}
    }

    // (it's fairly easy ot generate an HTML report from the rules, if you really want one.)

    // --------
    // mark both samples as reconciled
    // -- set ;RECONCILED field to Y
    // -- rename to ".REC", if possible
    public void markAsReconciled() throws IOException {
	s1.setMeta("reconciled", "Y");
	s2.setMeta("reconciled", "Y");

	// change each filename to ".rec"
	changeExtension(s1, "REC");
	changeExtension(s2, "REC");

	// BUG: doesn't deal with possible errors:
	// -- file already exists
	// ---- ask user: "blah.rec already exists"?
	// ---- this button shouldn't be enabled in that case
	// -- file can't be saved
	// ---- that's a normal error case (?)
	// ---- no, that doesn't come up here (but it should -- BUG!)
	// -- file doesn't have meta/filename
	// ---- can this ever happen?  sure, create a new sample, then rec it.
	
    }

    // BUG: OpenRecent should get the new filename, then!  (or any time a file
    // is renamed)

    private void changeExtension(Sample s, String ext) throws IOException {
	// -- make .rec filename
	File f = new File((String) s.getMeta("filename"));
	int dot = f.getName().indexOf('.');
	String rec;
	if (dot == -1)
	    rec = f.getName() + "." + ext;
	else
	    rec = f.getName().substring(0, dot) + "." + ext;

	// -- set my filename to that
	s.setMeta("filename", f.getParent() + File.separator + rec);

	// -- rename old file to that file
	f.renameTo(new File(f.getParent() + File.separator + rec));

	// save!
	s.save();
    }
}
