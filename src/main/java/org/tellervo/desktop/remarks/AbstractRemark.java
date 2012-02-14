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
package org.tellervo.desktop.remarks;

import java.util.ListIterator;

import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasValue;

public abstract class AbstractRemark implements Remark {
	/** Construct a copy of this Remark as a tridasRemark */
	public abstract TridasRemark asTridasRemark();
	
	/**
	 * Apply a given remark to a value
	 */
	public void applyRemark(TridasValue value) {
		TridasRemark remark = asTridasRemark();

		// first, remove any remarks
		removeRemarkFromValue(remark, value);
		
		// add the remark
		value.getRemarks().add(remark);
	}

	/**
	 * Check if a remark is set on a given value
	 */
	public boolean isRemarkSet(TridasValue value) {
		TridasRemark remark = asTridasRemark();
		
		for(TridasRemark aRemark : value.getRemarks()) {	
			if(RemarkEquals.remarksEqual(remark, aRemark)) {
				// must test for special 'disabled override'
				if(aRemark.isSetInheritedCount() && aRemark.getInheritedCount() < 0)
					return false;
				
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Check if a remark is inherited
	 */
	public boolean isRemarkInherited(TridasValue value) {
		TridasRemark remark = asTridasRemark();
		
		for(TridasRemark aRemark : value.getRemarks()) {	
			if(RemarkEquals.remarksEqual(remark, aRemark)) {
				// get the inherited count from the remark associated with the value
				return (aRemark.isSetInheritedCount() && aRemark.getInheritedCount() > 0) ? true : false;
			}
		}
		
		return false;
	}

	/**
	 * Force override a remark from a given value
	 */
	public void overrideRemark(TridasValue value) {
		TridasRemark source = asTridasRemark();
		
		// create a copy of the remark, as we're going to be modifying it
		TridasRemark remark = (TridasRemark) source.clone();
		source.copyTo(remark);

		// first, remove any remarks
		removeRemarkFromValue(remark, value);
		
		// flag the new remark as disabled
		remark.setInheritedCount(-1);
		
		// add the remark
		value.getRemarks().add(remark);
	}

	/**
	 * Remove a remark
	 */
	public void removeRemark(TridasValue value) {
		removeRemarkFromValue(asTridasRemark(), value);
	}
	
	/**
	 * Remove all TridasRemarks 'remark' from the given value
	 * @param remark
	 * @param value
	 */
	protected void removeRemarkFromValue(TridasRemark remark, TridasValue value) {
		ListIterator<TridasRemark> remarkIterator = value.getRemarks().listIterator();
		
		while(remarkIterator.hasNext()) {
			TridasRemark aRemark = remarkIterator.next();
			
			if(RemarkEquals.remarksEqual(remark, aRemark))
				remarkIterator.remove();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		// compare against another AbstractRemark (drop through)
		if(o instanceof AbstractRemark)
			o = ((AbstractRemark)o).asTridasRemark();
		
		// compare directly to a TridasRemark
		if(o instanceof TridasRemark) {
			TridasRemark remark = (TridasRemark) o;

			// compare the remarks
			return RemarkEquals.remarksEqual(asTridasRemark(), remark);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return asTridasRemark().hashCode();
	}
}
