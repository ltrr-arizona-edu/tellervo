// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * A draw strategy that uses a BufferedImage
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class BufferedImageDrawStrategy implements DrawStrategy {
  private Component parent;
  private BufferedImage backBuffer;
  private Dimension dimension;
  private boolean needsRendering;

  public void init(Component parent) {
    this.parent = parent;
  }
  private void clearBuffer() {
    if (backBuffer != null) {
      backBuffer.flush();
      backBuffer = null;
    }
  }

  private void createBuffer() {
    clearBuffer();
    backBuffer = parent.getGraphicsConfiguration().createCompatibleImage(dimension.width, dimension.height, Transparency.OPAQUE);
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

    // First, we validate the back buffer
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
  }

  public void endDraw(Graphics g, Layer layer) {
  }
}