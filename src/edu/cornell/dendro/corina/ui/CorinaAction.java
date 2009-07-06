package edu.cornell.dendro.corina.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import edu.cornell.dendro.corina.core.App;

/**
 * A base Corina action which sets Mnemonic and Accelerator based on I18N settings.
 * @author Aaron Hamid
 * @author Lucas Madar
 */
public abstract class CorinaAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct an action, given an i18n key
	 * @param key
	 */
	public CorinaAction(String key) {
		super(I18n.getText(key));
		initializeMnemonics(key);
	}
	
	/**
	 * Construct an action, given an i18n key and an icon
	 * @param key
	 * @param icon
	 */
	public CorinaAction(String key, Icon icon) {
		super(I18n.getText(key), icon);
		initializeMnemonics(key);
	}

	/**
	 * Construct an action, given an i18n key and an icon name
	 * (in Images directory)
	 * @param key
	 * @param iconName
	 */
	public CorinaAction(String key, String iconName) {
		this(I18n.getText(key), Builder.getIcon(iconName));
	}
	
	/**
	 * Construct an action, given an i18n key and an icon name
	 * (in given package directory)
	 * @param key
	 * @param iconName
	 * @param iconPackageName
	 */
	public CorinaAction(String key, String iconName, String iconPackageName) {
		this(I18n.getText(key), Builder.getIcon(iconName, iconPackageName));
	}
	
	/**
	 * Initialize the mnemonics for this action 
	 * @param key
	 */
	private final void initializeMnemonics(String key) {
		if (!App.platform.isMac()) {
			Integer mnemonic = I18n.getMnemonic(key);
			if (mnemonic != null)
				putValue(MNEMONIC_KEY, new Integer(mnemonic));
		}
		KeyStroke keystroke = I18n.getKeyStroke(key);
		if (keystroke != null)
			putValue(ACCELERATOR_KEY, keystroke);		
	}

	/**
	 * Perform this action
	 * 
	 * @param source
	 */
	public void perform(Object source) {
		ActionEvent ae = new ActionEvent(source == null ? this : source,
				ActionEvent.ACTION_PERFORMED,
				(String) getValue(Action.ACTION_COMMAND_KEY));
		actionPerformed(ae);
	}
}