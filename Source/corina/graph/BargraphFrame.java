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

import corina.Year;
import corina.Sample;
import corina.Element;
import corina.gui.XFrame;
import corina.gui.XMenubar;
import corina.editor.Editor;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.*;

/* goals:

   - add legend; make legend moveable
   x make this be a viewer for a Bargraph class, perhaps in e.c.d.graph
   - user can set title, remove axis
   - user can set scale=, or choose "fit to page/window", for x/y
   - user can load/save (xml)
   - sane printing, via e.c.d.print.BargraphPrinter (pager?)
   - scroll if too big for window
   - add standard "elements" menu: edit, graph from all, ...
   - add filenames-only view?
   - crop too-long titles appropriately?
   - add standard "file" menu: close, save, save as, open, exit, etc.
   x right-click on bar for menu: edit, graph, ... ?
   x accept dropped files
   - javadoc for this class
   - sapwood should take ++ information into account
   - draw ++ information as dotted lines?
   - (better) error handling
   - use "Format" menu for formatting options -- "View"?
   - allow graphs in non-fallback order
*/

public class BargraphFrame extends XFrame {

    // the data to graph
    private Bargraph b;

    // display panel
    private JLabel title;
    private BargraphPanel bgp = null;

    // the popup
    private PopupMenu popup;
    private Element currentBar; // bar under pointer

    // printing helpers
    private PageFormat pf = new PageFormat();
    private PrinterJob job = null;

    public class BargraphPanel extends JPanel {
        private int dy=0; // this gets continuously updated by paintComponent()

        // return the Bar at the given Point, if one exists, else null.
        // => shouldn't call a hit if not left-right lined up?
        public Element getBar(Point p) {
            int i = (int) p.getY() / dy;
            if (i > b.bars.size()-1)
                return null;
            return (Element) b.bars.get(i);
        }

	public BargraphPanel(List ss) {
	    // init bars
	    try {
		b = new Bargraph(ss);
	    } catch (IOException ioe) {
		System.out.println("ioe! -- " + ioe.getMessage());
	    }

	    // cursor
	    // super.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

	    // background
	    super.setBackground(Color.white);
	}
	public boolean isFocusTraversable() {
	    return true;
	}
	public boolean isOpaque() {
	    return true;
	}
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1.0f));
            g2.setColor(Color.black);

            // force antialiasing
            // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            // RenderingHints.VALUE_ANTIALIAS_ON);

            int w = super.getParent().getWidth();
            int h = super.getParent().getHeight();

            if (b.prefs.yfixed)
                dy = (int) (b.prefs.yscale * 72);
            else
                dy = super.getHeight() / (b.bars.size() + (b.prefs.xaxis ? 1 : 0));

            // temp vars
            int RISER = (int) (b.prefs.risers * 72);
            int PITH = (int) (b.prefs.pith * 72);
            int BARK = (int) (b.prefs.bark * 72);
            int SAPWOOD = (int) (b.prefs.sapwood * 72);

            // draw bars
            for (int i=0; i<b.bars.size(); i++) {
                Element bar = (Element) b.bars.get(i);

                // draw bar
                int l = bar.range.getStart().diff(b.range.getStart());
                int r = bar.range.getEnd().diff(b.range.getStart());
                int y = i*dy + dy/2;

                /*
                // -- STYLE 1 --
                //          title
                // start ||-------====|| end

                // start
                g2.drawString(bar.range.getStart().toString(), l-35, y+5);

                // pith
                g2.drawLine(l, y-RISER, l, y+RISER);
                if (bar.hasPith())
                    g2.drawLine(l+PITH, y-RISER, l+PITH, y+RISER);

                // heartwood
                g2.drawLine(l, y, r-bar.numSapwood(), y);

                // title
                g2.drawString((String) bar.details.get("title"), l+10, y-5);

                // sapwood
                int stopEarly = (bar.hasBark() ? BARK : 0);
                g2.drawLine(r-bar.numSapwood(), y-SAPWOOD, r - stopEarly, y-SAPWOOD);
                g2.drawLine(r-bar.numSapwood(), y+SAPWOOD, r - stopEarly, y+SAPWOOD);

                // bark
                if (bar.hasBark()) {
                    if (bar.numSapwood() > 0) {
                        g2.drawLine(r-BARK, y-RISER, r-BARK, y-SAPWOOD);
                        g2.drawLine(r-BARK, y+SAPWOOD, r-BARK, y+RISER);
                    } else {
                        g2.drawLine(r-BARK, y-RISER, r-BARK, y+RISER);
                    }
                }
                g2.drawLine(r, y-RISER, r, y+RISER);

                // end
                g2.drawString(bar.range.getEnd().toString(), r+5, y+5);
                */

                // -- STYLE 2 --
                // [=title=========]
                int x0 = bar.range.getStart().diff(b.range.getStart());
                int dx = bar.range.span();
                int y0 = i*dy + dy/2;
                g2.setColor(new Color(20, 80, 80)); // .blue);
                g2.fillRect(x0, y0, dx, dy);
                g2.setColor(Color.black);
                g2.drawRect(x0, y0, dx, dy);
                g2.drawString((String) bar.details.get("title"), x0+10, y0+dy-5);
            }

            // draw axis
            if (b.prefs.xaxis) {
                int y = h - 3*dy/2;
                g2.drawLine(0, y, b.range.span(), y);
                Year d = b.range.getStart().add(-b.range.getStart().mod(100));
                while (d.compareTo(b.range.getEnd())<=0) {
                    int x = d.diff(b.range.getStart());
                    g2.drawLine(x, y-RISER, x, y+RISER);
                    g2.drawString(d.toString(), x+5, y+15);
                    d = d.add(+100);
                }
            }
        }
    }

    /* here's how to do page setup / print:

       page setup:

       job = PrinterJob.getPrinterJob();
       job.setJobName("CORINA: Bargraph");
       pf = job.pageDialog(pf);

       print:

       try {
       job.setPrintable(glue); // this is bad
       if (job.printDialog())
       job.print();
       } catch (PrinterException pe) {
       JOptionPane.showMessageDialog(null,
       "Printer error: " + pe.getMessage(),
       "Error printing",
       JOptionPane.ERROR_MESSAGE);
       return;
    */

    // --- ClickListener ----------------------------------------
    public class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
	    // ignore?
        }
        public void mouseReleased(MouseEvent e) {
	    // replace the label with an edit block
	    JTextField edit = new JTextField();
	    edit.setText(b.prefs.title);
	    edit.setHorizontalAlignment(JTextField.CENTER);
	    if (System.getProperty("corina.bargraph.title.font") != null)
		edit.setFont(Font.getFont("corina.bargraph.title.font"));
	    getContentPane().remove(title);
	    getContentPane().add(edit, BorderLayout.NORTH);
	    edit.selectAll();
	    edit.requestFocus();
	    edit.addActionListener(new AbstractAction() { // "enter" pressed
		    public void actionPerformed(ActionEvent ae) {
			JTextField _edit = (JTextField) ae.getSource();
			String newTitle = _edit.getText();
			if (newTitle.length() == 0) // don't allow "", because then JLabel disappears
			    newTitle = " ";
			b.prefs.title = newTitle;
			title.setText(newTitle);
			getContentPane().remove(_edit);
			getContentPane().add(title, BorderLayout.NORTH);

			setVisible(true);
			repaint(); // this, too, otherwise there's junk still around
		    }
		});

	    setVisible(true); // apparently i need to call this to get the refresh...
        }
    }
    // --- ClickListener ----------------------------------------

    // --- PopupListener ----------------------------------------
    public class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
        private void maybeShowPopup(MouseEvent e) {
            // store what bar we're looking at.  abort if null.
            currentBar = bgp.getBar(e.getPoint());
            if (currentBar == null)
                return;

            // update title, range
            try {
                Sample s = new Sample(currentBar.filename);
                popup.setSample(s);
            } catch (IOException ioe) {
                return;
            }

            // if right-click, show popup
            if (e.isPopupTrigger())
                popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    // --- PopupListener ----------------------------------------

    // --- DropAdder ----------------------------------------
    public class DropAdder implements DropTargetListener {
        public void dragEnter(DropTargetDragEvent event) {
            event.acceptDrag(DnDConstants.ACTION_MOVE);
        }
        public void dragOver(DropTargetDragEvent event) { }
        public void dragExit(DropTargetEvent event) { }
        public void dropActionChanged(DropTargetDragEvent event) { }

        public void drop(DropTargetDropEvent event) {
            try {
                Transferable transferable = event.getTransferable();

                // we accept only filelists
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    event.acceptDrop(DnDConstants.ACTION_MOVE);
                    Object o = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    List l = (List) o; // a List of Files

                    // add each one in turn
                    for (int i=0; i<l.size(); i++) {
                        String pathname = ((File) l.get(i)).getPath();
                        try {
                            Element e = new Element(pathname);
                            e.loadMeta();
                            b.bars.add(e);
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
    // --- DropAdder ----------------------------------------

    // --- DragCopier ----------------------------------------
    public class DragCopier implements DragGestureListener, DragSourceListener {
	public void dragGestureRecognized(DragGestureEvent event) {
	    // ignore right-click-drags
	    if (((MouseEvent) event.getTriggerEvent()).isPopupTrigger())
		return;

	    // figure out what's being dragged
	    Point p = event.getDragOrigin();
	    final Element bar = bgp.getBar(p);

	    // put together a Transferable to send
	    Transferable transfer = new Transferable() {
		    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (!flavor.equals(DataFlavor.javaFileListFlavor))
			    throw new UnsupportedFlavorException(flavor);
			return Collections.singletonList(new File(bar.filename));
		    }
		    public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
		    }
		    public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.equals(DataFlavor.javaFileListFlavor);
		    }
		};

	    // let the drag begin!
	    event.startDrag(DragSource.DefaultCopyDrop, transfer, this);
	}
	public void dragDropEnd(DragSourceDropEvent event) {
	    // dragging ended
	}
	public void dragEnter(DragSourceDragEvent event) {
	    // dragging entered target
	}
	public void dragExit(DragSourceEvent event) {
	    // dragging exited target
	}
	public void dragOver(DragSourceDragEvent event) {
	    // dragging over target
	}
	public void dropActionChanged(DragSourceDragEvent event) {
	    // dragging action changed
	}
    }
    // --- DragCopier ----------------------------------------

    public BargraphFrame(List ss) {
	// menubar
	setJMenuBar(new XMenubar(this, null));

	// frame title
	setTitle("Bargraph");

	// create panel
	bgp = new BargraphPanel(ss);
	getContentPane().add(/*new JScrollPane(*/bgp/*)*/, BorderLayout.CENTER);

	// title goes on top
	title = new JLabel(b.prefs.title, SwingConstants.CENTER);
	title.setOpaque(true);
	title.setBackground(Color.white);
	title.setForeground(Color.black);
	if (System.getProperty("corina.bargraph.title.font") != null)
	    title.setFont(Font.getFont("corina.bargraph.title.font"));
	getContentPane().add(title, BorderLayout.NORTH);
	title.addMouseListener(new ClickListener());

	// create popup
	popup = new PopupMenu();
        MouseListener popupListener = new PopupListener();
        bgp.addMouseListener(popupListener);

	// enable drop-loading for panel
	DropTargetListener dropAdder = new DropAdder();
        DropTarget target = new DropTarget(bgp, dropAdder);

	// enable drag-copying for panel
	DragSource ds = new DragSource();
	ds.createDefaultDragGestureRecognizer(bgp, DnDConstants.ACTION_COPY, new DragCopier());

	// display
	pack();
	setSize(new Dimension(640, 480));
	show();
    }

}
