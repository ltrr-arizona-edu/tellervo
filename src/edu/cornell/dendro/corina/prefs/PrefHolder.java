package edu.cornell.dendro.corina.prefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Provides a simple framework for making package preferences
 * 
 * @author Lucas Madar
 */

public abstract class PrefHolder {
	protected PrefHolder() {
		
	}
	
	/**
	 * Make an enum pref
	 * @param <T>
	 * @param name
	 * @param type
	 * @param deflt
	 * @return
	 */
	protected final static <T extends Enum<T>> PrefHandle<T> mkEnum(String prefix, String name, Class<T> type, T deflt) {
		return new EnumPrefHandle<T>(prefix + name, type, deflt);
	}
	
	/**
	 * Make a normal pref
	 * @param <T>
	 * @param name
	 * @param type
	 * @param deflt
	 * @return
	 */
	protected final static <T> PrefHandle<T> mkPref(String prefix, String name, Class<T> type, T deflt) {
		return new PrefHandle<T>(prefix + name, type, deflt);
	}
	
	protected final static PrefHandle<String> mkPref(String prefix, String name, String deflt) {
		return mkPref(prefix, name, String.class, deflt);
	}

	protected final static PrefHandle<Color> mkPref(String prefix, String name, Color deflt) {
		return mkPref(prefix, name, Color.class, deflt);
	}
	
	protected final static PrefHandle<Font> mkPref(String prefix, String name, Font deflt) {
		return mkPref(prefix, name, Font.class, deflt);
	}

	protected final static PrefHandle<Dimension> mkPref(String prefix, String name, Dimension deflt) {
		return mkPref(prefix, name, Dimension.class, deflt);
	}
	
	protected final static PrefHandle<Boolean> mkPref(String prefix, String name, Boolean deflt) {
		return mkPref(prefix, name, Boolean.class, deflt);
	}
	
	protected final static PrefHandle<Integer> mkPref(String prefix, String name, Integer deflt) {
		return mkPref(prefix, name, Integer.class, deflt);
	}
}
