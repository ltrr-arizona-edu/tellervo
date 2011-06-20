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

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.sample.Sample;

import java.io.File;

import java.util.List;
import java.util.Collections;

import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceContext; // new

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class Tree extends JLabel implements DragGestureListener {

    /*
      todo: add right-click menu:
      - open (edit?)
      - graph
      - rename?
      - redate?
    */

    private Sample s;

    public Tree(Sample sample) {
        s = sample;
        setBorder(BorderFactory.createEmptyBorder());

        // icon
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Images/Tree.png"));

        // use line height, approximately
        int height = getFont().getSize() + 4;

        // scale -- assumes square icon
	// WRITEME: scale image?
        img = new ImageIcon(img.getImage().getScaledInstance(height, height,
                                                             Image.SCALE_SMOOTH));

        // use that
        setIcon(img);

        // tooltip
        setToolTipText("Drag this tree to drop this sample on a " +
                       "graph, bargraph, or even a disk");

        // drag source
        drag = new DragSource();
        drag.createDefaultDragGestureRecognizer(this, // component
                                                DnDConstants.ACTION_MOVE, // ?
                                                this); // dragger

        // double-click
        addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    new Editor(s);
            }
        });
    }

    // only used for crossdates, so far
    public void setSample(Sample newSample) {
	s = newSample;
    }

    // drag source
    private DragSource drag;

    // send out the data, as a file(name).  (implementation of DragGestureListener.)
    public void dragGestureRecognized(DragGestureEvent event) {
	// start the drag
        drag.startDrag(event,
		       DragSource.DefaultMoveDrop,
		       new TransferableFile((String) s.getMeta("filename")),
		       new EmptyDragSourceListener());
    }

    // a DragSourceListener that doesn't do anything.
    public static class EmptyDragSourceListener implements DragSourceListener {
	public void dragDropEnd(DragSourceDropEvent event) {
	    System.out.println("drag source listener: drag-drop-end");
	    System.out.println("-- action=" + event.getDropAction());
	    System.out.println("-- success=" + event.getDropSuccess());
	}
	public void dragEnter(DragSourceDragEvent event) {
	    DragSourceContext context = event.getDragSourceContext(); // Q: do i need to do any of this?
	    int myAction = event.getDropAction();
	    if ((myAction & DnDConstants.ACTION_COPY) != 0)
		context.setCursor(DragSource.DefaultCopyDrop);
	    else
		context.setCursor(DragSource.DefaultMoveDrop);
	    // context.setCursor(DragSource.DefaultCopyNoDrop);
	}
	public void dragExit(DragSourceEvent event) {
	    // System.out.println("drag source listener: drag-exit");
	}
	public void dragOver(DragSourceDragEvent event) {
	    // System.out.println("drag source listener: drag-over");
	}
	public void dropActionChanged(DragSourceDragEvent event) {
	    System.out.println("drag source listener: drop-action-changed, event=" + event);
	}
    }

    // if you want to drag a file, here's an easy way to do it.
    // (i'm not the only one to think of this, apparently.
    // see also com.apple.mrj.datatransfer.FileTransferable)
    // REFACTOR: move this to corina.util?
    // -- (or maybe a more awt/swing-specific util package?)
    public static class TransferableFile implements Transferable {
	private String filename;
	public TransferableFile(String filename) {
	    super();
	    this.filename = filename;
	}
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
	    if (!flavor.equals(DataFlavor.javaFileListFlavor))
		throw new UnsupportedFlavorException(flavor);

	    return Collections.singletonList(new File(filename));
	}
	public DataFlavor[] getTransferDataFlavors() {
	    return new DataFlavor[] { DataFlavor.javaFileListFlavor };
	}
	public boolean isDataFlavorSupported(DataFlavor flavor) {
	    return flavor.equals(DataFlavor.javaFileListFlavor);
	}
    }
    public static class TransferableFileList extends TransferableFile {
	// this class ignores private String filename -- oh well, a few bytes lost
	@SuppressWarnings("unchecked")
	private List files;
	@SuppressWarnings("unchecked")
	public TransferableFileList(List files) {
	    super(null); // HACK, but i need to call something (see previous comment)
	    this.files = files; // should i copy it?
	}
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
	    if (!flavor.equals(DataFlavor.javaFileListFlavor))
		throw new UnsupportedFlavorException(flavor);

	    return files;
	}
    }
    // -- with stringFlavor, i can drop a clipping onto a mac folder.
    // -- javaFileListFlavor doesn't seem to want to drop a file, though -- but sort of on win32 (?!?)
}
