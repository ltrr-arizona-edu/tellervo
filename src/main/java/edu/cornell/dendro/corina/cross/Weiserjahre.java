/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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

package edu.cornell.dendro.corina.cross;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

public class Weiserjahre extends Cross {

    // don't use me -- for getName() only -- HACK!
    Weiserjahre() { }

    // stupid, stupid -- DESIGN: make a factory?
    public Weiserjahre(Sample fixed, Sample moving) {
    	super(fixed, moving);
    }

	public String getFormat() {
        // FIXME: this should be a pref, as well
        return App.prefs.getPref(PrefKey.STATS_FORMAT_WEISERJAHRE, "0.0%") +
        	"of 0000";
    }

    // same as trend?  (this is old-style trend, even -- very obsolete!)
    @Override
	public boolean isSignificant(float score, int overlap) {
        return score > 0.65f;
	// WRITEME: it's more sophisticated than this, i think
    }
    @Override
	public float getMinimumSignificant() {
        return 0.65f;
    }

	public String getName() {
	return I18n.getText("statistics.weiserjahre");
    }

	public static String getNameStatic() {
		return new Weiserjahre().getName();
	}
	
	public static String getFormatStatic() {
		return new Weiserjahre().getFormat();
	}

	
    private int signifigantcount = 0;

    // returns the number of signifigant intervals of the last compute()
    @Override
	public int getSignifigant() {
    	return signifigantcount;
    }
    
    @Override
	public float compute(int offset_fixed, int offset_moving) {
	// value = (# trends synchronous with signature years) /
	//         (# signature years in overlap)

	// need these fields, or can't compute weiserjahre cross
	if (getFixed().getCount() == null || getFixed().getWJIncr() == null) {
	    String problem = "The fixed sample must be a sum,\n" +
		"with count and Weiserjahre data,\n" +
		"to run a WJ cross.";
	    throw new IllegalArgumentException(problem);
	}

	int i = offset_fixed;
	int j = offset_moving;

	int synchroTrends = 0;
	int totalSigs = 0;

	// do magic in here
	while (i<getFixed().getData().size()-1 && j<getMoving().getData().size()-1) {

	    // number of samples: need n>3
	    int n = (getFixed().getCount().get(i)).intValue();

	    // fraction with increasing trend: need pct<25% OR pct>75%
	    double pct = ((Number) getFixed().getWJIncr().get(i)).doubleValue() / n;

	    // signature year?  (j==0 is bad, too)
	    if (n>3 && (pct<=0.25 || pct>=0.75) && j>0) { // REFACTOR: use Weiserjahre.isSignificant() here somehow?
		// count it
		totalSigs++;

		// i'm ASSUMING that the trend of the fixed sample is
		// the trend of the majority of the elements, NOT the
		// trend of the sum.  if i'm backwards, it's easy
		// enough to fix: copy "compute moving trend", and
		// replace s/moving/fixed/ and s/j/i/

		// compute fixed trend
		int fixedTrend = 0;
		if (pct <= 0.25)
		    fixedTrend = -1;
		else if (pct >= 0.75)
		    fixedTrend = +1;

		// compute moving trend
		int movingTrend = 0;
		if (((Number) getMoving().getData().get(j-1)).intValue() < ((Number) getMoving().getData().get(j)).intValue())
		    movingTrend = +1;
		else if (((Number) getMoving().getData().get(j-1)).intValue() > ((Number) getMoving().getData().get(j)).intValue())
		    movingTrend = -1;

		// do they match?
		if (fixedTrend == movingTrend)
		    synchroTrends++;
	    }

	    // next one
	    i++;
	    j++;
	}

		signifigantcount = totalSigs;
		
        // if there were no significant intervals, call it 0.0.
        if (totalSigs == 0)
            return 0;

	return (float) synchroTrends / (float) totalSigs;
    }
}
