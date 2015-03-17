/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/

package org.tellervo.desktop.graph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.Layout;
import org.tellervo.desktop.gui.PrintableDocument;
import org.tellervo.desktop.gui.Tree;
import org.tellervo.desktop.gui.XFrame;
import org.tellervo.desktop.gui.menus.EditMenu;
import org.tellervo.desktop.gui.menus.HelpMenu;
import org.tellervo.desktop.gui.menus.WindowMenu;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementFactory;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.FileElement;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.ColorUtils;
import org.tellervo.desktop.util.Sort;
import org.tellervo.desktop.versioning.Build;


/*
 Strategy:
 -- refactor mercilessly!
 -- (move the bar-style into its own method, so i can extend this class with another style?)
 -- then: think about merging BargraphFrame with GraphFrame
 */

/* TODO:
 - make this another "style" of graph (2, actually: M and P), and integrate with GraphFrame
 - draw vertical lines between datasets which have crossdate scores above threshold?
 - add legend; make legend moveable
 - user can toggle title?
 - user can set scale=, or choose "fit to page/window", for x/y
 - user can load/save (xml) (one file format for all graph types)
 - add standard "elements" menu: edit, graph from all, ...
 - add filenames-only view?
 - crop too-long titles appropriately?  ("...") -- no, draw them outside(?)
 - javadoc for this class
 - sapwood should take ++ information into account
 - draw ++ information as dotted lines?
 - (better) error handling
 - use "View" menu for formatting options (or "Format"?)
 - allow graphs in non-fallback order
 - for long bars, have title text track left edge of window?
 */

/*
 note: scrollbars have unclickable 1-pixel border (bottom of h, right
 of v) -- this was apparently a java 1.3.1 bug; it's fixed in 1.4.1.
 */

@SuppressWarnings("serial")
public class BargraphFrame extends XFrame implements PrintableDocument {
	// display panel
	private BargraphPanel bgp = null;

	// constants used for drawing
	private static final Font BAR_FONT = new Font("dialog", Font.PLAIN, 12);
	private static final Font END_YEARS_FONT = new Font("dialog", Font.PLAIN, 9);
	private static final Font AXIS_FONT = new Font("dialog", Font.PLAIN, 12);
	private static final Stroke BAR_STROKE = new BasicStroke(1.0f);
	private static final Stroke CENTURY_STROKE = new BasicStroke(1.0f);
	private static final Stroke AXIS_STROKE = new BasicStroke(1.0f);
	private static final Color CENTURY_COLOR = new Color(0.75f, 0.75f, 0.75f);
	private static final Color END_YEARS_COLOR = new Color(0.25f, 0.25f, 0.25f);

	private static final int BAR_SPACING = 2;
	private static final int BAR_HEIGHT = 16; // FIXME: should be based on text -- BAR_FONT.height+4?

	private float X_SCALE = 0.33f; // "1 year = X_SCALE pixels"

	// user options
	private boolean blackAndWhite = false;

	// PrintableDocument
	public Object getPrinter(PageFormat pf) {
		return new BargraphPager(this, pf);
	}

	public String getPrintTitle() {
		return "Bargraph"; // FIXME
	}

	public class BargraphPanel extends JPanel implements Scrollable {
		private int dy = 0; // this gets continuously updated by paintComponent()

		// BUT WHAT THE HECK IS IT?

		// SCROLLING
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			// just scroll by the viewport amount; it's most natural
			return (orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width);
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			// vertical: one bar; horizontal: half a century
			return (orientation == SwingConstants.VERTICAL ? BAR_HEIGHT + BAR_SPACING : (int) (50 * X_SCALE));
		}

		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(600, 400); // ?
		}

		public boolean getScrollableTracksViewportHeight() {
			return false; // true if !b.yfixed
		}

		public boolean getScrollableTracksViewportWidth() {
			return false; // true if !b.xfixed
		}

		// ----

		// return the Bar at the given Point, if one exists, else null.
		// => FIXME: shouldn't call a hit if x-position not in range
		// DESIGN: return null?  ugh.  NoNullBeyondMethodScope!
		public BaseSample getBar(Point p) {
			int i = (int) p.getY() / dy;
			if (i > bars.size() - 1)
				return null;
			return bars.get(i);
		}

		public BargraphPanel() {
			// cursor
			super.setCursor(new Cursor(Cursor.HAND_CURSOR));

			// background
			super.setBackground(Color.white);
			setOpaque(true);
		}

		@Override
		public void addNotify() {
			super.addNotify();
			configureEnclosingScrollPane();
		}

		@Override
		public void removeNotify() {
			unconfigureEnclosingScrollPane();
			super.removeNotify();
		}

		private SpaceDragger spaceDragger = new SpaceDragger();

		// (ugly -- refactor)
		private boolean isEnclosedByScrollPane() {
			Container p = getParent();
			if (p instanceof JViewport) {
				Container gp = p.getParent();
				if (gp instanceof JScrollPane) {
					JScrollPane scrollPane = (JScrollPane) gp;
					JViewport viewport = scrollPane.getViewport();
					if (viewport == null || viewport.getView() != this) {
						return false;
					}
					return true;
				}
			}
			return false;
		}

		private void configureEnclosingScrollPane() {
			if (isEnclosedByScrollPane()) {
				JScrollPane scrollPane = (JScrollPane) getParent().getParent();
				scrollPane.setColumnHeaderView(ha = new HorizontalAxis());

				JLabel white = new JLabel();
				white.setBackground(Color.white);
				white.setOpaque(true);
				scrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, white);

				// JViewport viewPort = (JViewport) getParent();
				this.addMouseListener(spaceDragger);
				this.addMouseMotionListener(spaceDragger);
				this.addKeyListener(spaceDragger);
			}
		}

		private void unconfigureEnclosingScrollPane() {
			if (isEnclosedByScrollPane()) {
				JScrollPane scrollPane = (JScrollPane) getParent().getParent();
				scrollPane.setColumnHeaderView(null);

				// JViewport viewPort = (JViewport) getParent();
				this.removeMouseListener(spaceDragger);
				this.removeMouseMotionListener(spaceDragger);
				this.removeKeyListener(spaceDragger);
			}
		}

		// REFACTOR: put all of this into a single class, and abstract it out so it can be used for graphs, too

		// BUG: this doesn't work quite right.  it's all jumpy.  to investigate.
		private class SpaceDragger implements MouseListener, MouseMotionListener, KeyListener {
			private Point from = new Point();

			public void mousePressed(MouseEvent e) {
				from.x = e.getPoint().x;
				from.y = e.getPoint().y;
				// translate
				JScrollPane sp = (JScrollPane) getParent().getParent();
				from.x -= sp.getHorizontalScrollBar().getValue();
				from.y -= sp.getVerticalScrollBar().getValue();

				watchDrag = spaceDown;
			}

			public void mouseReleased(MouseEvent e) {
				// ignore
			}

			public void mouseClicked(MouseEvent e) {
				// ignore
			}

			public void mouseEntered(MouseEvent e) {
				// ignore
			}

			public void mouseExited(MouseEvent e) {
				// ignore
			}

			public void mouseMoved(MouseEvent e) {
				// ignore
			}

			public void mouseDragged(MouseEvent e) {
				// TODO: make the cursor a hand only when space is down?
				// TODO: if you drag off the side, keep the original start-location?
				if (watchDrag) {
					// CAREFUL: make sure getParent() is a JViewport; if it's not, don't allow this
					JScrollPane sp = (JScrollPane) getParent().getParent();
					JScrollBar h = sp.getHorizontalScrollBar();
					JScrollBar v = sp.getVerticalScrollBar();

					// translate
					int tx = e.getX() - h.getValue();
					int ty = e.getY() - v.getValue();

					int dx = from.x - tx;
					h.setValue(h.getValue() + dx);

					int dy = from.y - ty;
					v.setValue(v.getValue() + dy);

					from.x = tx;
					from.y = ty;
				}
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == ' ')
					spaceDown = true;
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == ' ') // OAOO!
					spaceDown = false;
			}

			public void keyTyped(KeyEvent e) {
				// ignore
			}

			// is the space bar being held down?
			private boolean spaceDown = false;

			// was the space bar being held down when the mouse was pressed?
			private boolean watchDrag = false;
		}

		// x-axis
		private class HorizontalAxis extends JComponent {
			// FIXME: compute height from height of text
			private final static int HEIGHT = 20;

			public HorizontalAxis() {
				// Q: need setMinimumSize()?
				setPreferredSize(new Dimension(Integer.MAX_VALUE, HEIGHT)); // Q: is MAX_VALUE right here?
			}

			// timing: usually 1-10 ms, rarely up to around 30 ms
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				int l = g2.getClipBounds().x;
				int r = l + g2.getClipBounds().width;

				// force antialiasing -- ?? -- REFACTOR: extract method, plus FORCE_AA flag
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				int h = getHeight();

				g2.setColor(Color.black);
				g2.setFont(AXIS_FONT);
				g2.setStroke(AXIS_STROKE);
				g2.drawLine(l, h - 1, r, h - 1);

				// compute the width of the longest (give-or-take) string.
				// (it might not be the longest, but it's close.)
				// PERF: memoize this?, no, compute it in the c'tor
				int maxWidth = Math.max(g2.getFontMetrics().stringWidth(range.getStart().toString()), g2.getFontMetrics().stringWidth(
						range.getEnd().toString()));
				int centuryWidth = (int) (100 * X_SCALE);
				boolean every500 = (maxWidth * 1.5 > centuryWidth);

				// start year; if every500, keep going to 1 or *00
				Year d = range.getStart().cropToCentury();
				if (every500) {
					while (!d.toString().endsWith("500") && !d.toString().endsWith("000") && !d.toString().equals("1"))
						// PERF: ugh!
						d = d.nextCentury();
				}

				while (d.compareTo(range.getEnd()) <= 0) {
					// why not contains()?  because i can't guarantee
					// initial value of d is in range

					int x = (int) (d.diff(range.getStart()) * X_SCALE);
					int width = g2.getFontMetrics().stringWidth(d.toString()); // PERF: 2 toString()s
					if (x - width / 2 > r) // off screen to the right, we're done
						break;
					g2.drawString(d.toString(), x - width / 2, h - 5);
					// FIXME: center vertically in component
					// BUG: if the width of the text is too wide, only draw every 500/1000 years

					d = d.nextCentury();
					if (every500) { // HACK: only works for 100/500!
						d = d.nextCentury();
						d = d.nextCentury();
						d = d.nextCentury();
						d = d.nextCentury(); // PERF: not very efficient, either
					}
				}
			}
		}

		@Override
		public boolean isFocusTraversable() { // WHY?
			return true;
		}

		// TEMP: ONLY VALID when b.yfixed, b.xfixed (but isn't that always true now?)
		@Override
		public Dimension getMinimumSize() {
			Dimension d = super.getMinimumSize();
			d.width = (int) (range.getSpan() * X_SCALE);
			d.height = bars.size() * (BAR_HEIGHT + BAR_SPACING) + BAR_SPACING;
			return d;
		}

		@Override
		public Dimension getPreferredSize() { // REFACTOR: aren't these 2 methods identical?
			Dimension d = super.getPreferredSize();
			d.width = (int) (range.getSpan() * X_SCALE);
			d.height = bars.size() * (BAR_HEIGHT + BAR_SPACING) + BAR_SPACING;
			return d;
		}

		// i would override getMaximumSize() here, if i wanted to.  i can't think
		// of any reason to, though, and it never seems to get called, anyway.
		// ----

		private List<Shape> clipStack = new ArrayList<Shape>(); // (of Shape)

		private void saveClip(Graphics g) {
			clipStack.add(0, g.getClip());
		}

		private void restoreClip(Graphics g) {
			g.setClip((Shape) clipStack.remove(0));
		}

		// a temporary rectangle for computation, to avoid making a new one every time
		private Rectangle barRect = new Rectangle();
		private Rectangle labelRect = new Rectangle();

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			//int w = getWidth();
			//int h = getHeight();
			saveClip(g2);

			// force antialiasing -- ??
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// REFACTOR: this used to be a complex expression; now it's just an alias!
			dy = BAR_HEIGHT;

			// draw background lines
			paintCenturyLines(g);

			// draw bars
			// timing: 50-100 ms, or as low as 1-2 ms if there aren't any visible
			for (int i = 0; i < bars.size(); i++) {
				BaseSample bs = bars.get(i);
				
				// start of the row for this bar (??)
				int y = i * (BAR_HEIGHT + BAR_SPACING) + BAR_SPACING; //  + BAR_HEIGHT/2;

				// EXTRACT METHOD: drawBar(left, right, top, bottom, title)?

				// ORGANIZATION: GraphStyle -> (NormalStyle BargraphPStyle BargraphMStyle SkeletonStyle)
				// where BargraphPStyle and BargraphMStyle both extend AbstractBargraphStyle,
				// which provides 99% of what a bargraph is.

				// style: [=title=========]
				barRect.x = (int) (X_SCALE * bs.getRange().getStart().diff(range.getStart()));
				barRect.y = y; // i*dy + BAR_SPACING;
				barRect.width = (int) (X_SCALE * bs.getRange().getSpan()); // scale me?
				barRect.height = BAR_HEIGHT; // dy; // - BAR_SPACING;

				// this is the area we're supposed to (re)draw
				Rectangle rr = g.getClipBounds();

				// if it's not scrolled vertically so this bar is visible, just 'continue' now
				// and avoid computing any more bounding boxes or string widths
				if (barRect.y > rr.y + rr.height || barRect.y + barRect.height < rr.y)
					continue;

				// skip it?
				if (rr.intersects(barRect)) {

					// box
					g2.setStroke(BAR_STROKE);
					if (blackAndWhite) {
						g2.setColor(Color.white);
						g2.fillRect(barRect.x, barRect.y, barRect.width - 1, barRect.height - 1); // PERF: only draw visible rect?
						g2.setColor(Color.black);
						g2.drawRect(barRect.x, barRect.y, barRect.width - 1, barRect.height - 1); // PERF: only draw visible rect?
					} else {
						g2.setColor(speciesToColor(bs));
						g2.fillRect(barRect.x, barRect.y, barRect.width, barRect.height); // PERF: only draw visible rect?
						// how's fill(Shape) perf?
					}
				}

				// label
				// Maybe this should really be changed? - lucas
				String barTitle = bs.getDisplayTitle();
				// BUG: doesn't this assume the map can't hold nulls?  how do i know that?

				g2.setColor(Color.black); // EXTRACT const!
				g2.setFont(BAR_FONT);
				int asc = g2.getFontMetrics().getAscent();
				int labelWidth = g2.getFontMetrics().stringWidth(barTitle);

				// skip?
				int leftEdge = barRect.x + (barRect.height - asc) / 2;
				int baseline = barRect.y + (barRect.height + asc) / 2; // WAS: -2
				// why -2?  it looks better, but i don't know why it should be needed --
				// is height being reported to me incorrectly?
				labelRect.setBounds(leftEdge, baseline - asc, Math.min(labelWidth, barRect.width), g2.getFontMetrics().getHeight());

				if (rr.intersects(labelRect)) {
					saveClip(g2);
					// g2.clip(barRect)?
					g2.clipRect(barRect.x, barRect.y, barRect.width, barRect.height);
					{
						g2.drawString(barTitle, leftEdge, baseline);
					}
					restoreClip(g2);
				}

				// start/end years
				g2.setFont(END_YEARS_FONT);
				asc = g2.getFontMetrics().getAscent();
				int yy = barRect.y + barRect.height / 2 + asc / 2;

				int sx = g2.getFontMetrics().stringWidth(bs.getRange().getStart().toString());
				int sx2 = g2.getFontMetrics().stringWidth(bs.getRange().getEnd().toString());
				int x1 = barRect.x - sx - (BAR_HEIGHT - asc) / 2;
				int x2 = barRect.x + barRect.width + (barRect.height - asc) / 2;

				// draw start year
				labelRect.setBounds(x1, yy - asc, sx, asc);
				if (rr.intersects(labelRect)) {
					// lighten background
					g2.setColor(blackAndWhite ? Color.white : LIGHTEN);
					g2.fillRect(x1, barRect.y, sx, barRect.height);

					// draw text
					g2.setColor(blackAndWhite ? Color.black : END_YEARS_COLOR);
					g2.drawString(bs.getRange().getStart().toString(), x1, yy);
				}

				// draw end year
				labelRect.setBounds(x2, yy - asc, sx2, asc);
				if (rr.intersects(labelRect)) {
					// lighten background
					g2.setColor(blackAndWhite ? Color.white : LIGHTEN);
					g2.fillRect(barRect.x + barRect.width + (barRect.height - asc) / 2, barRect.y, sx2, barRect.height);

					// draw text
					g2.setColor(blackAndWhite ? Color.black : END_YEARS_COLOR);
					g2.drawString(bs.getRange().getEnd().toString(), x2, yy);
				}
			}

			// not sure if this is needed, but it's probably cheap
			restoreClip(g2);
		}

		private Color LIGHTEN = ColorUtils.addAlpha(Color.white, 0.7f);

		// PERF: don't draw off-screen lines
		// timing: 0-1 ms if not visible, usually 1-3 ms, rarely 10-20 ms (or more)
		private void paintCenturyLines(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			int l = g2.getClipBounds().x;
			int r = l + g2.getClipBounds().width;

			g2.setColor(blackAndWhite ? Color.black : CENTURY_COLOR);
			g2.setStroke(CENTURY_STROKE);
			int h = getHeight();

			int y0 = Math.max(0, g2.getClipBounds().y);
			int y1 = Math.min(h, g2.getClipBounds().y + g2.getClipBounds().height);

			Year d = range.getStart().cropToCentury();
			while (d.compareTo(range.getEnd()) <= 0) {
				int x = (int) (d.diff(range.getStart()) * X_SCALE);
				if (x > r) // off screen to the right, stop
					break;
				g2.drawLine(x, y0, x, y1);

				d = d.nextCentury();
			}
		}
	}

	// color, based on species.
	// REFACTOR: shouldn't this belong in Species.java?
	private Color speciesToColor(BaseSample s) {
		String species = s.meta().getTaxon();
		if (species == null || !(species instanceof String))
			return unColor;
		String str = ((String) species).toUpperCase(); // PERF: allocates new string!

		if (str.startsWith("QU"))
			return quColor;
		if (str.startsWith("PI"))
			return piColor;
		if (str.startsWith("JU"))
			return juColor;

		return unColor;

		// how do i figure out "is this oak?"?
		// - it's either a code, or a name (later, it should be a code only)
		// - latter case, pass it to Species to parse; if unparseable by Species, it's unknown
		// - use first 2 chars of code to determine species
		// - "QU"=>oak, "PI"=>pine, "JU"=>juniper
	}

	// colors for quercus, pinus, juniperus, and unknown samples; compute only once!
	private final static Color quColor = new Color(Color.HSBtoRGB(4f / 360, 0.5f, 0.9f)); // oak => red
	private final static Color piColor = new Color(Color.HSBtoRGB(123f / 360, 0.5f, 0.9f)); // pine => green
	private final static Color juColor = new Color(Color.HSBtoRGB(207f / 360, 0.5f, 0.9f)); // junip => blue
	private final static Color unColor = new Color(Color.HSBtoRGB(0f / 360, 0.0f, 0.7f)); // unkn => gray

	// the range of this graph (union of bars, extended to centuries)
	/*private?*/Range range;

	// the bars, as a List of Elements
	public List<BaseSample> bars;

	// --- DropAdder ----------------------------------------
	public class DropAdder implements DropTargetListener {
		public void dragEnter(DropTargetDragEvent event) {
			event.acceptDrag(DnDConstants.ACTION_MOVE);
		}

		public void dragOver(DropTargetDragEvent event) {
		}

		public void dragExit(DropTargetEvent event) {
		}

		public void dropActionChanged(DropTargetDragEvent event) {
		}

		@SuppressWarnings("unchecked")
		public void drop(DropTargetDropEvent event) {
			try {
				Transferable transferable = event.getTransferable();

				// we accept only filelists
				if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_MOVE);
					List<File> l = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);

					// add each one in turn
					for (int i = 0; i < l.size(); i++) {
						String pathname = ((File) l.get(i)).getPath();
						try {
							Element e = ElementFactory.createElement(pathname);
							bars.add(e.loadBasic());
						} catch (IOException ioe) {
							new Bug(ioe); // FIXME: need better error handling!
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
			BaseSample bar = bgp.getBar(p);

			// this only makes sense with Files...
			if(bar.getLoader() instanceof FileElement) {
				// make a Transferable to send
				Transferable transfer = new Tree.TransferableFile(
						((FileElement)bar.getLoader()).getFilename());

				// let the drag begin!
				event.startDrag(DragSource.DefaultCopyDrop, transfer, this);
			}
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

	// TODO: add BargraphFrame(Sample sum) constructor, too?
	// -- trivial: it's just this(sum.elements), assuming elements exists
	public BargraphFrame(ElementList ss) {
		// menubar
		{
			JMenuBar menubar = new JMenuBar();

			//menubar.add(new FileMenu(this));
			menubar.add(new EditMenu(this));
			menubar.add(new BargraphViewMenu(this));
			if (Platform.isMac())
				menubar.add(new WindowMenu(this));
			menubar.add(new HelpMenu());

			setJMenuBar(menubar);
		}

		// frame title
		setTitle("Bargraph" + " - " + Build.getCompleteVersionNumber());

		// create panel
		bgp = new BargraphPanel();

		bars = new ArrayList<BaseSample>();
		// load bars
		try {
			// make sure they're loaded/cached
			for (int i = 0; i < ss.size(); i++) {
				bars.add(ss.get(i).loadBasic());
			}
		} catch (IOException ioe) {
			new Bug(ioe);
			dispose();
			return;
		}

		// sort into fallback order
		// TODO: sort by title, using case-insensitive natural-ordering sort, first?
		Sort.sort(bars, "range", true);

		// compute range
		range = computeRange();

		final JScrollPane sp = new JScrollPane(bgp, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		getContentPane().add(sp, BorderLayout.CENTER);

		// DEBUG: south is a slider for horiz-zoom
		final int PRECISION = 100;
		final JSlider zoomer = new JSlider((int) (0.1 * PRECISION), 2 * PRECISION, (int) (X_SCALE * PRECISION)); // divide by PREC later
		zoomer.setPreferredSize(new Dimension(75, zoomer.getPreferredSize().height));
		zoomer.setMaximumSize(new Dimension(75, zoomer.getMaximumSize().height));
		zoomer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// TODO: store scroll
				// int scroll = sp.getHorizontalScrollBar().getValue();
				// Year center = ...

				// change scale
				X_SCALE = zoomer.getValue() / (float) PRECISION;

				// TODO: restore scroll
				// sp.getHorizontalScrollBar().setValue(...)

				// update the scrollbars
				bgp.revalidate();
				ha.invalidate(); // for the x-axis header -- overkill!  FIXME
				ha.revalidate();
				ha.repaint(); // (but what's the minimal?  revalid isn't it.)
			}
		});
		JToolBar tb = new JToolBar();
		tb.setFloatable(false);
		tb.add(Layout.flowLayoutL(new JLabel("Scale:"), new JLabel(Builder.getIcon("viewmag-.png", 22)), zoomer, new JLabel(Builder
				.getIcon("viewmag+.png", 22))));
		getContentPane().add(tb, BorderLayout.NORTH);

		// create popup
		/*
		 * TODO: Fix the popup menu on the bar graph
		 * Will do when GANTT charting is made to work nicely
		 * 
		popup = new SamplePopupMenu();
		bgp.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				// store what bar we're looking at.  abort if null (=no bar here).
				currentBar = bgp.getBar(e.getPoint());
				if (currentBar == null)
					return;

				// update title, range
				try {
					Sample s = new Element(currentBar).load(); // ***
					// PERF: this (***) loads the sample, when we only want its
					// summary info, and we already have that.  ouch.
					// this isn't true, because SamplePopup requires a full sample (to pass to graph window, etc)
					popup.setSample(s);
				} catch (IOException ioe) {
					// TODO: make this show a dimmed popup with something
					// like
					//   "This sample could not be loaded."
					//   "Error: file not found."
					// BETTER: use Finder.java to find it, first.
					// BEST: this shouldn't happen; we already have the data we need.
					return;
				}

				// show popup
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		*/

		/*
		    // enable drop-loading for panel
		    DropTargetListener dropAdder = new DropAdder();
		    DropTarget target = new DropTarget(bgp, dropAdder);

		    // enable drag-copying for panel
		    DragSource ds = new DragSource();
		    ds.createDefaultDragGestureRecognizer(bgp, DnDConstants.ACTION_COPY, new DragCopier());
		 */

		// display
		setSize(new Dimension(720, 480));
		setVisible(true);

		// scroll to top-right
		sp.getHorizontalScrollBar().setValue(sp.getHorizontalScrollBar().getMaximum());
	}

	private JComponent ha; // ha?

	// union of all bars' ranges, extended to centuries
	// FIXME: add 50 years to each end of that?
	private Range computeRange() {
		// ASSUMES bars has at least 1 element
		// BUG: if it doesn't, what's the range?
		Range fullRange = bars.get(0).getRange();
				
		for (int i = 1; i < bars.size(); i++)
			fullRange = fullRange.union(bars.get(i).getRange());

		// ick, this isn't terribly pretty...
		Year early = fullRange.getStart().cropToCentury();
		Year late = fullRange.getEnd().add(100); // ??
		late = late.cropToCentury(); // -- cropToCentury().nextCentury()?
		return new Range(early.add(-50), late.add(50)); // ??
	}

	private class BargraphViewMenu extends JMenu {
		// |window| is what to repaint() when i change something.
		// (better: i should call getSuperDuperParent() or whatever to find out myself.)
		BargraphViewMenu(JFrame window) {
			super(I18n.getText("view"));

			final JFrame frame = window;

			// color / b&w menuitems
			final JMenuItem asColor = new JRadioButtonMenuItem("in Color", true);
			final JMenuItem asBW = new JRadioButtonMenuItem("in B&W");

			// only one selected at a time
			ButtonGroup group = new ButtonGroup();
			group.add(asColor);
			group.add(asBW);

			// set |blackAndWhite| flag based on source, and repaint
			AbstractAction listener = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					blackAndWhite = (e.getSource() == asBW);
					frame.repaint();
				}
			};
			asColor.addActionListener(listener);
			asBW.addActionListener(listener);

			// put them in this menu
			add(asColor);
			add(asBW);
		}
	}
}
