//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.graph;

public class SemilogPlot extends StandardPlot implements CorinaGraphPlotter {	
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
		return (int) (Math.log10(y) * 100.0);
	}
	
	@Override
	protected boolean validValue(int value) {
		return true;
	}

}
