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

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.ObsFileElement;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
   A sequence of crossdates.

   <p>Usually, users won't want to run just one crossdate between two
   samples, but will want to try a whole bunch of crossdates in
   succession.  That's a Sequence.</p>

   <p>Conceptually, a Sequence consists of:</p>
   <ul>
     <li>a list of fixed samples
     <li>a list of moving samples
     <li>a list of crossdates (algorithms) to run between them
   </ul>

   <p>The Sequence lets you step through <i>pairings</i>.  A pairing
   is just a crossdate out of the sequence.  A Sequence object makes a
   crossdate out of the current pairing by taking a sample from each
   list, and running an algorithm on it.</p>

   <p>For example, if you make a sequence with fixed samples New York
   1, New York 2, and New York 3, moving samples Cairo 1 and Cairo 2,
   and the default algorithms of T-score, Trend, and D-score, the
   pairings (crossdates) you'll get will be:</p>
   <ul>
   <li>New York 1 x Cairo 1, T-score
   <li>New York 1 x Cairo 1, Trend
   <li>New York 1 x Cairo 1, D-score
   <li>New York 1 x Cairo 2, T-score
   <li>New York 1 x Cairo 2, Trend
   <li>New York 1 x Cairo 2, D-score
   <li>New York 2 x Cairo 1, T-score
   <li>New York 2 x Cairo 1, Trend
   <li>New York 2 x Cairo 1, D-score
   <li>New York 2 x Cairo 2, T-score
   <li>... (18 total)
   </ul>

   <p>(Originally, this wasn't an Enumeration or Iterator because they
   only go forward, and users need to go both ways through a Sequence.
   The ListIterator interface, however, provides bidirectional support.
   Perhaps I should consider using that...)</p>

   <h2>Left to do</h2>
   <ul>
     <li>don't use "i" as a private field (too common!)
     <li>getAllFixed(), getAllMoving() methods can cause trouble
         because they return internal lists
     <li>bug: filename=null causes trouble (sol'n: accept samples,
         use hashcode?)
     <li>bug: if filename appears twice of different case,
         on case-insensitive fs (??)
     <li>bug: need to set algs, then use seq; can't change anything
         once created!  BETTER: get rid of setAlgs(), add Seq(f,m,algs)
     <li>getFixed()/getMoving() methods are inefficient -- see comments
   </ul>

   @see java.util.ListIterator

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Sequence {
    // lists of strings (filenames)
    private List fixed = new ArrayList();
    private List moving = new ArrayList();

    /**
       Returns a list of all fixed samples.

       @return all fixed samples of this sequence
    */
    public List getAllFixed() {
        return fixed; // FIXME: exposes internal representation!
    }

    /**
       Returns a list of all moving samples.

       @return all moving samples of this sequence
    */
    public List getAllMoving() {
        return moving; // FIXME: exposes internal representation!
    }

    // list of pairings
    private static class Pairing {
        int f, m;
        Pairing (int f, int m) {
            this.f = f;
            this.m = m;
        }
    }
    private List pairings = new ArrayList();

    // the current pairing (pointer into |pairings| list)
    private int pairingNr=0;

    // the current cross (pointer into |algorithms| array)
    private int crossNr = 0;

    /**
       Make a new sequence.

       <p>Provide lists of fixed and moving samples, as Lists of
       either filenames (java.lang.String) or elements
       (corina.Element).</p>

       <p>The default algorithms will be used, as specified by
       Cross.DEFAULT_CROSSDATES.</p>

       @param fixed the fixed samples
       @param moving the moving samples
    */
    public Sequence(List fixed, List moving) {
        /*
         SOLUTION:
         -- also accept Samples in the list
         -- copy filenames as now
         -- if a sample's filename is null, use "" (which isn't a valid filename)
         -- waitaminute, who uses this, anyway?

         WORKING HERE.
	*/

	// set algorithms
	setAlgorithms(Cross.DEFAULT_CROSSDATES);

        // copy active elements to filenames
        for (int i=0; i<fixed.size(); i++)
	    addElement(this.fixed, fixed.get(i));
        for (int i=0; i<moving.size(); i++)
	    addElement(this.moving, moving.get(i));

        // special case: some idiot is trying to crossdate exactly one file
        // against itself.  ok, whatever...
        if (this.fixed.size()==1 && this.moving.size()==1 &&
	    this.fixed.get(0).equals(this.moving.get(0))) {
            pairings.add(new Pairing(0, 0));
            return;
        }

	computePairings();
    }

    // add an element to a list, as a filename (string).
    // the element may be an element, or a filename.
    // (if the element is "inactive", we'll skip it, of course.)
    private void addElement(List list, Object obj) {
	if (obj instanceof String)
	    list.add(obj);
	else if (((ObsFileElement) obj).isActive())
	    list.add(((ObsFileElement) obj).getFilename());
    }

    // |fixed| and |moving| contain lists of filenames;
    // now i'll build the |pairings| list, by taking
    // unique tuples consisting of one element from each list.
    private void computePairings() {
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

                // ok, it's a good cross, add it
                used.add(s1 + File.pathSeparator + s2);
                pairings.add(new Pairing(i, j));
            }
        }

        // the hash was just for weeding out duplicates, and isn't
        // needed any more.
    }

    // these used to be the public interface to Sequence.  they're too
    // low-level for that now, but they're still used by getCross(),
    // and there's no reason to get rid of them.
    // EXCEPT THEY'RE UNNECESSARY AND HORRIBLY INEFFICIENT.  SUCK!
    // (5-50ms per load(), for small local files)
    private Sample getFixed() throws IOException {
        Pairing p = (Pairing) pairings.get(pairingNr);
        String fn = (String) fixed.get(p.f);
	Sample s = new Sample(fn); // PERF: calls load()!
        return s;
    }
    private Sample getMoving() throws IOException {
        Pairing p = (Pairing) pairings.get(pairingNr);
        String fn = (String) moving.get(p.m);
	Sample s = new Sample(fn); // PERF: calls load()!
        return s;
    }

    /**
       Return a new crossdate of the current pairing.

       @exception IOException if one of the samples needed to make
       this crossdate could not be loaded
       @exception IllegalArgumentException if one of the algorithms
       used by this Sequence isn't a valid crossdate (i.e.,
       Cross.makeCross() throws an exception)
    */
    public Cross makeCross() throws IOException, IllegalArgumentException {
	return Cross.makeCross(algorithms[crossNr], getFixed(), getMoving());
	// (PERF: this is inefficient for the d-score, but not
	// horrible.  the i/o problem is much worse than the extra
	// computation time.)
    }

    private String algorithms[];

    /**
       Sets the algorithms to use for this sequence.  The format is an
       array of Strings containing (fully-qualified) names of classes
       which subclass corina.cross.Cross.  The order used is the order
       they'll be presented to the user.

       @param algorithms the new algorithms to use
    */
    public void setAlgorithms(String algorithms[]) {
	// make copy
	int n = algorithms.length;
	this.algorithms = new String[n];
	for (int i=0; i<n; i++)
	    this.algorithms[i] = algorithms[i];
    }

    /**
       Returns the names of the algorithms.  The format is an array of
       Strings containing (fully-qualified) names of classes which
       subclass corina.cross.Cross.  (The returned value is a copy of
       the internal representation; modifying the returned array has
       no effect on this Sequence object.)

       @return an array containing the names of the algorithms
    */
    public String[] getAlgorithms() {
	String copy[] = new String[algorithms.length];
	for (int i=0; i<algorithms.length; i++)
	    copy[i] = algorithms[i];
	return copy;
    }

    /**
       Go to the next pairing.  If already at the end, does nothing.
    */
    public void nextPairing() {
        if (crossNr < algorithms.length-1) {
            crossNr++;
        } else if (pairingNr < pairings.size()-1) {
            pairingNr++;
            crossNr = 0;
        }
    }

    /**
       Go to the previous pairing.  If already at the beginning, does
       nothing.
    */
    public void prevPairing() {
        if (crossNr > 0) {
            crossNr--;
        } else if (pairingNr > 0) {
            pairingNr--;
            crossNr = algorithms.length-1;
        }
    }

    /**
       Is the current pairing the first pairing?

       @return true, if this is the first pairing
    */
    public boolean isFirst() {
        return (pairingNr==0 && crossNr==0);
    }

    /**
       Is the current pairing the last pairing?

       @return true, if this is the last pairing
    */
    public boolean isLast() {
        return (pairingNr==pairings.size()-1 && crossNr==algorithms.length-1);
    }
}
