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


import java.util.Collections;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;

// reverses a sample's data (and count and wj, if present);
// undoable.

// i don't like that it extends AbstractUndoableEdit (and therefore
// has a bunch of public methods), but the alternative is to use an
// inner or anonymous class to do that, and that's even worse.
// -- no, simply implement UndoableEdit.  that's not as hard as it sounds.

// (see also Clean.java)

// todo: this should add a "reversed" line to the comments.

// fixme: this is ugly...

@SuppressWarnings("serial")
public class Reverse extends AbstractUndoableEdit {
	private static final Logger log = LoggerFactory.getLogger(Reverse.class);

    private static void reverseSample(Sample s) {
	// reverse stuff
	Collections.reverse(s.getRingWidthData());
	if (s.hasCount())
		try{
	    Collections.reverse(s.getCount());
		} catch (UnsupportedOperationException e)
		{
			log.debug("Problem reversing counts" + e.getLocalizedMessage());
		}
	if (s.hasWeiserjahre()) {
		try{
	    Collections.reverse(s.getWJIncr());
	    Collections.reverse(s.getWJDecr());
		} catch (UnsupportedOperationException e)
		{
			log.debug("Problem reversing weiserjahre values" + e.getLocalizedMessage());
		}
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

    @Override
	public void undo() throws CannotUndoException {
	super.undo();
	reverseSample(sample);
	if (!wasMod)
	    sample.clearModified();

	// watchers
	sample.fireSampleMetadataChanged();
    }
    @Override
	public void redo() throws CannotRedoException {
	super.redo();
	reverseSample(sample);
	sample.setModified();

	// watchers
	sample.fireSampleMetadataChanged();
    }
    @Override
	public String getPresentationName() {
	return I18n.getText("reverse");
    }
}
