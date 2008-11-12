// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Interface which encapsulates drawing of (one of potentially many) a composable
 * layer onto a 2D surface.
 * NOTE: current implementations rely on proper clip bounding rectangle on the graphics object
 * to obtain viewport info. This may need to be changed to pass a view rect Rectangle
 * (taken from JViewport.getViewRect()) at some later point, but for now they happen to always
 * coincide (nice coincidence).
 * XXX: above note may be out of date
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public interface Layer {
  public void init(VectorMap map, Projection projection);
  public void setSize(Dimension size);
  public Dimension getSize();
  public void draw(Graphics g, Rectangle viewrect, boolean exclusive);
}