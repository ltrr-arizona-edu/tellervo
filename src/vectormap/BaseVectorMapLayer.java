// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.LinkedList;

/**
 * A layer which renders a VectorMap.  This class performs preprojection
 * tasks.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public abstract class BaseVectorMapLayer implements Layer {
  /** Not visible. */
  protected static final int VISIBLE_NO = 0;
  /** Visible. */
  protected static final int VISIBLE_YES = 1;
  /** Visible, but only as a point (1 pixel). */
  protected static final int VISIBLE_POINT = 2;

  protected static final int[] bit_number = new int[] {0,1,2,0,3,0,0,0,4};
  
  /* This array needs to be as big as the longest segment stroke array
   * We use this array to accumulate the current longitude projection block
   * as we iterate through the segment's longitude array.  We break the
   * longitude array (and also latitude array to match) into blocks divided
   * by "bad" strokes which wrap accross the prime meridian.
   */
  //private static final int[] BIG_ASS_ARRAY = new int[1024 * 10];

  private double pixPerHorDeg;
  private double pixPerVerDeg;
  private VectorMap map;
  private Projection projection;
  
  protected VectorMap.Segment[] segments;
  protected PaintScheme.Brush[][] paintScheme;
  protected int[][][] projectedx_blocks;
  protected int[][][] projectedy_blocks;
  protected int[] segment_bounds;
  protected boolean[] flipped_segment_bounds;
  protected Dimension dimension = new Dimension();
  protected int width;
  protected int height;
  
  // debugging stuff
  protected int numpoints;
  protected int numpoints_parsed;
  protected int numblocks;
  protected float blocks_per_segment;

  // for isVisible
  private final Point min = new Point();
  private final Point max = new Point();

  public void init(VectorMap map, Projection projection) {
    init(map, projection, FixedPaintScheme.INSTANCE.getScheme());
  }

  protected void init(VectorMap map, Projection projection, PaintScheme.Brush[][] scheme) {
    this.map = map;
    this.projection = projection;
    this.paintScheme = scheme;
    // pre-project
    segments = map.getSegments();
    int slen = segments.length;
    System.out.println("Segments: " + slen);
    projectedx_blocks = new int[slen][][];
    projectedy_blocks = new int[slen][][];
    segment_bounds = new int[slen * 4];
    flipped_segment_bounds = new boolean[slen];
    initProjection();
  }

  public void setSize(Dimension size) {
    //System.out.println("Setting size: "+ size);
    this.dimension.width = size.width;
    this.dimension.height = size.height;
    width = size.width;
    height = size.height;
    pixPerHorDeg = size.width / 360d;
    pixPerVerDeg = size.height / 180d;

    updateProjection();
  }
  public Dimension getSize() {
    return dimension;
  }
  private RunningAverage init = new RunningAverage();
  private RunningAverage update = new RunningAverage();
  /**
   * Initializes data structures to hold projected coordinates
   */
  private final void initProjection() {
    long start = System.currentTimeMillis();
    numpoints = 0;
    numpoints_parsed = 0;
    numblocks = 0;
    blocks_per_segment = 0;

    int slen = segments.length;
    LinkedList projectedx_blocks_list = new LinkedList();

    // use this as the block accumulator position
    int pos = 0;
    // for every segment...
    for (int i = 0; i < slen; i++) {
      VectorMap.Segment seg = segments[i];
  
      int long_len = seg.longitude.length;
      numpoints += seg.longitude.length;
      // prime the last point
      double last = seg.longitude[0];
      double next;
      pos = 1;
      // clear out our projected blocks list
      projectedx_blocks_list.clear();

      int[] block;
      // for every point
      for (int j = 1; j < long_len; j++) {
        next = seg.longitude[j];
        double len = next - last;
        // check whether this is a "bad" longitude stroke
        if (len > 356 ||
            len < -356) {
          //System.out.println("bad longitude line");

          // create a new block, up to and including the LAST
          // point, but NOT including THIS point.
          block = new int[pos];

          // add the block to our blocks list
          projectedx_blocks_list.add(block);
          numblocks++;
          numpoints_parsed += block.length;

          // reset our accumulator position
          pos = 0;
        }
        // increment the accumulation array tail
        pos++;
        // set the last value to this value
        last = next;
      }
      //if (i == 0) System.out.println("last block: "+ pos);
      block = new int[pos];
      projectedx_blocks_list.add(block);
      numblocks++;
      numpoints_parsed += block.length;
      
      blocks_per_segment = ((blocks_per_segment * i) + projectedx_blocks_list.size()) / (i + 1);

      // now convert our linkedlist of blocks into an array for fast access during rendering      
      projectedx_blocks[i] = (int[][]) projectedx_blocks_list.toArray(new int[projectedx_blocks_list.size()][]);
      // create the mirror projectedy blocks to match the number of blocks of projected longitude coords
      projectedy_blocks[i] = new int[projectedx_blocks[i].length][];
      for (int j = 0; j < projectedx_blocks[i].length; j++) {
        // first create a projectedy block the same size as the projectedx block
        projectedy_blocks[i][j] = new int[projectedx_blocks[i][j].length];
      }
    }

    System.out.println("numpoints: " + numpoints + " numpoints_parsed: " + numpoints_parsed + " numblocks: " + numblocks + " blocks_per_segment: " + blocks_per_segment);
    init.add(System.currentTimeMillis() - start);
    System.out.println("avg init time: " + init);
  }

  /**
   * This method pre-projects all strokes, breaking the per-segment stroke
   * arrays into sub-array blocks based on "bad" strokes which wrap around
   * the prime meridian (uglifying the view).  This is necessary SOLELY because
   * the java.awt.Graphics class does NOT provide a 'drawPolyline' method
   * which takes an offset in addition to length! This means we cannot use
   * a single set of x/y arrays and draw contiguous sections of it.  Instead
   * we must painstakingly break up the arrays so we can simply iterate through
   * the blocks calling 'drawPolyline' on each block.  This is a massive
   * Pain In The Ass.
   */
  private final void updateProjection() {
    long start = System.currentTimeMillis();
    Point p0 = new Point();
    int slen = segments.length;
    
    int projected_points = 0;
    // for every segment...
    for (int i = 0; i < slen; i++) {
      VectorMap.Segment seg = segments[i];
      updateSegmentBounds(seg, i);

      // use this as the pointer into the segment long/lat array
      int pos = 0;

      int[][] curxblocks = projectedx_blocks[i];
      int[][] curyblocks = projectedy_blocks[i];
      int numblocks = curxblocks.length;

      for (int j = 0; j < numblocks; j++) {
        int[] curxblock = curxblocks[j];
        int[] curyblock = curyblocks[j];
        int blocklen = curxblock.length;
        for (int k = 0; k < blocklen; k++) {
          projection.project(seg.longitude[pos], seg.latitude[pos], p0);
          curxblock[k] = p0.x;
          curyblock[k] = p0.y;
          pos++;
          projected_points++;
        }
      }
    }
    System.out.println("projected points: " + projected_points);
    update.add(System.currentTimeMillis() - start);
    System.out.println("avg update time: " + update);
  }
  private final void updateSegmentBounds(VectorMap.Segment seg, int i) {
    projection.project(seg.minlong, seg.minlat, min);
    projection.project(seg.maxlong, seg.maxlat, max);
    int width;
    int minx;
    flipped_segment_bounds[i] = false;
    if (max.x < min.x) {
      //System.out.println("max.x " + max.x + " < min.x " + min.x);
      flipped_segment_bounds[i] = true;
      minx = max.x;
      width = min.x - max.x;
    } else {
      minx = min.x;
      width = max.x - min.x;
    }
    int miny;
    int height;
    if (max.y < min.y) {
      //System.out.println("max.y " + max.y + " < min.y " + min.y);
      flipped_segment_bounds[i] = true;
      miny = max.y;
      height = min.y - max.y;
    } else {
      miny = min.y;
      height = max.y - min.y;
    }
    if (flipped_segment_bounds[i]) {
      //System.out.println(new Rectangle(minx, miny, width, height));
    }
    int offset = i * 4;
    segment_bounds[offset] = minx;
    segment_bounds[offset + 1] = miny;
    segment_bounds[offset + 2] = width;
    segment_bounds[offset + 3] = height;
    
    //if (width == 0 || height == 0) segment_bounds[offset] = -1;
  }

  protected static void cull(int[] block_x, int[] block_y, int len, int[] block_x_out, int[] block_y_out, float threshold, int[] lengthHolder) {
    int writeIdx = 1;
    int startIdx = 0;
    int endIdx = startIdx + 1;
    block_x_out[0] = block_x[0];
    block_y_out[0] = block_y[0];
    while (endIdx < len) {
      if (distance(block_x[startIdx], block_y[startIdx], block_x[endIdx], block_y[endIdx]) >= threshold) {
        block_x_out[writeIdx] = block_x[endIdx];
        block_y_out[writeIdx] = block_y[endIdx];
        startIdx = endIdx;
        writeIdx++;
      }
      endIdx++;
    }
    if (len > 1 && startIdx != (len - 1)) {
      block_x_out[writeIdx] = block_x[len - 1];
      block_y_out[writeIdx] = block_y[len - 1];
      writeIdx++;
    }
    lengthHolder[0] = writeIdx;
  }

  // pythagorean distance
  private static final double distance(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
  }

  protected static final int isVisible(VectorMap.Segment seg, Rectangle bounds, int segboundsx, int segboundsy, int segboundswidth, int segboundsheight) {
    //if (segboundsx == -1) return VISIBLE_NO;
    // just a point?
    if (seg.minlong == seg.maxlong && seg.minlat == seg.maxlat/*min.equals(max)*/) {
      if (bounds.contains(seg.minlong, seg.minlat)) return VISIBLE_POINT;
      else return VISIBLE_NO;
    }
    //  check for entirely outside viewport
    if (!bounds.intersects(segboundsx, segboundsy, segboundswidth, segboundsheight)) {
      //System.out.println(bounds + " " + segboundsx + " " + segboundsy + " " + segboundswidth + " " + segboundsheight);
      //System.exit(0);
      return VISIBLE_NO;
    }
    return VISIBLE_YES;
  }
  

  /*public void drawNon(Graphics g) {
    long now = System.currentTimeMillis();
    System.out.println("Rendering " + now);
    Rectangle bounds = g.getClipBounds();
    System.out.println("Bounds: " + bounds);
    System.out.println("pixPerHorDeg: " + pixPerHorDeg);
    System.out.println("pixPerVerDeg: " + pixPerVerDeg);
    Segment[] segments = map.getSegments();
    int slen = segments.length;

    for (int i = 0; i < slen; i++) {
      Segment seg = segments[i];

      if (bounds != null) {
        int res = isVisible(seg, projection, bounds);

        if (res == VISIBLE_NO) {
          //System.out.println("SEGMENT NOT VISIBLE");
          continue;
        } 

        if (res == VISIBLE_POINT) {
          //System.out.println("SEGMENT IS A POINT");
          projection.project(seg.minlong, seg.minlat, p0);
          g.drawLine(p0.x, p0.y, p0.x + 1, p0.y + 1);
          continue;
        }
      }

      int plen = seg.longitude.length;
      double lastlong = seg.longitude[0];
      double lastlat = seg.latitude[0];
      
      projection.project(lastlong, lastlat, p0);

      for (int j = 1; j < plen; j++) {
        //System.out.println("lastx: " + lastx + " lasty: " + lasty);
        double nextlong = seg.longitude[j];
        double nextlat = seg.latitude[j];

        double len = nextlong - lastlong; 
        if (len > 359 ||
            len < -359) {
          System.out.println("bad line");
          lastlong = nextlong;
          lastlat = nextlat;
          
          projection.project(lastlong, lastlat, p0);
          continue;                
        }

        projection.project(nextlong, nextlat, p1);
        g.drawLine(p0.x, p0.y, p1.x, p1.y);

        lastlong = nextlong;
        lastlat = nextlat;
        p0.x = p1.x;
        p0.y = p1.y;
      }
      //g.drawPolyline(seg.x, seg.y, seg.x.length);
    }
    System.out.println("drew lines: " + (System.currentTimeMillis() - now));
  }*/
}