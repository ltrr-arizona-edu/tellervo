/**
 * Created at Oct 1, 2010, 4:24:12 PM
 */
package edu.cornell.dendro.corina.components.table;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

import edu.cornell.dendro.corina.schema.WSIBox;

/**
 * @author Daniel
 *
 */
public class WSIBoxRenderer extends DefaultCellRenderer {

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		WSIBox box = (WSIBox) argValue;
		if(box == null){
			return "";
		}
		return box.getTitle();
	}
}
