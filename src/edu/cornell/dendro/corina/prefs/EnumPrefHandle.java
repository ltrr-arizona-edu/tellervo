package edu.cornell.dendro.corina.prefs;

import edu.cornell.dendro.corina.core.App;

public class EnumPrefHandle<T extends Enum<T>> extends PrefHandle<T> {
	/** The type of this preference */
	protected final Class<T> type;

	public EnumPrefHandle(String prefName, Class<T> prefClass, T defaultValue) {
		super(prefName, defaultValue);
		
		// store the enum preference
		this.type = prefClass;
	}

	@Override
	public T get(T defaultReturn) {
		return App.prefs.getEnumPref(prefName, defaultReturn, type);
	}

	@Override
	public void set(T value) {
		App.prefs.setPref(prefName, value.toString());
	}	
}
