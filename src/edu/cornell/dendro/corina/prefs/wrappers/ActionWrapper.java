package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A prefwrapper around an Action (e.g., jcombobox, jcheckbox); implements ActionListener
 * 
 * Useful if you have a boolean or a list of Strings, for instance.
 * 
 * @author lucasm
 *
 * @param <OBJTYPE>
 */

public abstract class ActionWrapper<OBJTYPE> extends PrefWrapper<OBJTYPE> implements ActionListener {
	public ActionWrapper(String prefName, Object defaultValue, Class<?> baseClass) {
		super(prefName, defaultValue, baseClass);
	}

	public ActionWrapper(String prefName, Object defaultValue) {
		super(prefName, defaultValue);
	}

	public ActionWrapper(String prefName) {
		super(prefName);
	}

	public abstract void actionPerformed(ActionEvent e);
}
