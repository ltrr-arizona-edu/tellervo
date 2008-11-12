// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

/**
 * A panel consisting of several Layers and associated DrawStrategys, each renderend
 * in sequence.  Currently consists of vector map and grid layers.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class VectorMapPanel extends JComponent implements MouseMotionListener {
  private static final int MAX_PRERENDERED_ZOOM = 20; 
  private static final Dimension MAX_PRERENDERED_DIMENSION = new Dimension(MAX_PRERENDERED_ZOOM  * 360, MAX_PRERENDERED_ZOOM  * 180);

  private VectorMap map;
  private Projection projection;
  private float zoom = 1.0f;
  private Dimension dimension = new Dimension(360, 180);
  //private BaseVectorMapLayer maplayer;
  private Point mousePointer;
  //private DrawStrategy strategy;
  private List layers = new LinkedList();

  public VectorMapPanel(VectorMap map) throws IOException {
    this.map = map;
    projection = new RectangularProjection();
    //maplayer = new ShapeVectorMapLayer(map, projection);
    layers.add(new LayerAndDrawStrategy(new PolyLineVectorMapLayer(),
                                        new ThresholdDrawStrategy(
                                            new ConstantSizeDrawStrategy(MAX_PRERENDERED_DIMENSION,
                                              new AsynchronousVolatileImageDrawStrategy()),
                                            new OnDemandDrawStrategy(), MAX_PRERENDERED_DIMENSION)));
    
    layers.add(new LayerAndDrawStrategy(new GridLayer(), new OnDemandDrawStrategy()));
    
    Iterator it = layers.iterator();
    while (it.hasNext()) {
      LayerAndDrawStrategy l = (LayerAndDrawStrategy) it.next();
      l.getLayer().init(map, projection);
      l.getDrawStrategy().init(this);
    }
    addMouseMotionListener(this);
    adjustSize(dimension);
  }
  public void finalize() throws Throwable {
    Iterator it = layers.iterator();
    while (it.hasNext()) {
      LayerAndDrawStrategy l = (LayerAndDrawStrategy) it.next();
      l.getDrawStrategy().destroy();
    }
    super.finalize();
  }
  protected void adjustSize(Dimension dimension) {
    super.setMinimumSize(dimension);
    super.setMaximumSize(dimension);
    super.setPreferredSize(dimension);
    super.setSize(dimension);
    projection.setSize(dimension);
    Iterator it = layers.iterator();
    //maplayer.setSize(dimension);
    while (it.hasNext()) {
      LayerAndDrawStrategy l = (LayerAndDrawStrategy) it.next();
      l.getLayer().setSize(dimension);
      
      l.getDrawStrategy().resize(dimension);
    }
    //clearBuffer();
    /*if (dimension.width < (40 * 360)) {
      createBuffer();
      System.out.println(backBuffer);
      //image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
      maplayer.draw(backBuffer.getGraphics(), new Rectangle(0, 0, dimension.width, dimension.height));
    }*/
    System.out.println("isDisplayable: "+ isDisplayable());
  }
  
  /*public void addNotify() {
    super.addNotify();
    System.out.println("Creating volatile image, isDisplayable: " + isDisplayable());
    vImg = createVolatileImage(dimension.width, dimension.height);
  }*/

  public void setZoom(float zoom) {
    System.out.println("Setting zoom: " + zoom);
    this.zoom = zoom;
    dimension.width = (int) (zoom * 360);
    dimension.height = (int) (zoom * 180);
  
    adjustSize(dimension);

    revalidate();
  }

  public void mouseMoved(MouseEvent me) {
    mousePointer = me.getPoint();
    repaint();
  }
  public void mouseDragged(MouseEvent e) {
  }

  public Dimension getMinimumSize() {
    System.out.println("getMinimumSize: " + super.getMinimumSize() + " map size: " + dimension);
    return super.getMinimumSize();
  }
  public Dimension getPreferredSize() {
    System.out.println("getPreferredSize: " + super.getPreferredSize() + " map size: " + dimension);
    return super.getPreferredSize();
  }
  public Dimension getSize() {
    System.out.println("getSize: " + super.getSize() + " map size: " + dimension);
    return super.getSize();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    Rectangle clip = g2d.getClipBounds();
    System.out.println("    clip: \t" + clip + "\t" + clip.getSize());

    Iterator it = layers.iterator();
    while (it.hasNext()) {
      LayerAndDrawStrategy l = (LayerAndDrawStrategy) it.next();
      Layer layer = l.getLayer();
      DrawStrategy strategy = l.getDrawStrategy();
      strategy.startDraw(g, layer);
      strategy.draw(g, layer);
      strategy.endDraw(g, layer);
    }

/*//  Get a DOMImplementation
       DOMImplementation domImpl =
           GenericDOMImplementation.getDOMImplementation();

       // Create an instance of org.w3c.dom.Document
       Document document = domImpl.createDocument(null, "svg", null);
//  Create an instance of the SVG Generator
         SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
         System.out.println(svgGenerator);

       maplayer.draw(svgGenerator, clip);
       
       try {
    boolean useCSS = true; // we want to use CSS style attribute
            Writer out = new BufferedWriter(new FileWriter("thedrawnmap.svg"));
            svgGenerator.stream(out, useCSS);
         out.close();
       } catch (Exception e) {
         e.printStackTrace();
       }*/

    if (mousePointer != null) {
      Point2D.Double p = new Point2D.Double();
      projection.unproject(mousePointer.x, mousePointer.y, p);
      g.drawString(p.toString(), mousePointer.x, mousePointer.y);
    }
  }
}