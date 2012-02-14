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

import javax.swing.Icon;

import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasRemark;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * A wrapper for a Tridas Remark
 * 
 * @author Lucas Madar
 */

public class TridasReadingRemark extends AbstractRemark {
	private final TridasRemark remark;
	
	public TridasReadingRemark(NormalTridasRemark value) {
		remark = new TridasRemark();

		// set the normal tridas value
		remark.setNormalTridas(value);
		
		// obnoxious, jaxb...
		remark.setValue("");
	}

	@Override
	public TridasRemark asTridasRemark() {
		return remark;
	}

	public String getDisplayName() {
		return remark.getNormalTridas().value();
	}

	public Icon getIcon() {
		String iconFn = Remarks.getTridasRemarkIcons().get(remark.getNormalTridas());
		
		return (iconFn == null) ? null : Builder.getIcon(iconFn, 16);
	}

}
