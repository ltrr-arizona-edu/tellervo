/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer
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
package edu.cornell.dendro.corina.components.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class OverwritePopupModel extends AbstractModel {
	public static final int OVERWRITE = 1;
	public static final int RENAME = 2;
	public static final int IGNORE = 3;
	
	private int response = IGNORE;
	private String message = "";
	private boolean applyToAll = false;
	
	public OverwritePopupModel(){}

	/**
	 * @return the applyToAll
	 */
	public boolean isApplyToAll() {
		return applyToAll;
	}

	/**
	 * @param argApplyToAll the applyToAll to set
	 */
	public void setApplyToAll(boolean argApplyToAll) {
		boolean old = applyToAll;
		applyToAll = argApplyToAll;
		firePropertyChange("applyToAll", old, applyToAll);
	}

	/**
	 * @return the response
	 */
	public int getResponse() {
		return response;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param argMessage the message to set
	 */
	public void setMessage(String argMessage) {
		String old = message;
		message = argMessage;
		firePropertyChange("message", old, message);
	}

	/**
	 * @param argResponse the response to set
	 */
	public void setResponse(int argResponse) {
		int old = response;
		response = argResponse;
		firePropertyChange("response", old, response);
	}
}
