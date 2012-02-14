/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import edu.cornell.dendro.corina.platform.Platform;

/**
 * A base Corina action which sets Mnemonic and Accelerator based on I18N settings.
 * @author Aaron Hamid
 * @author Lucas Madar
 */
public abstract class CorinaAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/** This allows us to gracefully handle booleans */
	protected static final String CORINA_SELECTED_KEY = "corina.selected";
	
	/** This is only available in 1.6, but we want it, so use it in 1.5 where it just gets ignored :) */
	protected static final String KLUDGE_DISPLAYED_MNEMONIC_INDEX_KEY = "SwingDisplayedMnemonicIndexKey";
	
	/** Toggle button adapters associated with this class */
	protected ArrayList<ButtonSelectionActionAdapter> buttonAdapters;
		
	/**
	 * Construct an action, given an i18n key
	 * @param key
	 */
	public CorinaAction(String key) {
		super(I18n.getText(key));
		initialize(key);
	}
	
	/**
	 * Construct an action, given an i18n key and an icon
	 * @param key
	 * @param icon
	 */
	public CorinaAction(String key, Icon icon) {
		super(I18n.getText(key), icon);
		initialize(key);
	}

	/**
	 * Construct an action, given an i18n key and an icon name
	 * (in Images directory)
	 * @param key
	 * @param iconName
	 */
	public CorinaAction(String key, String iconName, int iconSize) {
		this(key, Builder.getIcon(iconName, iconSize));
	}
	
	/**
	 * Construct an action, given an i18n key and an icon name
	 * (in given package directory)
	 * @param key
	 * @param iconName
	 * @param iconPackageName
	 */
	public CorinaAction(String key, String iconName, String iconPackageName, int iconSize) {
		this(key, Builder.getIcon(iconName, iconPackageName, iconSize));
	}
	
	/**
	 * Initialize the mnemonics for this action 
	 * @param key
	 */
	private final void initialize(String key) {
		if (!Platform.isMac()) {			
			Integer mnemonic = I18n.getMnemonic(key);
			
			if (mnemonic != null) {
				putValue(MNEMONIC_KEY, new Integer(mnemonic));
				
				mnemonic = I18n.getMnemonicPosition(key);
				if(mnemonic != null)
					putValue(KLUDGE_DISPLAYED_MNEMONIC_INDEX_KEY, mnemonic);
			}
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

	/** Called when our selection state changes */
	protected void selectionStateChanged(boolean newSelectedState) {
		// by default, we don't care :)
	}
	
	/**
	 * Associate a toggleable button with this action's internal toggle state
	 * 
	 * @param button the button to associate with
	 * @param defaultValue the default boolean value, or null if we should try to automatically figure this out
	 */
	public void connectToggleableButton(AbstractButton button, Boolean defaultValue) {
		if(buttonAdapters == null)
			buttonAdapters = new ArrayList<ButtonSelectionActionAdapter>();
		
		buttonAdapters.add(new ButtonSelectionActionAdapter(button, defaultValue));
	}
	
	/**
	 * Associate a toggleable button with this action's internal toggle state
	 * (Guess defaults!)
	 * @param button
	 */
	public void connectToggleableButton(AbstractButton button) {
		connectToggleableButton(button, null);
	}
	
	private class ButtonSelectionActionAdapter implements PropertyChangeListener, ItemListener {
		private AbstractButton button;
		private Boolean lastValue;
		
		public ButtonSelectionActionAdapter(AbstractButton button, Boolean defaultValue) {
			this.button = button;
			
			// set the default value
			if(defaultValue != null) {
				button.setSelected(defaultValue);
				CorinaAction.this.putValue(CORINA_SELECTED_KEY, defaultValue);
			}
			else if((defaultValue = (Boolean) CorinaAction.this.getValue(CORINA_SELECTED_KEY)) == null)
				// well, use the button's value then
				CorinaAction.this.putValue(CORINA_SELECTED_KEY, button.isSelected());
			else
				// ok, use the action's value then!
				button.setSelected(defaultValue);
			
			// tie listeners in
			CorinaAction.this.addPropertyChangeListener(this);
			button.addItemListener(this);
		}

		// called when the Action's value changes
		public void propertyChange(PropertyChangeEvent evt) {
			// only care about our special selection event
			if(!CORINA_SELECTED_KEY.equals(evt.getPropertyName()))
				return;
			
			Boolean selected = (Boolean) evt.getNewValue();
			
			button.setSelected(selected);
			
			// notify our superclass
			if(lastValue != selected) {
				lastValue = selected;
				CorinaAction.this.selectionStateChanged(selected);
			}
		}

		// called when the button's value changes
		public void itemStateChanged(ItemEvent e) {
			Boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			
			CorinaAction.this.putValue(CORINA_SELECTED_KEY, selected);
			
			// notify our superclass
			if(lastValue != selected) {
				lastValue = selected;
				CorinaAction.this.selectionStateChanged(selected);
			}
		}
	}
}
