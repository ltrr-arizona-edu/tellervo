// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.Image;

/**
 * Describes a DrawStrategy from which an Image representation is accessible.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public interface ImageBasedDrawStrategy extends DrawStrategy {
  public Image getImage();
}