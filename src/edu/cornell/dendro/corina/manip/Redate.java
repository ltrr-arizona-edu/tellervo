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
import edu.cornell.dendro.corina.ui.I18n;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

public class Redate extends AbstractUndoableEdit {

    // redate-force-relative (do i ever even use this?)
    public static Redate redate(Sample s, Range r) {
	return new Redate(s, r, "R");
	// (shouldn't this be a constant somewhere?)
    }

    public static Redate redate(Sample s, Range r, String dating) {
	return new Redate(s, r, dating);
    }

    // ----------------------------------------

    private Redate(Sample s, Range range, String dating) {
	this.s = s;
	this.oldRange = s.getRange();
	this.oldDating = (String) s.getMeta("dating");
	if (oldDating == null)
	    oldDating = "R"; // BUG: why do this?
	this.oldMod = s.isModified();

	this.newRange = range;
	this.newDating = dating;

	// do it a first time -- can't just call redo() because
	// that calls super.redo() (REFACTOR)
	s.setRange(newRange);
	s.setMeta("dating", newDating);
	s.fireSampleRedated();
	s.setModified();
	s.fireSampleMetadataChanged(); // for mod flag
    }

    // undo
    private Sample s;
    private Range oldRange, newRange;
    private String oldDating, newDating;
    private boolean oldMod;
    public void undo() throws CannotUndoException {
	super.undo();
	s.setRange(oldRange);
	s.setMeta("dating", oldDating);
	s.fireSampleRedated();
	if (!oldMod) {
	    s.clearModified();
	    s.fireSampleMetadataChanged(); // for mod flag
	}
    }
    public void redo() throws CannotRedoException {
	super.redo();
	s.setRange(newRange);
	s.setMeta("dating", newDating);
	s.fireSampleRedated();
	s.setModified();
	s.fireSampleMetadataChanged(); // for mod flag
    }
    public String getPresentationName() {
	return I18n.getText("redate");
    }
}
