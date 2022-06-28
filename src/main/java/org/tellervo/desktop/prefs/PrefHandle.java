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
package org.tellervo.desktop.prefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.hardware.device.LintabDevice;
import org.tellervo.desktop.prefs.Prefs.PrefKey;


public class PrefHandle<T> {
	/** The name of the preference */
	protected final String pref;
	/** The default value of this preference (can be null) */
	protected final T defaultValue;
	private final static Logger log = LoggerFactory.getLogger(PrefHandle.class);

	/** The type we use to determine which functions to use for setting */
	private final HandleType underlyingType;
	
	private static enum HandleType {
		STRING,
		COLOR,
		BOOLEAN,
		INTEGER,
		DIMENSION,
		FONT,
		DOUBLE
	}	
	
	public PrefHandle(PrefKey key, Class<T> prefClass, T defaultValue) {
		this.pref = key.toString();
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
		this.pref = prefName;
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
		else if(Double.class.isAssignableFrom(prefClass))
			underlyingType = HandleType.DOUBLE;
		else
			throw new IllegalArgumentException("Not sure how to handle this type: " + prefClass.getName());

	}
	
	
	protected PrefHandle(String key, T defaultValue) {
		this.pref = key;
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
			App.prefs.setPref(pref, null);
			return;
		}
		
		switch(underlyingType) {
		case STRING:
			App.prefs.setPref(pref, (String) value);
			break;
			
		case COLOR:
			App.prefs.setColorPref(pref, (Color) value);
			break;
			
		case DIMENSION:
			App.prefs.setDimensionPref(pref, (Dimension) value);
			break;
			
		case BOOLEAN:
			App.prefs.setBooleanPref(pref, (Boolean) value);
			break;

		case INTEGER:
			App.prefs.setIntPref(pref, (Integer) value);
			break;
			
		case FONT:
			App.prefs.setFontPref(pref, (Font) value);
			break;
			
		case DOUBLE:
			App.prefs.setDoublePref(pref, (Double) value);
			break;
		
		default:
			log.error("Unsupported data type in PrefHandle");
			
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
			return (T) App.prefs.getPref(pref, (String) defaultReturn);
						
		case COLOR:
			return (T) App.prefs.getColorPref(pref, (Color) defaultReturn);
			
		case DIMENSION:
			return (T) App.prefs.getDimensionPref(pref, (Dimension) defaultReturn);
			
		case BOOLEAN: {
			Boolean value = App.prefs.getBooleanPref(pref, (Boolean) defaultReturn);			
			return (T) value;
		}

		case INTEGER: {
			Integer value = App.prefs.getIntPref(pref, (Integer) defaultReturn);
			return (T) value;
		}
			
		case FONT:
			return (T) App.prefs.getFontPref(pref, (Font) defaultReturn);
		}
		
		// not possible to get here!
		throw new IllegalStateException("No such underlying type: " + underlyingType);
	}
}
