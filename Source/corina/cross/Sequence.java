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

import corina.Sample;
import corina.Element;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Sequence {

    // lists of strings (filenames)
    private List fixed = new ArrayList();
    private List moving = new ArrayList();

    // list of pairings
    private class Pairing {
	int f, m;
	Pairing (int f, int m) {
	    this.f = f;
	    this.m = m;
	}
    }
    private List pairings = new ArrayList();

    // i *could* use 2 lists (fixed_pairings, moving_pairings), but
    // i'd have to stuff them with Integers, so it's not really worth
    // it.

    // pointer into pairing list
    private int i=0;

    // inputs: lists of filenames (String) or elements (Element).
    public Sequence(List f, List m) {
	// copy active elements to filenames
	for (int i=0; i<f.size(); i++)
	    if (f.get(i) instanceof String)
		fixed.add(f.get(i));
	    else if (((Element) f.get(i)).isActive())
		fixed.add(((Element) f.get(i)).getFilename());
	for (int i=0; i<m.size(); i++)
	    if (m.get(i) instanceof String)
		moving.add(m.get(i));
	    else if (((Element) m.get(i)).isActive())
		moving.add(((Element) m.get(i)).getFilename());

	// a hash of "sample1:sample2", only used to keep track of
	// what crosses have been recorded so far.
	Set used = new HashSet();

	for (int i=0; i<fixed.size(); i++) {
	    for (int j=0; j<moving.size(); j++) {
		// use filenames for hashing
		String s1 = (String) fixed.get(i);
		String s2 = (String) moving.get(j);

		// crossing against myself?
		if (s1.equals(s2))
		    continue;

		// already used?  fuggedaboutit!
		if (used.contains(s1 + File.pathSeparator + s2) ||
		    used.contains(s2 + File.pathSeparator + s1))
		    continue;

		// it's a good cross, add it
		used.add(s1 + File.pathSeparator + s2);
		pairings.add(new Pairing(i, j));
	    }
	}

	// the hash was just for weeding out duplicates, and isn't
	// needed any more.  it gets GC'd now.
    }

    // these used to be the public interface to Sequence.  they're too
    // low-level for that now, but they're still used by getCross(),
    // and there's no reason to get rid of them.
    private Sample getFixed() throws IOException {
	Pairing p = (Pairing) pairings.get(i);
	String fn = (String) fixed.get(p.f);
	return new Sample(fn);
    }
    private Sample getMoving() throws IOException {
	Pairing p = (Pairing) pairings.get(i);
	String fn = (String) moving.get(p.m);
	return new Sample(fn);
    }

    // cross
    public Cross getCross() {
	try {
	    switch (crossNr) {
	    case 0: return new TScore(getFixed(), getMoving());
	    case 1: return new Trend(getFixed(), getMoving());
	    case 2: return new DScore(getFixed(), getMoving()); // inefficient, but what the hey...
		// if you add/remove a case here, change NUM_ALGS
	    default: return null; // never happens
	    }
	} catch (IOException ioe) {
	    return null; // eep
	}
    }

    private int crossNr = 0;
    private static final int NUM_ALGS = 3; // there are this many cases in getCross()

    public void nextPairing() {
	if (crossNr < NUM_ALGS-1) {
	    crossNr++;
	} else if (i < pairings.size()-1) {
	    i++;
	    crossNr = 0;
	}
    }
    public void prevPairing() {
	if (crossNr > 0) {
	    crossNr--;
	} else if (i > 0) {
	    i--;
	    crossNr = NUM_ALGS-1;
	}
    }

    public boolean isFirst() {
	return (i==0 && crossNr==0);
    }
    public boolean isLast() {
	return (i==pairings.size()-1 && crossNr==NUM_ALGS-1);
    }

}
