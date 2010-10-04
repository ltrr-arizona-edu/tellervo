/**
 * Created at Oct 1, 2010, 3:57:48 PM
 */
package edu.cornell.dendro.corina.components.table;

import javax.swing.DefaultListCellRenderer;

import org.tridas.util.TridasObjectEx;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * @author Daniel
 *
 */
public class TridasObjectExRenderer extends DefaultCellRenderer{

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
