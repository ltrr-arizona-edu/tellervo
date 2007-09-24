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

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Sample;

import java.util.Stack;

public class Truncate {

    private Sample s;
    public Truncate(Sample s) {
	this.s = s;
    }

    public void cropTo(Range r) {
	// make backup space
	endData = new Stack();
	endCount= new Stack();
	endIncr = new Stack();
	endDecr = new Stack();
	startData = new Stack();
	startCount = new Stack();
	startIncr = new Stack();
	startDecr = new Stack();

	// sapwood
	if (s.meta.containsKey("sapwood")) {
	    int sapwood = ((Number) s.meta.get("sapwood")).intValue();
	    oldSapwood = new Integer(sapwood);
	    sapwood -= s.range.getEnd().diff(r.getEnd()); // -= numCropEnd
	    s.meta.put("sapwood", new Integer(sapwood));
	}

	// crop the end
	int numCropEnd = s.range.getEnd().diff(r.getEnd());
	while (numCropEnd-- > 0) {
	    int i = s.data.size()-1;
	    endData.push(s.data.remove(i));
	    if (s.count != null)
		endCount.push(s.count.remove(i));
	    if (s.hasWeiserjahre()) {
		endIncr.push(s.incr.remove(i));
		endDecr.push(s.decr.remove(i));
	    }
	}

	// crop the start
	int numCropStart = r.getStart().diff(s.range.getStart());
	while (numCropStart-- > 0) {
	    startData.push(s.data.remove(0));
	    if (s.count != null)
		startCount.push(s.count.remove(0));
	    if (s.hasWeiserjahre()) {
		startIncr.push(s.incr.remove(0));
		startDecr.push(s.decr.remove(0));
	    }
	}

	// set the new range
	oldRange = s.range;
	s.range = r;
    }

    // undo
    private Range oldRange; // redundant, but helpful
    // FIXME: i think i only really need one stack, not 8 (though then
    // i probably need cropStart/cropEnd ints)
    private Stack endData, endCount;
    private Stack startIncr, startDecr, endIncr, endDecr;
    private Stack startData, startCount;
    private Integer oldSapwood=null;

    public void uncrop() {
	// end
	while (!endData.empty()) {
	    s.data.add(endData.pop());
	    if (s.count != null)
		s.count.add(endCount.pop());
	    if (s.hasWeiserjahre()) {
		s.incr.add(endIncr.pop());
		s.decr.add(endDecr.pop());
	    }
	}

	// start
	while (!startData.empty()) {
	    s.data.add(0, startData.pop());
	    if (s.count != null)
		s.count.add(0, startCount.pop());
	    if (s.hasWeiserjahre()) {
		s.incr.add(0, startIncr.pop());
		s.decr.add(0, startDecr.pop());
	    }
	}

	// sapwood
	if (oldSapwood != null)
	    s.meta.put("sapwood", oldSapwood);

	// restore range
	s.range = oldRange;
    }

}
