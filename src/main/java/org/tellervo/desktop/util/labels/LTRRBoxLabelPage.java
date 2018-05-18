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
package org.tellervo.desktop.util.labels;

import com.itextpdf.text.Rectangle;

public class LTRRBoxLabelPage implements LabelPage {
	private final static float dpi = 72.0f;

	private final static float H = 1.5f * dpi;
	private final static float W = 3.5f * dpi;

	private final static float LEFTRIGHTMARGIN = .17f * dpi;
	private final static float TOPBOTTOMMARGIN = .50f * dpi;

	private final static float LABELGAP = 0;

	public float getLabelHeight() {
		return H;
	}

	public float getLabelHorizontalGap() {
		return LABELGAP;
	}

	public float getLabelVerticalGap() {
		return 0;
	}

	public float getLabelWidth() {
		return W;
	}

	public float getPageBottomMargin() {
		return TOPBOTTOMMARGIN;
	}

	public float getPageLeftMargin() {
		return LEFTRIGHTMARGIN;
	}

	public float getPageRightMargin() {
		return LEFTRIGHTMARGIN;
	}

	public Rectangle getPageSize() {
		// 3.5 x 1.5" labels
		return new Rectangle(252, 108);
	}

	public float getPageTopMargin() {
		return TOPBOTTOMMARGIN;
	}

}
