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

package corina.gui;

import corina.Sample;
import corina.cross.Grid;
import corina.cross.GridFrame;
import corina.graph.GraphFrame;
import corina.files.WrongFiletypeException;
import corina.editor.Editor;

import java.io.IOException;

// try to open a file, given its filename.  if we can (get it?), open
// it, else throw an IOException

public class CanOpener {

    public static void open(String filename) throws WrongFiletypeException, IOException {
	try { // is it a grid?
	    new GridFrame(new Grid(filename));
	    OpenRecent.fileOpened(filename);
	    return;
	} catch (WrongFiletypeException wfte) {
	    // just need to hop out of that block
	}

	try { // is it a plot?
	    GraphFrame g = new GraphFrame(filename);
	    OpenRecent.fileOpened(filename);
	    return;
	} catch (WrongFiletypeException wfte) {
	    // just need to hop out of that block
	}

	// ---
	// add new filetypes here
	// ---

	try {// is it a sample?
	    Sample s = new Sample(filename);
	    new Editor(s);
	    OpenRecent.fileOpened(filename);
	    return;
	} catch (WrongFiletypeException wfte) {
	    // just need to hop out of that block
	}

	// didn't work, ok to barf now
	throw new WrongFiletypeException();
    }

}
