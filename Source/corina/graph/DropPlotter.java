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

package corina.graph;

import corina.Sample;

import java.io.File;
import java.io.IOException;

import java.util.List;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

public final class DropPlotter implements DropTargetListener {
    private GraphWindow _g;

    public DropPlotter(GraphWindow g) {
	_g = g;
    }

    public void dragEnter(DropTargetDragEvent event) {
	event.acceptDrag(DnDConstants.ACTION_COPY);
    }
    public void dragOver(DropTargetDragEvent event) {
	// do nothing
    }
    public void dragExit(DropTargetEvent event) {
	// do nothing
    }
    public void dropActionChanged(DropTargetDragEvent event) {
	// do nothing
    }
    public void drop(DropTargetDropEvent event) {
	try {
	    Transferable transferable = event.getTransferable();

	    // we accept only filelists
	    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
		event.acceptDrop(DnDConstants.ACTION_COPY);
		Object o = transferable.getTransferData(DataFlavor.javaFileListFlavor);
		List l = (List) o; // a List of Files

		// load each one in turn
		for (int i=0; i<l.size(); i++) {
		    String pathname = ((File) l.get(i)).getPath();
		    try {
			_g.add(new Sample(pathname));
		    } catch (IOException ioe) {
			System.out.println("error on " + pathname + "!"); // NEED BETTER ERROR HANDLING!
		    }
		}
		// _g.plot.computeRange(); -- broken!
		event.getDropTargetContext().dropComplete(true);
	    } else {
		event.rejectDrop();
	    }
	} catch (IOException ioe) {
	    // handle error?
	    event.rejectDrop();
	} catch (UnsupportedFlavorException ufe) {
	    // handle error?
	    event.rejectDrop();
	}
    }
}
