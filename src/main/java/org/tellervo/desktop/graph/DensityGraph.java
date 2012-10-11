/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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

/*
 * Density graph: graphable wrapper, essentially.
 */

import java.util.List;

import org.tellervo.desktop.Year;
import org.tridas.schema.TridasValue;


public class DensityGraph implements Graphable {
	@SuppressWarnings("unchecked")
	private List data;
	private Year start;
	private String gname;
	
	@SuppressWarnings("unchecked")
	public DensityGraph(List data, Year start, String name) {		
		this.data = data;
		this.start = start;
		this.gname = name;
	}
	
    @SuppressWarnings("unchecked")
	public List getRingWidthData() {
        return data;
    }
    
    public Year getStart() {
        return start;
    }
    
    public float getScale() {
        return 1.0f;
    }
    
    @Override
	public String toString() {
        return gname;
    }

	@Override
	public List<TridasValue> getTridasData() {
		// TODO Auto-generated method stub
		return null;
	}
}
