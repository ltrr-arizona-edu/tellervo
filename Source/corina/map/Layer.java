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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.map;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Shape;

/*
 todo:
 -- javadoc
 
  strategy: (already implemented, i think.  more or less)
  -- create a set of layers when mappanel is created
  -- on paintComponent(g),
  ---- draw all clean layers
  ---- if there isn't a worker thread running, start one
  -- worker thread:
  ---- dirty all layers
  ---- for each layer, update() it, then invalidate() the mappanel

  issues:
  -- the buffer never gets resized!  how/where can i resize it?
     -- in update(), look at Projection.View.size.  if it's different, re-c't buf.
  -- what if the worker thread is doing something slow, and i want to start drawing again?

  perf:
  -- should i composite all of them into a final buf, once they're all done?
     -- (might be especially useful on win32 where double-buf isn't the default)
 */

/**
   A layer of things to draw on a map.

   <p>Drawing the map in layers serves two purposes.  First, that's conceptually how
   maps are drawn.  You have boundaries, labels, a scale, and gridlines which are all
   drawn on top of each other, so it makes sense to separate them in code with a common
   interface.  Second, some layers take a while to draw, so the perceived performance
   of the map is much better if individual layers are drawn to the screen as they
   become available.</p>

   <p>To write a Layer, you just need to extend Layer and write a draw(Graphics2D, Projection)
   method.</p>

   <p>To use a Layer, call update(Projection) with the Projection you want it to have.
   (This could take a while to return.)  When that's finished, the buffer will contain
   the requested image; get it with getBuffer() and draw it to the screen.  If, while
   it's updating, you decide you don't want it, just call stop().  You can then call
   update() with a different Projection.</p>

   <p>To draw a Layer to a special Graphics2D (like an SVGGraphics2D), you can call
   draw(Graphics2D, Projection) yourself.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class Layer {
    /**
       Make a new layer.
    */
    public Layer() {
        // i guess this method is only here for the javadoc tag now
    }

    // size/center/zoom are the View, which are part of the Renderer,
    // which gets passed to update()/draw() for each render.

    // erase the buffer (make it completely transparent)
    // -- PERF: is this the fastest way?  usually 0-1ms, but sometimes 100ms or more.
    private void clear() {
	Graphics2D g2 = buf.createGraphics();
	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
	g2.fillRect(0, 0, buf.getWidth(), buf.getHeight());
    }

    /**
       Returns the buffer.  Only call this if you know it's not dirty.

       @see #isDirty()

       @return the buffer
    */
    public BufferedImage getBuffer() {
        // FIXME: throw something if dirty
        return buf;
    }

    // my buffer.  it's a bitmap i draw to, when asked to.
    // buf=null will mean "not created yet".  it'll get created when it's needed.
    // (otherwise, we'd likely guess the size wrong, and so all of the first round of
    // layers would each create a bunch of garbage, and that's not cool.)
    private BufferedImage buf = null;

    public void setDirty() {
	dirty = true;
    }

    public boolean isDirty() {
	return dirty;
    }

    private boolean dirty = true;

    // may be slow -- updates the internal buffer, using draw();
    // used for drawing to components
    public void update(Projection r) {
        // size changed: size i'm being asked to draw is different from
        // the size of my old buffer.  so make a new buffer that size.
        // (there's no way to resize a buffered image in java.)
        if ((buf == null) ||
            (r.view.size.width != buf.getWidth()) ||
            (r.view.size.height != buf.getHeight())) {

            int width = r.view.size.width;
            int height = r.view.size.height;
            buf = new BufferedImage(width, height,
                                    BufferedImage.TYPE_INT_ARGB_PRE);
        }
        
	clear(); // PERF: this means createGraphics() is called twice -- is that bad?

	Graphics2D g2 = buf.createGraphics();

	// antialias!  windows needs this or the labels are illegible.
	// plus, it generally looks nice.  (only for update(), but
	// that's fine: it's used for screen bitmaps; if you're
	// drawing to SVG, it won't be needed.)
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

	// (and time it)
	long t1 = System.currentTimeMillis();
	abort = false;
	draw(g2, r);
	long t2 = System.currentTimeMillis();
        // PROFILING: System.out.println(getClass().getName() + ": spent " + (t2-t1) + " ms drawing");

	dirty = false;
    }

    /**
       Draw this layer.  Subclasses must implement this.

       <p>This draws to the passed Graphics2D object, not the buffer, for cases when
       you'd want to draw to a special Graphics2D object (like an SVG-generating
       Graphics2D object).</p>

       <p>Calling this never holds up the event-handling thread, so while it should
       be fast, being accurate/high-quality is more important.  (It's also used for
       printing, where it must be high-resolution.)</p>
        
       @param g2 the Graphics2D object to draw to
       @param r the Projection to use for drawing
    */
    public abstract void draw(Graphics2D g2, Projection r);

    // CLIPPING: right now, this is optional -- any way to make it automatic?

    // set the clip to the current view
    // -- only noticeable for SVG export, but it's a nice touch.
    public void clip(Graphics2D g2, Projection r) {
	oldClip = g2.getClip();
	g2.setClip(0, 0, r.view.size.width, r.view.size.height);
    }

    private Shape oldClip = null;

    // restore the clip to before clip()
    public void unclip(Graphics2D g2) {
	g2.setClip(oldClip);
    }

    // TODO: add draw() -- and update()? -- methods given a rect/clip mask

    // BUT: won't i need an update(Graphics) for, e.g., SVG?
    // shouldn't update(Graphics, r) be the abstract method, and update(r) simply call that?

    // PERF: add profiling/logging/timing methods here, so they can be used
    // on all layers; add getName() to make it more friendly?

    // PERF: i might want "fast" and "good" versions of some layers.
    // for example, a low-quality map is faster to draw, and drawing
    // all of the sites is much faster if i only draw dots.  in the
    // latter case, the "good" version might simply be the "fast"
    // version with more things drawn.

    /**
       Asks the Layer to abort a long-running draw() as soon as possible.
       After this call returns, the dirty flag is set (you can't abort
       drawing and expect to have the result).
    */
    public void stop() {
	abort = true;
	dirty = true;
    }

    /**
       Abort drawing.  When set to true, means that the draw() method should stop
       as soon as possible.  It doesn't matter what the buffer looks like after this is
       called, because it will be reset and draw() called again before it's displayed.
       A good goal is 10 ms.  If draw() takes less than 10 ms, you can ignore the abort
       flag; otherwise, check this at least every 10 ms or so.
    */
    protected volatile boolean abort = false;
}
