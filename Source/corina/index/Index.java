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

package corina.index;

import corina.Year;
import corina.Sample;
import corina.graph.Graphable;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.undo.UndoableEdit;

/**
   Abstract representation of an index of a Sample.  ("Index", in
   this context, means a de-trending curve for a sample's data.)

   <p><a href="http://www.netlib.org/">Netlib</a> might have some
   useful functions for implementing indexes.</p>

   @see Sample

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public abstract class Index implements Graphable, Runnable, UndoableEdit {

    /** Index curve, stored as a List of Numbers. */
    public List data;

    /** ResourceBundle for internationalization. */
    protected static ResourceBundle msg =
	ResourceBundle.getBundle("IndexBundle");

    /** Returns the data List for graphing.
	@see Graphable
	@return data as a List of Integers */
    public final List getData() {
	return data;
    }

    /* Returns the start date of the Index for graphing.
       @see Graphable
       @return index's start date (same as its Sample's) */
    public final Year getStart() {
	return target.range.getStart();
    }

    /* Return the default scale for the index: always 1.0.
       @return default scale factor, 1.0 */
    public final double getScale() {
        return 1.0;
    }

    /** A reference to the Sample to use to compute the index.
        The original is not changed when the index is computed.
        @see target */
    public Sample source;

    /** A reference to the Sample to apply the index to.  In almost
        every case, this is the same as the source.  This is modified
    in-place by apply().
        @see source */
    private Sample target;
    public Sample getTarget() {
        return target; // for graphing
    }

    /** Constructs an index with a given source sample, and creates a
        list for data.
        @param s sample to index */
    public Index(Sample s) {
        source = target = s;
        data = new ArrayList(target.data.size());
    }

    // good shape.
    public void setProxy(Sample proxy) {
        source = (proxy==null ? target : proxy);
    }

    protected abstract void index();
    public void run() {
        // verify ranges
        if (source!=target && !source.range.union(target.range).equals(source.range)) {
            throw new RuntimeException("Proxy dataset doesn't cover this sample's range.");
        }

        // compute index
        index();

        // crop index when done, if needed.
        // -- crop (sample.start-master.start) from the start
        // -- crop (master.end-sample.end) from the end
        if (source != target) {
            int fromStart = target.range.getStart().diff(source.range.getStart());
            int fromEnd = source.range.getEnd().diff(target.range.getEnd());
            for (int i=0; i<fromStart; i++)
                data.remove(0);
            for (int i=0; i<fromEnd; i++)
                data.remove(data.size()-1);
        }
    }

    /** Return the name of this index in a user-readable format.  This
        abstract class returns the name of the instantiated class;
        concrete classes should append something to this value, if
        they wish to return a more specific value.
	@return a human-readable name of this index */
    public String getName() {
	String fqdn = getClass().getName();
	int dotIndex = fqdn.lastIndexOf(".");
	return fqdn.substring(dotIndex + 1);
    }

    // chi^2.  negative means "hasn't been computed" (computed lazily
    // the first time it's asked for).
    private double chi2=-10.36;

    /** Get the &chi;<sup>2</sup> value.  May fail (possibly in
	strange ways) if called before <code>run()</code>.
	@return &chi;<sup>2</sup> */
    public final double getChi2() {
        // not computed yet?
        if (chi2 < 0 && data.size()==target.data.size()) // BUG: not exactly threadsafe here
            chi2 = computeChi2(data, target.data);

        return chi2;
    }

    // "(loop for x in A for y in B summing (expt (- x y) 2))"
    private static double computeChi2(List A, List B) {
        int n = A.size();
        double chi2 = 0.0;
        for (int i=0; i<n; i++) {
            double a = ((Number) A.get(i)).doubleValue();
            double b = ((Number) B.get(i)).doubleValue();
            double chi = a - b;
            chi2 += chi*chi;
        }
        return chi2;
    }

    // undo: backup data
    private List backup=null;
    private String oldFormat;
    private boolean wasMod;

    /** After the <code>run()</code> method has returned, this method
        will apply the computed index to the source sample,
        in-place. */
    public final void apply() {
        // back up the old data
        if (backup == null)
            backup = (List) ((ArrayList) target.data).clone();

        // target[i] = ind[i] / raw[i] * 1000
        for (int i=0; i<data.size(); i++) {
            double ind = ((Number) data.get(i)).doubleValue();
            double raw = ((Number) target.data.get(i)).doubleValue();
            double ratio = raw / ind;
            target.data.set(i, new Integer((int) Math.round(ratio * 1000)));
        }

        // set modified flag
        wasMod = target.isModified();
        target.setModified();

        // format
        oldFormat = (String) target.meta.get("format");
        target.meta.put("format", "I");

        // index_type
        target.meta.put("index_type", new Integer(getID()));
        // should i fire a metadataChanged event here?
    }

    // get a unique id number for this algorithm -- (this is for ms-dos-corina compatibility)
    public abstract int getID();

    public final void unapply() {
	// restore target.data
	target.data = (List) ((ArrayList) backup).clone();

	// restore meta/format
	if (oldFormat == null)
	    target.meta.remove("format");
	else
	    target.meta.put("format", oldFormat);

	// remove meta/index_type
	target.meta.remove("index_type");

	// unset modified flag, maybe.  BUG: DOESN'T WORK!  after
	// unapply(), firing any events sets it again.  how to fix?
	if (!wasMod)
	    target.clearModified();
    }

    /** The complete title of this particular index: the indexing
	algorithm, and the target sample.
	@return complete title of this index */
    public final String toString() {
	return getName() + ' ' + msg.getString("index_of") + ' ' + target;
    }

    // -------------------- undoable edit --------------------
    // see api for abstractundoableedit for background
    private boolean alive=true, hasBeenDone=true;
    public boolean addEdit(UndoableEdit anEdit) {
	return false;
    }
    public boolean canRedo() {
	return alive && !hasBeenDone;
    }
    public boolean canUndo() {
	return alive && hasBeenDone;
    }
    public void die() {
	alive = false;
    }
    public String getPresentationName() {
	return msg.getString("index");
    }
    public String getRedoPresentationName() {
	return "Redo Index"; // i18n me!
    }
    public String getUndoPresentationName() {
	return "Undo Index"; // i18n me!
    }
    public boolean isSignificant() {
	return true;
    }
    public void redo() {
	hasBeenDone = true;
	apply();
	target.fireSampleDataChanged();
	target.fireSampleMetadataChanged();
	target.setModified();
    }
    public boolean replaceEdit(UndoableEdit anEdit) {
	return false;
    }
    public void undo() {
	hasBeenDone = false;
	unapply();
	target.fireSampleDataChanged();
	target.fireSampleMetadataChanged();
	if (!wasMod)
	    target.clearModified();
    }
}
