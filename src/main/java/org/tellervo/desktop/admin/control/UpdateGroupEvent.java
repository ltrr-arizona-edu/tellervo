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

package org.tellervo.desktop.admin.control;

import java.util.ArrayList;

import javax.swing.JDialog;

import org.tellervo.schema.WSISecurityGroup;

import com.dmurph.mvc.MVCEvent;


public class UpdateGroupEvent extends MVCEvent{ // implements ITrackable {
	private static final long serialVersionUID = 1L;
	public final WSISecurityGroup group;
	public final JDialog parent;

	public UpdateGroupEvent(WSISecurityGroup g, JDialog prnt) {
		super(UserGroupAdminController.UPDATE_GROUP);
		group = g;
		parent = prnt;
	}

//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
//	 */
//	@Override
//	public String getTrackingAction() {
//		return "Show";
//	}
//
//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
//	 */
//	@Override
//	public String getTrackingCategory() {
//		return "Bulk Import";
//	}
//
//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
//	 */
//	@Override
//	public String getTrackingLabel() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
//	 */
//	@Override
//	public Integer getTrackingValue() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
