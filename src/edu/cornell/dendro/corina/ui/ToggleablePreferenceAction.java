/**
 * 
 */
package edu.cornell.dendro.corina.ui;

import java.awt.event.ActionEvent;

import edu.cornell.dendro.corina.core.App;

/**
 * An action that wraps a boolean application preference and two I18n key values
 * Used to make nice toggle buttons!
 * 
 * @author Lucas Madar
 *
 */

@SuppressWarnings("serial")
public abstract class ToggleablePreferenceAction extends ToggleableAction {	
	/**
	 * 
	 * @param boolPrefName The name of the boolean preference we wrap
	 * @param defaultValue The default value to use if the preference doesn't exist
	 * @param keyTrue The I18n key to use if the value is true
	 * @param keyFalse The I18n key to use if false
	 * @param iconName The name of the icon
	 * @param iconSize The size of the icon
	 */
	public ToggleablePreferenceAction(String boolPrefName, boolean defaultValue,
			String keyTrue, String keyFalse, String iconName, int iconSize) {
		this(boolPrefName, App.prefs.getBooleanPref(boolPrefName, defaultValue),  
				defaultValue, keyTrue, keyFalse, iconName, iconSize);
	}


	private String keyTrue;
	private String keyFalse;
	private String prefName;

	/**
	 * 
	 * @param boolPrefName the name of the preference
	 * @param boolValue the current value
	 * @param defaultValue the value to use if it doesn't exist (NOT USED!)
	 * @param keyTrue the i18n key to use for name on true
	 * @param keyFalse the i18n key to use on false
	 * @param iconName the name of the icon
	 * @param iconSize The size of the icon
	 */
	private ToggleablePreferenceAction(String boolPrefName, boolean boolValue, boolean defaultValue,
			String keyTrue, String keyFalse, String iconName, int iconSize) {
		super(boolValue ? keyTrue : keyFalse, boolValue, iconName, Builder.ICONS, iconSize);
		
		this.prefName = boolPrefName;
		this.keyTrue = keyTrue;
		this.keyFalse = keyFalse;
	}
	
	@Override
	protected void selectionStateChanged(boolean newSelectedState) {
		// change our name
		putValue(NAME, newSelectedState ? I18n.getText(keyTrue) : I18n.getText(keyFalse));
			
		// change the pref
		App.prefs.setBooleanPref(prefName, newSelectedState);
	}
		
	/**
	 * Called when an action is performed
	 * @param ae
	 * @param value
	 */
	public abstract void togglePerformed(ActionEvent ae, Boolean value);
}