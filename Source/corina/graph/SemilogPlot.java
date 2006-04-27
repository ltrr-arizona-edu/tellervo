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

package corina.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import corina.Range;
import corina.Sample;
import corina.Year;
import corina.core.App;
import corina.index.Index;
import corina.util.ColorUtils;

public class SemilogPlot extends StandardPlot implements CorinaGraphPlotter {	
	public SemilogPlot() {
		super();
	}

	// This is only useful for indexed things...
	protected int yTransform(int y) {
		int tval = 1000 + (int) (1000.0 * Math.log(y / 1000.0));
		return tval;
	}
	
	protected boolean validValue(int value) {
		return true;
	}

}
