/**
 * Created at Oct 1, 2010, 3:57:48 PM
 */
package org.tellervo.desktop.components.table;

import org.tridas.util.TridasObjectEx;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * @author Daniel
 *
 */
public class TridasObjectExRenderer extends DefaultCellRenderer{

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		TridasObjectEx o = (TridasObjectEx) argValue;
		if(o == null){
			return "";
		}
		return o.getLabCode();
	}
}
