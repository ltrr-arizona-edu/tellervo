// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.BorderLayout;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import org.geotools.gui.swing.MapPane;
import org.geotools.renderer.geom.GeometryCollection;
import org.geotools.renderer.geom.Polyline;
import org.geotools.renderer.j2d.SLDRenderedGeometries;
import org.geotools.renderer.style.LineStyle2D;

import vectormap.PaintScheme.Brush;
import vectormap.VectorMap.Segment;

/**
 * Test of GeoTool library for rendering.  It actually works, but I don't think it is
 * as fast as a custom implementation.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class GeoToolWindow {
  protected static final int[] bit_number = new int[] {0,1,2,0,3,0,0,0,4};
  public static void main(String[] args) throws Exception {
    JFrame frame = new JFrame();
    MapPane mp = new MapPane();
    VectorMap map = VectorMap.load(args[0]);
    Segment[] segments = map.getSegments();
    GeometryCollection gc = new GeometryCollection();
    Brush[][] scheme = FixedPaintScheme.INSTANCE.getScheme();
    for (int i = 0; i < segments.length; i++) {
      double[] longitude = segments[i].longitude;
      double[] latitude = segments[i].latitude;
      float[] coords = new float[longitude.length * 2];
      for (int j = 0; j < longitude.length; j++) {
        coords[j * 2] = (float) longitude[j];
        coords[j * 2 + 1] = (float) latitude[j];
      }
      Polyline pl = new Polyline(gc.getCoordinateSystem());
      pl.append(coords, 0, coords.length);
      LineStyle2D style = new LineStyle2D();
      Brush brush = scheme[bit_number[segments[i].category]][segments[i].type];
      
      style.setContour(brush.color);
      style.setStroke(brush.stroke);
      //style.setContourComposite(null);
      pl.setStyle(style);
      gc.add(pl);
    }
    mp.getRenderer().addLayer(new SLDRenderedGeometries(gc));
    frame.getContentPane().add(mp.createScrollPane(), BorderLayout.CENTER);
    frame.pack();
    frame.show();
  }
}
