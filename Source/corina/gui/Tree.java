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
import corina.editor.Editor;

import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.net.URL;

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
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceDragEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tree extends JLabel
    implements DragSourceListener, DragGestureListener {

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
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Images/Tree.png"));

        // use line height, approximately
        int height = getFont().getSize() + 4;

        // scale -- assumes square icon
        img = new ImageIcon(img.getImage().getScaledInstance(height,
                                                             height,
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

    // start a drag
    public void dragEnter(DropTargetDragEvent event) {
        event.acceptDrag(DnDConstants.ACTION_MOVE);
    }

    // send out the data, as a file(name)
    public void dragGestureRecognized(DragGestureEvent event) {
        Transferable t = new Transferable() {
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                    if (!flavor.equals(DataFlavor.javaFileListFlavor))
                        throw new UnsupportedFlavorException(flavor);
		    return Collections.singletonList(new File((String) s.meta.get("filename")));
                }
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[] { DataFlavor.javaFileListFlavor };
                }
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor.equals(DataFlavor.javaFileListFlavor);
                }
            };
        drag.startDrag(event, DragSource.DefaultMoveDrop, t, this);
    }

    // empty stuff -- need writing?
    public void dragExit(DropTargetEvent event) { }
    public void dragOver(DropTargetDragEvent event) { }
    public void dragEnter(DragSourceDragEvent event) { }
    public void dragExit(DragSourceEvent event) { }
    public void dragOver(DragSourceDragEvent event) { }
    public void dropActionChanged( DragSourceDragEvent event) { }
    public void dragDropEnd(DragSourceDropEvent event) { }

}
