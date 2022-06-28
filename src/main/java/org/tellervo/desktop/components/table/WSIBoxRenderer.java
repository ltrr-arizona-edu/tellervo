/**
 * Created at Oct 1, 2010, 4:24:12 PM
 */
package org.tellervo.desktop.components.table;

import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tellervo.schema.WSIBox;


/**
 * @author Daniel
 *
 */
public class WSIBoxRenderer extends DefaultCellRendererEx {

	private static final long serialVersionUID = 1L;

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
