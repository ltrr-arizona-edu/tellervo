/**
 * 
 */
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.tellervo.desktop.gui.widgets.AutoCompletion;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.ColorUtils;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;

/**
 * @author Lucas Madar
 *
 */
public class SearchParameterPanel extends SearchParameterPanel_UI_2 {
	private static final long serialVersionUID = 1L;

	/** What we stick in the combo box when nothing is selected */
	private final static String CHOOSE_ITEM = "Choose one...";

	/** A sorted array of our search parameters */
	private static SearchParameterNameEx[] paramsArray;
	
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
	
	private SearchParameterNameEx lastSearchParameter;
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
			
			ArrayList<SearchParameterNameEx> paramsList = new ArrayList<SearchParameterNameEx>();
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ANYPARENTOBJECTCODE));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.OBJECTDESCRIPTION));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.OBJECTTITLE));
			
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTCODE));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTDESCRIPTION));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTDIAMETER));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTHEIGHT));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTDEPTH));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTWIDTH));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTORIGINALTAXONNAME));
			//paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTGENUSNAME));
			//paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTFAMILYNAME));
			//paramsList.add(new SearchParameterNameEx(SearchParameterName.ELEMENTORDERNAME));
			
			paramsList.add(new SearchParameterNameEx(SearchParameterName.SAMPLECODE));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.SAMPLEDESCRIPTION));
			
			paramsList.add(new SearchParameterNameEx(SearchParameterName.RADIUSCODE));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.RADIUSNUMBERSAPWOODRINGS));

			paramsList.add(new SearchParameterNameEx(SearchParameterName.SERIESCODE));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.SERIESANALYST));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.SERIESFIRSTYEAR));
			paramsList.add(new SearchParameterNameEx(SearchParameterName.SERIESVALUECOUNT));

			paramsArray = paramsList.toArray(new SearchParameterNameEx[paramsList.size()]);

		}
		
		invalidColor = ColorUtils.blend(getBackground(), .7f, Color.red, .3f);
		validColor = ColorUtils.blend(getBackground(), .7f, Color.green, .3f);

		setupContent();
		setupActions();
	}
	
	public void remove()
	{
		boolean oldRemoved = removed;
		removed = true;
		
		properties.firePropertyChange(PARAMETER_REMOVED_PROPERTY, oldRemoved, removed);
	}
	
	/**
	 * Set up any actions for our buttons, etc
	 */
	private void setupActions() {
		// fire event on remove
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		
		// fire event on change of search property
		cboSearchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchParameterNameEx name = (SearchParameterNameEx) cboSearchField.getSelectedItem();
				
				// set up the search operator combo, default to '='
				ArrayListModel<SearchOperatorEx> model = new ArrayListModel<SearchOperatorEx>(name.getPossibleOperators());

				cboSearchOperator.setModel(model);
				for(int i =0; i<model.size(); i++)
				{
					if(model.get(i).getSearchOperator().equals(SearchOperator.EQUALS))
					{
						cboSearchOperator.setSelectedIndex(i);
					}
				}

				lastSearchOperator = SearchOperator.EQUALS;
				
				cboSearchOperator.setModel(model);
				
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
		
		ArrayList<SearchParameterNameEx> paramsArrayList = new ArrayList(Arrays.asList(paramsArray));
		
		Collections.sort(paramsArrayList);
		ArrayListModel<SearchParameterNameEx> model = new ArrayListModel<SearchParameterNameEx>(paramsArrayList);

		cboSearchField.setModel(model);
		cboSearchField.setSelectedItem(null);
		AutoCompletion.enable(cboSearchField);
		lastSearchParameter = null;
		
		// set up the search operator combo, default to '='
		ArrayListModel<SearchOperatorEx> model2 = new ArrayListModel<SearchOperatorEx>(new SearchParameterNameEx(SearchParameterName.OBJECTCODE).getPossibleOperators());
		cboSearchOperator.setModel(model2);
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
		
		if(o instanceof SearchParameterNameEx)
			return ((SearchParameterNameEx)o).getSearchParameterName();
		
		return null;
	}
	
	/**
	 * Get the search operator
	 * @return the enum value
	 */
	public SearchOperator getOperator() {
	
		if(cboSearchOperator.getSelectedItem()==null) return null;
		return ((SearchOperatorEx) cboSearchOperator.getSelectedItem()).getSearchOperator();
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
		return (getParameterName() != null) && (getValue() != null) && (getOperator() !=null);
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
