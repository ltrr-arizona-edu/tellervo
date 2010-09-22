/**
 * Created at Aug 22, 2010, 3:21:51 PM
 */
package edu.cornell.dendro.corina.components.table;

import org.apache.commons.lang.StringUtils;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasUnitless;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * @author Daniel
 *
 */
public class TridasUnitRenderer extends DefaultCellRenderer {
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
		return StringUtils.capitaliseAllWords(unit.getNormalTridas().toString().replaceAll("___", " ").toLowerCase());
	}
}