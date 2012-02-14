/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui.support;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Scrollable;
import javax.swing.border.Border;

import com.l2fprod.common.swing.JButtonBar;

/**
 * An implementation of JButtonBar that properly supports scrolling
 * 
 * @author Lucas Madar
 * @version $Id$
 */

public class ScrollableJButtonBar extends JButtonBar implements Scrollable {
	private static final long serialVersionUID = 1L;

	public ScrollableJButtonBar(int orientation) {
		super(orientation);
	}

	public Dimension getPreferredScrollableViewportSize() {
		Dimension preferred = getPreferredSize();
		Border border = getBorder();
		int extraWidth = 0;
		
		if(border != null) {
			Insets insets = border.getBorderInsets(this);
			extraWidth += insets.left + insets.right;
		}
		
		return new Dimension(preferred.width + extraWidth, preferred.height);
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		
		return visibleRect.height;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		
		Component component[] = getComponents();
		
		if(component.length == 0)
			// ok, no components, why are we scrolling?
			return 10;
		
		return component[0].getHeight();
	}
}
