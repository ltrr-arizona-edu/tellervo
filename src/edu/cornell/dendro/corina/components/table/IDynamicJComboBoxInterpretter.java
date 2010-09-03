/**
 * Created at Aug 26, 2010, 2:44:38 PM
 */
package edu.cornell.dendro.corina.components.table;

/**
 * @author Daniel
 *
 */
public interface IDynamicJComboBoxInterpretter {
	
	/**
	 * @param argComponent can be null
	 * @return the string value displayed in the combo box.  If null, the entry is ommited.
	 */
	public String getStringValue(Object argComponent);
}
