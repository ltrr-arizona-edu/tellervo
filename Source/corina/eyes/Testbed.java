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

package corina.eyes;

import java.io.IOException;

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.editor.Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.BasicStroke;
import java.awt.Toolkit;
import java.awt.RenderingHints;
import java.awt.MediaTracker;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.AbstractAction;

public class Testbed extends JFrame {

    class ImageViewer extends JPanel implements MouseListener, MouseMotionListener {
	private BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB); // needed?
	private Point p1, p2; // eventually: list of points
	public void paintComponent(Graphics g) {
	    // boilerplate
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;

	    // paint image
	    g2.drawImage(img, 0, 0, this);

	    // thick red line (with AA, for non-macs)
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.red);
	    g2.setStroke(new BasicStroke(2.5f));

	    // dot
	    g2.fillOval(p1.x-3, p1.y-3, 6, 6);

	    // line
	    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
	    g2.setStroke(new BasicStroke((float) Scanner.SWIPE)); // really thick, and
	    g2.setColor(new Color(255, 0, 0, 64)); // translucent
	    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

	    // (return to normal)
	    g2.setStroke(new BasicStroke(2.5f));
	    g2.setColor(Color.red);

	    // arrow
	    double theta = Scanner.angle(p1, p2);
	    GeneralPath arrow = new GeneralPath();
	    arrow.moveTo((float) (p2.x + 10*Math.cos(theta+Math.PI*3/4)),
			 (float) (p2.y + 10*Math.sin(theta+Math.PI*3/4)));
	    arrow.lineTo((float) p2.x, (float) p2.y);
	    arrow.lineTo((float) (p2.x + 10*Math.cos(theta-Math.PI*3/4)),
			 (float) (p2.y + 10*Math.sin(theta-Math.PI*3/4)));
	    g2.draw(arrow);

	    // scan for life forms, sulu
	    if (scanner == null) {
		scanner = new Scanner(img, p1, p2);
	    } else {
		scanner.setPath(p1, p2);
		scanner.rescan();
	    }

	    // draw tick at each ring
	    g2.setStroke(new BasicStroke(2f));
	    g2.setColor(Color.red);
	    for (;;) {
		Scanner.Ring r = scanner.getNextRing();
		if (r == null) break;

		// use generalpath, which can take floats!
		GeneralPath gp = new GeneralPath();
		gp.moveTo((float) (r.p.x - 10*Math.cos(theta+Math.PI/2)),
			  (float) (r.p.y - 10*Math.sin(theta+Math.PI/2)));
		gp.lineTo((float) (r.p.x + 10*Math.cos(theta+Math.PI/2)),
			  (float) (r.p.y + 10*Math.sin(theta+Math.PI/2)));
		g2.draw(gp);
	    }
	}

	public ImageViewer(String file) {
	    // load an image
	    Image orig = Toolkit.getDefaultToolkit().createImage(file);

	    // have to wait for it to load
	    MediaTracker tracker = new MediaTracker(this);
	    tracker.addImage(orig, 0);
	    try {
		tracker.waitForID(0);
	    } catch (InterruptedException e) {
		// what to do here?
		System.out.println("error loading image");
	    }

	    // after it's loaded, measure it,
	    int height = orig.getHeight(null);
	    int width = orig.getWidth(null);
	    size = new Dimension(width, height);

	    // and stamp it onto a BufferedImage
	    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = img.createGraphics();
	    g2.drawImage(orig, 0, 0, null);
	    g2.dispose();

	    // create a default path
	    p1 = new Point(width/4, height/2);
	    p2 = new Point(width*3/4, height/2);

	    // and add mouse listeners
	    super.addMouseListener(this);
	    super.addMouseMotionListener(this);
	}

	private Dimension size;
	public Dimension getPreferredSize() {
	    return size;
	}

	// mouse
	private Point dragPoint = null;
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) {
	    Rectangle r1 = new Rectangle(p1.x-10/2, p1.y-10/2, 10, 10);
	    if (r1.contains(e.getPoint())) {
		dragPoint = p1;
		return;
	    }
	    Rectangle r2 = new Rectangle(p2.x-10/2, p2.y-10/2, 10, 10);
	    if (r2.contains(e.getPoint())) {
		dragPoint = p2;
		return;
	    }

	    dragPoint = null;
	    return;
	}
	public void mouseReleased(MouseEvent e) {
	    dragPoint = null;
	}
	public void mouseMoved(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) {
	    if (dragPoint == null)
		return;

	    dragPoint.x = e.getX();
	    dragPoint.y = e.getY();

	    // normalize
	    if (dragPoint.x < 0) dragPoint.x = 0;
	    if (dragPoint.x > img.getWidth(null)) dragPoint.x = img.getWidth(null);
	    if (dragPoint.y < 0) dragPoint.y = 0;
	    if (dragPoint.y > img.getHeight(null)) dragPoint.y = img.getHeight(null);

	    scrollRectToVisible(new Rectangle(dragPoint));
	    super.repaint();
	}

	/* private */ Scanner scanner=null; // construct here?
	public void resetScanner() {
	    scanner.setPath(p1, p2);
	    scanner.rescan();
	}
    }

    public Testbed(String fn) {
	setTitle("Eyes");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	final ImageViewer iv = new ImageViewer(fn);
	JScrollPane sp = new JScrollPane(iv);
	sp.getHorizontalScrollBar().setUnitIncrement(20);
	sp.getVerticalScrollBar().setUnitIncrement(20);

	JTabbedPane tabs = new JTabbedPane();
	tabs.addTab("Scanned Image", sp);
	tabs.addTab("Density Graph", new DensityPanel(iv.scanner));

	getContentPane().add(tabs);

	JPanel p = new JPanel(new GridLayout(1, 0));
	getContentPane().add(p, BorderLayout.SOUTH);

	// measure!
	JButton b = new JButton("Measure");
	b.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // make sample
		    Sample s = new Sample();
		    iv.resetScanner();
		    iv.scanner.getNextRing();
		    for (;;) {
			Scanner.Ring r = iv.scanner.getNextRing();
			if (r == null) break;

			// compute units (.01mm) from pixels; assume 600dpi
			double pixels = r.dist;
			final int DPI = 600;
			int units = (int) (2540 * pixels / DPI);
			s.data.add(new Integer(units));
		    }

		    // compute range
		    s.range = new Range(Year.DEFAULT, s.data.size());

		    // open it
		    new Editor(s);
		}
	    });
	p.add(b);

	// output density
	JButton d = new JButton("Density");
	d.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    try {
			iv.scanner.outputRawData();
		    } catch (IOException ioe) {
			// ignore
		    }
		}
	    });
	p.add(d);

	setSize(new Dimension(600, 600));
	show();
    }

    public static void main(String args[]) {
	new Testbed(args[0]);
    }

}
