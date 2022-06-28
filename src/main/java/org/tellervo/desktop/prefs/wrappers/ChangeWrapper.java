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
package org.tellervo.desktop.prefs.wrappers;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.tellervo.desktop.prefs.Prefs.PrefKey;

/**
 * A prefwrapper around an Change (e.g., jcombobox, jcheckbox); implements ChangeListener
 * 
 * Useful if you have a boolean or a list of Strings, for instance.
 * 
 * @author lucasm
 *
 * @param <OBJTYPE>
 */

public abstract class ChangeWrapper<OBJTYPE> extends PrefWrapper<OBJTYPE> implements ChangeListener {
	public ChangeWrapper(PrefKey prefName, Object defaultValue, Class<?> baseClass) {
		super(prefName, defaultValue, baseClass);
	}

	public ChangeWrapper(PrefKey prefName, Object defaultValue) {
		super(prefName, defaultValue);
	}

	public ChangeWrapper(PrefKey prefName) {
		super(prefName);
	}

	public abstract void stateChanged(ChangeEvent e);
}
