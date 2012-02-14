/**
 * Created at Aug 22, 2010, 3:18:58 PM
 */
package org.tellervo.desktop.components.table;

import org.apache.commons.lang.WordUtils;
import org.tridas.schema.TridasShape;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * @author Daniel
 *
 */
public class TridasShapeRenderer extends DefaultCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		if(argValue == null){
			return "";
		}
		TridasShape shape = (TridasShape) argValue;
		return WordUtils.capitalize(shape.getNormalTridas().toString().replaceAll("___", " ").toLowerCase());
	}
}
