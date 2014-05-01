/**
 * Created at Aug 22, 2010, 3:21:51 PM
 */
package org.tellervo.desktop.components.table;

import org.apache.commons.lang.WordUtils;
import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasUnitless;

/**
 * @author Daniel
 *
 */
public class TridasUnitRenderer extends DefaultCellRendererEx {
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		if(argValue == null){
			return "";
		}
		if(argValue instanceof TridasUnitless){
			return "Unitless";
		}
		TridasUnit unit = (TridasUnit) argValue;
		return WordUtils.capitalize(unit.getNormalTridas().toString().replaceAll("___", " ").toLowerCase());
	}
}