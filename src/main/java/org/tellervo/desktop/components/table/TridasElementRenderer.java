/**
 * Created at Oct 1, 2010, 4:19:36 PM
 */
package org.tellervo.desktop.components.table;

import org.tellervo.desktop.bulkdataentry.model.TridasElementOrPlaceholder;
import org.tellervo.desktop.tridasv2.ui.DefaultCellRendererEx;
import org.tridas.schema.TridasElement;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;


/**
 * @author Daniel
 *
 */
public class TridasElementRenderer extends DefaultCellRendererEx {

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object argValue) {
		
		
		if(argValue instanceof TridasElement)
		{
			TridasElement e = (TridasElement) argValue;
			return e.getTitle();
		}
		else if (argValue instanceof TridasElementOrPlaceholder)
		{
			TridasElementOrPlaceholder teop = (TridasElementOrPlaceholder) argValue;
			if (teop.getTridasElement()==null)
			{
				return teop.getCode();
			}
			else
			{
				return teop.getTridasElement().getTitle();
			}
		}
		else
		{
			return "";
		}
	}
}
