package edu.cornell.dendro.corina.prefs.wrappers;

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
