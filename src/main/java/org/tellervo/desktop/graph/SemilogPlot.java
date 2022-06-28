/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.graph;

public class SemilogPlot extends StandardPlot implements TellervoGraphPlotter {	
	public SemilogPlot() {
		super();
	}
	
	// This is only useful for indexed things...
	@Override
	protected int yTransform(float y) {
		/*
		int tval = 1000 + (int) (1000.0 * Math.log(y / 1000.0));
		return tval;
		*/
		return (int) (Math.log10(y) * 1000.0);
	}
	
	@Override
	protected boolean validValue(int value) {
		return true;
	}

	@Override
	public int getFirstValue(Graph g) {
		return g.graph.getRingWidthData().get(0).intValue()*1000;
	}
	
}
