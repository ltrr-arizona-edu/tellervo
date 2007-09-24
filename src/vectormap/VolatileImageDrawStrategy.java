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

/**
 * A DrawStrategy that renders serially onto a VolatileImage backBuffer.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class VolatileImageDrawStrategy implements ImageBasedDrawStrategy {
  private Component parent;
  private VolatileImage backBuffer;
  private Dimension dimension;
  private boolean needsRendering;

  public void init(Component parent) {
    this.parent = parent;
  }
  public Image getImage() {
    return backBuffer;
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
        Dimension layerDimension = layer.getSize();
        vg.clearRect(0, 0, layerDimension.width, layerDimension.height);
        layer.draw(vg, new Rectangle(layerDimension), false);
        needsRendering = false;
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