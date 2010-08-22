/**
 * Created at Aug 20, 2010, 2:59:18 AM
 */
package edu.cornell.dendro.corina.components.table;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel Murphy
 *
 */
public class DynamicJComboBoxEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;
	
	public final String[] comboBoxItems;
	
	public DynamicJComboBoxEvent(String argKey, String[] argItems) {
		super(argKey);
		comboBoxItems = argItems;
	}
}
