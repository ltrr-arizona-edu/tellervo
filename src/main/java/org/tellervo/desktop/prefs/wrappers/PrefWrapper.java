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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import org.tellervo.desktop.core.App;


/**
 * A wrapper around a preference string
 * Defaults to autocommit (you set a preference value, it is saved automatically)
 * 
 * This class is not suitable for generic-ization because compile-time checks are useless;
 * the underlying prefs class doesn't use generics.
 * Thus, OBJTYPE is only used to type-check setValue and getValue (and remove ugly casting)
 * 
 * @author lucasm
 *
 */

public abstract class PrefWrapper<OBJTYPE> {
	private String prefName;
	private Object prefValue;
	private Object defaultValue;
	private Class<?> baseClass;
	private boolean valueModified;
	private boolean autocommit = true;
	
	/**
	 * Create a new wrapper for this preference name, 
	 * wrapping the specified type
	 * @param prefname
	 */
	public PrefWrapper(String prefName, Object defaultValue, Class<?> baseClass) {
		this.prefName = prefName;
		this.baseClass = baseClass;
		this.defaultValue = defaultValue;
		
		valueModified = false;
		load();		
	}
	
	/**
	 * Shortcut for creating a string-based pref
	 * @param prefName
	 */
	public PrefWrapper(String prefName, Object defaultValue) {
		this(prefName, defaultValue, String.class);
	}
	
	/**
	 * Shortcut for creating a string-based pref with no default
	 * @param prefName
	 */
	public PrefWrapper(String prefName) {
		this(prefName, null, String.class);
	}
	
	public boolean isModified() {
		return valueModified;
	}

	/**
	 * Set the value of this preference
	 * @param value
	 */
	public void setValue(OBJTYPE value) {
		// same value? ignore it
		if(prefValue == value)
			return;
		
		// they equal the same thing? ignore it
		if(prefValue != null && prefValue.equals(value))
			return;
		
		valueModified = true;
		prefValue = value;
		
		if(autocommit)
			commit();
	}

	/**
	 * Get the value of the pref referenced by this wrapper
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OBJTYPE getValue() {
		return (OBJTYPE) prefValue;
	}
	
	/**
	 * Commit the value represented in this pref to
	 * prefs storage. Not useful to call if autocommit is on (which it is by default)
	 */
	public void commit() {
		if(!valueModified)
			return;
		
		if(prefValue == null) {
			App.prefs.removePref(prefName);
			valueModified = false;
			return;
		}
		
		if(baseClass == String.class)
			App.prefs.setPref(prefName, (String) prefValue);
		else if(baseClass == Font.class)
			App.prefs.setFontPref(prefName, (Font) prefValue);
		else if(baseClass == Integer.class)
			App.prefs.setIntPref(prefName, (Integer) prefValue);
		else if(baseClass == Dimension.class)
			App.prefs.setDimensionPref(prefName, (Dimension) prefValue);
		else if(baseClass == Color.class)
			App.prefs.setColorPref(prefName, (Color) prefValue);
		else if(baseClass == Boolean.class)
			App.prefs.setBooleanPref(prefName, (Boolean) prefValue);
		else if(baseClass == Double.class)
			App.prefs.setDoublePref(prefName, (Double) prefValue);
		
		valueModified = false;
	}
	
	private void load() {
		if(baseClass == String.class)
			prefValue = App.prefs.getPref(prefName, (String) defaultValue);
		else if(baseClass == Font.class) 
			prefValue = App.prefs.getFontPref(prefName, (Font) defaultValue);
		else if(baseClass == Integer.class)
			prefValue = App.prefs.getIntPref(prefName, (Integer) defaultValue);
		else if(baseClass == Dimension.class) 
			prefValue = App.prefs.getDimensionPref(prefName, (Dimension) defaultValue); 
		else if(baseClass == Color.class)
			prefValue = App.prefs.getColorPref(prefName, (Color) defaultValue);
		else if(baseClass == Boolean.class)
			prefValue = App.prefs.getBooleanPref(prefName, (Boolean) defaultValue);
		else if(baseClass == Double.class)
			prefValue = App.prefs.getDoublePref(prefName, (Double) defaultValue);
		else
			throw new IllegalArgumentException("I don't know how to load a pref for type " + baseClass);
	}
}
