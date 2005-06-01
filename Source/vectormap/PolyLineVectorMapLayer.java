// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;

/**
 * A layer which draws a VectorMap using the Graphics.drawPolyLine call.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public final class PolyLineVectorMapLayer extends BaseVectorMapLayer {
  private RunningAverage draw = new RunningAverage();
  private RunningAverage cull = new RunningAverage();
  private RunningAverage paint = new RunningAverage();

  private final BasicStroke BASIC_STROKE = new BasicStroke();

  public final void draw(Graphics g, Rectangle viewrect, boolean exclusive) {
    long now = System.currentTimeMillis();
    Graphics2D g2d = (Graphics2D) g;
    Rectangle bounds = viewrect; //g.getClipBounds();
    System.out.println("Lviewrect:\t" + viewrect + "\t" + viewrect.getSize());
    //System.out.println("L   clip:\t" + g.getClipBounds() + "\t" + g.getClipBounds().getSize());
    
    boolean lowdetail = true;
    
    if (height > 180 * 5 ||
        width > 360 * 5) {
      lowdetail = false;
      System.out.println("Turning on antialiasing for close view");
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
    } else {
      g2d.setStroke(BASIC_STROKE);
    }

    /*if (bounds.y < 0) bounds.y = 0;
    if (bounds.x < 0) bounds.x = 0;
    bounds = g.getClipBounds();*/
    /*System.out.println("clip " + g.getClipBounds());
    g.setClip(bounds);
    System.out.println("clip " + g.getClipBounds());*/
    //g.setClip(null);
    //System.out.println("pixPerHorDeg: " + pixPerHorDeg);
    //System.out.println("pixPerVerDeg: " + pixPerVerDeg);
    int slen = segments.length;

    int yes = 0;
    int no = 0;
    int flipped_not_shown = 0;
    int normal_not_shown = 0;
    int point = 0;
    int drawn = 0;
    int[] temp_block_x = new int[1024 * 10];
    int[] temp_block_y = new int[1024 * 10];
    int[] lengthHolder = new int[1];
    for (int i = 0; i < slen; i++) {
      VectorMap.Segment seg = segments[i];

      int offset = i * 4;
      int segboundsx = segment_bounds[offset];
      int segboundsy = segment_bounds[offset + 1];
      int segboundswidth = segment_bounds[offset + 2];
      int segboundsheight = segment_bounds[offset + 3];

      //if (bounds != null) {
        int visibility = isVisible(seg, bounds, segboundsx, segboundsy, segboundswidth + 1, segboundsheight + 1);

        if (exclusive) {
          if (visibility == VISIBLE_YES || visibility == VISIBLE_POINT) {
            visibility = VISIBLE_NO;
          } else {
            visibility = VISIBLE_YES;
          }
        }
        if (visibility == VISIBLE_NO) {
          no++;
          if (flipped_segment_bounds[i]) {
            flipped_not_shown++;
          } else {
            normal_not_shown++;
          }
          continue;
        } 

        if (visibility == VISIBLE_POINT) {
          point++;
          // System.out.println("SEGMENT IS A POINT");
          PaintScheme.Brush brush = paintScheme[bit_number[seg.category]][seg.type];
          if (!lowdetail) g2d.setStroke(brush.stroke);
          g2d.setColor(brush.color);
          long startPaint = System.currentTimeMillis();
          g.drawLine(segboundsx, segboundsy, segboundsx, segboundsy);
          paint.add(System.currentTimeMillis() - startPaint);
          drawn++;
          continue;
        }

        yes++;
      //}

      /*int plen = seg.longitude.length;
          
      int lastx = projectedx[i][0];
      int lasty = projectedy[i][0];

      for (int j = 1; j < plen; j++) {
        int nextx = projectedx[i][j];
        int nexty = projectedy[i][j];
        if (lastx == -1 || nextx == -1) {
          System.out.println("Skipping bad line");
          lastx = nextx;
          lasty = nexty;
          continue;
        } 
        g.drawLine(lastx, lasty,
                   nextx, nexty);
      }*/
      /*System.out.println("Category: " + seg.category);
      System.out.println("Type: " + seg.type);*/
      //try {
        PaintScheme.Brush brush = paintScheme[bit_number[seg.category]][seg.type];
        if (!lowdetail) g2d.setStroke(brush.stroke);
        g2d.setColor(brush.color);
      /*} catch (ArrayIndexOutOfBoundsException aioobe) {
        System.out.println("cat: "+ seg.category + " catb: " + bit_number[seg.category] + " type: "+ seg.type);
      }*/
      int[][] blocks_x = projectedx_blocks[i];
      int[][] blocks_y = projectedy_blocks[i];
      long startPaint = System.currentTimeMillis();

      for (int j = 0; j < blocks_x.length; j++) {
        cull(blocks_x[j], blocks_y[j], blocks_x[j].length, temp_block_x, temp_block_y, 2, lengthHolder);
        g2d.drawPolyline(temp_block_x, temp_block_y, lengthHolder[0]);
        //g2d.drawPolyline(result.array_x, result.array_y, result.numpoints);
        //g2d.drawPolyline(blocks_x[j], blocks_y[j], blocks_x.length);
      }
      paint.add(System.currentTimeMillis() - startPaint);
      drawn+=blocks_x.length;

      g2d.setStroke(BASIC_STROKE);
      /* DEBUG - DRAW SEGMENT BOUNDING BOXES */ 
      /*if (flipped_segment_bounds[i]) {
        g2d.setColor(Color.CYAN);
        g2d.drawRect(segboundsx,
                     segboundsy,
                     segboundswidth,
                     segboundsheight);
      }*/
      
      /* OLD - DRAW ENTIRE SEGMENT INCLUDING "BAD" STROKES
      g.drawPolyline(projectedx[i], projectedy[i], projectedx[i].length);
      */
    }
    /* DEBUG - DRAW BOUNDS */
    g2d.setColor(Color.green);
    g2d.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
    
    g2d.setColor(Color.red);
    g2d.drawRect(viewrect.x, viewrect.y, viewrect.width - 1, viewrect.height - 1);
    /**/
    long duration = System.currentTimeMillis() - now;
    System.out.println("rendered: " + duration + " yes: "+ yes + " no: "+ no + " flipped not: "+ flipped_not_shown + " normal not: "+ normal_not_shown + " point: "+ point + " drawn: "+ drawn);
    draw.add(duration);
    System.out.println("Avg draw time: "+ draw);
    System.out.println("Avg paint time: "+ draw);
  }
}
