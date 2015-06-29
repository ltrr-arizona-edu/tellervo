/**
 * Created at Aug 22, 2010, 3:21:51 PM
 */
package org.tellervo.desktop.components.table;

import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tridas.schema.NormalTridasLocationType;

/**
 * @author Daniel
 *
 */
public class LocationTypeRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		if(argValue == null){
			return "";
		}

		NormalTridasLocationType type = (NormalTridasLocationType) argValue;
		
		return type.value();
	}
}