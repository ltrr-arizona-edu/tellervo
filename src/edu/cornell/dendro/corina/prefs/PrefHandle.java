package edu.cornell.dendro.corina.prefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import edu.cornell.dendro.corina.core.App;

public class PrefHandle<T> {
	/** The name of the preference */
	protected final String prefName;
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
	
	public PrefHandle(String prefName, Class<T> prefClass, T defaultValue) {
		this.prefName = prefName;
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
	
	protected PrefHandle(String prefName, T defaultValue) {
		this.prefName = prefName;
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
			App.prefs.setPref(prefName, null);
			return;
		}
		
		switch(underlyingType) {
		case STRING:
			App.prefs.setPref(prefName, (String) value);
			break;
			
		case COLOR:
			App.prefs.setColorPref(prefName, (Color) value);
			break;
			
		case DIMENSION:
			App.prefs.setDimensionPref(prefName, (Dimension) value);
			break;
			
		case BOOLEAN:
			App.prefs.setBooleanPref(prefName, (Boolean) value);
			break;

		case INTEGER:
			App.prefs.setIntPref(prefName, (Integer) value);
			break;
			
		case FONT:
			App.prefs.setFontPref(prefName, (Font) value);
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
			return (T) App.prefs.getPref(prefName, (String) defaultReturn);
						
		case COLOR:
			return (T) App.prefs.getColorPref(prefName, (Color) defaultReturn);
			
		case DIMENSION:
			return (T) App.prefs.getDimensionPref(prefName, (Dimension) defaultReturn);
			
		case BOOLEAN: {
			Boolean value = App.prefs.getBooleanPref(prefName, (Boolean) defaultReturn);			
			return (T) value;
		}

		case INTEGER: {
			Integer value = App.prefs.getIntPref(prefName, (Integer) defaultReturn);
			return (T) value;
		}
			
		case FONT:
			return (T) App.prefs.getFontPref(prefName, (Font) defaultReturn);
		}
		
		// not possible to get here!
		throw new IllegalStateException("No such underlying type: " + underlyingType);
	}
}
