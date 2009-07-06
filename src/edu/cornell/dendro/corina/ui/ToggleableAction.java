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
public abstract class ToggleableAction extends CorinaAction {	
	/**
	 * 
	 * @param boolPrefName The name of the boolean preference we wrap
	 * @param defaultValue The default value to use if the preference doesn't exist
	 * @param keyTrue The I18n key to use if the value is true
	 * @param keyFalse The I18n key to use if false
	 * @param iconName The name of the icon
	 */
	public ToggleableAction(String boolPrefName, boolean defaultValue,
			String keyTrue, String keyFalse, String iconName) {
		this(boolPrefName, App.prefs.getBooleanPref(boolPrefName, defaultValue),  
				defaultValue, keyTrue, keyFalse, iconName);
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @param iconName
	 */
	public ToggleableAction(String key, boolean defaultValue, String iconName) {
		super(key, iconName, "Icons");
		usePref = false;
		
		putValue(CORINA_SELECTED_KEY, defaultValue);
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 */
	public ToggleableAction(String key, boolean defaultValue) {
		super(key);
		usePref = false;
		
		putValue(CORINA_SELECTED_KEY, defaultValue);
	}

	private final boolean usePref;
	private String keyTrue;
	private String keyFalse;
	private String prefName;

	private ToggleableAction(String boolPrefName, boolean boolValue, boolean defaultValue,
			String keyTrue, String keyFalse, String iconName) {
		super(boolValue ? keyTrue : keyFalse, iconName, "Icons");
		
		this.prefName = boolPrefName;
		this.keyTrue = keyTrue;
		this.keyFalse = keyFalse;
		
		usePref = true;
		
		putValue(CORINA_SELECTED_KEY, boolValue);
	}
	
	@Override
	protected void selectionStateChanged(boolean newSelectedState) {
		if(usePref) {
			// change our name
			putValue(NAME, newSelectedState ? I18n.getText(keyTrue) : I18n.getText(keyFalse));
			
			// change the pref
			App.prefs.setBooleanPref(prefName, newSelectedState);
		}
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