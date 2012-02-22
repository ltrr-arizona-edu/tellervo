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

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class CornellSampleLabelPage implements LabelPage {
	private final static float dpi = 72.0f;
	private final static float mmpd = dpi / 25.4f;

	public float getLabelHeight() {
		return 8 * mmpd;
		//return 20 * mmpd;
	}

	public float getLabelHorizontalGap() {
		return 0 * mmpd;
	}

	public float getLabelVerticalGap() {
		return 0;
	}

	public float getLabelWidth() {
		return 65 * mmpd;
	}

	public float getPageTopMargin() {
		return 1f * mmpd;
	}
	
	public float getPageBottomMargin() {
		return 1f * mmpd;
	}

	public float getPageLeftMargin() {
		return 1f * mmpd;
	}

	public float getPageRightMargin() {
		return 1f * mmpd;
	}

	public Rectangle getPageSize() {
		return PageSize.LETTER;
	}



}
