// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * DrawStrategy wrapper that uses a fixed size.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class ConstantSizeDrawStrategy implements DrawStrategy {
  private final DrawStrategy nested;
  private final Dimension dimension;
  public ConstantSizeDrawStrategy(Dimension d, DrawStrategy strategy) {
    this.dimension = d;
    this.nested = strategy;
  }

  public void init(Component parent) {
    nested.init(parent);
  }

  public void resize(Dimension d) {
    nested.resize(dimension);
  }

  public void startDraw(Graphics g, Layer layer) {
    nested.startDraw(g, layer);
  }

  public void draw(Graphics g, Layer layer) {
    nested.draw(g, layer);
  }

  public void endDraw(Graphics g, Layer layer) {
    nested.endDraw(g, layer);
  }

  public void destroy() {
    nested.destroy();
  }
}