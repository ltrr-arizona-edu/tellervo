/**
 * Created at Oct 1, 2010, 4:19:36 PM
 */
package edu.cornell.dendro.corina.components.table;

import org.tridas.schema.TridasElement;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;


/**
 * @author Daniel
 *
 */
public class TridasElementRenderer extends DefaultCellRenderer {

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		TridasElement e = (TridasElement) argValue;
		if(argValue == null){
			return "";
		}
		return e.getTitle();
	}
}
