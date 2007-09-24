// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Rectangle;
import java.awt.image.VolatileImage;
import java.util.Iterator;
import java.util.List;

/**
 * Draw strategy that asynchronously queues image rendering.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class AsynchronousVolatileImageDrawStrategy implements ImageBasedDrawStrategy {
  private static final class RenderRequest {
    private Graphics graphics;
    private Layer layer;
    private Rectangle region;
    private boolean excludes;
  }
  private static final class Worker implements Runnable {
    private Queue queue;
    private boolean stop;
    private AsynchronousVolatileImageDrawStrategy owner;
    
    public Worker(Queue queue, AsynchronousVolatileImageDrawStrategy owner) {
      this.queue = queue;
      this.owner = owner;
    }
    public void stop() {
      stop = true;
    }
    public void run() {
      while (!stop) {
        System.out.println("Worker looping...");
        List items;
        while (true) try {
          System.out.println("Waiting for request...");
          items = queue.dequeueAll();
          break;
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
        
        owner.setNeedsRendering(false);
        Iterator iterator = items.iterator();
        System.out.println("Worker got: " + items.size() + " items");
        while (iterator.hasNext()) {
          RenderRequest[] requests = (RenderRequest[]) iterator.next();
          System.out.println("Worker got: " + requests.length + " requests");
          for (int i = 0; i < requests.length; i++) {
            System.out.println("drawing: " + i);
            requests[i].layer.draw(requests[i].graphics, requests[i].region, requests[i].excludes);
            System.out.println("done drawing: " + i);
            owner.sendRepaint();
          }
        }
        System.out.println("Worker continuing loop...");
      }
      System.out.println("Worker EXITING LOOP...");
    }
  }
  private Queue workQueue = new Queue();
  private Worker worker = new Worker(workQueue, this);
  private Component parent;
  private VolatileImage backBuffer;
  private Dimension dimension;
  private boolean needsRendering;

  public void init(Component parent) {
    this.parent = parent;
    Thread t = new Thread(worker);
    t.setDaemon(true);
    //t.setPriority(Thread.MAX_PRIORITY);
    t.start();
  }
  public Image getImage() {
    return backBuffer;
  }
  private void setNeedsRendering(boolean b) {
    needsRendering = b;
  }
  private void sendRepaint() {
    System.out.println("GOT REPAINT");
    parent.invalidate();
    parent.repaint();
  }
  private void clearBuffer() {
    if (backBuffer != null) {
      backBuffer.flush();
      backBuffer = null;
    }
  }

  private void createBuffer() {
    clearBuffer(); 
    ImageCapabilities ic = new ImageCapabilities(true);
    try {
      //backBuffer = createVolatileImage(MAX_PRERENDERED_DIMENSION.width, MAX_PRERENDERED_DIMENSION.height, ic);
      GraphicsConfiguration gc = parent.getGraphicsConfiguration();
      backBuffer = gc.createCompatibleVolatileImage(dimension.width, dimension.height);
      System.out.println("Requested size: " + dimension);
      System.out.println("Received size: " + backBuffer.getWidth() + ", " + backBuffer.getHeight());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void resize(Dimension dim) {
    if (!dim.equals(dimension)) {
      this.dimension = dim;
      clearBuffer();
    }
    needsRendering = true;
  }

  public void destroy() {
    clearBuffer();
    worker.stop();
  }

  public void finalize() throws Throwable {
    clearBuffer();
    super.finalize();
  }

  public void startDraw(Graphics g, Layer layer) {
    if (backBuffer == null) {
      createBuffer();
      needsRendering = true;
    }
  }

  public void draw(Graphics g, Layer layer) {
    Graphics2D g2d = (Graphics2D) g;
    while (true) {
      // First, we validate the back buffer
      int returnCode = backBuffer.validate(parent.getGraphicsConfiguration());
      if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
        createBuffer();
        needsRendering = true;
      } else if (returnCode == VolatileImage.IMAGE_RESTORED) {
        needsRendering = true;
      }
      if (needsRendering) {
        Graphics vg = backBuffer.getGraphics();
        vg.clearRect(0, 0, dimension.width, dimension.height);

        System.out.println("Draw, needs rendering...");
        // render in the bounds
        RenderRequest[] requests = new RenderRequest[2];
        requests[0] = new RenderRequest();
        requests[0].graphics = backBuffer.getGraphics();
        requests[0].layer = layer;
        requests[0].region = new Rectangle(g.getClipBounds());
        requests[0].excludes = false;
        // render outside the bounds
        requests[1] = new RenderRequest();
        requests[1].graphics = backBuffer.getGraphics();
        requests[1].layer = layer;
        requests[1].region = new Rectangle(g.getClipBounds());
        requests[1].excludes = true;
        while (true) try {
          workQueue.queue(requests);
          break;
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
      // Now we've handled validation, get on with the rendering
      //g.drawImage(vImg, clip.x, clip.y, clip.width, clip.height, clip.x, clip.y, clip.width, clip.height, this);
      // apparently the clip is taken care of automatically
      g2d.drawImage(backBuffer, 0, 0, Color.white, parent);
      if (backBuffer.contentsLost()) {
        needsRendering = true;
      } else {
        break;
      }
    }
  }

  public void endDraw(Graphics g, Layer layer) {
  }
}