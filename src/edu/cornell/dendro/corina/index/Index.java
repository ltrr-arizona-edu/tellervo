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

package edu.cornell.dendro.corina.index;

import edu.cornell.dendro.corina_indexing.IndexFunction;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.graph.Graphable;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.List;
import java.util.ArrayList;

import java.text.MessageFormat;

import javax.swing.undo.UndoableEdit;

/**
   Abstract representation of an index of a Sample.  ("Index", in
   this context, means a de-trending curve for a sample's data.)

   <p><a href="http://www.netlib.org/">Netlib</a> might have some
   useful functions for implementing other indexes (but I don't use
   any of them myself).</p>

   @see Sample

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public final class Index implements Graphable, Runnable, UndoableEdit {
	/**
	 * The actual index function this is tied to.
	 */
	private IndexFunction ixFunction;
	
	public IndexFunction getIndexFunction() { return ixFunction; }

	/** 
	 * Index curve, stored as a List of Numbers. 
	 * @deprecated
	 */
//	public List data;

	/** 
	 * Returns the data List for graphing.
	 * @see Graphable
	 * @return data as a List of Integers 
	 */
	public final List<? extends Number> getData() {
		return ixFunction.getOutput();
	}

	/** Returns the start date of the Index for graphing.
	   @see Graphable
	   @return index's start date (same as its Sample's) */
	public final Year getStart() {
		return target.getRange().getStart();
	}

	/** Return the default scale for the index: always 1.0.
	   @return default scale factor, 1.0 */
	public final float getScale() {
		return 1.0f;
	}

	/**
	   A reference to the Sample to use to compute the index.
	   The original is not changed when the index is computed.
	   @see #getTarget
	 */
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
	public Index(Sample s, IndexFunction ixf) {
		source = target = s;
		this.ixFunction = ixf;
	}

	// good shape.
	public void setProxy(Sample proxy) {
		source = (proxy == null ? target : proxy);
		ixFunction.setInput(source);
	}

	public void run() {
		// verify ranges
		if (source != target && !source.getRange().contains(target.getRange())) {
			throw new RuntimeException(
					"Proxy dataset doesn't cover this sample's range.");
		}

		// compute index
		ixFunction.doIndex();

		// crop index when done, if needed.
		// -- crop (sample.start-master.start) from the start
		// -- crop (master.end-sample.end) from the end
		if (source != target) {
			int fromStart = target.getRange().getStart().diff(
					source.getRange().getStart());
			int fromEnd = source.getRange().getEnd().diff(target.getRange().getEnd());
			for (int i = 0; i < fromStart; i++)
				getData().remove(0);
			for (int i = 0; i < fromEnd; i++)
				getData().remove(getData().size() - 1);
		}
	}

	/**
	   Return the name of this index in a user-readable format.  This
	   abstract class returns the name of the instantiated class (like
	   "CubicSpline")

	   @return the name of this index
	 */
	public String getName() {
		String tag = ixFunction.getI18nTag();
		String trailer = ixFunction.getI18nTagTrailer();
		
		if(trailer == null)
			return I18n.getText(tag);
		
		return MessageFormat.format(I18n.getText(tag),
				new Object[] { trailer });
		/*
		String fqdn = getClass().getName();
		int dotIndex = fqdn.lastIndexOf(".");
		return fqdn.substring(dotIndex + 1);
		*/
	}

	// chi^2.  negative means "hasn't been computed" (computed lazily
	// the first time it's asked for).
	private double chi2 = -10.36;

	/** Get the &chi;<sup>2</sup> value.  May fail (possibly in
	strange ways) if called before <code>run()</code>.
	@return &chi;<sup>2</sup> */
	public final double getChi2() {
		// not computed yet?
		if (chi2 < 0 && getData().size() == target.getData().size()) // BUG: not exactly threadsafe here
			chi2 = computeChi2(getData(), target.getData());

		return chi2;
	}

	// "(loop for x in A for y in B summing (expt (- x y) 2))"
	private static double computeChi2(List<? extends Number> A, List<? extends Number> B) {
		int n = A.size();
		double chi2 = 0.0;
		for (int i = 0; i < n; i++) {
			double a = A.get(i).doubleValue();
			double b = B.get(i).doubleValue();
			double chi = a - b;
			chi2 += chi * chi;
		}

		// according to http://www.na.astro.it/datoz-bin/corsi?chi2,
		// i should now say chi2/=n.

		// numerical methods (which i don't trust) seems to say not,
		// but it's not terribly clear.  what to do?

		// 12.aug.2002: carol says average-chi^2 makes sense to her,
		// so let's try that.
		chi2 /= n;

		return chi2;
	}

	private Double r = null; // dunno what's a valid r, so just to be safe

	public final double getR() {
		if (r == null) // see getChi2()
			r = new Double(computeR(getData(), target.getData()));
		return r.doubleValue();
	}

	// uses the r-algorithm stolen from t-score.
	// why can't i steal it directly?  because they use arrays,
	// i use lists (and t-score uses funny offsets).
	// i really shouldn't violate OAOO, though -- REFACTOR.
	private static double computeR(List<? extends Number> A, List<? extends Number> B) {
		// compute means
		int n = A.size();
		double Amean = 0.0, Bmean = 0.0;
		for (int i = 0; i < n; i++) {
			Amean += A.get(i).doubleValue();
			Bmean += B.get(i).doubleValue();
		}
		Amean /= n;
		Bmean /= n;

		// compute xx, yy, z1/z2/z3
		double z1 = 0.0, z2 = 0.0, z3 = 0.0;
		for (int i = 0; i < n; i++) {
			// BUG?  this doesn't look like the r-value from the t-score algorithm in the manual...
			double xx = ((Number) A.get(i)).doubleValue() - Amean;
			double yy = ((Number) B.get(i)).doubleValue() - Bmean;

			z1 += xx * xx;
			z2 += yy * yy;
			z3 += xx * yy;
		}

		// compute r
		double r = z3 / Math.sqrt(z1 * z2);
		return r;
	}

	// undo: backup data
	private List<Number> backup = null;
	private String oldFormat;
	private boolean wasMod;

	/** After the <code>run()</code> method has returned, this method
	    will apply the computed index to the source sample,
	    in-place. */
	public final void apply() {
		// back up the old data
		if (backup == null) {
			backup = new ArrayList<Number>();
			backup.addAll(target.getData());
		}
		// INEFFICIENT: make a copy of the old data, then overwrite it all.
		// why not just move the old data, and create a new list for the new stuff?
		// (well, you only save an O(n) copy, it won't help that much.)

		// target[i] = ind[i] / raw[i] * 1000
		for (int i = 0; i < getData().size(); i++) {
			double ind = ((Number) getData().get(i)).doubleValue();
			double raw = ((Number) target.getData().get(i)).doubleValue();
			double ratio = raw / ind;
			target.getData().set(i, new Integer((int) Math.round(ratio * 1000)));
		}

		// set modified flag
		wasMod = target.isModified();
		target.setModified();

		// format
		oldFormat = (String) target.getMeta("format");
		target.setMeta("format", "I");

		// index_type
		target.setMeta("index_type", new Integer(ixFunction.getLegacyID()));
		// should i fire a metadataChanged event here?
	}

	// get a unique id number for this algorithm -- (this is for ms-dos-corina compatibility)
	//public abstract int getID();

	public final void unapply() {
		// restore target.data
		target.setData(new ArrayList<Number>());
		target.getData().addAll(backup);

		// restore meta/format
		if (oldFormat == null)
			target.removeMeta("format");
		else
			target.setMeta("format", oldFormat);

		// remove meta/index_type
		target.removeMeta("index_type");

		// unset modified flag, maybe.  BUG: DOESN'T WORK!  after
		// unapply(), firing any events sets it again.  how to fix?
		if (!wasMod)
			target.clearModified();
	}

	/** The complete title of this particular index: the indexing
	algorithm, and the target sample.
	@return complete title of this index */
	@Override
	public final String toString() {
		return MessageFormat.format(I18n.getText("x_index_of"), new Object[] {
				getName(), target.toString() });
	}

	// -------------------- undoable edit --------------------
	// see api for abstractundoableedit for background
	private boolean alive = true, hasBeenDone = true;

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
		return I18n.getText("index");
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
