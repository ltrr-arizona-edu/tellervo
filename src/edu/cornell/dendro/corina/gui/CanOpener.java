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

package edu.cornell.dendro.corina.gui;

import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.io.WrongFiletypeException;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

import java.io.IOException;

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
	    GraphWindow g = new GraphWindow(filename);
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
