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

import corina.Range;

    // a significant score.  there's a brilliant refactoring to be
    // done here -- i think it should be usable for tables and grids,
    // as well -- that i'm just not seeing yet.  FIXME.

// currently only used in sigstable -- should be used in table and grid.
// (no, it shouldn't, that would be silly.  no silliness!)

// this should go back to being an inner class of Cross.

// unless you want to make it extend Single...

public class Score {

    public int number; // 0, 1, 2, ... -- ugly, fixme?
    public Range fixedRange;
    public Range movingRange;
    public float score;
    public int span;
    public float confidence;

    public Score(Cross cross, int index, int nr) {
        int movedBy = cross.range.getStart().add(index).diff(cross.moving.range.getEnd());
        fixedRange = cross.fixed.range.redateBy(-movedBy);

        movingRange = cross.moving.range.redateEndTo(cross.range.getStart().add(index));

        score = (float) cross.data[index]; // FIXME:  what?  why's this still doubles?

        span = cross.fixed.range.overlap(movingRange);

	confidence = Bayesian.getSignificance(cross, (float) score); // WRITEME!

        number = nr;
    }

	/* -- something like this later?
	public String toString() {
	    return new DecimalFormat(getFormat()).format(score);
	}
	*/
}
