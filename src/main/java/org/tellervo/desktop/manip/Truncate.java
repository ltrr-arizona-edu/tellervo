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

package org.tellervo.desktop.manip;


import java.util.List;
import java.util.Stack;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.sample.Sample;

public class Truncate {

    private Sample s;
    public Truncate(Sample s) {
	this.s = s;
    }

    public void cropTo(Range r) {
	// make backup space
	endData = new Stack<Number>();
	endCount= new Stack<Integer>();
	endIncr = new Stack<Integer>();
	endDecr = new Stack<Integer>();
	startData = new Stack<Number>();
	startCount = new Stack<Integer>();
	startIncr = new Stack<Integer>();
	startDecr = new Stack<Integer>();

	// sapwood
	if (s.hasMeta("sapwood")) {
	    int sapwood = ((Number) s.getMeta("sapwood")).intValue();
	    oldSapwood = new Integer(sapwood);
	    sapwood -= s.getRange().getEnd().diff(r.getEnd()); // -= numCropEnd
	    s.setMeta("sapwood", new Integer(sapwood));
	}

	// crop the end
	int numCropEnd = s.getRange().getEnd().diff(r.getEnd());
	while (numCropEnd-- > 0) {
	    int i = s.getRingWidthData().size()-1;
	    endData.push(s.getRingWidthData().remove(i));
	    if (s.hasCount())
		endCount.push(s.getCount().remove(i));
	    if (s.hasWeiserjahre()) {
		endIncr.push(s.getWJIncr().remove(i));
		endDecr.push(s.getWJDecr().remove(i));
	    }
	}

	// crop the start
	int numCropStart = r.getStart().diff(s.getRange().getStart());
	while (numCropStart-- > 0) {
	    startData.push(s.getRingWidthData().remove(0));
	    if (s.getCount() != null && s.getCount().size()>0)
	    {
	    	List<Integer> counts = s.getCount();
	    	int zero = 0;
	    	Integer count = s.getCount().remove(zero);
	    	startCount.push(count);
	    }
	    if (s.hasWeiserjahre() && s.getWJDecr().size()>0) {
		startIncr.push(s.getWJIncr().remove(0));
		startDecr.push(s.getWJDecr().remove(0));
	    }
	}

	// set the new range
	oldRange = s.getRange();
	s.setRange(r);
    }

    // undo
    private Range oldRange; // redundant, but helpful
    // FIXME: i think i only really need one stack, not 8 (though then
    // i probably need cropStart/cropEnd ints)
    private Stack<Number> endData, startData; 
    private Stack<Integer> endCount;
    private Stack<Integer> startIncr, startDecr, endIncr, endDecr;
    private Stack<Integer> startCount;
    private Integer oldSapwood=null;

    public void uncrop() {
	// end
	while (!endData.empty()) {
	    s.getRingWidthData().add(endData.pop());
	    if (s.getCount() != null)
		s.getCount().add(endCount.pop());
	    if (s.hasWeiserjahre()) {
		s.getWJIncr().add(endIncr.pop());
		s.getWJDecr().add(endDecr.pop());
	    }
	}

	// start
	while (!startData.empty()) {
	    s.getRingWidthData().add(0, startData.pop());
	    if (s.hasCount())
		s.getCount().add(0, startCount.pop());
	    if (s.hasWeiserjahre()) {
		s.getWJIncr().add(0, startIncr.pop());
		s.getWJDecr().add(0, startDecr.pop());
	    }
	}

	// sapwood
	if (oldSapwood != null)
	    s.setMeta("sapwood", oldSapwood);

	// restore range
	s.setRange(oldRange);
    }

}
