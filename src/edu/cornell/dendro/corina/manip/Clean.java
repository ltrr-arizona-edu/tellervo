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

import edu.cornell.dendro.corina.ElementList;
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
 Cleans a summed file.  That is, keeps the numerical data, but
 removes evidence that it was summed.  Specifically,

 <ul>
 <li>Clears weiserjahre (<code>wj</code>)
 <li>Clears elements (<code>elements</code>)
 <li>Clears count (<code>count</code>)
 <li>Clears filename
 </ul>

 <p>The <code>run()</code> method does the dirty work.  It was used
 for consistency, only; it probably takes negligible time, so
 there's no reason to thread it.</p>

 @see Sample

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */

// i don't like that it extends AbstractUndoableEdit (and therefore
// has a bunch of public methods), but the alternative is to use an
// inner or anonymous class to do that, and that's even worse.
// => no, implementing undoableedit isn't bad at all...
// clumsy: "=null" parts are duplicated.  re-use that code, like (??) does.
/*
 TODO: this class should be the simplest class there is; it should
 only define a manipulate() { clean(); }, where clean() { incr = decr
 = count = elements = null; filename = null; } -- everything (like
 undo/redo) else should be handled by a superclass Manipulation.
 */

public class Clean extends AbstractUndoableEdit {

	// sample to clean
	private Sample s;

	// undo data
	private List<Integer> incr, decr, count;
	private ElementList elements;
	private String filename;
	private boolean wasMod;

	public static AbstractUndoableEdit clean(Sample s) {
		Clean c = new Clean(s);
		c.cleanSample();

		// return undo
		return c;
	}

	private void cleanSample() {
		// make backups for undo
		incr = s.getWJIncr();
		decr = s.getWJDecr();
		elements = s.getElements();
		count = s.getCount();
		filename = (String) s.getMeta("filename");
		wasMod = s.isModified();

		// erase wj, elements, count, filename
		s.setWJIncr(s.setWJDecr(null));
		s.setElements(null);
		s.setCount(null);
		s.removeMeta("filename");
		s.setModified();

		// tell watchers
		s.fireSampleDataChanged();
		s.fireSampleMetadataChanged();
		s.fireSampleElementsChanged();
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		s.setWJIncr(incr);
		s.setWJDecr(decr);
		s.setElements(elements);
		s.setCount(count);
		s.setMeta("filename", filename);
		if (!wasMod)
			s.clearModified();

		// tell watchers
		s.fireSampleDataChanged();
		s.fireSampleMetadataChanged();
		s.fireSampleElementsChanged();
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		s.setWJIncr(s.setWJDecr(null));
		s.setElements(null);
		s.setCount(null);
		s.removeMeta("filename");
		s.setModified();

		// tell watchers
		s.fireSampleDataChanged();
		s.fireSampleMetadataChanged();
		s.fireSampleElementsChanged();
	}

	@Override
	public String getPresentationName() {
		return I18n.getText("clean");
	}

	private Clean(Sample s) {
		this.s = s;
	}
}
