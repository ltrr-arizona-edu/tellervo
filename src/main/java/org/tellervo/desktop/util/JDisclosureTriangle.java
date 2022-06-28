/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.util;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JLabel;

import javax.swing.Icon;
import javax.swing.UIManager;

/*
  this class should go away.  it causes more harm than good.  even
  apple is shying away from disclosure triangles for this sort of
  thing now.

  i've already suggested a better way (a button) in bug.java.
  prefs.java can do the same thing.
*/

// TODO: on non-mac platforms, still use some sort of arrow for the icon

// DND support has been commented out, because i couldn't get it working
// exactly as i wanted.

// NOTE: can only be used in Windows (i.e., not Applets)

@SuppressWarnings("serial")
public class JDisclosureTriangle extends JPanel /*implements DropTargetListener*/ {

    // disclosure triangle, initially visible by default
    public JDisclosureTriangle(String title, JComponent component) {
	this(title, component, true);
    }

    private static final Icon collapsedIcon = (Icon) UIManager.get("Tree.collapsedIcon");
    private static final Icon expandedIcon = (Icon) UIManager.get("Tree.expandedIcon");

    private boolean visible = true;

    private JLabel label;
    private JComponent component;

    // disclosure triangle, possibly hidden by default
    public JDisclosureTriangle(String title, JComponent componentToAdd, boolean initiallyVisible) {
	setLayout(new BorderLayout());

	// north: label with icon
	label = new JLabel(title);
	this.visible = initiallyVisible;
	label.setIcon(visible ? expandedIcon : collapsedIcon);
	add(label, BorderLayout.NORTH);

	// add component, if initially visible
	this.component = componentToAdd;
	if (visible)
	    add(component, BorderLayout.CENTER);

	// clicking on label shows or hides component
	label.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    // update visible flag
		    visible = !visible;

		    // expand or collapse
		    if (visible)
			expand();
		    else
			collapse();

		    // update view
		    update();
		}
	    });

	// let DND operations drop on my component (spring-loaded)
	// BUGGY: new DropTarget(label /* component */, this /* droptargetlistener */);
    }

    // TODO: if something (an object to be dropped) is held over the label and it's closed, it should open
    // (possible to close it when the drop is done, then?)

    // width when closed should be same as width when opened
    @Override
	public Dimension getPreferredSize() {
	Dimension d = super.getPreferredSize();
	d.width = component.getPreferredSize().width;
	return d;
    }

    private void expand() {
	visible = true;
	label.setIcon(expandedIcon);
	add(component, BorderLayout.CENTER);
    }

    private void collapse() {
	visible = false;
	label.setIcon(collapsedIcon);
	remove(component);
    }

    private void update() {
	Window window = (Window) getTopLevelAncestor();
	window.pack();
	window.invalidate(); // this seems to work elsewhere -- does it work here?
	window.repaint(); // BUG: doesn't work!
	Toolkit.getDefaultToolkit().sync();
    }

    // ----------------------------------------
    // drag-n-drop:

    /*
    private long t=-1; // time when enter occurred, or -1 if not in
    private boolean expandedByDragNDropOperation = false;

    public void dragEnter(DropTargetDragEvent dtde) {
	// record the time when the drag entered
	t = System.currentTimeMillis();

	// HACK: for now, just open it
	// expandedByDragNDropOperation = true;
	// expand();
	new Thread() {
	    public void run() {
		try {
		    Thread.sleep(1600);
		} catch (InterruptedException ie) {
		    // ignore
		}
		if (t != -1) { // BUG: all sorts of race conditions here
		    expandedByDragNDropOperation = true;
		    expand();
		    update();
		}
	    }
	}.start();
    }
    public void dragExit(DropTargetEvent dte) {
	t=-1;

	// eep ... can't just ... hmm ...
	if (expandedByDragNDropOperation) {
	    expandedByDragNDropOperation = false;
	    collapse(); // won't work, will it?
	    update();
	}
    }
    public void dragOver(DropTargetDragEvent dtde) {
	// ignore -- nothing special to do while dragging over
    }
    public void drop(DropTargetDropEvent dtde) {
	// ignore -- we don't accept drops, only let you drop stuff inside me
    }
    public void dropActionChanged(DropTargetDragEvent dtde) {
	// ignore
    }
    */

    // ----------------------------------------
    // events

    /*
    public static interface TriangleWatcher {
    }
    */
}
