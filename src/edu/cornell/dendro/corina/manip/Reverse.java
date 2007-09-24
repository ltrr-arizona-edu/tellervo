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

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.Collections;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

// reverses a sample's data (and count and wj, if present);
// undoable.

// i don't like that it extends AbstractUndoableEdit (and therefore
// has a bunch of public methods), but the alternative is to use an
// inner or anonymous class to do that, and that's even worse.
// -- no, simply implement UndoableEdit.  that's not as hard as it sounds.

// (see also Clean.java)

// todo: this should add a "reversed" line to the comments.

// fixme: this is ugly...

public class Reverse extends AbstractUndoableEdit {

    private static void reverseSample(Sample s) {
	// reverse stuff
	Collections.reverse(s.data);
	if (s.count != null)
	    Collections.reverse(s.count);
	if (s.hasWeiserjahre()) {
	    Collections.reverse(s.incr);
	    Collections.reverse(s.decr);
	}

	// fire events
	s.fireSampleDataChanged();
    }

    public static AbstractUndoableEdit reverse(Sample s) {
	Reverse r = new Reverse(s);

	// can't just call redo(), because that calls super.redo() -- hmm...
	reverseSample(s);
	s.setModified();
	s.fireSampleMetadataChanged();

	return r; // return the undo
    }

    private Sample sample;
    private boolean wasMod;
    private Reverse(Sample s) {
	sample = s;
	wasMod = sample.isModified();
    }

    public void undo() throws CannotUndoException {
	super.undo();
	reverseSample(sample);
	if (!wasMod)
	    sample.clearModified();

	// watchers
	sample.fireSampleMetadataChanged();
    }
    public void redo() throws CannotRedoException {
	super.redo();
	reverseSample(sample);
	sample.setModified();

	// watchers
	sample.fireSampleMetadataChanged();
    }
    public String getPresentationName() {
	return I18n.getText("reverse");
    }
}
