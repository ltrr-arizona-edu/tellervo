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
package edu.cornell.dendro.corina.util.labels;

import com.lowagie.text.Rectangle;

public interface LabelPage {
	public float getLabelHeight();
	public float getLabelWidth();
	
	public float getPageTopMargin();
	public float getPageBottomMargin();
	public float getPageLeftMargin();
	public float getPageRightMargin();
	
	public float getLabelHorizontalGap();
	public float getLabelVerticalGap();
	
	public Rectangle getPageSize();
}
