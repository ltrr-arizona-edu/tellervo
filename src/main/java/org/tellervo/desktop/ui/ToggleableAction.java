/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.ui;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

public abstract class ToggleableAction extends TellervoAction {
	private static final long serialVersionUID = 1L;

	/**
	 * @param key
	 * @param icon
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue, Icon icon) {
		super(key, icon);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * @param key
	 * @param iconName
	 * @param iconPackageName
	 * @param toggleValue
	 * @param iconSize
	 */
	public ToggleableAction(String key, boolean toggleValue, String iconName, String iconPackageName, int iconSize) {
		super(key, iconName, iconPackageName, iconSize);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * @param key
	 * @param iconName
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue, String iconName, int iconSize) {
		super(key, iconName, iconSize);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * @param key
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue) {
		super(key);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * Override and force actionPerformed to use togglePerformed instead
	 */
	public final void actionPerformed(ActionEvent ae) {
		togglePerformed(ae, (Boolean) getValue(CORINA_SELECTED_KEY));
	}
	
	/**
	 * Called when an action is performed
	 * @param ae
	 * @param value
	 */
	public abstract void togglePerformed(ActionEvent ae, Boolean value);
}
