// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Materialization strategy that just renders on demand
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class OnDemandDrawStrategy implements DrawStrategy {
  public void init(Component parent) { /* nothing */ }

  public void resize(Dimension d) { /* nothing */ }

  public void startDraw(Graphics g, Layer layer) { /* nothing */ }

  public void draw(Graphics g, Layer layer) {
    layer.draw(g, g.getClipBounds(), false);
  }

  public void endDraw(Graphics g, Layer layer) { /* nothing */ }

  public void destroy() { /* nothing */ }
}