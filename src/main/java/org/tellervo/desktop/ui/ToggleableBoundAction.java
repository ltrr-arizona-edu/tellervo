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
package org.tellervo.desktop.ui;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleableBoundAction<T> extends ToggleableAction implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(ToggleableBoundAction.class);

	private final String keyTrue;
	private final String keyFalse;
	private final PropertyDescriptor property;
	private final T bound;

	/**
	 * Call for a string property
	 */
	public ToggleableBoundAction(String keyTrue, String keyFalse, T bound, Class<T> boundClass, String propertyName) throws IntrospectionException {
		this(keyTrue, keyFalse, bound, new PropertyDescriptor(propertyName, boundClass));
	}
	
	// phase 2
	private ToggleableBoundAction(String keyTrue, String keyFalse, T bound, PropertyDescriptor property) {
		this(keyTrue, keyFalse, bound, property, getBoundValue(bound, property));
	}
	
	// phase 3
	private ToggleableBoundAction(String keyTrue, String keyFalse, T bound, PropertyDescriptor property, boolean defaultValue) {
		super(defaultValue ? keyTrue : keyFalse, defaultValue);
		
		this.keyFalse = keyFalse;
		this.keyTrue = keyTrue;
		this.property = property;
		this.bound = bound;
	}

	/**
	 * Call for an icon property
	 */
	public ToggleableBoundAction(String keyTrue, String keyFalse, T bound, Class<T> boundClass, String propertyName, String iconName, String iconPackageName, int iconSize) throws IntrospectionException {
		this(keyTrue, keyFalse, bound, new PropertyDescriptor(propertyName, boundClass), iconName, iconPackageName, iconSize);
	}
	
	// phase 2
	private ToggleableBoundAction(String keyTrue, String keyFalse, T bound, PropertyDescriptor property, String iconName, String iconPackageName, int iconSize) {
		this(keyTrue, keyFalse, bound, property, getBoundValue(bound, property), iconName, iconPackageName, iconSize);
	}
	
	// phase 3
	private ToggleableBoundAction(String keyTrue, String keyFalse, T bound, PropertyDescriptor property, boolean defaultValue, String iconName, String iconPackageName, int iconSize) {
		super(defaultValue ? keyTrue : keyFalse, defaultValue, iconName, iconPackageName, iconSize);
		
		this.keyFalse = keyFalse;
		this.keyTrue = keyTrue;
		this.property = property;
		this.bound = bound;
	}

	
	/**
	 * Get the value of the given bean using the given property descriptor
	 * @param <T>
	 * @param bound
	 * @param property
	 * @return
	 */
	public static <T> boolean getBoundValue(T bound, PropertyDescriptor property) {
		Method method = property.getReadMethod();
		
		if(!(method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class)))
			throw new IllegalArgumentException("ToggleableBoundAction only works with booleans, not " + method.getReturnType().getName());
		
		try {
			return (Boolean) method.invoke(bound, (Object[]) null);
		} catch (Exception e) {
			IllegalArgumentException ex = new IllegalArgumentException("Invalid method passed to ToggleableBoundAction/read");
			ex.initCause(e);
			throw ex;
		}
	}
	
	/**
	 * Sets a boolean bound value
	 * 
	 * @param <T>
	 * @param bound
	 * @param property
	 * @param value
	 */
	public static <T> void setBoundValue(T bound, PropertyDescriptor property, boolean value) {
		Method method = property.getWriteMethod();

		try {
			method.invoke(bound, new Object[] { value });
		} catch (Exception e) {
			IllegalArgumentException ex = new IllegalArgumentException("Invalid method passed to ToggleableBoundAction/write");
			ex.initCause(e);
			throw ex;
		} 
	}
	
	@Override
	protected void selectionStateChanged(boolean newSelectedState) {
		// change our name
		putValue(NAME, newSelectedState ? I18n.getText(keyTrue) : I18n.getText(keyFalse));

		setBoundValue(bound, property, newSelectedState);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() == bound && evt.getPropertyName().equals(property.getName())) {
			Boolean value = (Boolean) evt.getNewValue();

			// this shouldn't happen, so stop it from happening
			if(value == null)
				return;

			log.debug(property.getName() + " changed: " + value);

			if(!value.equals(getValue(CORINA_SELECTED_KEY)))
				// this will trigger the change in the button
				putValue(CORINA_SELECTED_KEY, value);
		}
	}

	@Override
	public void togglePerformed(ActionEvent ae, Boolean value) {
		// do nothing!
		
		// setBoundValue is handled in selectionStateChanged()
	}	
}
