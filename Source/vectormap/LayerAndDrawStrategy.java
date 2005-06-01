// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt
// Created on Mar 28, 2005

package vectormap;

/**
 * Holds Layer and DrawStrategy combination
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public final class LayerAndDrawStrategy {
  private Layer layer;
  private DrawStrategy drawStrategy;
  public LayerAndDrawStrategy(Layer layer, DrawStrategy drawStrategy) {
    this.layer = layer;
    this.drawStrategy = drawStrategy;
  }
  public Layer getLayer() {
    return layer;
  }
  public DrawStrategy getDrawStrategy() {
    return drawStrategy;
  }
}
