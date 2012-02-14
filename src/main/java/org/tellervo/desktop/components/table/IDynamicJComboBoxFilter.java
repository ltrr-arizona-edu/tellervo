/**
 * Created at Aug 26, 2010, 2:44:38 PM
 */
package org.tellervo.desktop.components.table;

/**
 * @author Daniel
 */
public interface IDynamicJComboBoxFilter<E>{
	
	/**
	 * @param argComponent can be null
	 * @return if the value will be put in the combo box
	 */
	public boolean showItem(E argComponent);
}
