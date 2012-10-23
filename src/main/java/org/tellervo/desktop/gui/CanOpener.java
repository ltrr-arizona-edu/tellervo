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
package org.tellervo.desktop.gui;


import java.io.IOException;

import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.io.WrongFiletypeException;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementFactory;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

// try to open a file, given its filename.  if we can (get it?), open
// it, else throw an IOException

public class CanOpener {

    public static void open(String filename) throws WrongFiletypeException, IOException {
	try {// is it a sample?
		Element e = ElementFactory.createElement(filename);
	    Sample s = e.load();
	    new Editor(s);
	    OpenRecent.sampleOpened(new SeriesDescriptor(s));
	    return;
	} catch (WrongFiletypeException wfte) {
	    // just need to hop out of that block
	}

	/*
	try { // is it a grid?
	    new GridFrame(new Grid(filename));
	    OpenRecent.fileOpened(filename);
	    return;
	} catch (WrongFiletypeException wfte) {
	    // just need to hop out of that block
	}
	*/

	try { // is it a plot?
	    new GraphWindow(filename);
	    OpenRecent.fileOpened(filename);
	    return;
	} catch (WrongFiletypeException wfte) {
	    // just need to hop out of that block
	}

	// ---
	// add new filetypes here
	// ---

	// didn't work, ok to barf now
	throw new WrongFiletypeException();
    }
}
