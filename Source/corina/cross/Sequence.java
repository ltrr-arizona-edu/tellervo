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

import java.lang.reflect.Constructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Sequence {
    // lists of strings (filenames)
    private List fixed = new ArrayList();
    private List moving = new ArrayList();

    List getAllFixed() {
        return fixed;
    }
    List getAllMoving() {
        return moving;
    }

    // FIXME: no reason for "fixed" and "moving" to be separate.
    // pairings are just indices into one or the other...  consolidate.
    // (this'll save more memory when i pre-load the samples.)
    // OTOH, the new interface will display "fixed" and "moving"
    // samples.  hmm...

    // SOLUTION:
    // List samples; // of Element
    // List fixed, moving; // of Integer (indices)
    // List pairings; // of Pairing (indices)
    
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
    // i'd have to stuff them with Integers, so it's not really worth it.

    // the current cross (pointer into pairing list)
    private int i=0;

    // inputs: lists of filenames (String) or elements (Element).
    // -- BUG: you can't crossdate unsaved samples (filename=null) with this.
    // -- (I should accept plain ol' Samples, too, then.)
    public Sequence(List f, List m) {
        /*
         SOLUTION:
         -- also accept Samples in the list
         -- copy filenames as now
         -- if a sample's filename is null, use "" (which isn't a valid filename)
         -- waitaminute, who uses this, anyway?

         WORKING HERE.
         */
        
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

        // special case: some idiot is trying to crossdate exactly one file
        // against itself.  ok, whatever...
        if (fixed.size()==1 && moving.size()==1 && fixed.get(0).equals(moving.get(0))) {
            pairings.add(new Pairing(0, 0));
            return;
        }

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
    // EXCEPT THEY'RE UNNECESSARY AND HORRIBLY INEFFICIENT.  SUCK!
    // (5-50ms per load(), for small local files)
    private Sample getFixed() throws IOException {
        Pairing p = (Pairing) pairings.get(i);
        String fn = (String) fixed.get(p.f);
	Sample s = new Sample(fn); // PERF: calls load()!
        return s;
    }
    private Sample getMoving() throws IOException {
        Pairing p = (Pairing) pairings.get(i);
        String fn = (String) moving.get(p.m);
	Sample s = new Sample(fn); // PERF: calls load()!
        return s;
    }

    // cross
    // REFACTOR: use reflection, and allow any list of crossdates -- default is '(TScore Trend DScore)
    public Cross getCross() throws IOException {
	// get fixed/moving samples
	Sample fixed = getFixed();
	Sample moving = getMoving();

	// depending on the nr, return the right cross
	// (note, this is inefficient for DScore, but not too bad.)
	try {
	    Class c = Class.forName(algs[crossNr]);
	    Constructor cons = c.getConstructor(new Class[] { Sample.class, Sample.class });
	    Cross x = (Cross) cons.newInstance(new Object[] {fixed, moving});
	    return x;
	} catch (Exception e) {
	    System.out.println("e -- " + e);
	    return null;
	}
    }

    private static final String DEFAULT_CROSSES[] = new String[] {
	"corina.cross.TScore",
	"corina.cross.Trend",
	"corina.cross.DScore",
    };

    private String algs[] = DEFAULT_CROSSES;
    void setAlgorithms(String a[]) {
	this.algs = a;
    }
    String [] getAlgorithms() {
	return algs;
    }
    // next step in refactoring: allow changing this at runtime.
    // but: what about crosses like the weiserjahre that don't apply to every pairing?
    // also: add a row of checkboxes ("t-score", "trend", "d-score", "weiserjahre") near the bottom of the CdK

    private int crossNr = 0;

    // go to next pairing; if at end, does nothing
    public void nextPairing() {
        if (crossNr < algs.length-1) {
            crossNr++;
        } else if (i < pairings.size()-1) {
            i++;
            crossNr = 0;
        }
    }
    // go to previous pairing; if at start, does nothing
    public void prevPairing() {
        if (crossNr > 0) {
            crossNr--;
        } else if (i > 0) {
            i--;
            crossNr = algs.length-1;
        }
    }

    public boolean isFirst() {
        return (i==0 && crossNr==0);
    }
    public boolean isLast() {
        return (i==pairings.size()-1 && crossNr==algs.length-1);
    }
}
