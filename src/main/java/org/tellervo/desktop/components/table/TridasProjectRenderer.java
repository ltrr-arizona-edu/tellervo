/**
 * Created at Oct 1, 2010, 3:57:48 PM
 */
package org.tellervo.desktop.components.table;

import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tridas.schema.TridasProject;

/**
 * @author Daniel
 *
 */
public class TridasProjectRenderer extends DefaultCellRendererEx{

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		
		if(argValue instanceof TridasProject)
		{
			TridasProject o = (TridasProject) argValue;			
			return o.getTitle();
		}
		else if(argValue instanceof String)
		{
			return (String) argValue;
		}
		
		return "";
	}
}
