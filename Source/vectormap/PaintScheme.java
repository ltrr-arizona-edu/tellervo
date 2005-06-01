// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Color;
import java.awt.Stroke;

/**
 * An interface encapsulating a scheme which provides colors and
 * stroke styles for painting the map.  Created so that both the "fixed"/hardcode
 * Corina scheme, as well as one configurable through an rmap.colors file
 * could be supported.  To make the rmap.colors file scheme more useful, it should
 * probably be extended to allow specification of stroke styles (currently it just
 * specifies colors).
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public interface PaintScheme {
  public Brush[][] getScheme();
  
  public static final class Brush {
    public final Color color;
    public final Stroke stroke;
    public Brush(Color c, Stroke s) {
      color = c;
      stroke = s;
    }
  }
}