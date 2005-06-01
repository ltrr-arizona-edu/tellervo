// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Projects longitude and latitude coords into x/y coords based
 * on image size (which is derived from some zoom factor).
 * Methods are broken out for longitude and latitude to support
 * some klunky preprocessing that has to occur in VectorMapLayer
 * to avoid the ugly-prime-meridian-spanning-strokes phenomenon.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public interface Projection {
  public void setSize(Dimension size);
  public int projectLongitude(double longitude);
  public int projectLatitude(double latitude);
  public void project(double longitude, double latitude, Point point);
  public void unproject(int x, int y, Point2D.Double point);
}