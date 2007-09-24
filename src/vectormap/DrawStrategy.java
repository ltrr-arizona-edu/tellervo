// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Interface representing a strategy for materializing a portion of the
 * view (given by graphics bounds or clipping box) at a given zoom onto
 * a rendering context.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public interface DrawStrategy {
  public void init(Component parent);
  public void resize(Dimension d);
  public void startDraw(Graphics g, Layer layer);
  public void draw(Graphics g, Layer layer);
  public void endDraw(Graphics g, Layer layer);
  public void destroy();
}