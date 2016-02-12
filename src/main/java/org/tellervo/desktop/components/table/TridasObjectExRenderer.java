/**
 * Created at Oct 1, 2010, 3:57:48 PM
 */
package org.tellervo.desktop.components.table;

import org.tellervo.desktop.bulkdataentry.model.TridasObjectOrPlaceholder;
import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tridas.util.TridasObjectEx;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * @author Daniel
 *
 */
public class TridasObjectExRenderer extends DefaultCellRendererEx{

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		
		if(argValue instanceof TridasObjectEx)
		{
			TridasObjectEx o = (TridasObjectEx) argValue;			
			return o.getLabCode();
		}
		else if (argValue instanceof TridasObjectOrPlaceholder)
		{
			TridasObjectOrPlaceholder toph = (TridasObjectOrPlaceholder) argValue;
			return toph.getCode();
			
		}
		else if(argValue instanceof String)
		{
			return (String) argValue;
		}
		
		return "";
	}
}
