package corina.cross;

import corina.Sample;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteNotFoundException;

import java.util.ResourceBundle;

// a single crossdate between two samples, where they are.
// used by Grid, Table, and anybody else who wants to see if samples crossdate.

public class Single {
    // the scores themselves
    /*private*/ float t, tr, d;

    // the overlap
    /*private*/ int n;

    // distance between sites, in km, or null if unknown
    /*private*/ Integer dist;

    // is it significant?
    /*private*/ boolean isSig;

    // run a single crossdate between 2 samples
    public Single(Sample fixed, Sample moving) {
	// fill in crosses, if they overlap
	n = fixed.range.overlap(moving.range);
	if (n > 0) {
	    // this use of single() is kind of hackish.  since it's only used here, it should be REFACTORED.
	    TScore t_cross = new TScore(fixed, moving);
	    t = t_cross.single();
	    Trend tr_cross = new Trend(fixed, moving);
	    tr = tr_cross.single();
	    DScore d_cross = new DScore(t_cross, tr_cross);
	    d = d_cross.single();
	} else {
	    t = tr = d = 0.0f; // right?
	}

	// distance
	try {
	    Site s1 = SiteDB.getSiteDB().getSite(fixed);
	    Site s2 = SiteDB.getSiteDB().getSite(moving);
	    dist = new Integer(s1.distanceTo(s2));
	} catch (SiteNotFoundException snfe) {
	    dist = null;
	}

	// is it significant?  use the t-score to check.
	isSig = new TScore().isSignificant(t, n);
    }

    // -- FOR BACKWARDS COMPATIBILITY ONLY -- REFACTOR AND REMOVE ME -- ??
    public Single(double t, double tr, double d, int n) {
	this.t = (float) t;
	this.tr = (float) tr;
	this.d = (float) d;
	this.n = n;
	isSig = new TScore().isSignificant(t, n);
    }

    public boolean isSignificant() {
	return isSig;
    }

    // as "x km", localized, or the empty string if unknown(null)
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");
    public String distanceAsString() {
	return (dist==null ? "" : dist+" "+msg.getString("km"));
    }
}
