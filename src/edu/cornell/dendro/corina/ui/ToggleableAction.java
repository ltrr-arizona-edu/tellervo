package edu.cornell.dendro.corina.ui;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

public abstract class ToggleableAction extends CorinaAction {
	private static final long serialVersionUID = 1L;

	/**
	 * @param key
	 * @param icon
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue, Icon icon) {
		super(key, icon);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * @param key
	 * @param iconName
	 * @param iconPackageName
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue, String iconName, String iconPackageName) {
		super(key, iconName, iconPackageName);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * @param key
	 * @param iconName
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue, String iconName) {
		super(key, iconName);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * @param key
	 * @param toggleValue
	 */
	public ToggleableAction(String key, boolean toggleValue) {
		super(key);
		
		putValue(CORINA_SELECTED_KEY, toggleValue);
	}

	/**
	 * Override and force actionPerformed to use togglePerformed instead
	 */
	public final void actionPerformed(ActionEvent ae) {
		togglePerformed(ae, (Boolean) getValue(CORINA_SELECTED_KEY));
	}
	
	/**
	 * Called when an action is performed
	 * @param ae
	 * @param value
	 */
	public abstract void togglePerformed(ActionEvent ae, Boolean value);
}
