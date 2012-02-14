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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A prefwrapper around an item (e.g., jcombobox, jcheckbox); implements ItemListener
 * 
 * Useful if you have a boolean or a list of Strings, for instance.
 * 
 * @author lucasm
 *
 * @param <OBJTYPE>
 */

public abstract class ItemWrapper<OBJTYPE> extends PrefWrapper<OBJTYPE> implements ItemListener {
	public ItemWrapper(String prefName, Object defaultValue, Class<?> baseClass) {
		super(prefName, defaultValue, baseClass);
	}

	public ItemWrapper(String prefName, Object defaultValue) {
		super(prefName, defaultValue);
	}

	public ItemWrapper(String prefName) {
		super(prefName);
	}

	public abstract void itemStateChanged(ItemEvent e);
}
