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
package edu.cornell.dendro.corina.remarks;

import org.tridas.schema.TridasRemark;

import edu.cornell.dendro.corina.util.EmptyStringEqualsBuilder;

public class RemarkEquals {
	private RemarkEquals() {}

	public final static boolean remarksEqual(TridasRemark r1, TridasRemark r2) {
		return remarksEqual(r1, r2, true);
	}
	
	public final static boolean remarksEqual(TridasRemark r1, TridasRemark r2, boolean ignoreInheritedCount) {
		EmptyStringEqualsBuilder e = new EmptyStringEqualsBuilder();
		
		e.append(r1.getNormal(), r2.getNormal());
		e.append(r1.getNormalId(), r2.getNormalId());
		e.append(r1.getNormalStd(), r2.getNormalStd());
		e.append(r1.getValue(), r2.getValue());
		e.append(r1.getNormalTridas(), r2.getNormalTridas());
		
		if(!ignoreInheritedCount)
			e.append(r1.getInheritedCount(), r2.getInheritedCount());
		
		return e.isEquals();
	}
}
