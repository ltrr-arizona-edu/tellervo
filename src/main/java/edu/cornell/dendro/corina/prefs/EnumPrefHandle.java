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
package edu.cornell.dendro.corina.prefs;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;

public class EnumPrefHandle<T extends Enum<T>> extends PrefHandle<T> {
	/** The type of this preference */
	protected final Class<T> type;

	public EnumPrefHandle(PrefKey key, Class<T> prefClass, T defaultValue) {
		super(key, defaultValue);
		
		// store the enum preference
		this.type = prefClass;
	}
	
	@Deprecated
	public EnumPrefHandle(String prefName, Class<T> prefClass, T defaultValue) {
		super(PrefKey.valueOf(prefName), defaultValue);
		
		// store the enum preference
		this.type = prefClass;
	}

	@Override
	public T get(T defaultReturn) {
		return App.prefs.getEnumPref(prefKey, defaultReturn, type);
	}

	@Override
	public void set(T value) {
		App.prefs.setPref(prefKey, value.toString());
	}	
}
