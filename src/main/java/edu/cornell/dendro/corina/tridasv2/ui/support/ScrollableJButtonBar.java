package edu.cornell.dendro.corina.tridasv2.ui.support;

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