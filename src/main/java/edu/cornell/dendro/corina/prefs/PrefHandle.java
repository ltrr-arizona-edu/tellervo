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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;

public class PrefHandle<T> {
	/** The name of the preference */
	protected final PrefKey prefKey;
	/** The default value of this preference (can be null) */
	protected final T defaultValue;
	
	/** The type we use to determine which functions to use for setting */
	private final HandleType underlyingType;
	
	private static enum HandleType {
		STRING,
		COLOR,
		BOOLEAN,
		INTEGER,
		DIMENSION,
		FONT
	}	
	
	public PrefHandle(PrefKey key, Class<T> prefClass, T defaultValue) {
		this.prefKey = key;
		this.defaultValue = defaultValue;

		// which routine do we use to set/get?
		if(prefClass.isEnum())
			throw new IllegalArgumentException("Use EnumPrefHandle for enums");
		
		if(String.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.STRING;
		else if(Color.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.COLOR;
		else if(Dimension.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.DIMENSION;
		else if(Boolean.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.BOOLEAN;
		else if(Integer.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.INTEGER;
		else if(Font.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.FONT;
		else
			throw new IllegalArgumentException("Not sure how to handle this type: " + prefClass.getName());

	}
	
	@Deprecated
	public PrefHandle(String prefName, Class<T> prefClass, T defaultValue) {
		this.prefKey = PrefKey.valueOf(prefName);
		this.defaultValue = defaultValue;
		// which routine do we use to set/get?
		if(prefClass.isEnum())
			throw new IllegalArgumentException("Use EnumPrefHandle for enums");
		
		if(String.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.STRING;
		else if(Color.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.COLOR;
		else if(Dimension.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.DIMENSION;
		else if(Boolean.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.BOOLEAN;
		else if(Integer.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.INTEGER;
		else if(Font.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.FONT;
		else
			throw new IllegalArgumentException("Not sure how to handle this type: " + prefClass.getName());

	}
	
	
	protected PrefHandle(PrefKey key, T defaultValue) {
		this.prefKey = key;
		this.defaultValue = defaultValue;
		this.underlyingType = null;
	}

	/**
	 * Effectively removes this preference
	 */
	public void clear() {
		set(null);
	}
	
	/** 
	 * Sets the preference to the given value
	 * @param value
	 */
	public void set(T value) {
		
		// special case for nulls (unset)
		if(value == null) {
			App.prefs.setPref(prefKey, null);
			return;
		}
		
		switch(underlyingType) {
		case STRING:
			App.prefs.setPref(prefKey, (String) value);
			break;
			
		case COLOR:
			App.prefs.setColorPref(prefKey, (Color) value);
			break;
			
		case DIMENSION:
			App.prefs.setDimensionPref(prefKey, (Dimension) value);
			break;
			
		case BOOLEAN:
			App.prefs.setBooleanPref(prefKey, (Boolean) value);
			break;

		case INTEGER:
			App.prefs.setIntPref(prefKey, (Integer) value);
			break;
			
		case FONT:
			App.prefs.setFontPref(prefKey, (Font) value);
			break;
		}
	}
	
	/**
	 * @return true if this preference has a non-null value
	 */
	public boolean isSet() {
		return get(null) != null;
	}

	/**
	 * Get the value of this preference
	 * @return the value of this preference, or its default
	 */
	public T get() {
		return get(defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public T get(T defaultReturn) {
		
		// Apologies for the horrible misuse of generics
		// It was either this or a StringPrefHandle, ColorPrefHandle, etc...
		
		switch(underlyingType) {
		case STRING:
			return (T) App.prefs.getPref(prefKey, (String) defaultReturn);
						
		case COLOR:
			return (T) App.prefs.getColorPref(prefKey, (Color) defaultReturn);
			
		case DIMENSION:
			return (T) App.prefs.getDimensionPref(prefKey, (Dimension) defaultReturn);
			
		case BOOLEAN: {
			Boolean value = App.prefs.getBooleanPref(prefKey, (Boolean) defaultReturn);			
			return (T) value;
		}

		case INTEGER: {
			Integer value = App.prefs.getIntPref(prefKey, (Integer) defaultReturn);
			return (T) value;
		}
			
		case FONT:
			return (T) App.prefs.getFontPref(prefKey, (Font) defaultReturn);
		}
		
		// not possible to get here!
		throw new IllegalStateException("No such underlying type: " + underlyingType);
	}
}
