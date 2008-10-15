package edu.cornell.dendro.corina.prefs.wrappers;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A prefwrapper around an Change (e.g., jcombobox, jcheckbox); implements ChangeListener
 * 
 * Useful if you have a boolean or a list of Strings, for instance.
 * 
 * @author lucasm
 *
 * @param <OBJTYPE>
 */

public abstract class ChangeWrapper<OBJTYPE> extends PrefWrapper<OBJTYPE> implements ChangeListener {
	public ChangeWrapper(String prefName, Object defaultValue, Class<?> baseClass) {
		super(prefName, defaultValue, baseClass);
	}

	public ChangeWrapper(String prefName, Object defaultValue) {
		super(prefName, defaultValue);
	}

	public ChangeWrapper(String prefName) {
		super(prefName);
	}

	public abstract void stateChanged(ChangeEvent e);
}
