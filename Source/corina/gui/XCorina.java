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
import corina.Element;
import corina.editor.Editor;
import corina.util.Platform;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.net.URL;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class XCorina extends JFrame {

    // menubar, and omnipresent menus
    private JMenuBar menubar;
    private List leftMenus, rightMenus;

    // --- DropLoader ----------------------------------------
    public final class DropLoader implements DropTargetListener {
	public void dragEnter(DropTargetDragEvent event) {
	    event.acceptDrag(DnDConstants.ACTION_COPY);
	}
	public void dragOver(DropTargetDragEvent event) { }
	public void dragExit(DropTargetEvent event) { }
	public void dropActionChanged(DropTargetDragEvent event) { }
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
			    new Editor(new Sample(pathname));
			} catch (IOException ioe) {
			    System.out.println("error on " + pathname + "!"); // NEED BETTER ERROR HANDLING!
			}
		    }
		    repaint();
		    event.getDropTargetContext().dropComplete(true);
		} else {
		    event.rejectDrop();
		}
	    } catch (IOException ioe) {
		event.rejectDrop(); // handle error?
	    } catch (UnsupportedFlavorException ufe) {
		event.rejectDrop(); // handle error?
	    }
	}
    }
    // --- DropLoader ----------------------------------------

    private static XCorina _self=null;
    public XCorina() {
	// there can be only one
	if (_self != null)
	    throw new RuntimeException("There can be only one!");
	_self = this;

	// boilerplate
	setTitle("Corina");

	// set tree icon (also in XFrame)
	ImageIcon treeIcon = null;
	URL iconURL = ClassLoader.getSystemResource("Images/tree.png");
	if (iconURL != null) {
	    treeIcon = new ImageIcon(iconURL);
	    setIconImage(treeIcon.getImage());
	}

	// menubar
	setJMenuBar(new XMenubar());

	/*
	// content: search
	JPanel top = new JPanel();
	JLabel search = new JLabel("Search for:");
	JTextField field = new JTextField(16);
	JButton advanced = new JButton("Advanced...");
	top.add(search);
	top.add(field);
	top.add(advanced);
	getContentPane().add(top, BorderLayout.NORTH);

	// content: list
	List el = new ArrayList();
	File dir = new File(System.getProperty("corina.dir.data"));
	File[] files = dir.listFiles();
	for (int i=0; i<files.length; i++)
	    if (!files[i].isDirectory())
		el.add(new Element(files[i].getPath()));
	ElementsPanel ep = new ElementsPanel(el);
	ep.setView(ElementsPanel.VIEW_STANDARD);
	getContentPane().add(ep, BorderLayout.CENTER);
	*/

	// exit when this window closes
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    quit();
		}
	    });

	// pack
	pack();

	// enable drop-loading for panel
	DropTargetListener dropLoader = new DropLoader();
        DropTarget target = new DropTarget(this, dropLoader);
        DropTarget target2 = new DropTarget(getJMenuBar(), dropLoader);

        // size it?
        setSize(320, 240);

        // show
        show();
    }

    // shutdown
    public static void quit() {
        // close all windows, asking the user
        Frame f[] = Frame.getFrames();
        for (int i=0; i<f.length; i++) {
            // skip invisible frames
            if (!f[i].isVisible())
                continue;

            // skip me
            if (f[i] instanceof XCorina)
                continue;

            if (f[i] instanceof XFrame)
                ((XFrame) f[i]).close(); // checks for unsaved
            else
                f[i].dispose();
        }

        // no frames left but me?  bye.
        int n=0;
        f = Frame.getFrames();
        for (int i=0; i<f.length; i++)
            if (f[i].isVisible() && !(f[i] instanceof XCorina))
                n++;
        if (n == 0)
            System.exit(0);

        // for Mac OS: by
        // http://developer.apple.com/techpubs/macosx/Java/Reference/Java/com/apple/mrj/MRJQuitHandler.html#handleQuit()
        // throw IllegalStateException to prevent the quit
        if (Platform.isMac)
            throw new IllegalStateException();
    }
}
