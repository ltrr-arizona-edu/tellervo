// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * DrawStrategy that delegates to one DrawStrategy while under
 * a dimension threshold, and to another once the threshold is reached.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class ThresholdDrawStrategy implements DrawStrategy {
  private DrawStrategy first;
  private DrawStrategy second;
  private Dimension thresholdDimension;
  private DrawStrategy current;

  public ThresholdDrawStrategy(DrawStrategy first, DrawStrategy second, Dimension dim) {
    this.first = first;
    this.second = second;
    this.current = first;
    this.thresholdDimension = dim;
  }

  public void init(Component parent) {
    first.init(parent);
    second.init(parent);
  }

  public void resize(Dimension d) {
    if (d.width < thresholdDimension.width) {
      current = first;
    } else {
      current = second;
    }
    current.resize(d);
  }

  public void startDraw(Graphics g, Layer layer) {
    current.startDraw(g, layer);
  }

  public void draw(Graphics g, Layer layer) {
    current.draw(g, layer);
  }

  public void endDraw(Graphics g, Layer layer) {
    current.endDraw(g, layer);
  }

  public void destroy() {
    first.destroy();
    second.destroy();
  }

}
