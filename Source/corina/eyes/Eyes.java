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

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.editor.Editor;
import corina.gui.Layout;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel; // DEBUGGING
import javax.swing.JSlider; // TESTING
import javax.swing.event.ChangeListener; // TESTING
import javax.swing.event.ChangeEvent; // TESTING
import javax.swing.JComponent;
import javax.swing.ImageIcon;

/**
   A window showing a scan and a density graph, and allowing the user
   to measure the scan.
*/
public class Eyes extends JFrame {

    public Eyes(String filename) {
	setTitle("Eyes");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	// load image
	Image img = loadImage(filename);
	BufferedImage buf = copyImageToBufferedImage(img);

	// path
	path = new Path();

	// scanner
	scanner = new Scanner(buf, path);

	// make (scrollable) section panel
	section = new SectionPanel(buf, path, scanner, this);
	JScrollPane sp = new JScrollPane(section);
	sp.getHorizontalScrollBar().setUnitIncrement(20);
	sp.getVerticalScrollBar().setUnitIncrement(20);
	sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	// make density panel
	density = new DensityPanel(scanner);
	JScrollPane sp2 = new JScrollPane(density);
	sp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

	// TODO: make this scrollable, too

	// put in top/bottom split
	JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	split.setTopComponent(sp);
	split.setBottomComponent(sp2);
	split.setResizeWeight(0.75);
	      // 1 = top gets all extra space, which is what i want, but see below
	split.setContinuousLayout(true);

	// measure button
	JButton m = new JButton("Measure");
	m.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // EXTRACT METHOD!
		    // make sample
		    Sample s = new Sample();

		    double rings[] = scanner.getWidths();
		    for (int i=0; i<rings.length; i++) {
			// compute units (.01mm) from pixels; assume 600dpi
			// TODO: add DPI popup: 300, 600, 1200, 2400, other...?
			double pixels = rings[i];
			final int DPI = 600;
			int units = (int) (2540 * pixels / DPI);
			s.data.add(new Integer(units));
		    }

		    // compute range
		    s.range = new Range(Year.DEFAULT, s.data.size());
		    // WHY NOT just Range(s.data.size())?

		    // open it
		    new Editor(s);
		}
	    });

	// DEBUGGING ONLY:

	// TEMP: put swipe in slider
	final JSlider swiper = new JSlider(1, 41, 21);
	swiper.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    int swipe = swiper.getValue();
		    // TODO: how to set?  need to hit Scanner...
		    Scanner.SWIPE = swipe; // !!

		    updateAll();
		}
	    });

	// TEMP: put thresh1 in slider
	final JSlider thresh1 = new JSlider(JSlider.VERTICAL, 0, 255, 100);
	thresh1.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    // TODO: how to set?  need to hit Scanner...
		    Schweingruber.THRESHOLD_TOP = thresh1.getValue(); // !!

		    // PERF: rescan does sample and schweingruber
		    // -- i only need schweingruber here.
		    updateAll();
		}
	    });

	// TEMP: put thresh1 in slider
	final JSlider thresh2 = new JSlider(JSlider.VERTICAL, 0, 255, 80);
	thresh2.setForeground(java.awt.Color.red);
	thresh2.setBackground(java.awt.Color.green);
	thresh2.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    // TODO: how to set?  need to hit Scanner...
		    Schweingruber.THRESHOLD_BOTTOM = thresh2.getValue(); // !!

		    // PERF: rescan does sample and schweingruber
		    // -- i only need schweingruber here
		    updateAll();
		}
	    });

	// right side
	JPanel right = Layout.gridLayout(new JSlider[][] { { thresh1, thresh2 } }, 0, 0);
	int spacer = 24; // new javax.swing.JScrollBar().getHeight();
	JPanel right2 = Layout.borderLayout(null,
					    null, right, null,
					    javax.swing.Box.createVerticalStrut(spacer));

	// HACK:
	split.setBottomComponent(Layout.borderLayout(null,
						     null, sp2, right2,
						     null));

	// TEMP: button panel on bottom
	JPanel buttons = Layout.buttonLayout(stack(swiper, "Swipe"),
					     null,
					     m);
	buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	JPanel content = Layout.borderLayout(null,
					     null, split, null,
					     buttons);
	setContentPane(content);

	setSize(new Dimension(600, 600));
	show();
	split.setResizeWeight(1);
	// this is a hack, but it's the right thing to do.  explanation:
	// if i set resizeWeight=1 above, then even if i set dividerLocation=0.75,
	// when show() is called, the top component gets all the weight.  so instead,
	// i set resizeWeight=0.75, call show(), and then set resizeWeight=1.
	// it's a slight race condition: it'll only be noticed if the user resizes
	// the window between show() and resizeWeight=1, which is extremely unlikely.
	// (the alternative, resizeWeight=1, show(), then dividerLocation=0.75,
	// blinks the split after it's shown, which is much worse.)
    }

    // something changed, so (1) rescan, and (2) tell all children.
    public void updateAll() {
	scanner.rescan();
	section.repaint();

	density.updateSize();
	density.repaint();
    }

    // children
    private SectionPanel section;
    private DensityPanel density;

    // scanner.
    private Scanner scanner = null;

    // path.
    private Path path = new Path();

    // put a small label under a component
    private JPanel stack(JComponent component, String text) {
	JLabel label = new JLabel(text, JLabel.CENTER);
	label.setFont(label.getFont().deriveFont(10f));
	return Layout.borderLayout(null,
				   null, component, null,
				   label);
    }

    //
    // image loading functions
    //

    // assumes: orig is loaded
    private static BufferedImage copyImageToBufferedImage(Image orig) {
	int width = orig.getWidth(null);
	int height = orig.getHeight(null);

	BufferedImage buf = new BufferedImage(width, height,
					      BufferedImage.TYPE_INT_RGB); // _pre?

	Graphics2D g2 = buf.createGraphics();
	g2.drawImage(orig, 0, 0, null);
	g2.dispose();

	return buf;
    }

    // loads an image (i.e., blocks until image is loaded)
    private static Image loadImage(String filename) {
	// use ImageIcon
	return new ImageIcon(filename).getImage();
    }

    //
    // main
    //
    public static void main(String args[]) {
	new Eyes(args[0]);
    }
}
