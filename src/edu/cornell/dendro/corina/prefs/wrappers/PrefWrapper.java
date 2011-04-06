package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import edu.cornell.dendro.corina.core.App;

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
		else
			throw new IllegalArgumentException("I don't know how to load a pref for type " + baseClass);
	}
}
