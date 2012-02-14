/**
 * 
 */
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ColorUtils;


/**
 * @author Lucas Madar
 *
 */
public class SearchParameterPanel extends SearchParameterPanel_UI_2 {
	private static final long serialVersionUID = 1L;

	/** What we stick in the combo box when nothing is selected */
	private final static String CHOOSE_ITEM = "Choose one...";

	/** A sorted array of our search parameters */
	private static SearchParameterName[] paramsArray;
	
	/** A list of parameter prefixes, for sorting */
	private static String paramPrefixes[] = {
		"object",
		"element",
		"sample",
		"radius",
		"series"
	};
	
	/** Has this parameter been flagged for removal? */
	private boolean removed = false;
	
	public final static String PARAMETER_NAME_PROPERTY = "parameterName";
	public final static String PARAMETER_OPERATOR_PROPERTY = "operator";
	public final static String PARAMETER_VALUE_PROPERTY = "value";
	public final static String PARAMETER_VALID_PROPERTY = "valid";
	public final static String PARAMETER_REMOVED_PROPERTY = "removed";
	
	/** Support a propertyChangeListener */
	private final PropertyChangeSupport properties;
	
	private SearchParameterName lastSearchParameter;
	private SearchOperator lastSearchOperator;
	private String lastValue;
	
	/** Is this a valid parameter entry? */
	private boolean valid;
	
	private Color invalidColor;
	private Color validColor;
	
	public SearchParameterPanel() {
		properties = new PropertyChangeSupport(this);
		
		// If the parameters list hasn't been created,
		// lazily do so
		if(paramsArray == null) {
			// get a cloned copy of the list of values (from enum)
			paramsArray = SearchParameterName.values();
			// make a list wrapping the array
			List<SearchParameterName> paramsList = Arrays.asList(paramsArray); 
			// sort it ...
			Collections.sort(paramsList, new SearchParameterSorter());
		}
		
		invalidColor = ColorUtils.blend(getBackground(), .7f, Color.red, .3f);
		validColor = ColorUtils.blend(getBackground(), .7f, Color.green, .3f);

		setupContent();
		setupActions();
	}
	
	/**
	 * Set up any actions for our buttons, etc
	 */
	private void setupActions() {
		// fire event on remove
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean oldRemoved = removed;
				removed = true;
				
				properties.firePropertyChange(PARAMETER_REMOVED_PROPERTY, oldRemoved, removed);
			}
		});
		
		// fire event on change of search property
		cboSearchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchParameterName name = getParameterName();
				properties.firePropertyChange(PARAMETER_NAME_PROPERTY, lastSearchParameter, name);
				lastSearchParameter = name;
				
				checkValid();
			}
		});

		// fire event on change of search operator
		cboSearchOperator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchOperator op = getOperator();
				
				properties.firePropertyChange(PARAMETER_OPERATOR_PROPERTY, lastSearchOperator, op);
				lastSearchOperator = op;

				checkValid();
			}
		});
		
		// we reuse this in places...
		final Runnable valueChanged = new Runnable() {
			public void run() {
				String value = getValue();
				
				properties.firePropertyChange(PARAMETER_VALUE_PROPERTY, lastValue, value);
				lastValue = value;

				checkValid();
			}
		};
		
		// notify of value changing on focus loss
		txtSearchText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent fe) {
				valueChanged.run();
			}
		});
		
		// when someone hits enter, notify of value changing
		txtSearchText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valueChanged.run();
			}
		});
	}

	/**
	 * Populate the content
	 */
	private void setupContent() {
		// set up the search parameter combo, default to "Choose..."
		DefaultComboBoxModel model = new DefaultComboBoxModel(paramsArray);
		model.insertElementAt(CHOOSE_ITEM, 0);
		cboSearchField.setModel(model);
		cboSearchField.setRenderer(new SearchComboRenderer());
		cboSearchField.getModel().setSelectedItem(CHOOSE_ITEM);
		lastSearchParameter = null;
		
		// set up the search operator combo, default to '='
		model = new DefaultComboBoxModel(SearchOperator.values());
		cboSearchOperator.setModel(model);
		cboSearchOperator.setRenderer(new SearchComboRenderer());
		cboSearchOperator.getModel().setSelectedItem(SearchOperator.EQUALS);
		lastSearchOperator = SearchOperator.EQUALS;
		
		// set up our add/remove button
		btnRemove.setIcon(Builder.getIcon("x.png", 16));
		btnRemove.setRolloverEnabled(true);
		btnRemove.setRolloverIcon(Builder.getIcon("x-rollover.png", 16));
		btnRemove.setBorderPainted(false);
		btnRemove.setContentAreaFilled(false);
		btnRemove.setMargin(new Insets(0,0,0,0));
		btnRemove.setFocusable(false); // can't focus on me, must click		
	}
	
	/**
	 * Simple Comparator for sorting param array by xml name, not enum name
	 * @author Lucas Madar
	 */
	private static class SearchParameterSorter implements Comparator<SearchParameterName> {
		public int compare(SearchParameterName o1, SearchParameterName o2) {
			// compare the xml values, not the enum values
			return o1.value().compareToIgnoreCase(o2.value());
		}		
	}

	/**
	 * Simple ListCellRenderer for each combo box item, using xml value, etc...
	 * @author Lucas Madar
	 */
	private static class SearchComboRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			// get xml value for search parameter name
			if(value instanceof SearchParameterName) {
				String s = ((SearchParameterName)value).value();
				
				value = s;
				setToolTipText(s);
				
				// kludgy hack for making the list look nicer
				// TODO: Annotate the xml list
				for(String prefix : paramPrefixes) {
					if(s.startsWith(prefix)) {
						StringBuffer sb = new StringBuffer();
						
						// Turn objectblahid -> Object blahid
						sb.append(Character.toUpperCase(prefix.charAt(0)));
						sb.append(prefix.substring(1));
						sb.append(" ");
						sb.append(s.substring(prefix.length()));
						
						// done, break out of loop
						value = sb.toString();
						break;
					}
				}
			}
			else if(value instanceof SearchOperator) {
				setToolTipText(value.toString());
				value = ((SearchOperator)value).value();
			}
			else {		
				setToolTipText(null);
			}
			
			return super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
		}
	}
	
	/**
	 * Check if an event changed our validity, and if it did, notify
	 */
	private void checkValid() {
		boolean isValid = isDataValid();
		
		if(valid == isValid)
			return;
		
		boolean oldValid = valid;
		valid = isValid;
		
		// change background color and repaint accordingly
		setBackground(valid ? validColor : invalidColor);
		repaint();

		properties.firePropertyChange(PARAMETER_VALID_PROPERTY, oldValid, valid);		
	}
	
	/**
	 * Get the search parameter name
	 * @return The enum value, or null if not selected
	 */
	public SearchParameterName getParameterName() {
		Object o = cboSearchField.getSelectedItem();
		
		if(o instanceof SearchParameterName)
			return (SearchParameterName) o;
		
		return null;
	}
	
	/**
	 * Get the search operator
	 * @return the enum value
	 */
	public SearchOperator getOperator() {
		return (SearchOperator) cboSearchOperator.getSelectedItem();
	}
	
	/**
	 * Get the search value
	 * @return the search value, or null if it is empty
	 */
	public String getValue() {
		String value = txtSearchText.getText();
		
		return (value.length() > 0) ? value : null;
	}
	
	/**
	 * @return true if this property has been flagged for removal
	 */
	public boolean isRemoved() {
		return removed;
	}
	
	/**
	 * @return true if this is a valid search parameter (fields filled in)
	 */
	public boolean isDataValid() {
		return (getParameterName() != null) && (getValue() != null);
	}
	
	/**
	 * Add a listener for searchproperty changes
	 * @param listener
	 */
	public void addSearchParameterPropertyChangeListener(PropertyChangeListener listener) {
		properties.addPropertyChangeListener(listener);
	}
	
	/**
	 * Remove a listener for search property changes
	 * @param listener
	 */
	public void removeSearchParameterPropertyChangeListener(PropertyChangeListener listener) {
		properties.removePropertyChangeListener(listener);
	}
}
