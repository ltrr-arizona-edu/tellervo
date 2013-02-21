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

import java.util.Comparator;

import javax.swing.Icon;

import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasRemark;


public class TellervoReadingRemark extends AbstractRemark {
	private final TridasRemark remark;
	
	public TellervoReadingRemark(TridasRemark tellervoRemark) {	
		remark = (TridasRemark) tellervoRemark.clone();
		tellervoRemark.copyTo(remark);
		remark.setValue("");
	}
	
	@Override
	public TridasRemark asTridasRemark() {
		return remark;
	}

	public String getDisplayName() {
		return remark.getNormal();
	}
	
	public Icon getIcon() {
		
		String iconFn;
		if(Remarks.getTellervoRemarkIcons().get(remark.getNormal())!=null)
		{
			iconFn = Remarks.getTellervoRemarkIcons().get(remark.getNormal());	
		}
		else
		{
			iconFn = "note.png";
		}
		
		
		return (iconFn == null) ? null : Builder.getIcon(iconFn, 16);
	}


	
}
