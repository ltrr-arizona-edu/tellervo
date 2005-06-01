// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.LinkedList;

/**
 * Implementation of a VectorMapLayer that constructs Java2D Shape objects
 * to represent the drawn paths.  This is an attempt to circumvent the
 * drawPolyLine API shortcomings, but doesn't turn out to gain much.  (Drawing
 * GeneralPath shapes is just as slow or slower than just drawing multiple
 * poly-lines)
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class ShapeVectorMapLayer extends BaseVectorMapLayer {
  private GeneralPath[] paths;
  private LinkedList pathIterators = new LinkedList();

  public void setSize(Dimension size) {
    super.setSize(size);
    System.out.println("Num segments: "+ segments.length);
    System.out.println("Projected x blocks: " + projectedx_blocks.length);
    //if (paths == null) {
      paths = new GeneralPath[segments.length];
      int slen = segments.length;  
      for (int i = 0; i < slen; i++) {
        GeneralPath path = paths[i];
        if (path == null) {
          int points = 0;
          for (int j = 0; j < projectedx_blocks[i].length; j++) {
            points += projectedx_blocks[i][j].length;
          }
          path = paths[i] = new GeneralPath(PathIterator.WIND_EVEN_ODD, points);
        } else {
          path.reset();
        }
        for (int j = 0; j < projectedx_blocks[i].length; j++) {
          path.moveTo(projectedx_blocks[i][j][0], projectedy_blocks[i][j][0]);
          for (int k = 1; k < projectedx_blocks[i][j].length; k++) {
            path.lineTo(projectedx_blocks[i][j][k], projectedy_blocks[i][j][k]);
          }
        }
        VectorMap.Segment seg = segments[i];
        PaintScheme.Brush brush = paintScheme[bit_number[seg.category]][seg.type];
        paths[i] = (GeneralPath) brush.stroke.createStrokedShape(path);
        //System.out.println("Path #" + i + ": " + paths[i]);
      }   
    //}
  }

  public void draw(Graphics g, Rectangle viewrect, boolean exclusive) {
    long now = System.currentTimeMillis();
    Graphics2D g2d = (Graphics2D) g;
    Rectangle bounds = g.getClipBounds();
    System.out.println("Lviewrect:\t" + viewrect + "\t" + viewrect.getSize());
    //System.out.println("L   clip:\t" + g.getClipBounds() + "\t" + g.getClipBounds().getSize());
    
    if (height > 180 * 2 ||
        width > 360 * 2) {
      System.out.println("Turning on antialiasing for close view");
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
    }

    int slen = segments.length;

    for (int i = 0; i < slen; i++) {
      VectorMap.Segment seg = segments[i];

      int offset = i * 4;
      int segboundsx = segment_bounds[offset];
      int segboundsy = segment_bounds[offset + 1];
      int segboundswidth = segment_bounds[offset + 2];
      int segboundsheight = segment_bounds[offset + 3];

      /*if (bounds != null) {
        int visibility = isVisible(seg, bounds, segboundsx, segboundsy, segboundswidth, segboundsheight);
        
        if (visibility == VISIBLE_NO) continue;
        
        if (visibility == VISIBLE_POINT) {
          // System.out.println("SEGMENT IS A POINT");
          PaintScheme.Brush brush = paintScheme[bit_number[seg.category]][seg.type];
          g2d.setStroke(brush.stroke);
          g2d.setColor(brush.color);
          g.drawLine(segboundsx, segboundsy, segboundsx, segboundsy);
          continue;
        }
      }*/
      
      PaintScheme.Brush brush = paintScheme[bit_number[seg.category]][seg.type];  
      //g2d.setStroke(brush.stroke);
      g2d.setColor(brush.color);
      g2d.draw(paths[i]);  
    }

    /* DEBUG - DRAW BOUNDS */
    g2d.setColor(Color.green);
    g2d.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);

    g2d.setColor(Color.red);
    g2d.drawRect(viewrect.x, viewrect.y, viewrect.width - 1, viewrect.height - 1);
    /**/
    System.out.println("rendered: " + (System.currentTimeMillis() - now));
  }
  
  /*private final class SegmentPathIterator implements PathIterator {
    private int segment;
    private int[][] xblocks;
    private int[][] yblocks;
    private int curblock = 0;
    private int curpoint = 0;
    public SegmentPathIterator(int segnum) {
      segment = segnum;
      xblocks = projectedx_blocks[segnum];
      yblocks = projectedy_blocks[segnum];
    }
    public int currentSegment(double[] coords) {
      return currentSegment(coords);
    }
    public int currentSegment(float[] coords) {
      //System.out.println("block length: " + projectedx_blocks[segment][curblock].length + " curpoint: "+ curpoint);

      try {      
       coords[0] = xblocks[curblock]
                          [curpoint];
      } catch (ArrayIndexOutOfBoundsException aioobe) {
        System.out.println("Segment " + segment + " block " + curblock + " curpoint " + curpoint);
        System.out.println("numblocks " + xblocks.length + " numpoints " + xblocks[curblock].length);
        throw aioobe;
      }
      coords[1] = yblocks[curblock][curpoint];
      
      if (curpoint == 0) return PathIterator.SEG_MOVETO;
      else return PathIterator.SEG_LINETO;
    }
    public int getWindingRule() {
      return PathIterator.WIND_EVEN_ODD; // XXX: does this really matter?
    }
    public boolean isDone() {
      return curblock == xblocks.length;
    }
    public void next() {
      if (curpoint == (xblocks[curblock].length - 1)) {
        curpoint = 0;
        curblock++;
      } else {
        curpoint++;
      } 
    }
  }*/
}