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

import corina.gui.ProgressMeter;
import corina.gui.Splash;
import corina.map.tools.Tool;
import corina.map.layers.GridlinesLayer;
import corina.map.layers.MapLayer;
import corina.map.layers.LegendLayer;
import corina.map.layers.SitesLayer;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.ProgressMonitor;
import javax.swing.RepaintManager;
import javax.swing.filechooser.FileFilter;
import javax.imageio.*;

/**
   A map component.

   <p>It extends JPanel, because that's what's recommended.
   <a href="http://java.sun.com/docs/books/tutorial/uiswing/painting/overview.html">The
   Java Tutorial</a>: "We recommend that you extend either JPanel or a more specialized
   Swing component class."  (Why?  Because "if you extend JComponent, your component's
   background won't be painted unless you paint it yourself."  Oh darn!)</p>

   <h2>Left to do:</h2>
   <ul>
     <li>Instances of this class need to keep track of "selected sites"; I need
         to be able to get it, set it, and find out when it changes
     <li>Instances of this class <i>may</i> need to keep track of offsets for
         sites (I haven't decided yet -- think printing)
     <li>Refactor
     <li>Refactor: look for ".getZ() < 0" -- that's repeated gobs of times
         (and shouldn't even exist)
     <li>Rename this to MapComponent, and make a MapPanel which consists of
         a MapComponent, the Toolbar(?), the scrollbars, and the zoomer
     <li>Javadoc
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class MapPanel extends JPanel {

    private View view;

    // private LabelSet labels;
    public LabelSet labels; // !!! -- (for tools)
    
    private MapFrame fr; // only used for setZoom() (which updates the slider based on the panel's zoom)
    
    private JPopupMenu popup = new JPopupMenu("Save");
    
    private BufferedImage backbuffer = null; // the backbuffer for double buffering the map. null= not created yet
    
    public void setZoom() {
        fr.setZoom();
    }
    
    public MapFrame getFrame() {
    	return fr;
    }

    // note: mapframe is only used for its setZoom() method.
    public MapPanel(final MapFrame fr, LabelSet labels) {
        view = new View(); // where?
        setBackground(Color.white);

        this.fr = fr;
        this.labels = labels;

        // make the layers; it's important that this run with a good copy of |labels|:
        // if i'd said "private Layer sitesLayer = new SitesLayer(labels);", labels
        // would be null there, and SitesLayer drawing would fail.
        grid = new GridlinesLayer();
        legend = new LegendLayer();
        sitesLayer = new SitesLayer(labels);
        mapLayer = new MapLayer();

        // arrays of layers
        layersDraw = new Layer[] { mapLayer, grid, sitesLayer, legend };
        layersCompute = new Layer[] { grid, mapLayer, sitesLayer, legend };
        layersComputeNoDraw = new boolean[] { true, true, false, false };

        // disable double-buffering: since i have a whole stack of buffers
        // that i draw myself, it's not really useful for me.
        this.setDoubleBuffered(false);
        // this seems to destroy double buffering for the rest of Corina
        //RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);

	// add listener: update map when DB changes
        // REMOVED: it wasn't getting called, and relied on some incorrect assumptions
        
      JMenuItem save = new JMenuItem("Save...");
      save.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          final JFileChooser chooser = new JFileChooser();
          chooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
              return f.getName().endsWith(".png");
            }
            public String getDescription() {
              return "PNG image files";
            }
          });
          chooser.setMultiSelectionEnabled(false);
          chooser.setAcceptAllFileFilterUsed(true);
          //chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
          int returnVal = chooser.showSaveDialog(fr);
          if (returnVal != JFileChooser.APPROVE_OPTION) return;
          
          /*
          Splash splash = new Splash("Exporting graph to PNG file...", null);
          ProgressMeter pm = new ProgressMeter();                
          pm.addProgressListener(splash);
          */
          
          ProgressMonitor pm = new ProgressMonitor(fr, // parent
                  "Exporting map to PNG file...", // message
                  "", // note
                  0, 50000); // round up to 45 MB
          
          pm.setMillisToDecideToPopup(100);
          pm.setMillisToPopup(200);
          
          pm.setProgress(1);
          pm.setNote("Creating memory image...");          
          Rectangle rect = getBounds();
          BufferedImage fileImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
          Graphics2D g = fileImage.createGraphics();
          
          try {
          Thread.sleep(5000);
          } catch (Exception e) {}
          
                   
          pm.setProgress(2);
          pm.setNote("Creating map...");          
          paint(g);

          pm.setProgress(3);
          pm.setNote("Encoding as PNG...");
          
          try {
        	  ImageIO.write(fileImage, "png", chooser.getSelectedFile());
          } catch ( IOException ioe ) {
              ioe.printStackTrace();        	  
          }
                   
          //dispose of the graphics content
          g.dispose();
          
          /*
          EventQueue.invokeLater(new Runnable() {
            public void run() {
              Rectangle rect = getBounds();
              final Image fileImage = 
                  createImage(rect.width,rect.height);
              final Graphics g = fileImage.getGraphics();

              //write to the image
              paint(g);
 
                  // write it out in the format you want

              new Thread(new Runnable() {
                public void run() {
                  try {
              
                    PngEncoder encoder = new PngEncoder(fileImage);
                    byte[] bytes = encoder.pngEncode(false);
                    /* (bytes == null) {
                      PngEncoderB encoderb = new PngEncoderB((BufferedImage) fileImage);
                      bytes = encoderb.pngEncode(false);
                    }*/
                  /*
                    if (bytes != null) {
                      FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                      try {
                        fos.write(bytes);
                        fos.flush();
                      } finally {
                        try {
                          fos.close();
                        } catch (IOException ioe) {
                          ioe.printStackTrace();
                        }
                      }
                    } else {
                      System.err.println("ERROR IN ENCODER");
                    }
              
                  } catch (Exception e) {
                    System.err.println("ERROR SAVING GRAPH TO: " + chooser.getSelectedFile());
                    e.printStackTrace();
                  }

                  //dispose of the graphics content
                  g.dispose();
                }
              }).start();
            }
          });
        }
      });
      */
        }
      });

      popup.add(save);

      addMouseListener(new MouseAdapter() {
        public void mouseReleased(MouseEvent e) {
          System.out.println(e);
          if (!e.isPopupTrigger()) return;
    
          System.out.println("Popup triggered!");
  
          popup.show(MapPanel.this, e.getX(), e.getY());
        }  
      });
      
    }

    // USED BY: toFront(), ArrowTool (hmm), etc.
    public void updateBufferLabelsOnly() {
        // (stolen, mostly, from updateBuffer(), of course)

	// whatever you're doing, stop it
	stop();

	// dirty this layer
	sitesLayer.setDirty();

	// start a new worker thread
	worker = new WorkerThread();
	worker.start();

	// BUG: if workers take a long time to die, what's to stop this from
	// building up a whole bunch of threads?
    }
    
    // used by ArrowTool -- if a lat/long changed, we need to fix the location cache
    public void notifyLabelsChanged() {
    	labels.rehashLocations();
    }

    // a Map of (Location=>(Site|List)); first element of each list is the frontmost site
    /*private*/ HashMap siteHash = new HashMap();
    // i'll need iterators ... (will i need to make my own site iterator?  no.  maybe.)

    public void setSites(List sites) {
    	// this is setSites, not addSites... clear it?
    	siteHash.clear();
    	
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.getLocation() == null)
                continue; // ignore these
            Location loc = (Location) s.getLocation().clone();
            if (siteHash.containsKey(loc)) {
                List list = (List) siteHash.get(loc);
                list.add(s);
            } else {
                List list = new ArrayList();
                list.add(s);
                siteHash.put(loc, list);
            }
        }
    }

    // DESIGN: the list of sites should live in exactly one place: LabelSet

    public void toFront(Site s) {
        Location loc = (Location) s.getLocation().clone();
        List list = (List) siteHash.get(loc);
        list.remove(s);
        list.add(0, s);
        updateBufferLabelsOnly();
        repaint();
    }

    // layers (listed from fastest to slowest) -- INIT?  use array?
    private Layer grid;
    private Layer legend;
    private Layer sitesLayer;
    private Layer mapLayer;

    // does this need to be synch'd?  might solve a misdraw problem.
    // (no, it doesn't.)
    public void updateBuffer() {
	// whatever you're doing, stop it
	stop();

	// dirty all layers
	for (int i=0; i<layersCompute.length; i++)
	    layersCompute[i].setDirty(layersComputeNoDraw[i]); // ??

	// start a new worker thread
	worker = new WorkerThread();
	worker.start();

	// BUG: if workers take a long time to die, what's to stop this from
	// building up a whole bunch of threads?  (well, workers better die
        // fast, then.)
    }

    // draw a label using this mappanel's offset hash
    // USED BY: ArrowTool, RulerTool
    public void drawLabel(Graphics2D g2, Point p, Site site, int numSites, View view) {
        // get offsets
        Offset o = getOffset(site.getLocation());

        // compute bubble point
        t.x = p.x + (int) (o.dist * view.getZoom() * Math.sin(o.angle));
        t.y = p.y - (int) (o.dist * view.getZoom() * Math.cos(o.angle));

        // call renderer to draw it for me
        SitesLayer.drawLabelSR(g2, p, site, numSites, view, t, (site==selection)); // false);
    }
    private Point t = new Point();

    // OBSOLETE: when everything is moved to Layer classes,
    // this stuff won't be needed

    // ** selecting sites!
    private Site selection=null;
    public void setSelection(Site s) { // or null = deselect
	selection = s;
    }
    /*
      IDEA for new behavior: select /n/ sites, click on the distance tool, then
      dragging from any one of those sites draws distance lines from all selected sites,
      not just that site.  (i can see it being useful; would this ever get in the way?)
    */
    /*
      IDEA for new behavior: select the way the finder does: with a darkened semitranslucent
      square; selected sites are 50% darker than normal
    */
    /*
      NEED TO BE ABLE TO select a bunch of sites, right-click -> "crossdate these against...",
      or maybe just drag them to a crossdate(kit).

      -- along with this, it would be really useful to have a "show only..." option, so only
      oak, or 20th-century, or medieval, or whatever sites are shown.

      -- but i've already used right-click for something (and something it's not typically
      used for).  what other mechanism can i think of for shuffling through a pile?  (tog?)
    */

    // make this GETfont, instead.
    public static void setFontForLabel(Graphics g, View view) {
        //        g.setFont(new Font("sans-serif", Font.BOLD, (int)view.getZoom()*5));
        // g.setFont(new Font("sans-serif", Font.PLAIN, 9));
        int size = 9;
        if (view.getZoom() < 1.2)
            size = 8;
        if (view.getZoom() > 3)
            size = 10;
        g.setFont(new Font("sans-serif", Font.PLAIN, size));
    }

    /* private */ public static class Offset {
        public float angle = 0f;
        public float dist = 0f; // new!
    }
    private HashMap offsets = new HashMap(); // (location => [angle, dist]) hash
    public void setOffset(Location loc, float angle, float dist) {
        Offset o = new Offset();
        o.angle = angle;
        o.dist = dist;
        offsets.put(loc.clone(), o);
    }
    public Offset getOffset(Location location) {
        if (offsets.containsKey(location))
            return (Offset) offsets.get(location); // anaphoric macros, where are you?
        else
            return nullOffset;
    }
    private final Offset nullOffset = new Offset(); // singleton

    private Point3D p2 = new Point3D(); // ugh!

    // PERF: when i recieve a sitemoved/codechanged event i need to
    // respond to, i only need to look at its bounding box and redraw
    // that, not the entire buffer.  that's going to be a LOT faster.

    private Point pt = new Point();

    // for actual POINT -- list all sites here, to be put in a popup menu
    public List sitesForPoint(Site target) {
        Location loc = target.getLocation();
        return (List) siteHash.get(loc);
    }
    // how to use: on click (where? how?) on site, show popup consisting of these sites

    // important: for LABEL -- (return topmost site, of course)
    // (who uses this? -- the tools.)
    public Site siteForPoint(Projection r, Point p, int dist) throws SiteNotFoundException {
        if (siteHash == null)
            throw new SiteNotFoundException();

        // i'll need a graphics, of some sort, for measuring text.
        BufferedImage buf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2 = buf.createGraphics();
        setFontForLabel(g2, view); // yeah, very weird...

        // (stuff that was originally in the loop, but are invariant and moved out for performance)
        int textHeight = g2.getFontMetrics().getHeight();
        Point t = new Point();
        
        // WAS: Iterator iter = siteHash.values().iterator();
        Iterator iter = labels.getLocations();
        // FIXME: should getLocations() return only visible locations?  or should i make a getVisibleLocations()?
        
        // normally, i'd check in backwards order, so you get the top-most (last-drawn) one first,
        // which is what the user expects.  IteratorsSuck, and I can't iterate backwards, so I'll
        // look at all and take the last one (it's still O(n)).
        Site returnValue = null;
        while (iter.hasNext()) {
            // WAS: List list = (List) iter.next();
            // WAS: Site s = (Site) list.get(0);
            Location loc = (Location) iter.next();

            // WAS: r.project(s.getLocation(), p2);
            r.project(loc, p2);
            if (p2.getZ() < 0)
                continue;

            // NEW
            Site s = SiteDB.getSiteDB().getSite(loc);
                
            String text = s.getCode(); // REFACTOR: violates OAOO -- should ask SiteRenderer what's there

            // measure the text
            int textWidth = g2.getFontMetrics().stringWidth(text);

            // get offset, if any
            // WAS: Offset o = getOffset(s.getLocation());
            Offset o = getOffset(loc);

            // center of Text bubble -- like drawLabel()
            // IT'S EXACTLY LIKE DRAWLABEL -- USE THAT!
            t.x = (int) p2.getX() + (int) (o.dist * view.getZoom() * Math.sin(o.angle));
            t.y = (int) p2.getY() - (int) (o.dist * view.getZoom() * Math.cos(o.angle));

            // ripped from drawLabel() -- SO MAKE IT A METHOD IN SITERENDERER AND USE THAT!
            int left = t.x - (textWidth/2 + EPS), width = textWidth + 2*EPS;
            int top = t.y - (textHeight/2 + EPS/4), height = textHeight + EPS/2;
            if (p.x>=left && p.x<=(left+width) && p.y>=top && p.y<=(top+height)) {
                returnValue = s;
            }
        }

        // if i have something, return it, else snfe
        if (returnValue != null)
            return returnValue;
        throw new SiteNotFoundException();
    }
    private static int EPS = 4; // OAOO VIOLATION: must this be same as in sites layer?

    // the layers, in the order they're drawn (bottom to top, first to last)
    private Layer layersDraw[];

    // the layers, in the order they're computed (first to last)
    private Layer layersCompute[];
    private boolean layersComputeNoDraw[];

    // the layer which is currently being updated, or null if no update is occurring
    private Layer currentLayer = null;

    private class WorkerThread extends Thread {
	public void run() {
	    // make an r
	    Projection r = Projection.makeProjection(view);

	    // for each layer,
	    for (int i=0; i<layersCompute.length && !abort; i++) {
                // if it's not dirty, skip it
                if (!layersCompute[i].isDirty())
                    continue;
                
                // render it
		currentLayer = layersCompute[i];
		currentLayer.update(r);
		currentLayer = null;

	    }
		// revalidate this panel
		revalidate();
		repaint();
	    
	}

	private boolean abort = false;
	public void pleaseStop() {
	    abort = true;
	}
    }

    // stop the worker thread, ASAP!
    private void stop() {
	if (worker == null)
	    return;

	worker.pleaseStop(); // stop thread

	if (currentLayer != null)
	    currentLayer.stop(); // stop layer
	currentLayer = null;

	worker = null; // bury it
    }

    private WorkerThread worker = null;

    // PERF: when a new layer is computed, simply draw it on top (!!!),
    // don't recomposite everything.
    // -- BUT that means i have to compute them in order, moron.  d'oh!

    // PERF: when you release the mouse button, it's redrawn.  if the view doesn't
    // change, don't recompute it!

    // blit out the buffer, and draw Extra Crap.
    public void paintComponent(Graphics g) {
	// -- draw all clean layers
	// -- if no worker thread, start one -- FIXME: either i should do this, or i shouldn't claim to.
    
    if ((backbuffer == null) || 
    		(view.size.width != backbuffer.getWidth()) ||
    		(view.size.height != backbuffer.getHeight()))
    {
    	backbuffer = new BufferedImage(getSize().width, getSize().height,
    			BufferedImage.TYPE_INT_RGB);
    }    	
    
    Graphics2D bg = backbuffer.createGraphics();
    
	bg.setColor(Color.white);
	bg.fillRect(0, 0, view.size.width, view.size.height);

	// draw all layers
	for (int i=0; i<layersDraw.length; i++) {
	    if (!layersDraw[i].noDraw()) {
		bg.drawImage(layersDraw[i].getBuffer(), 0, 0, null);
	    }
	}

        // turn on antialiasing for decorators.
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // current tool gets a chance to decorate
        decorate(bg); // BUG: the decoration isn't double-buffered! (on win32)
        
        g.drawImage(backbuffer, 0, 0, null);
    }

    // extra crap, er, decorators -- via callbacks.  probably not threadsafe, but
    // consolidate with sample listeners if you really want that.
    private List decorators = new ArrayList();
    public void addDecorator(Tool t) {
        decorators.add(t);
    }
    public void removeDecorator(Tool t) {
        decorators.remove(t);
    }
    public void decorate(Graphics g) {
        for (int i=0; i<decorators.size(); i++)
            ((Tool) decorators.get(i)).decorate(g);
    }

    // hrm...
    public void setHeight(int h) {
        view.size.height = h;
    }
    public void setWidth(int w) {
        view.size.width = w;
    }

    // they really want my view
    public View getView() {
        return view;
    }

    // there should be a permanent mouse/motion listener here:
    // right-click on a site should always show:
    // [see "map planned features"]
    // and the title bar should always show the current location.
}
